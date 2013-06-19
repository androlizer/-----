package org.chinamil.networkerr;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Tool {
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
	 public static BitmapDrawable getBitmapDrawable(Resources res,Bitmap bitmap, 
			 int w, int h) {
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
	        
	            return new BitmapDrawable(res,resizedBitmap);
	        } else {
	            return null;
	        }
	    }

}
