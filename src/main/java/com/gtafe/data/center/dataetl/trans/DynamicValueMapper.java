package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleDynamicValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.valuemapper.ValueMapperMeta;

import java.io.IOException;
import java.sql.SQLException;
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


            /**
             * 根据 targetDs 查询 数据
             * 两个查询：
             * a 查询 源字段值
             * b 根据 源字段值 查询对应的目标字段值
             *
             * 然后拼凑 类似 页面上 填写的 静态 值隐射
             *
             */
            System.out.println("SourceField===" + valuemapperVo.getSourceField());
            List<String> sourceList = null;
            try {
                sourceList = Utils.querySourceListBy(targetDs, valuemapperVo);
            } catch (SQLException e) {
                sourceList = new ArrayList<String>();
                e.printStackTrace();

            }
            long sourceSize = sourceList.size();
            String[] sourcestr = new String[Integer.parseInt(sourceSize + "")];
            String[] targetstr = new String[Integer.parseInt(sourceSize + "")];

            for (int i = 0; i < sourceSize; i++) {
                sourcestr[i] = sourceList.get(i);
                try {
                    targetstr[i] = Utils.queryTargetStrBy(targetDs, valuemapperVo, sourcestr[i]);
                } catch (SQLException e) {
                    targetstr[i] = "";
                    e.printStackTrace();
                }
            }

            valueMapperMeta.setSourceValue(sourcestr);
            valueMapperMeta.setTargetValue(targetstr);

            name += k;
            k++;
            stepMetas.add(initStep(valueMapperMeta));
            locationX += 100;
        }

        return stepMetas;
    }

}
