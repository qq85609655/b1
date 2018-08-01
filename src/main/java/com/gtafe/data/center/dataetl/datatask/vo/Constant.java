package com.gtafe.data.center.dataetl.datatask.vo;

/**
 * 常量
 */
public class Constant {
	/**
	 * 定时任务状态
	 * @author R & D
	 * @email 908350381@qq.com
	 * @date 2016年12月3日 上午12:07:22
	 */
	public enum ScheduleStatus {
		/**
		 * 正常
		 */
		NORMAL(0),
		/**
		 * 暂停
		 */
		PAUSE(1);

		private int value;

		private ScheduleStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
