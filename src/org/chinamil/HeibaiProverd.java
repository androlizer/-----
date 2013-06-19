package org.chinamil;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Contacts.Intents.Insert;

import java.util.HashMap;
import java.util.Map;

public class HeibaiProverd extends ContentProvider{
private SQLiteOpenHelper  sqlHelper;
private final static String authority = "mananger";

private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

private final static int mananger = 11;
private final static int jiaobao = 12;
private final static int temp = 13;
private final static int deaila = 14;
/**
 * 内容提供者类
 */
private static Map<String,String> mHEIMap,jiabao,Dbtemp,deail;
static{
	mHEIMap=new HashMap<String, String>();
	jiabao=new HashMap<String, String>();
	Dbtemp=new HashMap<String, String>();
	deail=new HashMap<String, String>();
	matcher.addURI(authority, "mananger", mananger);
	mHEIMap.put(Heibai.LINK, Heibai.LINK);
	mHEIMap.put(Heibai.TITLE, Heibai.TITLE);
	mHEIMap.put(Heibai.PUBLIC, Heibai.PUBLIC);
	   mHEIMap.put(Heibai.IMAG, Heibai.IMAG);
	   mHEIMap.put(Heibai.MONTH, Heibai.MONTH);
	   
	   matcher.addURI(authority, "jianbao", jiaobao);
       jiabao.put(Heibai.PUBLIC, Heibai.PUBLIC);
       jiabao.put(Heibai.BIGTIELE, Heibai.BIGTIELE);
	   jiabao.put(Heibai.BIGTIELE, Heibai.BIGTIELE);
	   jiabao.put(Heibai.TITLE, Heibai.TITLE);
	   jiabao.put(Heibai.CONTENT, Heibai.CONTENT);
	   jiabao.put(Heibai.SOURCE, Heibai.SOURCE);
	   jiabao.put(Heibai.DATE, Heibai.DATE);
	   
		matcher.addURI(authority, "temp", temp);
		 Dbtemp.put(Heibai.PUBLIC, Heibai.PUBLIC);
		  Dbtemp.put(Heibai.CONTENT, Heibai.CONTENT);
		  Dbtemp.put(Heibai.TITLE, Heibai.TITLE);
		  Dbtemp.put(Heibai.DATE, Heibai.DATE);
		  Dbtemp.put(Heibai.PATH, Heibai.PATH);
			matcher.addURI(authority, "deail", deaila);
			 deail.put(Heibai.PUBLIC, Heibai.PUBLIC);
			 deail.put(Heibai.CONTENT, Heibai.CONTENT);
			 deail.put(Heibai.DATE, Heibai.DATE);
			 deail.put(Heibai.PATH, Heibai.PATH);
}
	@Override
	public boolean onCreate() {
		sqlHelper=DBhelp.getIntence(getContext());
		return false;
	}
@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
		int match = matcher.match(uri);
		switch (match) {
		case mananger:
			builder.setTables("mananger");
			builder.setProjectionMap(mHEIMap);
			break;
		case jiaobao:
            builder.setTables("jianbao");
            builder.setProjectionMap(jiabao);
            break;
		case temp:
            builder.setTables("temp");
            builder.setProjectionMap(Dbtemp);
            break;
		case deaila:
            builder.setTables("deail");
            builder.setProjectionMap(deail);
            break;
		default:
			break;
		}
		/*SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = qb.query(db, projectionIn, selection, selectionArgs, null, null, sortOrder);
        //如果我们采用的是CursorAdapter的子类，
        //我们希望当cursor对应的uri发生改变之后，我们的listview能够自动刷新
        ret.setNotificationUri(getContext().getContentResolver(), uri);*/
		SQLiteDatabase db=sqlHelper.getReadableDatabase();
        Cursor curosr=builder.query(db,projection, selection, selectionArgs, null, null, sortOrder);
		curosr.setNotificationUri(getContext().getContentResolver(), uri);
		return curosr;
	}
@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase writableDatabase = sqlHelper.getWritableDatabase();
		long id=0;
		int key=matcher.match(uri);
		switch (key) {
		case mananger:
			id=writableDatabase.insert("mananger", "_id", values);
			break;
		case jiaobao:
			/* initialValues.put(Heibai.BIGTIELE, biString);
             initialValues.put(Heibai.TITLE, title);
             initialValues.put(Heibai.CONTENT, content);
             initialValues.put(Heibai.DATE, date);*/
			String title=values.getAsString(Heibai.TITLE);
			
			/*bigtitle TEXT," + "title TEXT,"+"source TEXT,"
	                +"date TEXT,"+"content TEXT)");*/
		//	String sql1="INSERT INTO jianbo (title, date,content) VALUES ("+title+","+date+","+content+")+where title !="+title;
			
		//	String sql="SELECT COUNT(title) AS CustomerNilsen FROM jianbao WHERE title="+title+")";
			Cursor cursor=writableDatabase.query("jianbao", new String[]{"_id","title"}, "title = ?",
					new String[]{title}, null, null, null);
			if (cursor!=null&&cursor.getCount()>0) {
			}else {
				id=writableDatabase.insert("jianbao", "_id", values);
			}
            break;
		case temp:
            id=writableDatabase.insert("temp", "_id", values);
            break;
		case deaila:
			id=writableDatabase.insert("deail", "_id", values);
            break;
		default:
			break;
		}

	// getContext().getContentResolver().notifyChange(uri, null);
	        Uri uri_result = ContentUris.withAppendedId(uri, id);
		return uri_result;
	}

	/*private class obb extends android.database.ContentObserver{

		public obb(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
		
			super.onChange(selfChange);
		}
		
	}*/
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase writableDatabase = sqlHelper.getWritableDatabase();
		int id=0;
		int key=matcher.match(uri);
		switch (key) {
		case mananger:
			id=writableDatabase.delete("mananger", selection, selectionArgs);
			break;
		  case jiaobao:
	            id=writableDatabase.delete("jianbao", selection, selectionArgs);
	            break;
		  case temp:
	            id=writableDatabase.delete("temp", selection, selectionArgs);
	            break;
		default:
			break;
		}
	    getContext().getContentResolver().notifyChange(uri, null);
		@SuppressWarnings("unused")
        Uri uri_result = ContentUris.withAppendedId(uri, id);
		return id;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
