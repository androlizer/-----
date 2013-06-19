package org.chinamil;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultDownloader {

    private ProgressBarListener listener;
    private File cach;
    private String title;

    public MultDownloader(ProgressBarListener listener, File cach, String title) {
        this.listener = listener;
        this.cach = cach;
        this.title = title;
    }

    /**
     * 下载方法 文件的title 命名
     * 
     * @param path
     * @param context
     * @throws Exception
     */
    public void download(String path, Context context) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "identity"); //取消默认的Gzip 压缩方式
        if (conn.getResponseCode() == 200) {
            int currentlen = 0;
            int filelength = conn.getContentLength();
            String name = MD5.getMD5(title) + ".zip";
            File file = new File(cach, name);
            FileOutputStream acf = new FileOutputStream(file);
            listener.getMax(filelength);
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                acf.write(buffer, 0, len);
                currentlen = currentlen + len;
              /*  if (currentlen == filelength) {
                    is.close();
                    acf.close();
                    System.out.println("下载完毕xxxxxxxxxxxxxxx");
                    listener.downloadsize(currentlen); // 回调
                    System.out.println("下载文件名" + cach.getAbsolutePath() + "xxxxxxxxx" + name);
                boolean success= ZipDecrypter.decrypter(new File(cach, MD5.getMD5(name) + ".zip"), cach);
                    System.out.println("下载文件解压文件" + cach.getAbsolutePath() + "xxxxxxxxx" + name+success);
                } else {*/

                    listener.downloadsize(currentlen); // 回调

              //  }

            }
            is.close();
            acf.close();
           /*
             * is.close();
             * acf.close();
             */

        }

    }

    /**
     * 文件名字
     */
    @SuppressWarnings("unused")
    private String getFilename(String path) {
        String name = path.substring(path.lastIndexOf("."));
        return name;
    }
}
