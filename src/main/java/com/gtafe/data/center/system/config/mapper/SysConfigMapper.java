package com.gtafe.data.center.system.config.mapper;

import com.gtafe.data.center.system.config.vo.SysConfigVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SysConfigMapper {

    SysConfigVo queryEntity(@Param("logo") boolean logo);

    @Update("update sys_config " +
            "set email_host=#{emailHost}," +
            "email_user=#{emailUser}," +
            "email_pwd=#{emailPwd}," +
            "email_smtp_addr=#{emailSmtpAddr}," +
            "email_smtp_port=#{emailSmtpPort}")
    boolean updateEmail(SysConfigVo vo);

    @Update("update sys_config " +
            "set sys_name=#{sysName}," +
            "school_name=#{schoolName}," +
            "copyright=#{copyRight} ," +
            " sfinit=#{sfInit}")
    boolean updateSys(SysConfigVo vo);

    @Update("update sys_config set logo_info=#{logoInfo}")
    boolean updateSystemLog(@Param("logoInfo") String logoInfo);

    @Update("update sys_menu set menu_name=#{sysName} where parent_id = 0")
    boolean updateSystemNameToMenu(@Param("sysName") String sysName);

    @Update("update sys_org set org_name=#{schoolName} where parent_id = 0")
    boolean updateSchoolNameToOrg(@Param("schoolName") String schoolName);

    @Select("select email_host emailHost,email_user emailUser,email_pwd emailPwd,email_smtp_addr emailSmtpAddr,email_smtp_port emailSmtpPort from sys_config where id=1")
    SysConfigVo sysConfig();
}
