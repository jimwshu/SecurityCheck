package security.zw.com.securitycheck.utils.json;

import android.text.TextUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * 可以从JSONObject对象里面解析过来，使用Java的反射 然后名字使用_J_开头后面是JSON的对象名字 如何使用： 这里如果从JSON过来的话，就可能有一个默认值的问题，
 * 如果JSON没有字段，我们就不会做相应的设置，因为这里默认值不大好处理 所以如果有特殊默认值的，并且多次使用的，可以实现initValue方法
 *
 * @author brooksfan
 */
public class JSONUtil {

    // 不要改，不然会死人
    // JSON中的字段名字就是这个前缀之后的
    //private static String FIELDS_PREFIX = "_J_";


    /**
     * 内存引用
     */
    static HashMap<Class, List<Field>> sFieldsCache = new HashMap<Class, List<Field>>();

    /**
     * 获取自己的字段（private也包括，以及自己的父类里面的protected和public字段）
     */
    private static List<Field> getJSONFieldsRecur(Class t) {
        if (t == null) {
            return null;
        }
        Class c = t;
        List<Field> f = new LinkedList<Field>();
        //f.addAll(Arrays.asList(c.getDeclaredFields()));
        do {
            Field[] array = c.getDeclaredFields();
            for (int i = 0; i < array.length; i++) {
                int modify = array[i].getModifiers();
                if ((Modifier.isProtected(modify) || Modifier.isPublic(modify) || (Modifier.isPrivate(modify) && c == t)) &&
                        (!Modifier.isStatic(modify)) &&
                        array[i].getAnnotation(JsonPrivate.class) == null) {
                    f.add(array[i]);
                }
            }
            c = c.getSuperclass();
        } while (c != JSONAble.class);
        return f;
    }

    /**
     * 从对象组合成一个JSON的对象
     */
    public static JSONObject encodeToJsonObject(IJSONAble jsonAble) {

        Class cls = jsonAble.getClass();
        List<Field> fields = sFieldsCache.get(cls);

        if (fields == null) {
            fields = getJSONFieldsRecur(cls);
            sFieldsCache.put(cls, fields);
        }

        Field f;
        String fieldName;

        JSONObject obj = new JSONObject();

        for (int i = 0; i < fields.size(); i++) {
            f = fields.get(i);
            JsonKeyName anotationKey = f.getAnnotation(JsonKeyName.class);

            fieldName = (anotationKey != null && !TextUtils.isEmpty(anotationKey.value())) ? anotationKey.value() : f.getName();
            if (f.getAnnotation(JsonPrivate.class) != null || f.getAnnotation(JsonEncodePrivate.class) != null) {
                continue;
            }
            String jsonFieldName = fieldName;

            Type t = f.getGenericType();

            try {
                f.setAccessible(true);
                //标记为如果是int
                if (t == boolean.class) {
                    JsonToInt annot = f.getAnnotation(JsonToInt.class);
                    boolean value = f.getBoolean(jsonAble);
                    //如果有这个标记，后台就生成为0和1这种数字
                    if (annot != null) {
                        obj.put(jsonFieldName, value ? 1 : 0);
                    } else {
                        obj.put(jsonFieldName, value);
                    }

                } else if (t == int.class) {
                    obj.put(jsonFieldName, f.getInt(jsonAble));
                } else if (t == long.class) {
                    obj.put(jsonFieldName, f.getLong(jsonAble));
                } else if (t == String.class) {
                    Object s = f.get(jsonAble);
                    if (s != null) {
                        obj.put(jsonFieldName, s);
                    }
                } else if (t == double.class) {
                    obj.put(jsonFieldName, f.getDouble(jsonAble));
                } else {
                    //LogUtil.e("un handle json field:" + fieldName);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                f.setAccessible(false);
            }
        }
        return obj;
    }

    // 从JSON中分析对象赋值给Fields
    // 有一些字段不能直接使用JSON中来的字段的时候，请重载这个方法，
    // 在调用完这个方法之后再处理其它字段(因为这里会调用initDefaultValue)
    // 如果你
    public static void parseFromJSONObject(JSONObject obj, IJSONAble jsonAble) {

        if (obj == null) {
            //LogUtil.e("obj is null");
            return;
        }

        Class cls = jsonAble.getClass();
        List<Field> fields = sFieldsCache.get(cls);

        if (fields == null) {
            fields = getJSONFieldsRecur(cls);
            sFieldsCache.put(cls, fields);
        }

        //Class cls = jsonAble.getClass();
        //Field[] fields = cls.getDeclaredFields();
        Field f;
        String fieldName;

        // 设置为初始值
        jsonAble.initJSONDefaultValue();
        int len = fields.size();
        for (int i = 0; i < len; i++) {
            f = fields.get(i);
            JsonKeyName anotationKey = f.getAnnotation(JsonKeyName.class);
            fieldName = (anotationKey != null && !TextUtils.isEmpty(anotationKey.value())) ? anotationKey.value() : f.getName();
            if (f.getAnnotation(JsonPrivate.class) != null) {
                continue;
            }
            String jsonFieldName = fieldName;


            // 默认值在initDefaultValue()里面实现
            // 没有字段以及有可能是null的问题
            if (!obj.has(jsonFieldName) || obj.isNull(jsonFieldName)) {
                continue;
            }

            Type t = f.getGenericType();

            try {
                //修改为可读
                f.setAccessible(true);
                if (t == boolean.class) {
                    // 特殊处理，后台返回如果是数字
                    // 就需要做特殊处理
                    String value = obj.optString(jsonFieldName).toLowerCase();
                    if (TextUtils.isDigitsOnly(value)) {
                        int iValue = Integer.parseInt(value);
                        f.setBoolean(jsonAble, iValue != 0);
                    } else {
                        f.setBoolean(jsonAble, obj.optBoolean(jsonFieldName));
                    }
                } else if (t == String.class) {
                    //JSONArray的object有点问题
                    //上面处理了
                    f.set(jsonAble, obj.optString(jsonFieldName));
                } else if (t == long.class) {
                    f.set(jsonAble, obj.optLong(jsonFieldName));
                } else if (t == int.class) {
                    f.set(jsonAble, obj.optInt(jsonFieldName));
                } else if (t == double.class) {
                    f.set(jsonAble, obj.optDouble(jsonFieldName));
                } else {
                    //LogUtil.e("unhandeld json field:" + jsonFieldName);
                }
            } catch (Exception e) {
                //LogUtil.e("set fields error");
                e.printStackTrace();
            } finally {
                //去掉可读
                f.setAccessible(false);
            }
        }
    }
}
