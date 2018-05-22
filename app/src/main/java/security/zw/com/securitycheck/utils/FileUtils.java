package security.zw.com.securitycheck.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import security.zw.com.securitycheck.SecurityApplication;

public class FileUtils {

	/**
	 * 异步缓存数据
	 * 根据参数分别缓存到本地和内存
	 * 
	 * @param path
	 * @param content
	 * @param url
	 * @return
	 */
	public static void cacheCotent(final String url, final String content, final String path) {

		new Thread("qbk-FileUtl1") {

			@Override
			public void run() {
				// 缓存到内存
				if (url != null) {
					// ContentCache.getInstance().put(Md5.MD5(url), content);
				}
				// 缓存到本地
				if (path != null) {
					saveContent(path, content);
				}
				super.run();
			}

		}.start();
	}

	public static String saveDrawable(Bitmap drawable, String imageName, String path,
                                      Handler handler, boolean isDelete) {

		String result = "";
		Message msg = new Message();

		File Path = new File(path);
		File Image = new File(path, imageName);

		if (!Path.isDirectory())
			Path.mkdirs();
		
		// 删除缓存
		File[] files = Path.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
		
		if (!Image.exists()) {
			try {
				Image.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(Image);
				if (drawable != null && drawable.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
					fileOutputStream.flush();
				}
				fileOutputStream.close();
				if (handler != null) {
					msg.what = 1;
					msg.obj = Image.getPath();
					handler.sendMessage(msg);
				}
				result = Image.getPath();
			} catch (IOException e) {
				Log.e("FileUtils", e.toString() + "--头像保存时出现异常！");
			}
		} else {
			if (isDelete) {
				Image.delete();
				try {
					Image.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(Image);
					if (drawable != null && drawable.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
					fileOutputStream.close();
					if (handler != null) {
						msg.what = 1;
						msg.obj = Image.getPath();
						handler.sendMessage(msg);
					}
					result = Image.getPath();
				} catch (IOException e) {
					if (handler != null) {
						msg.what = 0;
						msg.obj = "图片保存失败！";
						handler.sendMessage(msg);
					}
					e.printStackTrace();
				}
			}
			if (handler != null) {
				msg.what = 1;
				msg.obj = Image.getPath();
				handler.sendMessage(msg);
			}

		}

		return result;
	}

	/**
	 * 保存图片，返回图片路径，为空意味着保存出错。 
	 * @param drawable
	 * @param imageName
	 * @param path
	 * @return
	 */
	public static String saveDrawable(Bitmap drawable, String imageName, String path) {

		String sdcard = DeviceUtils.getSDPath();
		String ret = null;
		
		// 先判断目录是否存在
		File dir = new File(sdcard + File.separator + path);
		if (!dir.exists()) { // 不存在则创建
			dir.mkdirs();
		}
		if (drawable != null && !dir.getPath().equals("")) {
			File image = new File(dir.getPath() + File.separator + imageName);
			try{
				// http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
				File to = new File(image.getAbsolutePath() + System.currentTimeMillis());
				image.renameTo(to); 
				to.delete();
			} catch(Exception e) {
				Logger.getInstance().debug("FileUtils", "saveDrawable", e+"");
			}
			ret = image.getPath();
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(image, false);
				if (drawable.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream)) {
					fileOutputStream.flush();
				}
				fileOutputStream.close();
			} catch (IOException e) {
				Logger.getInstance().debug("FileUtils", "saveDrawable", e+"");
				ret = null;
			}
		} else {
			ret = null;
		}
		return ret;
	}

	/**
	 * 保存内容到缓存文件
	 * 缓存超过3分钟的文件才会重新缓存
	 * 
	 * @param path
	 * @param content
	 */
	public static void saveContent(String path, String content) {
		if (content == null || content.equals(""))
			return;
		if (!path.equals("")) {
			File file = new File(path);
			if (file.exists()) {
				long lastModified = file.lastModified();
				if ((System.currentTimeMillis() - lastModified) > (60000 * 3)) {
					file.delete();
					writeFile(file, content);
				}
			} else {
				writeFile(file, content);
			}

		}
	}

	/**
	 * 写内容到文件
	 * 
	 * @param file
	 * @param content
	 */
	private static void writeFile(File file, String content) {
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			Log.e("FileUtils", "文件缓存出错 path:" + file.getPath());
			e.printStackTrace();
		}
	}

	public static String getContent(String path) throws IOException {
		File file = new File(path);
		return getContent(file);
	}

	/**
	 * 获取指定路径的文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getContent(File file) throws IOException {

		String cacheContent = "";

		byte[] buffer = new byte[1024];
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(new FileInputStream(file));

		int len = -1; // =-1已读到文件末尾
		while ((len = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();

		in.close();
		outStream.close();
		cacheContent = new String(data);
		return cacheContent;

	}
	
    // 递归取得文件夹大小, 并把文件列到list里面去
    public static long getFileSize(File f, List<File> list){
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
        	if(list != null){
        		list.add(flist[i]);
        	}
            if (flist[i].isDirectory()){
                size = size + getFileSize(flist[i], list);
            } else{
                size = size + flist[i].length();
            }
        }
        return size;
    }
    /**
     * if files size greater than maxByte, delete the older( half of total)
     * 
     * @param parentDirectory
     * @param filter
     * @param maxByte
     * @return
     */
    public static int removeOldFiles(File parentDirectory, FilenameFilter filter, long maxByte){
    	int count = 0;
    	if(!parentDirectory.isDirectory()){
    		throw new IllegalArgumentException("the first param is not directory");
    	}
    	File[] files = null;
    	if(filter != null){
    		files = parentDirectory.listFiles(filter);
    	}else{
    		files = parentDirectory.listFiles();
    	}
    	final long totalSize = getFileSize(parentDirectory, null);
    	if(totalSize > maxByte){
    		/* 
    		 * older's index is bigger
    		 * crash-1381203919193.sent
    		 * crash-1381204042722.sent
    		 * crash-1381205103448.sent
    		 * 
    	     */
    		Arrays.sort(files);
    		int length = files.length;
    		for(int i = 0; i < length/2; i++){
    			if(files[i].delete()){
    				count++;
    			}
    		}
    	}
    	return count;
    }

	public static String getPath(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static boolean copyDir(File fromDir, File toDir) {
		if (!fromDir.exists() || !fromDir.isDirectory() || !fromDir.canRead()) {
			return false;
		}

		if (!toDir.getParentFile().exists()) {
			toDir.getParentFile().mkdirs();
		}

		if (!toDir.exists()) {
			toDir.mkdirs();
		}

		File[] files = fromDir.listFiles();

		for (int i=0; i<files.length; i++) {
			File curFile = files[i];
			String target = toDir.getPath() + "/" + curFile.getName();
			File targetFile = new File(target);

			if (curFile.isDirectory()) {
				copyDir(curFile, targetFile);
			} else {
				copyfile(curFile, targetFile, true);
			}
		}

		return true;
	}
	
	public static boolean copyfile(File fromFile, File toFile, boolean rewrite) {
		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead() || fromFile.length() == 0) {
			return false;
		}

		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}

		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		
		boolean result = false;

		FileInputStream fosfrom = null;
		FileOutputStream fosto = null;
		try {
			fosfrom = new FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);
			
			byte bt[] = new byte[4096];

			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			fosto.flush();
			result = true;
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
		} finally {
			if (fosfrom != null) {
				try {
					fosfrom.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fosto != null) {
				try {
					fosto.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static boolean mergeFile(File fromDir, int fileCount, File toFile, boolean rewrite) {
		if (!fromDir.exists() || !fromDir.isDirectory() || !fromDir.canRead() || fromDir.length() == 0) {
			return false;
		}

		try {
            if (toFile.exists() && rewrite) {
                toFile.delete();
            }

            if (!toFile.getParentFile().exists()) {
                toFile.getParentFile().mkdirs();
            }

			toFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean result = false;

        FileChannel inChannel = null;
        FileChannel outChannel = null;
		try {
            outChannel = new FileOutputStream(toFile).getChannel();

			byte bt[] = new byte[1024];
			int c;

			File[] files = fromDir.listFiles();
            if (files != null) {
                File file;
                int len = files.length > fileCount ? fileCount : files.length;
                for (int i = 0; i < len; i++) {
                    file = files[i];
                    if (file != null && file.exists() && file.canRead() && file.length() > 0) {
                        inChannel = new FileInputStream(file).getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);

                        try {
                            inChannel.close();
                            inChannel = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                result = true;
            }
		} catch (Error e) {
            Log.e("readfile", e.getMessage());
            if (toFile.exists() && rewrite) {
                toFile.delete();
            }
//            CrashReport.postCatchedException(e);
        } catch (Exception e) {
			Log.e("readfile", e.getMessage());
			if (toFile.exists() && rewrite) {
				toFile.delete();
			}
//            CrashReport.postCatchedException(e);
		} finally {
			if (inChannel != null) {
				try {
                    inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outChannel != null) {
				try {
                    outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static String getCacheDataPath() {
		return SecurityApplication.getInstance().getAppContext().getCacheDir().getAbsolutePath();
	}
	
	public static boolean deletCacheDataFile(String tag) {
		String followCacheDataPath = FileUtils.getCacheDataPath() + "/" + tag;
		File file = new File(followCacheDataPath);
		return file.delete();
	}

	/**
	 * 删除目录
	 */
	public static synchronized void deleteDir(String path) {
		deleteDir(path, true);
	}

	public static synchronized void deleteDir(String path, boolean includeDir) {
		if (!TextUtils.isEmpty(path) && SdcardUtils.isSDCardMounted()) {
			deleteAll(path, includeDir);
		}
	}

	private static void deleteAll(String path, boolean includeDir) {
		File rootFile = new File(path);
		if (rootFile.isDirectory()) {
			File[] fs = rootFile.listFiles();

			if (fs != null) {
				for (File file : fs) {
					if (null == file || !file.exists()) {
						continue;
					}

					if (file.isDirectory()) {
						deleteAll(file.getAbsolutePath(), true);
					} else if (file.isFile()) {
						file.delete();
					}
				}
			}

			if (includeDir) {
				rootFile.delete();
			}
		}
	}

	/**
	 * 清理该路径下的缓存
	 * @param path 路径
	 * @param num 保留的数目
	 */
    public static void clearOldCache(String path, int num) {
		File root = new File(path);
		if (root.isDirectory()) {
			List<File> files = Arrays.asList(root.listFiles());
			int size = files.size();
			if (size <= num) {
				return;
			}
			Collections.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long diff = f1.lastModified() - f2.lastModified();
					if (diff > 0) {
						return 1;
					} else if (diff == 0) {
						return 0;
					} else {
						return -1;
					}
				}

				public boolean equals(Object obj) {
					return true;
				}
			});

			for (int i = 0; i < size - num; i++) {
				File file = files.get(i);
				if (file.isDirectory()) {
					continue;
				}

				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	public static boolean isFileExist(String filepath) {
		if (TextUtils.isEmpty(filepath)) {
			return false;
		}

		return new File(filepath).exists();
	}

	public static boolean deleteFileIfExist(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}

		File file = new File(path);
		if (file.exists()) {
			return file.delete();
		}

		return false;
	}

	public static long getPathLength(String path) {
		long length = 0;

		TimeDelta delta = new TimeDelta();

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.isFile()) {
                if (file.exists()) {
                    length = file.length();
                } else {
                    File parentFile = file.getParentFile();
                    length = getDirLength(parentFile);
                }
            } else if (path.contains("%d.ts&0-")) {
                File parentFile = file.getParentFile();
                length = getDirLength(parentFile);
            } else {
                length = getDirLength(file);
            }
        }

		LogUtils.d("read file length cost: " + delta.getDelta());
		return length;
	}

	private static long getDirLength(File dir) {
		long length = 0;
		if (dir != null && dir.isDirectory() && dir.exists()) {
			File[] outFiles = dir.listFiles();
			if (outFiles != null) {
				for (int i = 0; i < outFiles.length; i++) {
					length += outFiles[i].length();
				}
			}
		}
		return length;
	}

	public static String getStringFromAssetsFile(String fileName) {
		InputStreamReader inputStreamReader = null;
		try {
			StringBuffer result = new StringBuffer();

			inputStreamReader = new InputStreamReader(SecurityApplication.getInstance().getAppContext().getAssets().open(fileName));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;
			while((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	// 获取连接返回的数据
	public static String readStream(InputStream is, String CHARSET) throws Exception {
		String str = "";
		try {
			if (is != null) {

				BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET));
				String line = null;
				StringBuffer sb = new StringBuffer();
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				str = sb.toString();
				if (str.startsWith("<html>")) // 获取xml或者json数据，如果获取到的数据为html，则为null
				{
					str = "";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Get the external app cache directory.
	 *
	 * @param context The context to use
	 * @return The external cache dir
	 */
	@SuppressLint("NewApi")
	public static File getExternalCacheDir(Context context) {
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	/**
	 * Check if OS version has built-in external cache dir method.
	 */
	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 删除文件（非文件夹）
	 */
	public static void deleteFiles(List<File> files) {
		if (null != files) {
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
					f = null;
				}
			}
		}
	}

	public static byte[] fileToBytes(String filePath) {
		byte[] buffer = null;
		File file = new File(filePath);

		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;

		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();

			byte[] b = new byte[1024];

			int n;

			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}

			buffer = bos.toByteArray();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != bos) {
					bos.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally{
				try {
					if(null!=fis){
						fis.close();
					}
				} catch (IOException ex) {
				}
			}
		}

		return buffer;
	}

	public static void notifyMediaScannerScanFile(Context context, String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			notifyMediaScannerScanFile(context, new File(filePath));
		}
	}

	public static void notifyMediaScannerScanFile(Context context, File imageFile) {
		if (context != null) {
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(imageFile);
			intent.setData(uri);
			context.getApplicationContext().sendBroadcast(intent);
		}
	}

	/**
	 * 通知手机，存储文件发生变化
	 */
	public static void notifyFileChanged(Context context, File file) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				MediaScannerConnection.scanFile(
						context,
						new String[]{file.getAbsolutePath()},
						null,
						new MediaScannerConnection.OnScanCompletedListener() {
							public void onScanCompleted(String path, Uri uri) {
							}
						});
			} else {
				final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
				context.sendBroadcast(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取android 相机拍摄的默认文件夹地址
	 */
	public static File getExternalDCIMDir(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			if (directory != null) {
				File qsbkDCIM = new File(directory, "security");
				if (!qsbkDCIM.exists()) {
					qsbkDCIM.mkdir();
				}
				return qsbkDCIM;
			}
		}

		File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
		if (!path.exists())
			path.mkdir();
		return path;
	}

}
