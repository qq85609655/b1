package com.gtafe.data.center.dataetl.trans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.DynamicValueMappingVo;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
基本步骤
 */
public class BaseStep {

    protected int locationX, locattionY;

    protected String name;

    ObjectMapper mapper;

    public BaseStep(int locationX, int locattionY, String name) {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.locationX = locationX;
        this.locattionY = locattionY;
        this.name = name;
    }

    protected StepMeta initStep(StepMetaInterface stepMeta) {
        StepMeta step = new StepMeta(name, stepMeta);
        step.setLocation(locationX, locattionY);
        step.setDraw(true);
        step.setDescription(name);
        return step;
    }
}
