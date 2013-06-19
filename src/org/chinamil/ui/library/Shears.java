package org.chinamil.ui.library;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.chinamil.Heibai;
import org.chinamil.R;
@SuppressWarnings("unused")
public class Shears extends Activity implements OnItemClickListener, OnClickListener {

    private ListView listView;
    private Myquery query;
    private LayoutInflater layout;
    private Mycursor adapter;
    private ImageButton minButton, bigButton, searchButton, delButton;
    private LinkedList<TextView> list;
    private LinkedList<CheckBox> checklist;
    private  ProgressBar progressDialog;
    Display display ;
    int width;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.listview);
         display = getWindowManager().getDefaultDisplay();
         width=display.getWidth();
        progressDialog=(ProgressBar) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.jianbaovontent);
        bigButton = (ImageButton) findViewById(R.id.big_button3);
        minButton = (ImageButton) findViewById(R.id.min_button3);
        delButton = (ImageButton) findViewById(R.id.del);
        searchButton = (ImageButton) findViewById(R.id.seach);
        bigButton.setOnClickListener(this);
        minButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        delButton.setOnClickListener(this);
        layout = getLayoutInflater();
        adapter = new Mycursor(this, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        query = new Myquery(getContentResolver());
        query.startQuery(0, null, Heibai.JIANBAO_URI, new String[] {
                "_id", "bigtitle", 
                "title", "date", "content" },
                null, null, Heibai.DATE + " desc");
        list = new LinkedList<TextView>();
        checklist = new LinkedList<CheckBox>();
        delLinkedList = new LinkedList<String>();
    }

    TextView ttTextView;

    private class Mycursor extends CursorAdapter {

        String newdate, olddate;

        public Mycursor(Context context, Cursor c) {
            super(context, c);
        }

        
        public int getCount() {
            // TODO Auto-generated method stub
            return super.getCount(); // 不能够动态的修改

        }
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final Cace cace = new Cace();
            View view = layout.inflate(R.layout.jianbao, null);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            cace.checkBox = checkbox;
            checklist.add(checkbox);
            TextView textView = (TextView) view.findViewById(R.id.jianbaotitle);
            
           
            TextView textView2 = (TextView) view.findViewById(R.id.date);
            TextView textView3 = (TextView) view.findViewById(R.id.content);
            TextView textViewd = (TextView) view.findViewById(R.id.jianbaodate);
            TextView get = (TextView) view.findViewById(R.id.get);
            if (width<=480) {
           	 textView.setTextSize(12);
           	textView.setMaxWidth(150);
           	textView.setMaxLines(1);
           	textView2.setTextSize(12);
           	get.setTextSize(12);
           	
			}
            cace.tv_body = textView3;
            cace.tv_date = textView2;
            cace.tv_name = textView;
            cace.tv_order = textViewd;
            view.setTag(cace);
            return view;
        }

        
        public void bindView(View view, Context context, Cursor cursor) {
            Cace cace = (Cace) view.getTag();
            TextView textViewd = cace.tv_order;
             String bigtitleString = cursor.getString(2);
          if (mode == DISPLAYMODE.edit) {
                if (delLinkedList.contains(bigtitleString)) {
                    cace.checkBox.setChecked(true);
                }else {
                    cace.checkBox.setChecked(false);
                }

            }
            if (textViewd.getVisibility() == View.VISIBLE) {
                textViewd.setVisibility(View.GONE);
            }
            int position = cursor.getPosition();
            if (position != 0) {
                newdate = cursor.getString(3);
                cursor.moveToPosition(position - 1);
                olddate = cursor.getString(3);
                if (!newdate.equals(olddate)) {
                    textViewd.setText(newdate);
                    textViewd.setVisibility(View.VISIBLE);
                }
                cursor.moveToPosition(position);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                String c = sdf.format(System.currentTimeMillis());
                textViewd.setVisibility(View.VISIBLE);
                 textViewd.setText(cursor.getString(3));
            }
            TextView textView = cace.tv_name;
            String titString=cursor.getString(2);
            if (titString.length()>10) {
                textView.setText(Html.fromHtml(titString.substring(0, 10)));
            }else {
                textView.setText(Html.fromHtml(titString));
            }
            TextView textView2 = cace.tv_date;
            textView2.setText("日期：" + cursor.getString(3));
            TextView textView3 = cace.tv_body;
            list.add(textView3);
     /*       String content = cursor.getString(4).replaceAll("<p>", "");
            content = content.replaceAll("</p>", "");*/
            textView3.setText(Html.fromHtml(cursor.getString(4)) + "......");
            /*
             * if (content.length()>468) {
             * // textView3.setText(Html.fromHtml( content.substring(0,
             * content.length()/4)+"........."+"<font color=\"#ff0000\">详细内容</font>"));
             * }else {
             * textView3.setText(content);
             * //textView3.setText(Html.fromHtml(content.substring(0,
             * content.length()/2)+"........."+"<font color=\"#ff0000\">详细内容</font>"));
             * }
             */
        }

    }

    private class Cace {
        CheckBox checkBox;
        TextView tv_name;
        TextView tv_body;
        TextView tv_date;
        TextView tv_order;
    }

    private class Myquery extends AsyncQueryHandler {

        public Myquery(ContentResolver cr) {
            super(cr);
        }

protected void onDeleteComplete(int token, Object cookie, int result) {
    // TODO Auto-generated method stub
    super.onDeleteComplete(token, cookie, result);

}

        
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    /*
                     * while (cursor.moveToNext()) {//计算有多少个不同的日期简报
                     * String newdate = cursor.getString(3);
                     * if (!cursor.isFirst()) {
                     * int c=cursor.getPosition();
                     * cursor.moveToPosition(c-1);
                     * String olddate = cursor.getString(3);
                     * if (!olddate.equals(newdate)) {
                     * adapter.count++;
                     * }
                     * cursor.moveToPosition(c);
                     * }
                     * }
                     * System.out.println( adapter.count+"不同的日期个数");
                     * cursor.move(-1);
                     * adapter.cursor=cursor;
                     */
                    adapter.changeCursor(cursor);
                    progressDialog.setVisibility(View.GONE);
                }
            }else {
                
            }
        }
    }
    enum DISPLAYMODE {
        list, edit
    };

    private DISPLAYMODE mode = DISPLAYMODE.list;
    private LinkedList<String> delLinkedList;// 保存将要删除的简报

    /**
     * 简报 点击的 时候
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);
        if (mode == DISPLAYMODE.list) {
             Intent intent=new Intent(Shears.this,Details.class);
            intent.putExtra("title",  cursor.getString(2));
            intent.putExtra("who",  "jianbao");
            startActivity(intent);
           
        } else {
          //  CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            //    checkbox.setChecked(!checkbox.isChecked()) ;
            String bigtitleString = cursor.getString(2);
        //    TextView ssTextView = (TextView) view.findViewById(R.id.jianbaotitle);
            if (delLinkedList.contains(bigtitleString)) {
                delLinkedList.remove(bigtitleString);
            } else {
                delLinkedList.add(bigtitleString);
            }
        adapter.notifyDataSetChanged();
           
        }

    }

    private Float textsize=10F;
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min_button3:
                if (textsize > 15f) {
                	textsize=textsize - 5f;
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setTextSize(textsize );
                    }
                }
              
                break;
            case R.id.big_button3:
            	 if (textsize <=30F ) {
                  	textsize=textsize + 5f;
                  	 for (int i = 0; i < list.size(); i++) {
                         list.get(i).setTextSize(textsize);
                     }
                  }
            	
                break;
            case R.id.seach:
                onSearchRequested();
                break;
            case R.id.del:
                if (mode == DISPLAYMODE.edit) {
                    v.setBackgroundResource(R.drawable.guanli);
                    if (delLinkedList.size()>0) {
                        for (int i = 0; i < delLinkedList.size(); i++) {
                            progressDialog.setVisibility(View.VISIBLE);
                            query.startDelete(2, null, Heibai.JIANBAO_URI,
                                    "title = ?", new String[] { delLinkedList.get(i) });
                        }         
                        progressDialog.setVisibility(View.GONE);
                        
                    }  
                    mode = DISPLAYMODE.list;
                    for (int i = 0; i < checklist.size(); i++) {
                       CheckBox checkBox= checklist.get(i);
                       checkBox.setVisibility(View.GONE);
                       checkBox.setChecked(false);
                    }
                } else {
                    Toast.makeText(Shears.this, "选择删除简报", 1).show();
                    v.setBackgroundResource(R.drawable.shanchu);
                    mode = DISPLAYMODE.edit;
                    for (int i = 0; i < checklist.size(); i++) {
                        checklist.get(i).setVisibility(View.VISIBLE);
                        
                    }

                }

                break;
        }
    }

    
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        checklist.clear();
    }

}
