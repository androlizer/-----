package org.chinamil;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import java.util.LinkedList;
/**
 * 工具类
 * @author zhang
 *
 */
public class Tools {
    private static int[] deviceWidthHeight = new int[2];

    /*public static Bitmap resizeBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.i("info", width + " " + height);
            Matrix matrix = new Matrix();
            matrix.postScale(Config.scaleWidth, Config.scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }*/

    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }
    public static Bitmap resizeBitmap(String path,int  width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.outWidth = width;
            options.outHeight = height;
           
         Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options.inSampleSize = options.outWidth / height;
    options.inJustDecodeBounds = false;
     bmp = BitmapFactory.decodeFile(path, options);
    return bmp;
    }
   
    
    
    
    public static int[] getDeviceInfo(Context context) {
        if ((deviceWidthHeight[0] == 0) && (deviceWidthHeight[1] == 0)) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metrics);
              deviceWidthHeight[0] = metrics.widthPixels;
            deviceWidthHeight[1] = metrics.heightPixels;
        }
        return deviceWidthHeight;
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
           return false;
        }else {
            return true;
        }
       /* boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        .isConnectedOrConnecting();
        if (!wifi) { // 提示使用wifi
        Toast.makeText(context.getApplicationContext(), "建议您使用WIFI以减少流量！",
       Toast.LENGTH_SHORT).show();
        }*/
    
        }

    /**
     * 判断网络状态
     * @param context
     * @return
     */
 /*   public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (int i = 0; i < netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }*/
   
public static LinkedList<DBdemo> getDB(Cursor c) {
    LinkedList<DBdemo> list=new LinkedList<DBdemo>();
    while(c.moveToNext()){
        list.add(new DBdemo(c.getString(2),c.getString(1)));
    } return list;
    
    
}


}
