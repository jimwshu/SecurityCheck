package security.zw.com.securitycheck.utils.json;

import org.json.JSONObject;

import java.io.Serializable;


/**
 * 可以从JSONObject对象里面解析过来，使用Java的反射 默认解析非abc字段 如何使用： 这里如果从JSON过来的话，就可能有一个默认值的问题，
 * 如果JSON没有字段，我们就不会做相应的设置，因为这里默认值不大好处理 所以如果有特殊默认值的，并且多次使用的，可以实现initJSONDefaultValue方法
 *
 * @author brooksfan
 */
public class JSONAble implements IJSONAble, Serializable {

    public void initJSONDefaultValue() {
    }

    /**
     * 从对象组合成一个JSON的对象
     */
    public JSONObject encodeToJsonObject() {
        return JSONUtil.encodeToJsonObject(this);
    }

    // 从JSON中分析对象赋值给Fields
    // 有一些字段不能直接使用JSON中来的字段的时候，请重载这个方法，
    // 在调用完这个方法之后再处理其它字段(因为这里会调用initJSONDefaultValue)
    // 如果你
    public void parseFromJSONObject(JSONObject obj) {
        JSONUtil.parseFromJSONObject(obj, this);
    }

    public static class A extends JSONAble {
        public static int st;
        public String a;
        public String b;

        private String privatea = "should in a but should not in b";
        @JsonPrivate
        public String c;
    }

    public static class B extends A {
        public String d;

        @JsonKeyName("new_e")
        public String e;
    }
}
