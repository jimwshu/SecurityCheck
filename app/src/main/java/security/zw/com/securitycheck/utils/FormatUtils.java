package security.zw.com.securitycheck.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by luohong on 2017/1/12.
 */

public class FormatUtils {

    public static String formatCoupon(long coupon) {
        String formatted;
        if (coupon >= 10000) {
            double cp = (double) coupon / 10000.0f;
            DecimalFormat df;
            if  (coupon >= 100000) {
                df = new DecimalFormat("#");
            } else {
                df = new DecimalFormat("#.0");
            }
            df.setRoundingMode(RoundingMode.DOWN);
            formatted = df.format(cp) + "万";
        } else {
            formatted = String.valueOf(coupon);
        }
        return formatted;
    }

    public static String formatBalance(long balance) {
        String formatted;
        if (balance >= 1000000) {
            double cp = (double) balance / 10000.0f;
            DecimalFormat df = new DecimalFormat("#");
            df.setRoundingMode(RoundingMode.DOWN);
            formatted = df.format(cp) + "万";
        } else {
            formatted = String.valueOf(balance);
        }
        return formatted;
    }

    public static String formatResultMoney(long balance) {

        return (BigDecimal.valueOf(balance).divide(new BigDecimal(100)).setScale(2)).toString();
    }
}
