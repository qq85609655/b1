package com.gtafe.framework.base.utils;

import com.gtafe.data.center.common.login.controller.LoginController;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.exception.OrdinaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/27
 * @Description:
 */
@Component
public class MailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    SysConfigMapper sysConfigMapper;

    public boolean sendEmail(String reciever, String title, String content) {

        SysConfigVo sysConfig = sysConfigMapper.sysConfig();

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (sysConfig != null) {
            if (StringUtil.isNotBlank(sysConfig.getEmailSmtpAddr())) {
                sender.setHost(sysConfig.getEmailSmtpAddr());
            } else {
                throw new OrdinaryException("系统邮件配置【SMTP】未配置");
            }
            if (StringUtil.isNotBlank(sysConfig.getEmailSmtpPort() + "")) {
                sender.setPort(sysConfig.getEmailSmtpPort());
            } else {
                throw new OrdinaryException("系统邮件配置【端口】未配置");
            }
            if (StringUtil.isNotBlank(sysConfig.getEmailUser())) {
                sender.setUsername(sysConfig.getEmailUser());
            } else {
                throw new OrdinaryException("系统邮件配置【用户名】未配置");
            }
            if (StringUtil.isNotBlank(sysConfig.getEmailPwd())) {
                sender.setPassword(sysConfig.getEmailPwd());
            } else {
                throw new OrdinaryException("系统邮件配置【密码】未配置");
            }
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            try {
                helper.setFrom(sysConfig.getEmailHost());
                helper.setTo(reciever);
                helper.setSubject(title);
                helper.setText(content);
            } catch (MessagingException e) {
                return false;
            }
            sender.send(message);
        }
        LOGGER.info("邮件已经发送了。。。。");
        return true;
    }


}
