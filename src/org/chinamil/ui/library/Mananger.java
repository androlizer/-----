package org.chinamil.ui.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.ParserXml;
import org.chinamil.R;
/**
 * 做一个保险
 * 如果没有解压
 * 说明  解压失败
 * 这里解aes算法
 * @author Administrator
 *
 */
public class Mananger   extends Activity implements OnItemClickListener,
        OnItemLongClickListener, android.view.View.OnClickListener,
        OnCheckedChangeListener {

    private  GridView listView;
    private   Myadapter aMyadapter;
    private   Myquery myquery;
    private   LayoutInflater layout;
    private   ProgressBar progrd; 
    private   File topcache;
    private   Mytask taskMytask;
    private   ArrayList<String> weList;//记录删除了那些
    boolean delent = false;
    private   ImageView imageView, delImageView; //返回  清除 按钮
    private  LinearLayout linearLayout;
    private Calendar aCalendar; //日历   辅助分类
    int month;
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mananger);
        getScreenSize();//获取 屏幕尺寸
        weList = new ArrayList<String>();
        linearLayout=(LinearLayout) findViewById(R.id.manageliner);
        progrd = (ProgressBar) findViewById(R.id.progressBar1);
        aCalendar = Calendar.getInstance();
        month = aCalendar.get(Calendar.MONTH) + 1;
        listView = new GridView(this);//new gridview 及设置各项参数
        listView.setNumColumns(4);
  
      //  listView.setSelector(android.R.color.black);
        //(GridView) findViewById(R.id.managelist);
        imageView = (ImageView) findViewById(R.id.fanhui);
        delImageView = (ImageView) findViewById(R.id.shanchu);
       LayoutParams  pppLayoutParams=imageView.getLayoutParams();
       LayoutParams  delImageViewms=delImageView.getLayoutParams();
       //设置顶部图片参数  粗略的
        if (screenHeight>800) {
          pppLayoutParams.height=50;
            pppLayoutParams.width=100;
            delImageViewms.height=50;
            delImageViewms.width=100;
            imageView.setLayoutParams(pppLayoutParams);
            delImageView.setLayoutParams(delImageViewms);
        }
        imageView.setOnClickListener(this);
        delImageView.setOnClickListener(this);
        aMyadapter = new Myadapter(Mananger.this, null);
        topcache = getCacheDir();
        myquery = new Myquery(getContentResolver());
       //查询  暂不加条件
        myquery.startQuery(0, null, Heibai.MANAGE_URI, new String[] { "_id", "imag", "link", "title","month" },
                null,null,
                null);//" month = ?", new String[]{month},
        layout = getLayoutInflater();
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        TextView textView=new TextView(this); //文本框
        textView.setGravity(Gravity.CENTER);
        textView.setText("已下载报刊列表");
        //添加文本框以及gridview
        linearLayout.addView(textView);
        linearLayout.addView(listView);
    }
/**适配器类
 * 
 * @author zhang
 *
 */
    private class Myadapter extends CursorAdapter {

        public Myadapter(Context context, Cursor c) {
            super(context, c);
        }

        
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
	
           
			return layout.inflate(R.layout.gridviewitem, null);
        }

        
        public void bindView(View view, Context context, Cursor cursor) {
            TextView ttTextView = (TextView) view.findViewById(R.id.tv_main_item);
              ttTextView.setText(cursor.getString(3));
             CheckBox checkBox=(CheckBox) view.findViewById(R.id.checkBox1);//选择的性删除  暂时不加
                int i=cursor.getPosition();
             checkBox.setTag(i);
               checkBox.setOnCheckedChangeListener(Mananger.this);
            ImageView iamge = (ImageView) view.findViewById(R.id.gridviewimage);
            LayoutParams dfsdLayoutParams = iamge.getLayoutParams();
            if (screenHeight>800) {
                dfsdLayoutParams.width = (screenWidth - 80) / 4;
                dfsdLayoutParams.height = 200;
            }else {
                dfsdLayoutParams.width = (screenWidth - 40) / 4;
                dfsdLayoutParams.height = 100;
            }
            iamge.setLayoutParams(dfsdLayoutParams);
            taskMytask = new Mytask(iamge); //执行异步加载缩略图
            
          //String ssString= cursor.getString(1);
            taskMytask.execute(cursor.getString(1), 
            		cursor.getString(3));
        }
    }



    private class Myquery extends AsyncQueryHandler {

        public Myquery(ContentResolver cr) {

            super(cr);
        }

        
       

        
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (cursor != null) {  //查询结束设置  适配数据
               
                if (cursor.getCount() > 0) {
                 aMyadapter.changeCursor(cursor);
                    listView.setAdapter(aMyadapter);
                    progrd.setVisibility(View.GONE);
              /*     while (cursor.moveToNext()) {
               System.out.println("数据库内容"+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+" ");
              }
                 */
                } else {
                    Toast.makeText(Mananger.this, Heibai.NO, 1).show();
                    finish();
                }
            } else {
                Toast.makeText(Mananger.this, Heibai.NO, 1).show();
                finish();
            }

        }
    }

    
 

    int screenHeight, screenWidth;

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
      
    }

    private class Mytask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public Mytask(ImageView imaget) {
            this.imageView = imaget;
        }

        
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result); 
        }

        
        protected Bitmap doInBackground(String... params) {
           // Toast.makeText(Mananger.this, topcache.getAbsolutePath() + MD5.getMD5(params[1])+".png", 1).show();
         //   System.out.println();
         // System.out.println( "图片路径"+topcache.getAbsolutePath() + MD5.getMD5(params[1])+".png");
            //创建图片http://wap.chinamil.com.cn/wap-paper/
            if (params[0].contains(ParserXml.urlhead)) {
                return BitmapFactory.decodeFile(topcache.getAbsolutePath() 
                		+ "/"+MD5.getMD5(params[1])+".png");
            }else {
                return BitmapFactory.decodeResource(getResources(),
                		R.drawable.ic_launcher_yuedu);
            }
                 
           
        }

    }

    //阅读pdf
    public void onItemClick(AdapterView<?> parent, View view, int position, 
    		long id) {
        Cursor cursor = (Cursor) aMyadapter.getItem(position);
        String title = cursor.getString(3);
        if (!new File(topcache,MD5.getMD5(title)+".zip").exists()) {
            myquery.startDelete(2, null, Heibai.MANAGE_URI, "title = ?", new String[] { title });
            Toast.makeText(Mananger.this, Heibai.OPENERROR, 1).show(); 
            }else {
                Intent intent=new Intent(Mananger.this,WebviewActivity.class);
                intent.putExtra("who", MD5.getMD5(title));
                intent.putExtra("classify","ccc");
                startActivity(intent);
            }
  //myquery.startDelete(2, null, Heibai.MANAGE_URI, "title = ?", new String[] { title });
    }

    
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ErrorDialog(position);
        return false;
    }
/**
 * 删除 指定的 数据库记录以及文件
 * @param position
 */
    private void del(int position) {

        Cursor cursor = (Cursor) aMyadapter.getItem(position);
        if (cursor!=null) {      
          String title = cursor.getString(3);
        File ffFile = new File(topcache, MD5.getMD5(title) + ".zip");
        File ffFile2 = new File(topcache, MD5.getMD5(title));
        if (ffFile2.exists()) {
            delcache(ffFile2);
        }
     
        if (ffFile.exists()) {
            ffFile.delete();
            myquery.startDelete(1, null, Heibai.MANAGE_URI, "title = ?", new String[] { title });

        } else {
            // 需要Toast.makeText(Mananger.this, "鎶ュ垔涓嶅瓨鍦ㄥ彲鑳藉凡缁忓垹闄�, 1).show();
            myquery.startDelete(2, null, Heibai.MANAGE_URI, "title = ?", new String[] { title });
        }
        weList.add(title);//添加被删除 pdf 的标题
        }
    
    }
/**单个 删除确定
 * 
 * @param i
 */
    public void ErrorDialog(final int i) {

        new AlertDialog.Builder(this).setTitle(Heibai.TISHI).setIcon(R.drawable.imageback)
        .setMessage(Heibai.OK)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
  public void onClick(DialogInterface dialog, int which) {
                        del(i);
                        delent = true;
                                      }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        }
/**
 * 全部清除确定
 */
    public void delDialog() {
        new AlertDialog.Builder(this).setTitle(Heibai.CLEAR).setIcon(R.drawable.imageback)
        .setMessage(Heibai.ALL)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           delall();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        }

    protected void onDestroy() {
        super.onDestroy();
        delent = false;
    }

    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shanchu:
                delDialog();
                break;
            case R.id.fanhui:
                onBackPressed(); // 
            default:
                break;
        }
    }
/**
 * 删除全部
 */
    private void delall() {
       for (int i = 0; i < aMyadapter.getCount(); i++) {
        del(i);
    }
    Toast.makeText(Mananger.this,Heibai.DONE, 0).show();
                   delent = true;

    }

    
    public void onBackPressed() {
        if (delent) {//如果执行了删除  就添加返回数据
            Intent intent = new Intent();
            intent.putStringArrayListExtra("me", weList);
            setResult(9, intent);
        }
 // finish();
        super.onBackPressed();

    }


    //单选按钮监听  暂时不做
    
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    
  //      System.out.println(buttonView.getTag()+"选择按钮的tag");
        
   }
  
    public void delcache(File filename) {
       
        File[] files = filename.listFiles();
        for(File file :files){
          if (file.isDirectory()) {
              delcache(file);
        }else {
            file.delete();
        }
           
            
        }filename.delete();
     
    }
}
