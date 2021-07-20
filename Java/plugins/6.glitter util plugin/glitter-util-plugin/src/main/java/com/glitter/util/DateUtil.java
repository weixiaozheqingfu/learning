package com.glitter.util;

import com.glitter.threadlocal.RequestTimeContext;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 公共更新工具
 *
 * @author limengjun
 * @date 2020/7/13 10:51
 **/
@Log4j2
public class DateUtil {

    private static final DateTimeFormatter SIMPLE = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String now2Str() {
        return LocalDate.now().format(SIMPLE);
    }

    public static Date getBeforeNDay(Date date, Integer n){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获取对日期操作的类对象
        //两种写法都可以获取到前1天的日期
        // calendar.set(Calendar.DAY_OF_YEAR,calendar1.get(Calendar.DAY_OF_YEAR) -3);
        //在当前时间的基础上获取前1天的日期
        calendar.add(Calendar.DATE, -n);
        //add方法 参数也可传入 月份，获取的是前几月或后几月的日期//calendar1.add(Calendar.MONTH, -3);
        Date result = calendar.getTime();
        return result;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = dateFormat.format(date);
        return str;
    }

    public static String DateToStr(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String str = dateFormat.format(date);
        return str;
    }

    public static Date DateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = DateToStr(date);
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String str = dateFormat.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            log.error(e);
            e.printStackTrace();
        }
        return date;
    }

    public static Date StrToDate(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            log.error(e);
            e.printStackTrace();
        }
        return date;
    }

    public static Date getTimeAfterNDays(Date currentDate, Integer days) {
        Calendar calendar = new GregorianCalendar(); // 定义calendar对象
        calendar.setTime(currentDate); // 把当前系统时间赋值给calendar
        calendar.add(calendar.DATE, days); // 在日期中增加天数,1天
        Date date = calendar.getTime(); // 把calendar转换回日期格式
        return date;
    }

    public static Date get0Date(Date date) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(calendar.getTime());
        System.out.println(format);

        return df.parse(format);
    }

    public static Date get23Date(Date date) throws ParseException {
        log.info("DateUtil.get23Date,输入参数,date:{}", date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        // 分
        calendar.set(Calendar.MINUTE, 59);
        // 秒
        calendar.set(Calendar.SECOND, 59);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(calendar.getTime());
        System.out.println(format);

        Date result = df.parse(format);
        log.info("DateUtil.get23Date,输入参数,date:{},输出参数,result:{}", date, result);
        return result;
    }

    public static Date getLastWeekStartDay(Date date) {
        Map<String, Date> map = DateUtil.getLastWeek(date);
        return map.get("monday");
    }

    public static Date getLastWeekEndDay(Date date) {
        Map<String, Date> map = DateUtil.getLastWeek(date);
        return map.get("sunday");
    }

    public static Date getLastMonthStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public static Date getLastMonthEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    private static Map<String, Date> getLastWeek(Date date) {
        Map<String, Date> map = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();

        if (date != null) {
            cal.setTime(date);
        }

        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
        cal.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
        Date monday = cal.getTime();
        map.put("monday", monday);

        cal.add(Calendar.DATE, 1);
        Date tuesday = cal.getTime();
        map.put("tuesday", tuesday);

        cal.add(Calendar.DATE, 1);
        Date wednesday = cal.getTime();
        map.put("wednesday", wednesday);

        cal.add(Calendar.DATE, 1);
        Date thursday = cal.getTime();
        map.put("thursday", thursday);

        cal.add(Calendar.DATE, 1);
        Date friday = cal.getTime();
        map.put("friday", friday);

        cal.add(Calendar.DATE, 1);
        Date saturday = cal.getTime();
        map.put("saturday", saturday);

        cal.add(Calendar.DATE, 1);
        Date sunday = cal.getTime();
        map.put("sunday", sunday);
        return map;
    }

    public static Date getLastQuarterStartDay(Date date) {
        int currentQuarter = DateUtil.getCurrentQuarter(date);
        // 如果是第一季度,则取上一年第四季度值
        if (currentQuarter == 1) {
            return DateUtil.getQuarterStartDay(DateUtil.getCurrentYear(date) -1, 4);
        } else {
            return DateUtil.getQuarterStartDay(DateUtil.getCurrentYear(date), currentQuarter - 1);
        }
    }

    public static Date getLastQuarterEndDay(Date date) {
        int currentQuarter = DateUtil.getCurrentQuarter(date);
        // 如果是第一季度,则取上一年第四季度值
        if (currentQuarter == 1) {
            return DateUtil.getQuarterEndDay(DateUtil.getCurrentYear(date) -1, 4);
        } else {
            return DateUtil.getQuarterEndDay(DateUtil.getCurrentYear(date), currentQuarter - 1);
        }
    }

    public static Date getLastYearStartDay(Date date) {
        int currentQuarter = DateUtil.getCurrentYear(date);
        String dateStr = (currentQuarter - 1) + "-01-01 00:00:00";
        return DateUtil.StrToDate(dateStr);
    }

    public static Date getLastYearEndDay(Date date) {
        int currentQuarter = DateUtil.getCurrentYear(date);
        String dateStr = (currentQuarter - 1) + "-12-31 23:59:59";
        return DateUtil.StrToDate(dateStr);
    }

    public static Date getLastHalfYearStartDay(Date date) {
        int currentQuarter = DateUtil.getCurrentHalfYear(date);
        // 如果是前半年,则取上一年后半年值
        if (currentQuarter == 1) {
            return DateUtil.getHalfYearStartDay(DateUtil.getCurrentYear(date) -1, 2);
        } else {
            return DateUtil.getHalfYearStartDay(DateUtil.getCurrentYear(date), currentQuarter - 1);
        }
    }

    public static Date getLastHalfYearEndDay(Date date) {
        int currentQuarter = DateUtil.getCurrentHalfYear(date);
        // 如果是前半年,则取上一年后半年值
        if (currentQuarter == 1) {
            return DateUtil.getHalfYearEndDay(DateUtil.getCurrentYear(date) -1, 2);
        } else {
            return DateUtil.getHalfYearEndDay(DateUtil.getCurrentYear(date), currentQuarter - 1);
        }
    }

    /**
     * 获取前/后半年
     * @return
     */
    private static int getCurrentHalfYear(Date date) {
        int result = 0;
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 6) {
            result = 1;
        } else if (currentMonth >= 7 && currentMonth <= 12) {
            result = 2;
        }
        return result;
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getCurrentYear(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取当前季度
     */
    public static int getCurrentQuarter(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int month = c.get(c.MONTH) + 1;
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }

    /**
     * 获取某一年的半年开始时间
     * @param year
     * @param n
     * @return
     */
    private static Date getHalfYearStartDay(int year, int n){
        String dateStr = null;
        if (n == 1) {
            dateStr = year + "-01-01 00:00:00";
        }
        if (n == 2) {
            dateStr = year + "-07-01 00:00:00";
        }
        Date date = DateUtil.StrToDate(dateStr);
        return date;
    }

    /**
     * 获取某一年的半年结束时间
     * @param year
     * @param n
     * @return
     */
    private static Date getHalfYearEndDay(int year, int n){
        String dateStr = null;
        if (n == 1) {
            dateStr = year + "-06-30 23:59:59";
        }
        if (n == 2) {
            dateStr = year + "-12-31 23:59:59";
        }
        Date date = DateUtil.StrToDate(dateStr);
        return date;
    }

    /**
     * 获取某一年的第n季度开始时间
     * @param year
     * @param n
     * @return
     */
    private static Date getQuarterStartDay(int year, int n){
        String dateStr = null;
        if (n == 1) {
            dateStr = year + "-01-01 00:00:00";
        }
        if (n == 2) {
            dateStr = year + "-04-01 00:00:00";
        }
        if (n == 3) {
            dateStr = year + "-07-01 00:00:00";
        }
        if (n == 4) {
            dateStr = year + "-10-01 00:00:00";
        }
        Date date = DateUtil.StrToDate(dateStr);
        return date;
    }

    /**
     * 获取某一年的第n季度结束时间
     * @param year
     * @param n
     * @return
     */
    private static Date getQuarterEndDay(int year, int n){
        String dateStr = null;
        if (n == 1) {
            dateStr = year + "-03-31 23:59:59";
        }
        if (n == 2) {
            dateStr = year + "-06-30 23:59:59";
        }
        if (n == 3) {
            dateStr = year + "-09-30 23:59:59";
        }
        if (n == 4) {
            dateStr = year + "-12-31 23:59:59";
        }
        Date date = DateUtil.StrToDate(dateStr);
        return date;
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     *
     * @param pattern     字符串的格式
     * @param currentDate 被格式化日期
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     */
    public static String formatDate(Date currentDate, String pattern) {
        if (currentDate == null || "".equals(pattern) || pattern == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentDate);
    }


    public static void main(String[] args) throws ParseException {
//        Date now = new Date();
//        System.out.println(now);
//        Date date1 = get0Date(now);
//        System.out.println(date1);
//        Date now = new Date();
//        System.out.println(now);
//        Date date1 = get23Date(now);
//        System.out.println(date1);

//        Date now = new Date();
//        System.out.println(now);
//        Date date1 = getTimeAfterNDays(now, 90);
//        System.out.println(date1);

//        Date monday = DateUtil.get0Date(DateUtil.getLastWeekMonday());
//        System.out.println(monday);
//        Date sunday = DateUtil.get23Date(DateUtil.getLastWeekSunday());
//        System.out.println(sunday);

//        Date lastMonthFirstDay = DateUtil.get0Date(DateUtil.getLastMonthFirstDay());
//        System.out.println(lastMonthFirstDay);
//        Date lastMonthEndDay = DateUtil.get23Date(DateUtil.getLastMonthEndDay());
//        System.out.println(lastMonthEndDay);

//        Date lastMonthFirstDay = DateUtil.get0Date(DateUtil.getLastQuarterStartDay());
//        System.out.println(lastMonthFirstDay);
//        Date lastMonthEndDay = DateUtil.get23Date(DateUtil.getLastQuarterEndDay());
//        System.out.println(lastMonthEndDay);

//        Date lastMonthFirstDay = DateUtil.get0Date(DateUtil.getLastHalfYearStartDay());
//        System.out.println(lastMonthFirstDay);
//        Date lastMonthEndDay = DateUtil.get23Date(DateUtil.getLastHalfYearEndDay());
//        System.out.println(lastMonthEndDay);

//        Date lastMonthFirstDay = DateUtil.get0Date(DateUtil.getLastYearStartDay());
//        System.out.println(lastMonthFirstDay);
//        Date lastMonthEndDay = DateUtil.get23Date(DateUtil.getLastYearEndDay());
//        System.out.println(lastMonthEndDay);

//        long times = 1598486400000L;
//        Date date = new Date(times);
//        System.out.println(date);
//        Date aa = DateUtil.get23Date(date);
//        System.out.println(aa);

//        Date aa = DateUtil.StrToDate("2020-08-26","yyyy-MM-dd");
//        Date date = DateUtil.getLastWeekStartDay(aa);
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-08-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastWeekEndDay(aa);
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-06-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastMonthStartDay(aa);
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-06-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastMonthEndDay(RequestTimeContext.get());
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-06-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastQuarterStartDay(aa);
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-06-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastQuarterEndDay(RequestTimeContext.get());
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2020-07-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastHalfYearStartDay(RequestTimeContext.get());
//        System.out.println(date);

//        Date aa = DateUtil.StrToDate("2019-06-03","yyyy-MM-dd");
//        Date date = DateUtil.getLastHalfYearEndDay(aa);
//        System.out.println(date);

        Date aa = DateUtil.StrToDate("2019-06-03","yyyy-MM-dd");
        Date date = DateUtil.getLastYearEndDay(RequestTimeContext.get());
        System.out.println(date);
    }
}
