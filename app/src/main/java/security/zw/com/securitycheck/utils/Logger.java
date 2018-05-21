package security.zw.com.securitycheck.utils;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *	写本地文件的日志系统。 
 *	文件路径/{externalStoragePath}/qsbk.app/log/***.log
 */
public class Logger {

	private SimpleDateFormat mSimpleDateFormat; // for file name
	private SimpleDateFormat mSimpleTimeFormat; // for file content
	private boolean mDebug = false;
	private static String mFileName;
	private final static String LOG_FORMAT = "Time:%s, Tag:%s, Function:%s, Text:%s";
	private final static String LOG_DATA_FORMAT = "yyyy-MM-dd"; // 2014-06-17
	private final static String LOG_TIME_FORMAT = "HH:mm:ss.SSS"; // 13:14:52.343
	public final static String LOG_SUFIXX = ".log";
	public final static String LOG_DIRECTORY = "log";
	public final static String PACKAGE_NAME = "security.app";
	private final static TaskExecutor DEFUALT_TASK_EXECUTOR = TaskExecutor.getInstance();
	private static Logger INSTANCE = null;

	private Logger() {
		mSimpleDateFormat = new SimpleDateFormat(LOG_DATA_FORMAT); //2014-06-17
		mSimpleTimeFormat = new SimpleDateFormat(LOG_TIME_FORMAT); // 13:14:52
		mFileName = new StringBuffer(mSimpleDateFormat.format(new Date())).append(LOG_SUFIXX).toString();
	}
	
	public synchronized static Logger getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}
	
	/**
	 * Write format log to file, will not write file if {@linkplain Logger#setDebug(boolean)} to false.
	 * @param tag
	 * @param content
	 */
	public void debug(String tag, String function, String content){
		LogUtils.d(tag,  function+" "+ content+" ");
		if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(content) || !mDebug){
			return;
		}
		String externalStoragePath = DeviceUtils.getSDPath();
		if(!TextUtils.isEmpty(externalStoragePath)){
			String filePath = new StringBuffer(externalStoragePath).append(File.separator).append(PACKAGE_NAME).append(File.separator).append(LOG_DIRECTORY).toString();
			File f = new File(filePath);
			if(!f.exists()){
				f.mkdirs();
			}
			File saveFile = new File(f, mFileName);
			Date date = new Date();
			String text = String.format(LOG_FORMAT, mSimpleTimeFormat.format(date), tag, function, content) + "\n\n";
			writeFileAsync(saveFile, text, true);
		}
	}
	
	/**
	 * Close IO stream silently.
	 * @param closeable
	 */
	public static void closeSilently(Closeable closeable){
		if(null == closeable){
			return;
		}
		try{
			closeable.close();
		} catch (IOException ioe) {
			
		}
	}
	
	/**
	 * Write string to file. Run on current thread.
	 * @param f
	 * @param content
	 * @param append
	 */
	public void writeFile(File f, String content, boolean append) throws IOException {
		if(f == null || TextUtils.isEmpty(content)){
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f, append);
			fos.write(content.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException ioe){
			ioe.printStackTrace();
			throw ioe;
		} finally {
			closeSilently(fos);
		}
	}
	/**
	 * Write string to file asynchronously.
	 * @param f
	 * @param content
	 * @param append
	 */
	public void writeFileAsync(final File f, final String content, final boolean append){
		TaskExecutor.Task task = new TaskExecutor.SimpleTask(){

			@Override
			public Object proccess() throws Exception {
				try{
					writeFile(f, content, append);
				} catch (IOException e){
					throw new Exception(e);
				}
				return null;
			}
			
		};
		DEFUALT_TASK_EXECUTOR.addTask(task);
	}
	
	/**
	 * Set whether write file or not when {@linkplain #debug(String, String)} invoked.
	 * @param debug
	 */
	public void setDebug(boolean debug){
		this.mDebug = debug;
	}

	public boolean isDebug() {
		return mDebug;
	}

}
