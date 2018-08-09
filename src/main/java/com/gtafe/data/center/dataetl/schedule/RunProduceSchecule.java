package com.gtafe.data.center.dataetl.schedule;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 执行目标数据库特定的存过以帮助完成数据改造
 * GTA 115566
 */
@PropertySource("classpath:config.properties")
@Component
public class RunProduceSchecule {

    @Resource
    private DatasourceMapper mapper;

    @Scheduled(cron = "${jobs.schedule}")
    public void run() {
        DateFormat dateFormatterChina = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);//格式化输出
        TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");//获取时区 这句加上，很关键。
        dateFormatterChina.setTimeZone(timeZoneChina);//设置系统时区
        String produceNames = PropertyUtils.getProperty("config.properties", "produceNames");
        String produceDbLink = PropertyUtils.getProperty("config.properties", "produceDbLink");
        System.out.println(produceNames);
        System.out.println(produceDbLink);
        if (StringUtil.isNotBlank(produceNames) && StringUtil.isNotBlank(produceDbLink)) {
            String[] prodstr = produceNames.split(";");
            DatasourceVO datasourceVO = mapper.queryDatasourceInfoById(Integer.parseInt(produceDbLink));
            ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
            Connection connection = null;
            Date nowTime = new Date(System.currentTimeMillis());
            System.out.println(System.currentTimeMillis());
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            String retStrFormatNowDate = sdFormatter.format(nowTime);
            System.out.println(retStrFormatNowDate);
            connection = null;
            try {
                for (String prd : prodstr) {
                    long s = System.currentTimeMillis();
                    System.out.println(" 开始执行存储过程：" + prd);
                    connection = connectDB.getConn();
                    if (connection != null) {
                        CallableStatement cs = connection.prepareCall("{call " + prd + "}");
                        cs.execute();
                        System.out.println("执行结束了");
                        long s2 = System.currentTimeMillis();
                        System.out.println("耗时：" + (s2 - s) / 1000 % 60 + " 秒");       //执行
                        cs.close();
                        connection.close();
                    } else {
                        System.out.println("数据库连接不上啊!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
