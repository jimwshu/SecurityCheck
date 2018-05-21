package security.zw.com.securitycheck.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by chandler on 2017/3/13.
 * From http://blog.csdn.net/xinzheng_wang/article/details/9159969
 */

public class AESCipherUtils {
    private static final String IV = "0102030405060708";
    private static final String CIPHERMODE = "AES/CBC/PKCS7Padding";   //algorithm/mode/padding

    public static String encrypt(String key, String src) throws Exception {
        byte[] result = encrypt(key.getBytes(), src.getBytes());
        return Base64.encodeToString(result, Base64.DEFAULT).replace("\n","");
    }

    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(key.getBytes(), enc);
        return new String(result);
    }

    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHERMODE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV.getBytes()));
        return cipher.doFinal(src);
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }


}



