package com.framework.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * x
 * 测试定时任务(演示Demo，可删除) testTask为spring bean的名称
 */
@Component("testTask")
public class TestTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void test(String params) {
        logger.info("定时任务完毕");
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }
}
