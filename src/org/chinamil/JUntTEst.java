package org.chinamil;

import android.database.Cursor;
import android.database.MergeCursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.test.AndroidTestCase;
import android.util.Log;

public class JUntTEst extends AndroidTestCase{
	
	public void  deail() {
		
		Cursor c = getContext().getContentResolver().query(Data.CONTENT_URI,
	          new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
	          null,
	          null, null);
		while (c.moveToNext()) {
			
			System.out.println(c.getString(1));
		}
		
		}
	public void deail2() {
		String[]  clounm=new String[] {Heibai.PUBLIC, Heibai.CONTENT, Heibai.DATE };
		Cursor  cursor = getContext().getContentResolver().query(Heibai.TEMP_URI, new String[]{"_id","content","date"},
				null, null, null);
		String de="_id  DESC  LIMIT 7 OFFSET "+10*7; //取前7个;
		String where2=Heibai.DATE+" = ?";
		Cursor	mcursor =getContext().getContentResolver() .query(Heibai.DEAIL_URI, clounm, null, null, de);
		Cursor [] cursors=new Cursor[]{cursor,mcursor}; //简单的合并2个cursor
		MergeCursor mergeCursor=new MergeCursor(cursors);
		while (cursor.moveToNext()) {
			Log.i("xx",cursor.getString(3));			
					}
		while (mcursor.moveToNext()) {
			Log.i("xx",mcursor.getString(0));			
					}
		while (mergeCursor.moveToNext()) {
Log.i("xx",mergeCursor.getString(1));			
		}
	}
	
	
}
