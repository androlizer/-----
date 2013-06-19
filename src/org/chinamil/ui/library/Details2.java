package org.chinamil.ui.library;

import org.chinamil.Heibai;
import org.chinamil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Details2 extends Activity implements OnClickListener {

   
    private TextView  titleTextView, contentTextView;
    private ImageButton minButton, bigButton, shearsButton;
    private SharedPreferences sp;
    private Editor editor;
    
    private Float textsize;
    Intent intent;
    private Cursor cursor;
    private  String image,titleString,date,contenString,who;
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
		     image=intent.getStringExtra("image");
			 cursor=getContentResolver().query(Heibai.TEMP_URI, new String[]{"_id","content","date"},
        		     " title = ?", new String[]{titleString}, null);
		}
        if (cursor!=null && cursor.getCount()>0) {
        	cursor.moveToNext();
        	date=cursor.getString(2);
        	titleTextView.setText(Html.fromHtml(titleString));
        	contenString=cursor.getString(1);
              	 ImageGetter imgGetter=new ImageGetter() {
        			public Drawable getDrawable(String source) {
        				source=image+source;
        			       Drawable drawable=null;
        				if (source!=null) {
        				/*    drawable 图片	    
        				 * int rId=Integer.parseInt(source);
        					    drawable=getResources().getDrawable(rId);
        					    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        					    return drawable;	
        					    */
        					// sd卡图片
        					   try {    drawable=Drawable.createFromPath(source);
        		         drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        					    return drawable;
        					    //网络图片   需要另开线程
        					/*    try {
        					        url = new URL(source);
        					        drawable = Drawable.createFromStream(url.openStream(), "");
        					    } catch (Exception e) {
        					        e.printStackTrace();
        					        return null;
        					    }
        					    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());            
        					    return drawable;*/
        					   } catch (Exception e) {
        					        return null;
        					    }}
        				    return null;
        		    }
        		};
        	
				contentTextView.setText(Html.fromHtml(contenString,imgGetter,null));
        	
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
                Toast.makeText(Details2.this, "裁剪报纸", 1);
           /* 裁剪报 =====会再加  
            *   query.startQuery(1, null, Heibai.JIANBAO_URI, new String[] { "title" }, " title = ? ",
                        new String[] { title }, null);*/
                ContentValues initialValues = new ContentValues();
                initialValues.put(Heibai.TITLE, titleString);
                initialValues.put(Heibai.CONTENT, contenString);
                initialValues.put(Heibai.DATE, date);
                getContentResolver().insert(Heibai.JIANBAO_URI, initialValues);
                Toast.makeText(Details2.this, "操作已完成", 1).show();
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
 
	
  



}
