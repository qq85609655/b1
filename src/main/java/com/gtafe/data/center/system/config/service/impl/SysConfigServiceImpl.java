package com.gtafe.data.center.system.config.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.information.code.vo.MysqlTableVo;
import com.gtafe.data.center.information.code.vo.TableEntity;
import com.gtafe.data.center.information.data.mapper.DataStandardItemMapper;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.information.data.mapper.DataStandardMapper;
import com.gtafe.data.center.information.data.service.DataTableService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.listener.GTAServletContextListener;
import com.gtafe.framework.base.service.BaseService;
import com.gtafe.framework.base.utils.MailSender;
import com.gtafe.framework.base.utils.StringUtil;

@Service
public class SysConfigServiceImpl extends BaseService implements SysConfigService {

    @Override
    public SysConfigVo getSysConfigVO() {
        return this.sysConfigMapper.queryEntity(true);
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    private boolean init = false;

    private static int CONFIG = 35;

    private static String DEF_LOGO_NAME = "common/resources/login_logo.png";

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private LogService logServiceImpl;

    @Resource
    private DataTableService dataTableServiceImpl;
    @Resource
    private DataStandardMapper dataStandardMapper;

    @Resource
    private DataStandardItemMapper dataStandardItemMapper;

    @Autowired
    private MailSender mailSender;


    @Override
    public SysConfigVo getCacheSysConfigVO() {
        return (SysConfigVo) GTAServletContextListener.getServletContext().getAttribute(SysConfigVo.class.getSimpleName());
    }

    @Override
    public SysConfigVo getBasicSysConfigVO() {
        System.out.println("系统配置信息初始化...start......");
    /*    SysConfigVo newVo = new SysConfigVo();
        try {
            System.out.println("系统配置信息初始化...start......");
            flushSystemInfo(true);
            SysConfigVo vo = this.getCacheSysConfigVO();
            BeanUtils.copyProperties(newVo, vo);
            System.out.println("系统配置信息初始化...over.....");
        } catch (Exception e) {

        }
        newVo.setEmailHost(null);
        newVo.setEmailPwd(null);
        newVo.setEmailSmtpAddr(null);
        newVo.setEmailSmtpPort(0);
        newVo.setEmailUser(null);
        newVo.setSfInit(0);//未初始化中心库*/
        SysConfigVo vo = this.sysConfigMapper.queryEntity(false);
        System.out.println("系统配置信息初始化...over.....");
        return vo;
    }

    @Override
    public boolean updateEmail(SysConfigVo vo) {
        boolean result = false;
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(vo.getEmailSmtpAddr());
            sender.setPort(vo.getEmailSmtpPort());
            sender.setUsername(vo.getEmailUser());
            sender.setPassword(vo.getEmailPwd());
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(vo.getEmailHost());
            helper.setTo("253479240@qq.com");
            helper.setSubject("只是验证");
            helper.setText("验证是否有效邮件配置");
            sender.send(message);
            result = true;
        } catch (Exception e) {
            throw new OrdinaryException("邮件服务器设置无效!请输入有效的邮件服务器配置!");
        }
        if (result) {
            result = sysConfigMapper.updateEmail(vo);
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_System);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改邮件信息：" + vo.getEmailUser());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public boolean updateSys(SysConfigVo vo) {
        LOGGER.info(vo.toString());
        sysConfigMapper.updateSys(vo);
        this.sysConfigMapper.updateSystemNameToMenu(vo.getSysName());
        this.sysConfigMapper.updateSchoolNameToOrg(vo.getSchoolName());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_System);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改系统信息：" + vo.getSysName() + "/" + vo.getSchoolName());

        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean updateSystemLogo(MultipartFile file) throws Exception {
        File tempFile = null;
        try {
            tempFile = new File(System.getProperty("java.io.tmpdir") + UUID.randomUUID().toString() + ".tmp");
            file.transferTo(tempFile);
            if (tempFile.length() > 1024 * 1024) {
                throw new OrdinaryException("图片文件大小超过限制！");
            }
            InputStream in = new FileInputStream(tempFile);
            byte[] bs = new byte[(int) tempFile.length()];
            in.read(bs);
            in.close();
            String logoInfo = Base64.encodeBase64String(bs);
            this.sysConfigMapper.updateSystemLog(logoInfo);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        return true;
    }

    @Override
    public boolean flushSystemInfo(boolean flushImage) throws Exception {
        SysConfigVo vo = this.sysConfigMapper.queryEntity(true);
        SysConfigVo cacheVo = this.getCacheSysConfigVO();
        if (!flushImage) {
            //仅修改系统信息时候，考虑不刷新图片
            vo.setSysLogoUrl(cacheVo.getSysLogoUrl());
        } else {
            String fileName = DEF_LOGO_NAME;
            if (vo.getLogoInfo() != null && vo.getLogoInfo().trim().length() > 0) {
                fileName = "common/resources/" + UUID.randomUUID().toString().replace("-", "") + ".png";
                byte[] bs = Base64.decodeBase64(vo.getLogoInfo());
                String filePath = GTAServletContextListener.getRootpath() + fileName;
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(filePath, false);
                    out.write(bs);
                    out.flush();
                    out.close();
                    out = null;
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
            vo.setSysLogoUrl(fileName);
        }
        //置空，避免内存浪费
        vo.setLogoInfo(null);
        GTAServletContextListener.getServletContext().setAttribute(SysConfigVo.class.getSimpleName(), vo);
        return true;
    }


    @Override
    public boolean initCenterDataBase() {
        if (init) {
            return false;
        }
        try {
            init = true;
            return initCenterDataBase_inner();
        } finally {
            init = false;
        }
    }

    /**
     * 第一步：判断中心库配置是否存在，
     * 第二步：删除中心库所有的表
     * 第三步：初始化建表语句
     * 第四步：开始创建表
     *
     * @return
     */
    private boolean initCenterDataBase_inner() {
        List<DataStandardVo> dataStandardVos = this.dataStandardMapper.queryAll(1);
        List<String> subclassList = new ArrayList<String>();
        for (DataStandardVo vo : dataStandardVos) {
            if (vo.getNodeType() != 3 || StringUtil.isBlank(vo.getTableName())) {
                continue;
            }
            subclassList.add(vo.getCode());
            if (subclassList.size() >= 20) {
                dataTableServiceImpl.createTables(subclassList);
                subclassList.clear();
            }
        }
        if (!subclassList.isEmpty()) {
            dataTableServiceImpl.createTables(subclassList);
        }
        return true;
    }

    @Override
    public boolean addPrimaryKeys() {
        LOGGER.info("第一步：查询当前国家数据标准的子类集合");
        List<DataStandardVo> dataStandardVos = this.dataStandardMapper.queryAll(1);
        for (DataStandardVo vo : dataStandardVos) {
            LOGGER.info("當前的子類編號是：" + vo.getCode());
            DataStandardItemVo dataStandardItemVo = new DataStandardItemVo();
            dataStandardItemVo.setSelectable("M");
            dataStandardItemVo.setItemCode(vo.getCode() + "000");
            dataStandardItemVo.setSubclassCode(vo.getCode());
            dataStandardItemVo.setDataExplain("主鍵哦");
            dataStandardItemVo.setDataLength("10");
            dataStandardItemVo.setDataNullable('N');
            dataStandardItemVo.setDataType("N");
            dataStandardItemVo.setItemName("ID");
            dataStandardItemVo.setItemComment("主鍵");
            dataStandardItemVo.setDataPrimarykey(1);
            this.dataStandardItemMapper.insertDataStandardItemVo(dataStandardItemVo, 1, 1);
            LOGGER.info("插入成功》》》》》》》" + vo.getCode() + "000");
        }
        //  LOGGER.info("第二步開始重新生成建表語句");
        initCenterDataBase_inner();
        return true;
    }

    @Override
    public void saveTransFile(TransFileVo transFileVo) {
        this.sysConfigMapper.saveTransFile(transFileVo);
    }

    @Override
    public void deleteAllFilesInfo(String fileType) {
        this.sysConfigMapper.truncateTransFile(fileType);
    }

    @Override
    public SysConfigVo queryCenterDbInfo() {
        return this.sysConfigMapper.queryCenterDbInfo();

    }

    @Override
    public void saveCenterDbConfig(SysConfigVo vo) {
        sysConfigMapper.saveCenterDbConfig(vo);
    }

    /**
     * 根据vo 判断数据库类型 然后 读取 表信息
     *
     * @param vo
     * @param connection
     * @return
     */
    @Override
    public List<TableEntity> findByConnection(SysConfigVo vo, Connection connection) {
        List<TableEntity> list = new ArrayList<TableEntity>();
        String sql = "";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String dbType = vo.getDbType();
            if (dbType.equals("1")) {
                sql = "SELECT table_name tableName,TABLE_TYPE tableType," +
                        " DATE_FORMAT(CREATE_TIME,'%m-%d-%Y %h:%i %p') create_date," +
                        " DATE_FORMAT(UPDATE_TIME,'%m-%d-%Y %h:%i %p') modify_date" +
                        " FROM information_schema.tables WHERE table_schema='"
                        + vo.getDbName() + "' AND table_type='base table'";
            } else if (dbType.equals("2")) {
                sql = "select table_name,'user_table',to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') create_date," +
                        "to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') modify_date from user_tables";
            } else if (dbType.equals("3")) {
                sql = "select name,type_desc,create_date,modify_date from sys.tables go";
            }
            LOGGER.info(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TableEntity tableEntity = new TableEntity();
                String tableName = (String) rs.getString(1);
                String tableType = (String) rs.getString(2);
                String createDate = (String) rs.getString(3);
                String updateDate = (String) rs.getString(4);
                LOGGER.info(tableName);
                LOGGER.info(tableType);
                LOGGER.info(createDate);
                LOGGER.info(updateDate);
                if (StringUtil.isNotBlank(tableName)) {
                    tableEntity.setTableName(tableName);
                }
                if (StringUtil.isNotBlank(tableType)) {
                    tableEntity.setTableType(tableType);
                }
                if (StringUtil.isNotBlank(createDate)) {
                    tableEntity.setCreateDate(createDate);
                }
                if (StringUtil.isNotBlank(updateDate)) {
                    tableEntity.setUpdateDate(updateDate);
                }
                list.add(tableEntity);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public boolean saveIntoVo(List<TableEntity> tableVos) {
        return this.dataTableServiceImpl.saveIntoCenterTables(tableVos);
    }


    @Override
    public boolean updateCenterDb(SysConfigVo vo) {
        return this.sysConfigMapper.saveCenterDbConfig(vo);
    }
}
