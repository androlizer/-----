package org.chinamil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class Dolond {
public static void getPdf(String path,File file) throws Exception {
 URL url = new URL(path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setConnectTimeout(5000);
    conn.setRequestMethod("GET");
    if(conn.getResponseCode() == 200){
        InputStream is = conn.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer)) != -1){
            fos.write(buffer, 0, len);  
        }
        is.close();
        fos.close();
     }}

}
