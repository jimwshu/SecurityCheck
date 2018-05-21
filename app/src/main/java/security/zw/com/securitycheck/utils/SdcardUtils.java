package security.zw.com.securitycheck.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

/**
 * sdcard工具类
 * @author lk
 *
 */
public class SdcardUtils {
	/**
	 * 创建文件夹
	 * @param folderPath
	 * @return
	 * @throws IOException
	 */
	public static boolean createFolder(String folderPath) {
		File folder = new File(folderPath);
		if(!folder.exists()) {
			try {
				return folder.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		return true;
	}
	
	/**
	 * 创建文件
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createNewFile(String filePath, String fileName) throws IOException {
	    if (!isSDCardMounted()) {
	        return null;
	    }
	
	    File folderFile = new File(filePath);
	
	    if (!folderFile.exists()) {
	        folderFile.mkdirs();
	    }
	
	    File file = new File(filePath, fileName);
	    if (file.exists()) {
	        file.delete();
	    }
	
	    file.createNewFile();
	    file.setReadable(true);
	    return file;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File createNewFile(String filePath) throws IOException {
		   if (!isSDCardMounted()) {
	           return null;
	       }
	
	       File file = new File(filePath);
	
	       if (!file.getParentFile().exists()) {
	    	   file.getParentFile().mkdirs();
	       }
	
	       if (file.exists()) {
	           file.delete();
	       }
	
	       file.createNewFile();
	       return file;
	}
	
	
	
	/**
	 * 获取sdcard可用空间大小
	 * @return
	 */
	public static long getAvailableStorage() {
	    if (!isSDCardMounted()) {
	        return 0;
	    }
	
	    try {
	        String storageDirectory = Environment.getExternalStorageDirectory().toString();
	        StatFs stat = new StatFs(storageDirectory);
	        return ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
	    } catch (RuntimeException e) {
	        return 0;
	    }
	}
	
	/**
	 * 判断sdcard是否存在需求大小空间
	 * @param size
	 * @return
	 */
	public static boolean isAvailableSpace(long size) {
		try {
			File path = Environment.getExternalStorageDirectory();
			StatFs statFs = new StatFs(path.getPath());
			long blockSize = statFs.getBlockSize();
			long availableBlocks = statFs.getAvailableBlocks();
			return size < availableBlocks * blockSize;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * sdcard是否挂载
	 * @return
	 */
	public static boolean isSDCardMounted() {
	    return Environment.getExternalStorageState().equals(
	            Environment.MEDIA_MOUNTED);
	}
}
