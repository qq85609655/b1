package com.gtafe.data.center.system.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/30
 * @Description:
 */
@Component
public class BackUpDbTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackUpDbTask.class);

    //每天凌晨1点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
    public void executBackUp() {
        //先备份中心库
        LOGGER.info("############开始备份中心库表####################");
        backUpCenterDb();
        LOGGER.info("############备份中心库表####################成功完毕");
        //再备份系统库
        LOGGER.info("############开始备份本系统####################");
        backUpSystemDb();
        LOGGER.info("############备份本系统库表####################成功完毕");
    }

    private void backUpSystemDb() {

    }

    private void backUpCenterDb() {

    }
}
