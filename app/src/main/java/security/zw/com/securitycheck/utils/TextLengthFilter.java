package security.zw.com.securitycheck.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import security.zw.com.securitycheck.utils.toast.ToastUtil;

/**
 * 
 * 按中文2个字符，英文1个字符计算长度
 * @author yong
 *  http://blog.csdn.net/songzhiyong1121/article/details/8758902
 *  mAX_EN 限制输入框  最长输入字符数
 *  warnMsg 字符数超出时的提示语
 *  
 */

public class TextLengthFilter implements InputFilter {
	
	private int MAX_EN;									// 最大英文/数字长度 一个汉字算两个字母
	private static String regEx = "[\u4e00-\u9fa5]|[\ufe30-\uffa0]|"  // unicode编码，判断是否为汉字
			+"[\u3001\u3002\u300a\u300b\u3010\u3011\uff5b\uff5d\uffe5\uff1c\uff1e]"; 	
	private int mWarnMsg;								// 字符数超出的提示语
	
	public TextLengthFilter(int mAX_EN, int warnMsg){
		super();
		MAX_EN = mAX_EN;
		mWarnMsg = warnMsg;
	}
	
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
		int destCount = dest.toString().length()
				+ getChineseCount(dest.toString());
		int sourceCount = source.toString().length()
				+ getChineseCount(source.toString());
		if (destCount + sourceCount > MAX_EN) {
			ToastUtil.Short(mWarnMsg);
			return "";
		}else {
			return source;
		}
	}

	public static int getChineseCount(String string) {
		int count = 0;
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(string);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
	}

}
