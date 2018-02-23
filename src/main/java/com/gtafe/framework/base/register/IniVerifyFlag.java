package com.gtafe.framework.base.register;

import GTA.License.Register;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IniVerifyFlag implements ServletContextListener {

	private static final int intervalMin = 5; // 定时器间隔时间
	public static  final String productCode ="gtasjzxv1.1";
	public static boolean verifyFlag = false;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			verifyFlag =Register.Check(productCode);
			// 在这里初始化监听器，在tomcat启动的时候监听器启动，可以在这里实现定时器功能
			// 执行多线程
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						// 执行授权验证方法并复制静态变量
						verifyFlag =Register.Check(productCode);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			};

			// 定时器线程池
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
			service.scheduleAtFixedRate(runnable, intervalMin, intervalMin,
					TimeUnit.MINUTES); // 每五分钟执行一次run方法
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}