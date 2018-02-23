package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleCalculator;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleTypeconvert;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.CalculatorVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.TypeconvertVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段选择
 */
public class SelectValues extends BaseStep{

    String stepstr;

    public SelectValues(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta selectValueStep() {

        List<TypeconvertVo> typeconvertVoList;

        try {
            typeconvertVoList=mapper.readValue(stepstr, ConvertRuleTypeconvert.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        SelectValuesMeta selectValuesMeta=new SelectValuesMeta();
        selectValuesMeta.setSelectingAndSortingUnspecifiedFields(true);
        Map<String,List<String>> selectItems=selectItems(typeconvertVoList);
        selectValuesMeta.allocate(selectItems.get("ori").size(),0,typeconvertVoList.size());

        for(int i=0;i<selectItems.get("ori").size();i++) {

            selectValuesMeta.getSelectFields()[i].setName(selectItems.get("ori").get(i));
            selectValuesMeta.getSelectFields()[i].setRename(selectItems.get("tar").get(i));

        }

        for(int i=0;i<typeconvertVoList.size();i++) {

            selectValuesMeta.getMeta()[i].setName(typeconvertVoList.get(i).getSourceField());

            selectValuesMeta.getMeta()[i].setRename(typeconvertVoList.get(i).getTargetField());

            selectValuesMeta.getMeta()[i].setType(typeconvertVoList.get(i).getTargetType());

            if (!typeconvertVoList.get(i).getLongVal().equals("")) {
                selectValuesMeta.getMeta()[i].setLength(Integer.parseInt(typeconvertVoList.get(i).getLongVal()));
            }

            if (!typeconvertVoList.get(i).getAccuracy().equals("")) {
                selectValuesMeta.getMeta()[i].setPrecision(Integer.parseInt(typeconvertVoList.get(i).getAccuracy()));
            }
            selectValuesMeta.getMeta()[i].setConversionMask(typeconvertVoList.get(i).getFormat());

        }

        return  initStep(selectValuesMeta);

    }

    public StepMeta addValueStep(String stepId,List<String> fieldName) {

        SelectValuesMeta selectValuesMeta=new SelectValuesMeta();
        selectValuesMeta.setSelectingAndSortingUnspecifiedFields(true);

        selectValuesMeta.allocate(fieldName.size()*2,0,0);
        SelectValuesMeta.SelectField[] selectFields=new SelectValuesMeta.SelectField[fieldName.size()*2];
        for (int i=0;i<fieldName.size();i++) {

            selectValuesMeta.getSelectFields()[i*2].setName(fieldName.get(i));
            selectValuesMeta.getSelectFields()[i*2].setRename(fieldName.get(i));

            selectValuesMeta.getSelectFields()[i*2+1].setName(fieldName.get(i));
            selectValuesMeta.getSelectFields()[i*2+1].setRename(fieldName.get(i)+"_add_"+stepId);

//            selectFields[i*2]=new SelectValuesMeta.SelectField();
//            selectFields[i*2].setName(fieldName.get(i));
//            selectFields[i*2].setRename(fieldName.get(i));
//
//            selectFields[i*2+1]=new SelectValuesMeta.SelectField();
//            selectFields[i*2+1].setName(fieldName.get(i));
//            selectFields[i*2+1].setRename(fieldName.get(i)+"_add_"+stepId);
        }

   //     selectValuesMeta.setSelectFields(selectFields);

        return  initStep(selectValuesMeta);

    }

    private Map<String,List<String>> selectItems(List<TypeconvertVo> typeconvertVoList) {

        Map<String,List<String>> selectItems=new HashMap<>();

        List<String> oriStr=new ArrayList<>();
        List<String> tarStr=new ArrayList<>();

        for(int i=0;i<typeconvertVoList.size();i++) {

            if (!oriStr.contains(typeconvertVoList.get(i).getSourceField())) {
                oriStr.add(typeconvertVoList.get(i).getSourceField());
                tarStr.add(typeconvertVoList.get(i).getSourceField());
            }
            oriStr.add(typeconvertVoList.get(i).getSourceField());
            tarStr.add(typeconvertVoList.get(i).getSourceField()+"_add_"+i);
            typeconvertVoList.get(i).setSourceField(typeconvertVoList.get(i).getSourceField()+"_add_"+i);
        }

        selectItems.put("ori",oriStr);
        selectItems.put("tar",tarStr);

        return selectItems;
    }

    public StepMeta caculateStep() {

        SelectValuesMeta selectValuesMeta=new SelectValuesMeta();
        selectValuesMeta.setSelectingAndSortingUnspecifiedFields(true);

        List<CalculatorVo> calculatorVos;

        try {
            calculatorVos=mapper.readValue(stepstr, ConvertRuleCalculator.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        List<String> items=new ArrayList<>();
        for (CalculatorVo calculatorVo : calculatorVos) {
            if (!items.contains(calculatorVo.getFieldA()) && !calculatorVo.getFieldA().equals("")) {
                items.add(calculatorVo.getFieldA());
            }
            if (!items.contains(calculatorVo.getFieldB()) && !calculatorVo.getFieldB().equals("")) {
                items.add(calculatorVo.getFieldB());
            }
            if (!items.contains(calculatorVo.getFieldC()) && !calculatorVo.getFieldC().equals("")) {
                items.add(calculatorVo.getFieldC());
            }
        }
        selectValuesMeta.allocate(0,0,items.size());

        for (int i=0;i<items.size();i++) {
            selectValuesMeta.getMeta()[i].setName(items.get(i));
            selectValuesMeta.getMeta()[i].setType(1);
        }

        return  initStep(selectValuesMeta);

    }

}
