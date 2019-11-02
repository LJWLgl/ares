package cn.ganzhiqiang.ares.common.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static final long OneDay = 24 * 60 * 60 * 1000;
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final long MILLISECONDS_FOR_ONE_MINUTE = 60 * 1000;
  public static final long MILLISECONDS_FOR_ONE_HOUR = 60 * MILLISECONDS_FOR_ONE_MINUTE;
  public static final long MILLISECONDS_FOR_ONE_DAY = 24 * MILLISECONDS_FOR_ONE_HOUR;


  /**
   * timestamp to yyyy-MM-dd HH:mm:ss
   * 别用了,代码质量太差了,用 Joda
   */
  @Deprecated
  //CHECKSTYLE:OFF
  public static String TimestampToStrDate(long timestamp) {
    //CHECKSTYLE:ON

    return TimestampToStrDate("yyyy-MM-dd HH:mm:ss", timestamp);
  }

  /**
   * timestamp to dateFormat Date
   * 别用了,代码质量太差了,用 Joda
   */
  @Deprecated
  //CHECKSTYLE:OFF
  public static String TimestampToStrDate(String dateFormat, long timestamp) {
    //CHECKSTYLE:ON
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    return df.format(new Date(timestamp));
  }

  public static long strDateToTimestamp(String date) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      return df.parse(date).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static String dateToString(Date date) {
    SimpleDateFormat formater = new SimpleDateFormat(DATETIME_FORMAT);
    return formater.format(date);
  }

  public static long dayTimestamp() {
    long time = System.currentTimeMillis();

    long days = time / OneDay;
    return days * OneDay - 8 * 60 * 60 * 1000;
  }

  /**
   * n 天前时间
   * 别用了,代码质量太差了,用 Joda
   */
  //CHECKSTYLE:OFF
  @Deprecated
  public static long nDaysAgo(long startDate, int n) {
    return startDate - n * OneDay;
  }
  //CHECKSTYLE:ON

  /**
   * 绝对值显示时间规则 http://wiki.dev.duitang.com/pages/viewpage.action?pageId=10224016
   * 别用了,代码质量太差了,用 Joda
   */
  //CHECKSTYLE:OFF
  @Deprecated
  public static long nDaysAgo(Date date, int n) {
    return nDaysAgo(date.getTime(), n);
  }
  //CHECKSTYLE:ON

  /**
   * 全站统一时间显示格式
   * * @return 格式化的日期字符串
   *     当天时间 今天 12:00
   *     隔天时间 昨天 12:00
   *     当年时间 5月5日 12:00
   *     隔年时间 2015年5月5日 12:00
   */
  public static String date2NapiOldDatetimeFormat(Date date) {
    Calendar dateCalendar = DateUtils.toCalendar(date);
    Date now = new Date();
    Calendar nowCalendar = DateUtils.toCalendar(now);
    if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)) {
      if (DateUtils.isSameDay(date, now)) {
        return String.format("今天 %d:%02d", dateCalendar.get(Calendar.HOUR_OF_DAY),
            dateCalendar.get(Calendar.MINUTE));
      } else if (DateUtils.isSameDay(date, DateUtils.addDays(date, -1))) {
        return String.format("昨天 %d:%02d", dateCalendar.get(Calendar.HOUR_OF_DAY),
            dateCalendar.get(Calendar.MINUTE));
      } else {
        return String.format("%d月%d日 %d:%02d", dateCalendar.get(Calendar.MONTH) + 1,
            dateCalendar.get(Calendar.DAY_OF_MONTH),
            dateCalendar.get(Calendar.HOUR_OF_DAY), dateCalendar.get(Calendar.MINUTE));
      }
    } else {
      return String.format("%d年%d月%d日 %d:%02d", dateCalendar.get(Calendar.YEAR),
          dateCalendar.get(Calendar.MONTH) + 1,
          dateCalendar.get(Calendar.DAY_OF_MONTH), dateCalendar.get(Calendar.HOUR_OF_DAY),
          dateCalendar.get(Calendar.MINUTE));
    }
  }

  /**
   * 倒计时显示规则 http://wiki.dev.duitang.com/pages/viewpage.action?pageId=10224016 1分钟内显示“刚刚”
   * 1小时内按分钟显示，如“1分钟前”、“12分钟前”、“55分钟前”... 1天内按小时显示，如“1小时前”、“12小时前”、“22小时前”...
   * 1月内按天显示，如“1天前”、“12天前”、“28天前”... 1年内按月显示，如“1个月前”、“6个月前”、“11个月前”... 大于1年按年显示，如“1年前”、“3年前”...
   */
  public static String date2Readable(Date date) {
    Date now = new Date();
    final long minute = 60;
    final long hour = minute * 60;
    final long day = 24 * hour;
    final long month = 30 * day;
    final long year = 365 * day;
    long deltaInSeconds = (now.getTime() - date.getTime()) / 1000;
    if (deltaInSeconds < 60) {
      return "刚刚";
    } else if (deltaInSeconds < hour) {
      return String.format("%d分钟前", deltaInSeconds / minute);
    } else if (deltaInSeconds < day) {
      return String.format("%d小时前", deltaInSeconds / hour);
    } else if (deltaInSeconds < month) {
      return String.format("%d天前", deltaInSeconds / day);
    } else if (deltaInSeconds < year) {
      return String.format("%d个月前", deltaInSeconds / month);
    } else {
      return String.format("%d年前", deltaInSeconds / year);
    }
  }

  /**
   * 全站时间展示规范
   * 1分钟内：刚刚
     超过1分钟并在1小时内：某分钟前 （1分钟前）
     超过1小时并在当日内：某小时前（1小时前）
     昨天：昨天 + 小时分钟（昨天 08:30）
     昨天之前并在当年内：某月某日 + 小时分钟（1月1日 08:30）
     隔年：某年某月某日 + 小时分钟（2015年1月1日 08:30）
   */
  public static String standardNapiDate(Date date) {
    Date now = new Date();
    long deltaMilliSeconds = now.getTime() - date.getTime();
    Calendar dateCalendar = DateUtils.toCalendar(date);
    Calendar nowCalendar = DateUtils.toCalendar(now);

    if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)) {
      if (DateUtils.isSameDay(date, now)) {
        if (deltaMilliSeconds < MILLISECONDS_FOR_ONE_MINUTE) {
          return "刚刚";
        } else if (deltaMilliSeconds < MILLISECONDS_FOR_ONE_HOUR) {
          return String.format("%d分钟前", deltaMilliSeconds / MILLISECONDS_FOR_ONE_MINUTE);
        } else if (deltaMilliSeconds < MILLISECONDS_FOR_ONE_DAY) {
          return String.format("%d小时前", deltaMilliSeconds / MILLISECONDS_FOR_ONE_HOUR);
        }
      }

      if (DateUtils.isSameDay(date, DateUtils.addDays(now, -1))) {
        return String.format("昨天 %d:%02d", dateCalendar.get(Calendar.HOUR_OF_DAY),
            dateCalendar.get(Calendar.MINUTE));
      } else {
        return String.format("%d月%d日 %d:%02d", dateCalendar.get(Calendar.MONTH) + 1,
            dateCalendar.get(Calendar.DAY_OF_MONTH),
            dateCalendar.get(Calendar.HOUR_OF_DAY), dateCalendar.get(Calendar.MINUTE));
      }
    } else {
      return String.format("%d年%d月%d日 %d:%02d", dateCalendar.get(Calendar.YEAR),
          dateCalendar.get(Calendar.MONTH) + 1,
          dateCalendar.get(Calendar.DAY_OF_MONTH), dateCalendar.get(Calendar.HOUR_OF_DAY),
          dateCalendar.get(Calendar.MINUTE));
    }
  }
}
