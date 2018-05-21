package security.zw.com.securitycheck.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yong on 17/3/6.
 */

public class TimeUtils {

    public static boolean isArrivedTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date2 = simpleDateFormat.parse(time);
            long curTime = System.currentTimeMillis();
            return curTime >= date2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String secToHour(long sec) {

        Date dates = new Date(sec);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String times= sdf.format(dates);

        return times;
    }

}
