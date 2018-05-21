package security.zw.com.securitycheck.utils;

import android.content.Context;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;

public class DateUtil {
	public static String getToday(Date today) {
		String todayString = new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());
		return todayString;
	}
	
	public static String getDateStr(long created_at) {
		Date date = new Date(created_at);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDate
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(Date specifiedDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDate);

		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDate
	 * @return
	 */
	public static String getSpecifiedDayAfter(Date specifiedDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDate);

		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 *
	 * @param time
	 * @return
	 */
	public static String getTimePostStr(long time) {
		return getAccuracyTimePostStr(time * 1000);
	}

	public static String getAccuracyTimePostStr(long time) {
		long seconds = (System.currentTimeMillis() - time) / 1000;

		Context context = SecurityApplication.getInstance().getApplicationContext();
		if (seconds > 60 * 60 * 24) {
			int tmp = (int) seconds / (60 * 60 * 24);
			return String.format(context.getString(R.string.nearby_days_before), tmp);
		} else if (seconds > 60 * 60) {
			int tmp = (int) seconds / (60 * 60);
			return String.format(context.getString(R.string.nearby_hours_before), tmp);
		} else if (seconds > 60) {
			int tmp = (int) seconds / (60);
			return String.format(context.getString(R.string.nearby_minutes_before), tmp);
		} else {
			return context.getString(R.string.seconds_before);
		}
	}

	public static StringBuffer getDiffDateInfo(Context context, long post_time, int isLater) {
		StringBuffer result = new StringBuffer();

		Calendar today = Calendar.getInstance();

		int seconds = 0;
		if (isLater > 0) {
			seconds = (int) (post_time - today.getTimeInMillis()) / 1000;
		} else {
			seconds = (int) (today.getTimeInMillis() - post_time) / 1000;
		}

		if (seconds > 60 * 60 * 24 * 30) {
			result.append(getDateStr(post_time));
		} else if (seconds > 60 * 60 * 24) {
			int tmp = seconds / (60 * 60 * 24);
			result.append(String.format(context.getString(R.string.days_before), tmp));
		} else if (seconds > 60 * 60) {
			int tmp = seconds / (60 * 60);
			result.append(String.format(context.getString(R.string.hours_before), tmp));
		} else if (seconds > 60) {
			int tmp = seconds / (60);
			result.append(String.format(context.getString(R.string.minutes_before), tmp));
		} else if (seconds > 0) {
			result.append(context.getString(R.string.seconds_before));
		} else {
			result.append(context.getString(R.string.seconds_before));
		}

		return result;
	}

	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static String getTimeOrYestody(String strDate) {
		String time[] = strDate.split(" ");
		Date date = strToDateLong(strDate);
		Date today = new Date();
		if (time.length == 2) {
			if (date.getYear() == today.getYear() && date.getMonth() == today.getMonth() && date.getDay() == today.getDay()) {
				return time[1];
			} else {
				return "昨天 " + time[1];
			}
		}
		return "";
	}
}
