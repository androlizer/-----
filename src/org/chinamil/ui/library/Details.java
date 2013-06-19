package org.chinamil.ui.library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.chinamil.Heibai;
import org.chinamil.R;
import org.chinamil.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends Activity implements OnClickListener {

   
    private TextView  titleTextView, contentTextView;
    private ImageButton minButton, bigButton, shearsButton;
    private SharedPreferences sp;
    private Editor editor;
    
    private Float textsize;
    Intent intent;
    private Cursor cursor;
    private boolean isJan,more;//用来判断是简报过来 还是 页面点击过来的  是否有多张图片
    private  String image,titleString,date,contenString,who;
    private ArrayList<String> imageUrl;
    private Gallery imageGallery;
    @SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       
        setContentView(R.layout.html);
        sp = getSharedPreferences("text", Context.MODE_PRIVATE);
        textsize = sp.getFloat("textsize", 12);
        intent = getIntent();
   
        titleString=intent.getStringExtra("title");
        who=intent.getStringExtra("who");
        minButton = (ImageButton) findViewById(R.id.min_button);
        bigButton = (ImageButton) findViewById(R.id.big_button2);
        shearsButton = (ImageButton) findViewById(R.id.shears);
        minButton.setOnClickListener(this);
        bigButton.setOnClickListener(this);
        shearsButton.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.title);
            contentTextView = (TextView) findViewById(R.id.content);
        if (textsize != 0) {
            contentTextView.setTextSize(textsize);
        }
        if (who.equals("jianbao")) {
        	
        	 cursor=getContentResolver().query(Heibai.JIANBAO_URI, new String[]{"_id","content","date"},
        		     " title = ?", new String[]{titleString}, null);
		}else {
			isJan=true;
		     image=intent.getStringExtra("image");
			 cursor=getContentResolver().query(Heibai.TEMP_URI, new String[]{"_id","content","date"},
        		     " title = ?", new String[]{titleString}, null);
		}
        if (cursor!=null && cursor.getCount()>0) {
        	cursor.moveToNext();
        	date=cursor.getString(2);
        	titleTextView.setText(Html.fromHtml(titleString));
        	contenString=cursor.getString(1);
        	if (isJan) {//处理图片地址即 src 的属性值
        		Document dc=Jsoup.parse(contenString);
        		Elements essElements=dc.select("img[src$=.jpg]");
        		
    			if (essElements.size()>0) {
    				imageUrl=new ArrayList<String>();
        			imageGallery=(Gallery) findViewById(R.id.urlitem_image);
        			imageGallery.setVisibility(View.VISIBLE);
        			for (Element element : essElements) {
            			imageUrl.add(image+element.attr("src"));
            		}
        			if (imageUrl.size()>1) {
						more=true;
					}
            		imageGallery.setAdapter(new Myadapter());
            	
				}
        		
        		
        		
			}
				contentTextView.setText(Html.fromHtml(contenString));
			
        	
		}
    }

    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min_button:
                if (textsize > 15f) {
                	textsize=textsize - 5f;
                    contentTextView.setTextSize(textsize);
                }
                break;
            case R.id.big_button2:
            	 if (textsize <=30F ) {
                 	textsize=textsize + 5f;
                     contentTextView.setTextSize(textsize);
                 }
                break;
            case R.id.shears:
                Toast.makeText(Details.this, "裁剪报纸", 1);
           /* 裁剪报 =====会再加  
            *   query.startQuery(1, null, Heibai.JIANBAO_URI, new String[] { "title" }, " title = ? ",
                        new String[] { title }, null);*/
                ContentValues initialValues = new ContentValues();
                initialValues.put(Heibai.TITLE, titleString);
                initialValues.put(Heibai.CONTENT, contenString);
                initialValues.put(Heibai.DATE, date);
                getContentResolver().insert(Heibai.JIANBAO_URI, initialValues);
                Toast.makeText(Details.this, "操作已完成", 1).show();
                break;
        }
    }
    @SuppressLint("NewApi")
	public void onBackPressed() {
        super.onBackPressed();
        editor = sp.edit();
        editor.putFloat("textsize", textsize);
        editor.commit();
    getContentResolver().delete(Heibai.TEMP_URI, "title = ?", new String[]{titleString});
    }
 
  @Override
protected void onDestroy() {
	  more=false;
	  isJan=false;
	super.onDestroy();
	
	
}
  //多张图片的 时候  图片 大小参数
  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT);
  
  private class Myadapter extends BaseAdapter {


      
      public int getCount() {
          return imageUrl.size();
      }

      
      public Object getItem(int position) {
          return imageUrl.get(position);
      }

      
      public long getItemId(int position) {
          return position;
      }

      
      public View getView(int position, View convertView, ViewGroup parent) {
          if (convertView == null) {
        	  convertView    = getLayoutInflater().inflate(R.layout.gallyitem, null);
          }
          ImageView text = (ImageView) convertView.findViewById(R.id.gally_item);
     
          try {
        	  if (more) {//有多张的话 统一 大小
        		    lp.height=200;
        	         text.setLayoutParams(lp);
        		  text.setImageBitmap(Tools.resizeBitmap(imageUrl.get(position), 200, 200));
			}else{//单张的话 显示原始大小
				text.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream( imageUrl.get(position))));// 这个可能会快点
			} 
          } catch (FileNotFoundException e) {
              e.printStackTrace();
              finish();
          }
          return convertView;
      }

  }



}
