package com.gtafe.framework.base.listener;

import com.gtafe.data.center.dataetl.datasource.service.IDatasourceService;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统组件加载完毕后 检测一些系统配置项： 中心库是否有配置
 *
 * @author 周刚
 * 2017-10-26
 */
@Component("DataSourceCheckListener")
public class SpringContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextRefreshedListener.class);
    @Resource
    private IDatasourceService datasourceServiceImpl;
    @Resource
    private SysConfigService sysConfigServiceImpl;
    
    /**
     * 当一个ApplicationContext被初始化或刷新触发
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            sysConfigServiceImpl.flushSystemInfo(true);
        }catch (Exception e) {
            LOGGER.error("系统信息初始化异常!", e);
            System.exit(1);
        }
        
        List<DatasourceVO> datasourceVOs = this.datasourceServiceImpl.queryCenterData();
        if (datasourceVOs == null || datasourceVOs.isEmpty()) {
            LOGGER.error("请检查 系统表 【data_etl_dataconnection】是否 配置中心库数据!");
            System.exit(1);
        }
        DatasourceVO datasourceVO = datasourceVOs.get(0);
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        if (null != connectDB.getConn()) {

        } else {
            LOGGER.error("请检查 系统表 【data_etl_dataconnection】 中心库表配置是否正确!");
            System.exit(1);
        }
        LOGGER.info("完美！");
    }
}
