package com.gtafe.framework.base.register;

import GTA.License.Register;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/common/reg")
public class RegController {

    @RequestMapping
    public String regPage(Model model) {

        try {
            if (!Register.Check(IniVerifyFlag.productCode)) {// 校验失败，进入到授权页面
                String applyId = Register.GetCode(IniVerifyFlag.productCode);// 根据产品码生成申请码。

                model.addAttribute("applyId",applyId);
                return "reg/reg";// 跳转到注册授权页面
            }else {
            	IniVerifyFlag.verifyFlag=true;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "forward:reg/success";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String reg(String registerCode,String grantCode) {

        boolean success = false;
        try {
            // 授权
            success = Register.Registe(IniVerifyFlag.productCode, registerCode, grantCode);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (success) {
            IniVerifyFlag.verifyFlag=true;
            return "forward:reg/success";
        } else {
            return  "redirect:reg";
        }

    }

    @RequestMapping("success")
    public String success(Model model) {
        Map<String, String> infos = Register.getInfo(IniVerifyFlag.productCode);
        model.addAttribute("applyId",infos.get("applyId"));
        model.addAttribute("start", infos.get("start"));
        model.addAttribute("end", infos.get("end"));
        return "reg/success";
    }

}
