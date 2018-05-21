package security.zw.com.securitycheck.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by chandler on 2017/2/27.
 * 解压工具
 * from http://www.cnblogs.com/wainiwann/archive/2013/07/17/3196196.html
 */
public class CompressUtils {
    public interface CompressListener{
        void onFinished();
    }
    public static void  unZipFile(String archive, String decompressDir, CompressListener compressListener)throws IOException, FileNotFoundException, ZipException
    {
        BufferedInputStream bi;
        ZipFile zf = new ZipFile(archive);
        Enumeration e = zf.entries();
        long last = System.currentTimeMillis();
        while (e.hasMoreElements())
        {
            ZipEntry ze2 = (ZipEntry) e.nextElement();
            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;
            if (ze2.isDirectory())
            {
                LogUtils.d("正在创建解压目录 - " + entryName);
                File decompressDirFile = new File(path);
                if (!decompressDirFile.exists())
                {
                    decompressDirFile.mkdirs();
                }
            } else
            {
                LogUtils.d("正在创建解压文件 - " + entryName);
                String fileDir = path.substring(0, path.lastIndexOf("/"));
                File fileDirFile = new File(fileDir);
                if (!fileDirFile.exists())
                {
                    fileDirFile.mkdirs();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + entryName));
                bi = new BufferedInputStream(zf.getInputStream(ze2));
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
                while (readCount != -1)
                {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
                bi.close();
                bos.close();
            }
        }
        zf.close();
        long cur = System.currentTimeMillis();
        LogUtils.e("uncompress time: ", (cur - last) + "");
        //bIsUnzipFinsh = true;
        if (compressListener != null) {
            compressListener.onFinished();
        }
    }
}
