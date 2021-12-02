package com.topit.mylibrary.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

/**
 * 解压Zip文件工具类
 *
 * @author zhangyongbo
 */
public class ZipUtil {

    private static ZipUtil instance;

    public static ZipUtil getInstance(){
        if (instance==null){
            instance=new ZipUtil();
        }
        return instance;
    }


    /**
     * 解压指定zip文件
     *
     * @param unZipfile 压缩文件的路径
     * @param destFile  　　　解压到的目录
     */
    public  void unZip( String unZipfile, String destFile,ZipUDoCallBack zipUDoCallBack) {
        callback=zipUDoCallBack;
        try {
            BufferedInputStream bi;
            File file = new File(unZipfile);
            ZipFile zipFile = new ZipFile(file, "GBK");

            @SuppressWarnings("rawtypes")
            Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = destFile + "/" + entryName;
                if (ze2.isDirectory()) {
                    System.out.println("正在创建解压目录 - " + entryName);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    System.out.println("正在创建解压文件 - " + entryName);
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(destFile + "/" + entryName));
                    bi = new BufferedInputStream(zipFile.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zipFile.close();
            if (callback!=null){
                callback.onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback!=null){
                callback.onFailed(e.toString());
            }
            LogToFileUtils.write(e.toString());
        }
    }
    public void setZipUnDoCallback(ZipUDoCallBack callback) {
        this.callback = callback;
    }
    private ZipUDoCallBack callback;
    public interface ZipUDoCallBack {
        void onSuccess();

        void onFailed(String error);
    }
}