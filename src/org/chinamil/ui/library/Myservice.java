package org.chinamil.ui.library;
import java.io.File;
import java.security.PublicKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

public class Myservice extends IntentService{
	String path,who;
	//private ExecutorService pool;
	ContentResolver resolver;
	Elements songs=null;
	Document	doc ;
	Elements song_box;
	public Myservice() {
		super("解放军报");
	}
	//Mythread thread;
	@Override
	protected void onHandleIntent(Intent intent) {
		//pool = Executors.newSingleThreadExecutor();
	 path=intent.getStringExtra("path");  //文件的路径  getcacedir()
	 who= MD5.getMD5(intent.getStringExtra("who"));//文件的日期
	 resolver=getContentResolver();
	if (path!=null&&who!=null) {
	//	pool.execute(	new Mythread(new File(path),	MD5.getMD5(who))		);
     File mypath=new File(path);
		try {
			boolean susses=ZipDecrypter.decryptercrypto( new File(mypath,  who + ".zip"),mypath); //解压
		if (susses) {     
			//获取目录  通过缩略图来判断有多少目录
		File [] files=	new File(mypath,who+"/thumbs").listFiles();
		if (files!=null&&files.length>0) {
			for (int x=0;x<files.length;x++) {
			String  quannameString=files[x].getName();
	String nameString=quannameString.substring(0,quannameString.indexOf(".")); // 获取包含 htm 文件的名字
					doc = Jsoup.parse(new File(path,who+"/"+nameString+"/index.htm"), "utf-8");
				 song_box = doc.getElementsByAttribute("onclick");
			for (Element element : song_box) {
				//element.gete
				ContentValues values=new ContentValues();
				values.put(Heibai.DATE,Myservice.this.who);
				values.put(Heibai.CONTENT,element.html());
				values.put(Heibai.PATH,nameString);
			   values.put(Heibai.TITLE,getTitle(element.attr("onclick")));
				resolver.insert(Heibai.DEAIL_URI, values);
			} //2013年4月28日
			doc=null;
			song_box=null;
			System.gc();
			}
		}
		path=null;
		who=null;
		files=null;
		resolver=null;
		System.gc();;
		}
		} catch (Exception e) {
			Log.i("xx", "异常发生");
		}
	
	}
	}
	/**
	 * 获取名字
	 * @param titString
	 * @return
	 */
	public String getTitle(String titString){
		String teString2=titString.substring(titString.lastIndexOf(",")).trim();
		return teString2.substring(2,teString2.length()-3);
	}
	
	
	
	

}
