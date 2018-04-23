package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleDynamicValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.valuemapper.ValueMapperMeta;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态值映射  实质还是 值隐射
 */
public class DynamicValueMapper extends BaseStep {

    String stepstr;
    DatasourceVO targetDs;

    public DynamicValueMapper(int locationX, int locattionY, String name, String stepstr, DatasourceVO targetDs) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
        this.targetDs = targetDs;
    }

    public List<StepMeta> valueMapperStep() {

        List<DynamicValueMappingVo> valuemapperVos;

        List<StepMeta> stepMetas = new ArrayList<>();

        try {
            valuemapperVos = mapper.readValue(stepstr, ConvertRuleDynamicValuemapper.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        int k = 0;

        for (DynamicValueMappingVo valuemapperVo : valuemapperVos) {

            ValueMapperMeta valueMapperMeta = new ValueMapperMeta();
            valueMapperMeta.setFieldToUse(valuemapperVo.getSourceField());
            if (valuemapperVo.getTargetField() != null) {
                valueMapperMeta.setTargetField(valuemapperVo.getTargetField());
            }
            if (valuemapperVo.getDefValue() != null) {
                valueMapperMeta.setNonMatchDefault(valuemapperVo.getDefValue());
            }
            valueMapperMeta = this.CompactMapperMeta(valueMapperMeta, targetDs, valuemapperVo);
            name += k;
            k++;
            stepMetas.add(initStep(valueMapperMeta));
            locationX += 100;
        }

        return stepMetas;
    }

    private ValueMapperMeta CompactMapperMeta(ValueMapperMeta valueMapperMeta, DatasourceVO targetDs, DynamicValueMappingVo valuemapperVo) {

        StringBuilder sql = new StringBuilder("select distinct ").append(" ");

        sql.append(valuemapperVo.getReleaseFieldName()).append(" ").append(" , ");

        sql.append(valuemapperVo.getTargetFieldName()).append(" ");

        sql.append(" from ");

        sql.append(valuemapperVo.getReleaseTableName()).append(" ");

        System.out.println(sql.toString());

        ConnectDB connectDB = StringUtil.getEntityBy(targetDs);

        Connection connection = connectDB.getConn();

        if (connection != null) {
            try {
                List<String> sourceList = new ArrayList<String>();
                List<String> targetList = new ArrayList<String>();

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql.toString());
                while (rs.next()) {
                    String res1 = rs.getString(1);
                    sourceList.add(res1);
                    String res2 = rs.getString(2);
                    targetList.add(res2);
                }
                String[] sourcestr = new String[sourceList.size()];
                String[] targetstr = new String[targetList.size()];
                if (sourceList.size() > 0) {
                    sourceList.toArray(sourcestr);
                }
                if (targetList.size() > 0) {
                    targetList.toArray(targetstr);
                }
                valueMapperMeta.setSourceValue(sourcestr);
                valueMapperMeta.setTargetValue(targetstr);
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                connectDB.closeDbConn(connection);
            }
        }
        return valueMapperMeta;
    }
}

/**
 * System.out.println("SourceField===" + valuemapperVo.getSourceField());
 * List<String> sourceList = null;
 * try {
 * sourceList = Utils.querySourceListBy(targetDs, valuemapperVo);
 * } catch (SQLException e) {
 * sourceList = new ArrayList<String>();
 * e.printStackTrace();
 * <p>
 * }
 * long sourceSize = sourceList.size();
 * String[] sourcestr = new String[Integer.parseInt(sourceSize + "")];
 * String[] targetstr = new String[Integer.parseInt(sourceSize + "")];
 * <p>
 * for (int i = 0; i < sourceSize; i++) {
 * sourcestr[i] = sourceList.get(i);
 * try {
 * targetstr[i] = Utils.queryTargetStrBy(targetDs, valuemapperVo, sourcestr[i]);
 * } catch (SQLException e) {
 * targetstr[i] = "";
 * e.printStackTrace();
 * }
 * }
 * <p>
 * valueMapperMeta.setSourceValue(sourcestr);
 * valueMapperMeta.setTargetValue(targetstr);
 */