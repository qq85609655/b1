package com.gtafe.framework.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            String dt = dateStr.replaceAll("-", "-");
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt = dt
                        + format.substring(dt.length()).replaceAll(
                        "[YyMmDdHhSs]", "0");
            }
            date = dateFormat.parse(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    public static Date parseDateTime(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception localException) {
        }
        return result;
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatToss(Date date) {
        return format(date, "HH:mm");
    }


    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(1);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static long getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    public static String getDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        if (format == null) {
            format = DATE_FORMAT.LONG;
        }
        return format(new Date(), format);
    }

    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String now() {
        return getDateTime(new Date());
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String now(String format) {
        return format(new Date(), format);
    }

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }


    public static Date addDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        long millis = getMillis(date) + day * 24L * 3600L * 1000L;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public static Date minDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        long millis = getMillis(date) - day * 24L * 3600L * 1000L;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / 86400000L);
    }

    public static int diffDate(String date, String date1) {
       // System.out.println(date + "--date-----" + getMillis(parseDate(date)));
       // System.out.println(date1 + "--date1-----" + getMillis(parseDate(date1)));
        return (int) ((getMillis(parseDate(date)) - getMillis(parseDate(date1))) / 86400000L);
    }

    public static String getMonthBegin(String strdate) {
        Date date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }

    public static String getMonthEnd(String strdate) {
        Date date = parseDate(getMonthBegin(strdate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, 1);
        calendar.add(6, -1);
        return formatDate(calendar.getTime());
    }

    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }


    public static String getNewDateStr(){
        String timeStr = "";
        String createDateTime = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
        if (StringUtil.isNotBlank(createDateTime)) {
            String year1 = createDateTime.substring(0, 4) + "年";
            String yue1 = createDateTime.substring(5, 7) + "月";
            String day1 = createDateTime.substring(8, 10) + "日";
            timeStr = year1 + yue1 + day1;
        }
        return timeStr;
    }
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取星期
     *
     * @return
     */
    public static String getWeek() {
        int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String wk = "";
        switch (week) {
            case 1:
                wk = "星期天";
                break;
            case 2:
                wk = "星期一";
                break;
            case 3:
                wk = "星期二";
                break;
            case 4:
                wk = "星期三";
                break;
            case 5:
                wk = "星期四";
                break;
            case 6:
                wk = "星期五";
                break;
            case 7:
                wk = "星期六";
                break;
        }
        return wk;
    }

    public static String getWeek(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT.LONG);
            Date date = sdf.parse(dateStr);
            SimpleDateFormat weekSdf = new SimpleDateFormat("EEEE");
            String week = weekSdf.format(date);
            if("monday".equals(week.toLowerCase())){
            		return "星期一";
            }
			if("tuesday".equals(week.toLowerCase())){
				return "星期二";      	
			}
			if("wednesday".equals(week.toLowerCase())){
				return "星期三";
			}
			if("thursday".equals(week.toLowerCase())){
				return "星期四";
			}
			if("friday".equals(week.toLowerCase())){
				return "星期五";
			}
			if("saturday".equals(week.toLowerCase())){
				return "星期六";
			}
			if("sunday".equals(week.toLowerCase())){
				return "星期天";
			}
            return week;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据日期获取星期
     *
     * @param date
     * @return
     */
    public static String getWeekByDate(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDateTime(date));
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String wk = "";
        switch (week) {
            case 1:
                wk = "星期天";
                break;
            case 2:
                wk = "星期一";
                break;
            case 3:
                wk = "星期二";
                break;
            case 4:
                wk = "星期三";
                break;
            case 5:
                wk = "星期四";
                break;
            case 6:
                wk = "星期五";
                break;
            case 7:
                wk = "星期六";
                break;
        }
        return wk;
    }

    /**
     * 根据当前日期获取多少天之后的日期
     *
     * @param num
     * @return
     */
    public static Date dateAdd(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, num);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 根据日期获取多少天之后的日期
     *
     * @param num
     * @return
     */
    public static Date dateAdd(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, num);
        return calendar.getTime();
    }

    public static String getKsJsStr(String dd1, String dd2) {
        if (StringUtil.isNotBlank(dd1) && StringUtil.isNotBlank(dd2)) {
            String year1 = dd1.substring(0, 4) + "年";
            String yue1 = dd1.substring(5, 7) + "月";
            String day1 = dd1.substring(8, 10) + "日";
            String year2 = dd2.substring(0, 4) + "年";
            String yue2 = dd2.substring(5, 7) + "月";
            String day2 = dd2.substring(8, 10) + "日";
            String lstkssj = year1 + yue1 + day1;
            String lstjssj = year2 + yue2 + day2;
            String lstriqi = lstkssj + "-" + lstjssj;
          //  System.out.println(lstriqi);
            return lstriqi;
        }
        return "";
    }

    public static String getKsJsStr2(String dd1, String dd2) {
        if (StringUtil.isNotBlank(dd1) && StringUtil.isNotBlank(dd2)) {
            String year1 = dd1.substring(0, 4);
            String yue1 = dd1.substring(5, 7);
            String day1 = dd1.substring(8, 10);
            String year2 = dd2.substring(0, 4);
            String yue2 = dd2.substring(5, 7);
            String day2 = dd2.substring(8, 10);
            String lstkssj = year1 + "-" + yue1 + "-" + day1;
            String lstjssj = year2 + "-" + yue2 + "-" + day2;
            String lstriqi = lstkssj + " ~ " + lstjssj;
          //  System.out.println(lstriqi);
            return lstriqi;
        }
        return "";
    }

    //日期格式
    public interface DATE_FORMAT {
        public static final String LONG = "yyyy-MM-dd HH:mm:ss";
        public static final String SHORT = "yyyy-MM-dd";
        public static final String LONG_NO_SPLIT = "yyyyMMddHHmmss";
    }

    /**
     * 获取某年的所有月份
     *
     * @param yearStr 2017
     * @return
     */
    public static List<String> getMonthBetween(String yearStr) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String beginTime = yearStr + "-01";
        String endTime = yearStr + "-12";
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        Date minDate = DateUtil.parseDate(beginTime);
        Date maxDate = DateUtil.parseDate(endTime);
        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * 获取某月的所有天数
     *
     * @param time 2017-02
     * @return
     */
    public static List<String> getMonthFullDay(String time) {
        String strs[] = time.split("-");
        int year = Integer.parseInt(strs[0]);
        int month = Integer.parseInt(strs[1]);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        List<String> fullDayList = new ArrayList<String>();
        int day = 1;
        Calendar cal = Calendar.getInstance();// 获得当前日期对象
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// 1月从0开始
        cal.set(Calendar.DAY_OF_MONTH, day);// 设置为1号,当前日期既为本月第一天
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 0; j <= (count - 1); ) {
            if (sdf.format(cal.getTime()).equals(getLastDay(year, month)))
                break;
            cal.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
            j++;
            fullDayList.add(sdf.format(cal.getTime()));
        }
        Collections.sort(fullDayList);
        return fullDayList;
    }

    /**
     * 获取当月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDay(int year, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(cal.getTime());
    }


    public static List<String> getYear(String beginTime, String endTime) {
        if (null == beginTime || "".equals(beginTime) || null == endTime || "".equals(endTime)) {
            return null;
        }
        List<String> yearList = new ArrayList<String>();
        try {
            Date start = new SimpleDateFormat("yyyy").parse(beginTime);
            Date end = new SimpleDateFormat("yyyy").parse(endTime);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(start);
            c2.setTime(end);
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            int num = Math.abs(year1 - year2);
            for (int i = 0; i <= num; i++) {
                String y = String.valueOf(year1 + i);
                yearList.add(y);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return yearList;
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param date         当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd   结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin,
                                   String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);

        int strDateN = Integer.parseInt(strDate.substring(0, 3));
        int strDateY = Integer.parseInt(strDate.substring(5, 7));
        int strDateR = Integer.parseInt(strDate.substring(9, 11));
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginN = Integer.parseInt(strDate.substring(0, 3));
        int strDateBeginY = Integer.parseInt(strDate.substring(5, 7));
        int strDateBeginR = Integer.parseInt(strDate.substring(9, 11));
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(11, 13));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(14, 16));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(17, 19));
        // 截取结束时间时分秒
        int strDateEndN = Integer.parseInt(strDate.substring(0, 3));
        int strDateEndY = Integer.parseInt(strDate.substring(5, 7));
        int strDateEndR = Integer.parseInt(strDate.substring(9, 11));
        int strDateEndH = Integer.parseInt(strDateEnd.substring(11, 13));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(14, 16));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(17, 19));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
                return true;
            }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
            else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
            } else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS <= strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    //比较当前时间和获得时间的大小到时分秒
    public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
  //比较当前时间和获得时间的大小到天
    public static int compare_day(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() >= dt2.getTime()) {
				return 1;
			} else {
				return 0;
			} 
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
    public static Date getDateAfter(Date d,int day){  
 	   Calendar now =Calendar.getInstance();  
 	   now.setTime(d);  
 	   now.set(Calendar.DATE,now.get(Calendar.DATE)+day);  
 	   return now.getTime();  
 	}  
    
}