package com.gtafe.data.center.dataetl.trans;

import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleStringReplace;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.StringreplaceVo;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.replacestring.ReplaceStringMeta;

import java.io.IOException;
import java.util.List;

/**
 * 字符串替换
 */
public class ReplaceString extends BaseStep{

    String stepstr;

    public ReplaceString(int locationX, int locattionY, String name, String stepstr) {
        super(locationX, locattionY, name);
        this.stepstr = stepstr;
    }

    public StepMeta replace() {

        ReplaceStringMeta replaceStringMeta=new ReplaceStringMeta();

        List<StringreplaceVo> stringreplaceVos=null;

        try {
            stringreplaceVos=mapper.readValue(stepstr, ConvertRuleStringReplace.class).getDataList();
        } catch (IOException e) {
            return null;
        }

        String[] sourceFields=new String[stringreplaceVos.size()];
        String[] targetFields=new String[stringreplaceVos.size()];
        String[] findValue=new String[stringreplaceVos.size()];
        String[] replaceValue=new String[stringreplaceVos.size()];

        for (int i=0;i<stringreplaceVos.size();i++) {

            sourceFields[i]= stringreplaceVos.get(i).getSourceField();
            targetFields[i]= stringreplaceVos.get(i).getTargetField();
            findValue[i]= stringreplaceVos.get(i).getFindValue();
            replaceValue[i]= stringreplaceVos.get(i).getReplaceValue();

        }

        replaceStringMeta.allocate(stringreplaceVos.size());
        replaceStringMeta.setFieldInStream(sourceFields);
        replaceStringMeta.setFieldOutStream(targetFields);
        replaceStringMeta.setReplaceString(findValue);
        replaceStringMeta.setReplaceByString(replaceValue);

        return initStep(replaceStringMeta);

    }
}
