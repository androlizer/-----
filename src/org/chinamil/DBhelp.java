package org.chinamil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 创建数据库
 * @author zhang
 *
 */
public class DBhelp  extends SQLiteOpenHelper{
    private static DBhelp db;
    public static  synchronized  SQLiteOpenHelper  getIntence(Context context) {
        if (db==null) {
            db=new DBhelp(context, "mananger.db", null, 1);
        }                             
        return db;
    }
    private DBhelp(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE jianbao(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "bigtitle TEXT," + "title TEXT,"+"source TEXT,"
                +"date TEXT,"+"content TEXT)");
           db.execSQL("CREATE TABLE mananger(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "imag TEXT," + "month TEXT,"+ "link TEXT,"+"title TEXT)");
           db.execSQL("CREATE TABLE temp(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		   + "date TEXT,"  + "content TEXT,"+ "path TEXT,"+"title TEXT)");
           db.execSQL("CREATE TABLE deail (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		   + "date TEXT,"  + "content TEXT," + "path TEXT,"+ "title TEXT)");
           
     }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }

}
