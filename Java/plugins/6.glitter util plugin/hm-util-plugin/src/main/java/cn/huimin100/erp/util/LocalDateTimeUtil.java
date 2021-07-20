package cn.huimin100.erp.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDateTimeUtil
 *
 * @author wangshuai
 * @date 2020/6/11 11:56
 **/
public class LocalDateTimeUtil {

    public static final ZoneId zoneId = ZoneId.systemDefault();

    /**
     * 获取某一时刻前推一个月的时间(资质提前一个月提示过期)
     */
    public static Date minusMonths(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTimeToDate(minusMonths(localDateTime));
    }

    public static LocalDateTime minusMonths(LocalDateTime localDateTime) {
        return localDateTime.minusMonths(1L);
    }


    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        long time = date.getTime();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), zoneId);
    }

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = minusMonths(LocalDateTime.now());
        System.out.println(dtf.format(localDateTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = minusMonths(new Date());
        System.out.println(sdf.format(date));

    }
//    public Map<String, String> getContractDeadline() {
//        Map<String, String> timeMap = new HashMap<String, String>();
//        Integer currentNo = 0;
//        Date currentDate = new Date();
//        Calendar current = Calendar.getInstance();
//        current.setTime(currentDate);
//        if (current.get(Calendar.DAY_OF_WEEK) == 1) {
//            // 即已周一为开始日: 当前为本周周日
//            currentNo = 7;
//        } else {
//            // 即已周一为开始日: 当前为本周周几
//            currentNo = current.get(Calendar.DAY_OF_WEEK) - 1;
//        }
//        current.add(Calendar.DAY_OF_MONTH, -(currentNo + 6));
//        timeMap.put("qryStartTime", sdf.format(current.getTime()));
//        current.setTime(currentDate);
//        current.add(Calendar.DAY_OF_MONTH, -(currentNo));
//        timeMap.put("qryEndTime", sdf.format(current.getTime()));
//        return timeMap;
//    }
}
