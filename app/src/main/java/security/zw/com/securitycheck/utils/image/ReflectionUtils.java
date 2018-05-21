package security.zw.com.securitycheck.utils.image;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yrsx on 21/12/2016.
 */

public final class ReflectionUtils {

    public final static Object get(Object receiver, String fieldName) {
        if (receiver == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Field field = receiver.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(receiver);
            field.setAccessible(false);
            return obj;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static Method getMethod(Object receiver, String methodName, Class<?>... parameterType) {
        if (receiver == null || TextUtils.isEmpty(methodName)) {
            return null;
        }
        try {
            Method method = receiver.getClass().getDeclaredMethod(methodName, parameterType);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static Object invokeMethod(Object receiver, Method method, Object... args) {
        if (method == null || receiver == null) {
            return null;
        }
        try {
            method.setAccessible(true);
            Object object = method.invoke(receiver, args);
            method.setAccessible(false);
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
