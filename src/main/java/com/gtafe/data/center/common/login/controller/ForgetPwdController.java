package com.gtafe.data.center.common.login.controller;

import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.ResultVO;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.MailSender;
import com.gtafe.framework.base.utils.StringUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Map;


@Controller
@RequestMapping(path = "/forgetPwd")
@Api(value = "ForgetPwdController")
@CrossOrigin
public class ForgetPwdController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private SysUserService sysUserServiceImpl;
    @Resource
    private SysConfigService sysConfigServiceImpl;
    @Autowired
    MailSender mailSender;


    /**
     * 把修改密码的地址给用户邮箱 发邮件  提醒修改密码
     * 有 需要判断用户状态
     * 需要检测 当前邮箱是否存在系统用户中，无则 反馈结果：
     * -1无此用户
     * 1正常:则发邮件。
     * 2停用：则不发邮件 反馈结果：用户已经被停用 。
     *
     * @return
     */
    @RequestMapping("/sendMail")
    public @ResponseBody
    ResultVO sendMail(@RequestBody Map map, HttpServletRequest request) throws Exception {
        String loginName = map.get("account").toString();
        String email = map.get("email").toString();
        ResultVO vo = new ResultVO();
        //1:根据邮箱地址去用户表查询是否存在
        SysUserVo userVo = this.sysUserServiceImpl.queryUserByEmailAndLoginName(email, loginName);
        if (userVo == null) {
            vo.setCounts(-1);
            return vo;
        }
        //
        if (userVo.isState()) {
            SysConfigVo vo1 = this.sysConfigServiceImpl.getBasicSysConfigVO();
            String sourcePwd = (int) ((Math.random() * 9 + 1) * 100000) + "";
            String pwd_md5 = StringUtil.MD5(sourcePwd);
            LOGGER.info(pwd_md5);
            boolean result = true;
            userVo.setLoginPwd(pwd_md5);
            if (StringUtil.isNotBlank(email)) {
                long ds = System.currentTimeMillis();
                result = mailSender.sendEmail(email, "找回密码",
                        "\n" +
                                userVo.getRealName() + "用户， 这封信是由 “" + vo1.getSysName() + "” 发送的。\n" +
                                "\n" +
                                "您收到这封邮件，是由于这个邮箱地址在 “" + vo1.getSysName() + "” 被登记为用户邮箱， 且该用户请求使用 Email 密码重置功能所致。\n" +
                                "\n" +
                                "----------------------------------------------------------------------\n" +
                                "\n" +
                                "重要！\n" +
                                "\n" +
                                "----------------------------------------------------------------------\n" +
                                "\n" +
                                "如果您没有提交密码重置的请求或不是“" + vo1.getSysName() + "” 的注册用户，请立即忽略 并删除这封邮件。只有在您确认需要重置密码的情况下，才需要继续阅读下面的 内容。\n" +
                                "\n" +
                                "----------------------------------------------------------------------\n" +
                                "\n" +
                                "密码重置说明\n" +
                                "\n" +
                                "----------------------------------------------------------------------\n" +
                                "\n" +
                                "您只需在提交请求后的三天内，通过点击下面的链接重置您的密码：\n" +
                                "\n" +
                                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/GTADataCenter/#/editPwd?userNo=" + userVo.getUserNo() + "&rd=" + ds +
                                "\n" +
                                "(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)\n" +
                                "\n" +
                                "在上面的链接所打开的页面中输入新的密码后提交，您即可使用新的密码登录网站了。您可以在用户控制面板中随时修改您的密码。\n" +
                                "\n" +
                                "署名：" + vo1.getSysName());
                if (!result) {
                    throw new OrdinaryException("请检查服务器的邮件配置!");
                }
            }
            vo.setCounts(result ? 1 : 0);
            return vo;
        } else {
            //用户已经被停用 则不能发邮件了。
            vo.setCounts(2);
            return vo;
        }
    }

    @RequestMapping("/editPwd")
    public @ResponseBody
    ResultVO editPwd(@RequestBody Map map, HttpServletRequest request) throws Exception {
        ResultVO vo = new ResultVO();
        String pwd = (String) map.get("pwd").toString();
        String confirmpwd = (String) map.get("confirmpwd").toString();
        String userNo = (String) map.get("userNo").toString();
        LOGGER.info(pwd + "===confirmpwd==" + confirmpwd + "====userNo" + userNo);
        boolean result = this.sysUserServiceImpl.updatePwdForUserNo(pwd, confirmpwd, userNo);
        vo.setCounts(result ? 1 : 0);
        return vo;
    }
}

