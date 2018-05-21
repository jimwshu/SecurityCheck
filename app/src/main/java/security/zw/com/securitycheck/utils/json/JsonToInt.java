package security.zw.com.securitycheck.utils.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//表示那个字段在json存储的时候需要用int来表示咯，1表示true 0表示false，因为后台是这样表示的
//但是为了我们这边处理方便，我们把这样的值定制为boolean
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonToInt {
}


