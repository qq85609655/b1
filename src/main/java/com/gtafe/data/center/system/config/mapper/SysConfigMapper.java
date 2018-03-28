package com.gtafe.data.center.system.config.mapper;

import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import org.apache.ibatis.annotations.*;
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
            "sys_type=#{sysType} ," +
            "kettle_install_path=#{kettleInstallPath} ," +
            "ktr_files_path=#{ktrFilesPath} ," +
            "kjb_files_path=#{kjbFilesPath} ," +
            " sfinit=#{sfInit}")
    boolean updateSys(SysConfigVo vo);

    @Update("update sys_config set logo_info=#{logoInfo}")
    boolean updateSystemLog(@Param("logoInfo") String logoInfo);

    @Update("update sys_menu set menu_name=#{sysName} where parent_id = 0")
    boolean updateSystemNameToMenu(@Param("sysName") String sysName);

    @Update("update sys_org set org_name=#{schoolName} where parent_id = 0")
    boolean updateSchoolNameToOrg(@Param("schoolName") String schoolName);

    @Select("select email_host emailHost,email_user emailUser," +
            "email_pwd emailPwd,email_smtp_addr emailSmtpAddr," +
            "email_smtp_port emailSmtpPort,sys_type sysType," +
            "kettle_install_path kettleInstallPath ," +
            "ktr_files_path ktrFilesPath," +
            "kjb_files_path kjbFilesPath from sys_config where id=1")
    SysConfigVo sysConfig();

    @Insert("insert into t_trans_file_info(file_name,create_time,update_time," +
            "create_user_info,access_time,file_type,file_path,schedule_info)" +
            " values(#{vo.fileName},#{vo.createTime},#{vo.updateTime}," +
            "#{vo.createUserInfo},#{vo.accessTime},#{vo.fileType},#{vo.filePath},#{vo.scheduleInfo})")
    void saveTransFile(@Param("vo") TransFileVo transFileVo);

    @Delete("delete from t_trans_file_info where file_type= #{fileType}")
    void truncateTransFile(@Param("fileType") String fileType);

    @Select("select " +
            "centerdb_dbType dbType," +
            "centerdb_dbName dbName," +
            "centerdb_tableSpaces tableSpaces," +
            "centerdb_port port," +
            "centerdb_username username," +
            "centerdb_password password," +
            "centerdb_ipAddress ipAddress" +
            " from sys_config ")
    SysConfigVo queryCenterDbInfo();
}
