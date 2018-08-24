package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.EtlTaskNoteVo;
import com.gtafe.data.center.dataetl.datatask.vo.TaskFieldDetailsVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleTarget;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SourceTargetVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.TargetCondition;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.TargetMappingVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
输出表
 */
public class OutputTable extends BaseStep {

    DatasourceVO ds;

    String targetDBName;

    String stepstr;

    public OutputTable(int locationX, int locattionY, String name, DatasourceVO ds, String targetDBName, String stepstr) {
        super(locationX, locattionY, name);
        this.ds = ds;
        this.stepstr = stepstr;
        this.targetDBName = targetDBName;
    }

    public List<TaskFieldDetailsVo> getTaskFieldDetailsVoList(String stepstr) {
        List<TaskFieldDetailsVo> detailsVos = new ArrayList<TaskFieldDetailsVo>();
        List<TargetMappingVo> mapping;
        SourceTargetVo sourceTargetVo;
        List<TableFieldVo> targetTableFieldVoList;
        try {
            mapping = mapper.readValue(stepstr, ConvertRuleTarget.class).getData().getMappings();
            sourceTargetVo = mapper.readValue(stepstr, ConvertRuleTarget.class).getData();
            targetTableFieldVoList = mapper.readValue(stepstr, ConvertRuleTarget.class).getTargetList();
        } catch (IOException e) {
            return null;
        }
        for (int i = 0; i < mapping.size(); i++) {
            TaskFieldDetailsVo detailsVoo = new TaskFieldDetailsVo();
            detailsVoo.setSourceField(mapping.get(i).getSourceField());
            detailsVoo.setTargetField(mapping.get(i).getTargetField());
            detailsVos.add(detailsVoo);
        }
        for (int i = 0; i < targetTableFieldVoList.size(); i++) {
            TableFieldVo tableFieldVo = targetTableFieldVoList.get(i);
            System.out.println(tableFieldVo.toString());
        }

        return detailsVos;
    }

    public StepMeta outputStep() {
        System.out.println("目标表为：" + targetDBName);
        List<TargetMappingVo> mapping;
        List<TargetCondition> conditions;
        try {
            mapping = mapper.readValue(stepstr, ConvertRuleTarget.class).getData().getMappings();
            conditions = mapper.readValue(stepstr, ConvertRuleTarget.class).getData().getConditions();
        } catch (IOException e) {
            return null;
        }
        if (conditions == null || conditions.size() == 0) {
            TableOutputMeta tableOutput = new TableOutputMeta();
            tableOutput.setDatabaseMeta(Utils.InitDatabaseMeta(ds));

            String targetTableName;
            if (ds.getDbType() == 2) {
                targetTableName = "\"" + targetDBName + "\"";
            } else {
                targetTableName = targetDBName.toLowerCase();
            }

            tableOutput.setTableName(targetTableName);
            tableOutput.setTruncateTable(true);
            tableOutput.setCommitSize(1000);
            tableOutput.setSpecifyFields(true);
            String sourceFields[] = new String[mapping.size()];
            String targetFields[] = new String[mapping.size()];
            for (int i = 0; i < mapping.size(); i++) {
                sourceFields[i] = mapping.get(i).getSourceField();
                targetFields[i] = mapping.get(i).getTargetField();
            }
            tableOutput.setFieldStream(sourceFields);
            tableOutput.setFieldDatabase(targetFields);
            return initStep(tableOutput);
        } else {
            InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
            insertUpdateMeta.setDatabaseMeta(Utils.InitDatabaseMeta(ds));
            //只插入不更新
            //insertUpdateMeta.setUpdateBypassed(true);

            String targetTableName;
            if (ds.getDbType() == 2) {
                targetTableName = "\"" + targetDBName + "\"";
            } else {
                targetTableName = targetDBName.toLowerCase();
            }

            insertUpdateMeta.setTableName(targetTableName);
            insertUpdateMeta.setCommitSize(String.valueOf(1000));
            insertUpdateMeta.allocate(conditions.size(), mapping.size());

            String[] keyStream = new String[conditions.size()];
            String[] keyLookup = new String[conditions.size()];
            String[] keyCondition = new String[conditions.size()];

            for (int i = 0; i < conditions.size(); i++) {
                keyStream[i] = conditions.get(i).getSourceField();
                keyLookup[i] = conditions.get(i).getTargetField();
                keyCondition[i] = conditions.get(i).getKeyCondition();
            }

            String[] updateStream = new String[mapping.size()];
            String[] updateLookup = new String[mapping.size()];
            Boolean[] update = new Boolean[mapping.size()];
            for (int i = 0; i < mapping.size(); i++) {
                updateStream[i] = mapping.get(i).getSourceField();
                updateLookup[i] = mapping.get(i).getTargetField();
                System.out.println("updateStream[" + i + "]========" + updateStream[i]);
                System.out.println("updateLookup[" + i + "]========" + updateLookup[i]);
                update[i] = true;
            }
            insertUpdateMeta.setKeyLookup(keyLookup);
            insertUpdateMeta.setKeyStream(keyStream);
            insertUpdateMeta.setKeyCondition(keyCondition);
            insertUpdateMeta.setUpdateLookup(updateLookup);
            insertUpdateMeta.setUpdateStream(updateStream);
            insertUpdateMeta.setUpdate(update);

            return initStep(insertUpdateMeta);

        }


    }


}
