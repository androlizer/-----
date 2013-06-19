package org.chinamil.ui.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import org.chinamil.Heibai;
import org.chinamil.R;

public class UrlItem extends Activity implements OnClickListener, OnItemClickListener {

    private Intent intent;
    private TextView bigTextView, titleTextView, contentTextView;
    private ImageButton minButton, bigButton, shearsButton;
    private Myquery query;
    private SharedPreferences sp;
    private Editor editor;
    private String biString, title, content, date,imString;
    private float textsize;
    private ImageView listone, listtwo;
    private ListView lView;
    private View xxx;
    private Mycursor adapter;
private ImageView imageView;
    // private ScrollView scrollView;
    /*
     * intent.putExtra("bigtitle", getBigTitle(str));
     * intent.putExtra("title", getTitle(str));
     * intent.putExtra("content", getContent(str));
     */
    
    @SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.html);
        sp = getSharedPreferences("text", Context.MODE_PRIVATE);
        textsize = sp.getFloat("textsize", 0);
        intent = getIntent();
       imageView=(ImageView) findViewById(R.id.urlitem_image);
        minButton = (ImageButton) findViewById(R.id.min_button);
        bigButton = (ImageButton) findViewById(R.id.big_button2);
        shearsButton = (ImageButton) findViewById(R.id.shears);
        minButton.setOnClickListener(this);
        bigButton.setOnClickListener(this);
        shearsButton.setOnClickListener(this);
        bigTextView = (TextView) findViewById(R.id.bigtitle);
        titleTextView = (TextView) findViewById(R.id.title);
        contentTextView = (TextView) findViewById(R.id.content);
        query = new Myquery(getContentResolver());
        Bundle myBundle = intent.getExtras();
        biString = myBundle.getString("bigtitle", "zz");
        listone = (ImageView) findViewById(R.id.wxnm);
        if ("jiaobao".equals(biString)) {
            xxx = findViewById(R.id.xxx);
            xxx.setVisibility(View.VISIBLE);
            listtwo = (ImageView) xxx.findViewById(R.id.playList222);
            listtwo.setOnClickListener(this);
            lView = (ListView) xxx.findViewById(R.id.pllist);
            adapter = new Mycursor(this, null);
            lView.setAdapter(adapter);
            listone.setOnClickListener(this);
            layout = getLayoutInflater();
            query.startQuery(0, null, Heibai.JIANBAO_URI, new String[] { "_id", "bigtitle", "title", "content" }, null,
                    null, null);
            lView.setOnItemClickListener(this);
        } else {
            listone.setVisibility(View.GONE);
            title = myBundle.getString("title", "");
            content = myBundle.getString("content", "");
            date = myBundle.getString("date", "");
            imString = myBundle.getString("image", "");
            if (imString!=null) {
                imageView.setImageURI(Uri.fromFile(new File(imString)));
            }
            bigTextView.setText(Html.fromHtml(biString));
            titleTextView.setText(Html.fromHtml(title));
            contentTextView.setText("不行");
            contentTextView.setVisibility(View.VISIBLE);
          
        }
        if (textsize != 0) {
            contentTextView.setTextSize(textsize);
        }

        // String quanbu=intent.getStringExtra("quanbu");
        // System.out.println(quanbu);
        /*
         * String bigtitle = intent.getStringExtra("bigtitle");
         * String title =intent.getStringExtra("title");
         * String content= intent.getStringExtra("content");
         * bigTextView.setText(Html.fromHtml(bigtitle));
         * titleTextView.setText(Html.fromHtml(title));
         * contentTextView.setText(Html.fromHtml(content));
         */
        // System.out.println(bigtitle+" "+title+" "+content);

        /*
         * startQuery(1, null, Heibai.JIANBAO_URI,
         * new String[] { "_id", "bigtitle" ,"title","content" },
         * null, null, null);
         */
    }

    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min_button:
                float minsize = contentTextView.getTextSize();
                if (minsize > 15) {
                    contentTextView.setTextSize(minsize - 5);
                }
                break;
            case R.id.big_button2:
                float bigsize = contentTextView.getTextSize();
                if (bigsize < 25) {
                    contentTextView.setTextSize(bigsize + 5);
                }
                break;
            case R.id.playList222:
                xxx.setVisibility(View.GONE);
                listone.setVisibility(View.VISIBLE);
                break;
            case R.id.wxnm:
                xxx.requestFocus();
                lView.setFocusable(true);
                lView.requestFocusFromTouch();
                lView.setVisibility(View.VISIBLE);
                /*
                 * TranslateAnimation showAction = new TranslateAnimation(
                 * Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                 * Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                 * lView.startAnimation(showAction);
                 */
                listone.setVisibility(View.GONE);
                xxx.setVisibility(View.VISIBLE);
                listtwo.setVisibility(View.VISIBLE);

                break;
            case R.id.shears:
                Toast.makeText(UrlItem.this, "裁剪报纸", 1);
                query.startQuery(1, null, Heibai.JIANBAO_URI, new String[] { "title" }, " title = ? ",
                        new String[] { title }, null);
                Toast.makeText(UrlItem.this, "操作已完成", 1).show();
                break;
        }
    }

    
    @SuppressLint("NewApi")
	public void onBackPressed() {
        super.onBackPressed();
        editor = sp.edit();
        Float ff = contentTextView.getTextSize();
        editor.putFloat("textsize", ff);
        editor.commit();
    }

    private class Myquery extends AsyncQueryHandler {

        public Myquery(ContentResolver cr) {
            super(cr);
        }

        
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            switch (token) {// 插入數據庫操作
                case 1:
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToNext();
                        if (!cursor.getString(0).equals(title)) {
                            ContentValues initialValues = new ContentValues();
                            initialValues.put(Heibai.BIGTIELE, biString);
                            initialValues.put(Heibai.TITLE, title);
                            initialValues.put(Heibai.CONTENT, content);
                            initialValues.put(Heibai.DATE, date);
                            query.startInsert(0, null, Heibai.JIANBAO_URI, initialValues);
                        } else {
                        }
                    } else {
                        ContentValues initialValues = new ContentValues();
                        initialValues.put(Heibai.BIGTIELE, biString);
                        initialValues.put(Heibai.TITLE, title);
                        initialValues.put(Heibai.CONTENT, content);
                        initialValues.put(Heibai.DATE, date);
                        query.startInsert(0, null, Heibai.JIANBAO_URI, initialValues);

                    } // SELECT title FROM jianbao WHERE ( where title = ? )

                    break;
                case 0:// 查詢操作
                    cursor.moveToNext();
                    bigTextView.setText(Html.fromHtml(cursor.getString(1)));
                    titleTextView.setText(Html.fromHtml(cursor.getString(2)));
                    contentTextView.setText(Html.fromHtml(cursor.getString(3)));
                    cursor.move(-1);
                    adapter.changeCursor(cursor);
                    lView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }

    }

    private LayoutInflater layout;

    private class Mycursor extends CursorAdapter {

        public Mycursor(Context context, Cursor c) {
            super(context, c);
        }

        
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return layout.inflate(R.layout.jiaobaolistitem, null);
        }

        
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(R.id.text22);
            textView.setText(cursor.getString(1));
        }

    }

    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);
        bigTextView.setText(Html.fromHtml(cursor.getString(1)));
        titleTextView.setText(Html.fromHtml(cursor.getString(2)));
        contentTextView.setText(Html.fromHtml(cursor.getString(3)));
    }
}
