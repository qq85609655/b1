package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleStringoper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.StringoperVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.stringoperations.StringOperationsMeta;

import java.io.IOException;
import java.util.List;

/**
 * 字符串操作
 */
public class StringOperations extends BaseStep{

    String stepstr;

    public StringOperations(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta strOpe() {

        List<StringoperVo> stringoperVos=null;
        try {
            stringoperVos=mapper.readValue(stepstr, ConvertRuleStringoper.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        StringOperationsMeta stringOperationsMeta=new StringOperationsMeta();

        String[] sourceFields=new String[stringoperVos.size()];
        String[] targetFields=new String[stringoperVos.size()];
        String[] paddingChars=new String[stringoperVos.size()];
        String[] paddingLengths=new String[stringoperVos.size()];
        int[] blanks=new int[stringoperVos.size()];
        int[] caseTypes=new int[stringoperVos.size()];
        int[] specials=new int[stringoperVos.size()];
        int[] numbers=new int[stringoperVos.size()];
        int[] paddingTypes=new int[stringoperVos.size()];

        for (int i=0;i<stringoperVos.size();i++) {
            sourceFields[i]= stringoperVos.get(i).getSourceField();
            targetFields[i]= stringoperVos.get(i).getTargetField();
            paddingChars[i]= stringoperVos.get(i).getPaddingChar();
            paddingLengths[i]= String.valueOf(stringoperVos.get(i).getPaddingLength());
            blanks[i]= stringoperVos.get(i).getBlank();
            caseTypes[i]= stringoperVos.get(i).getCaseType();
            specials[i]= stringoperVos.get(i).getSpecial();
            numbers[i]= stringoperVos.get(i).getNumber();
            paddingTypes[i]= stringoperVos.get(i).getPaddingType();
        }

        stringOperationsMeta.allocate(stringoperVos.size());
        stringOperationsMeta.setFieldInStream(sourceFields);
        stringOperationsMeta.setFieldOutStream(targetFields);
        stringOperationsMeta.setPadChar(paddingChars);
        stringOperationsMeta.setPadLen(paddingLengths);
        stringOperationsMeta.setPaddingType(paddingTypes);
        stringOperationsMeta.setTrimType(blanks);
        stringOperationsMeta.setLowerUpper(caseTypes);
        stringOperationsMeta.setDigits(numbers);
        stringOperationsMeta.setRemoveSpecialCharacters(specials);

        return initStep(stringOperationsMeta);
    }
}
