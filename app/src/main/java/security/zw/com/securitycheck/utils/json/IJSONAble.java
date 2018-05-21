package security.zw.com.securitycheck.utils.json;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. User: brooksfan Date: 12-12-26 Time: 下午4:41 To change this template
 * use File | Settings | File Templates.
 */
public interface IJSONAble {
    /**
     * 从对象组合成一个JSON的对象
     */
    public JSONObject encodeToJsonObject();


    // 从JSON中分析对象赋值给Fields
    // 有一些字段不能直接使用JSON中来的字段的时候，请重载这个方法，
    // 在调用完这个方法之后再处理其它字段(因为这里会调用initDefaultValue)
    //
    public void parseFromJSONObject(JSONObject obj);

    /**
     * 同一个对象多次调用parseFromJSONObject对象的，请实现这个默认值方法，不让可能会 出现parse之后这些字段还是老的值的问题只实现哪些标记为让这个类类处理的字段，因为
     * 这个方法会被parseFromJSONObject调用
     */
    public void initJSONDefaultValue();
}