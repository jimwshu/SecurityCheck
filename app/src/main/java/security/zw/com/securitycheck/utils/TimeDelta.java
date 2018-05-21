package security.zw.com.securitycheck.utils;

/**
 * Created by power on 14-3-13.
 */
public class TimeDelta {

    public long start= System.currentTimeMillis();

    public long getDelta(){
        return System.currentTimeMillis()-start;
    }

    /**
     * 重设开始
     */
    public void renew(){
        start= System.currentTimeMillis();
    }
}
