package org.chinamil.ui.library;

import android.app.Activity;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.chinamil.Heibai;
import org.chinamil.R;
/**
 *查询结果列表 以标题作为目标条件来查
 * @author zhang
 *
 */
public class SearchableActivity extends Activity {
    String queryname;
    Queryhandle queryhandle;
    ListView list;
    TextView text;
    Myadapter adapter;
    int width;
@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
     setContentView(R.layout.search);//永远都要放在这个位置
      Intent intent=getIntent();
       width=getWindowManager().getDefaultDisplay().getWidth();
if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    queryname=intent.getStringExtra(SearchManager.QUERY);
    adapter=new Myadapter(this,null);
    queryhandle=new Queryhandle(getContentResolver());
    doMySearch(queryname);
    list =(ListView) findViewById(R.id.listview);
    list.setAdapter(adapter);   
    list.setOnItemClickListener(new MyOnItemClickListener());
    text =(TextView) findViewById(R.id.empty);
    }
}
private final class MyOnItemClickListener implements OnItemClickListener{
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Cursor cursor=(Cursor) adapter.getItem(position);
        Intent intent=new Intent(SearchableActivity.this,Details.class);
        intent.putExtra("title",  cursor.getString(1));
        intent.putExtra("who",  "jianbao");
        startActivity(intent);
      /*  Bundle bundle=new Bundle();
        bundle.putString("bigtitle",cursor.getString(1) );
        bundle.putString("title", cursor.getString(2));
        bundle.putString("content", cursor.getString(4));
        bundle.putString("date", cursor.getString(3));
        Intent intent=new Intent(SearchableActivity.this,UrlItem.class);
        intent.putExtras(bundle);   */    
}
}
private void doMySearch(String query) {
    // TODO Auto-generated method stub
    Uri uri = Heibai.JIANBAO_URI;
    //select * from sms where body like '%l%'
    String selection = Heibai.CONTENT + " like '%" + query + "%'";
    queryhandle.startQuery(0, null, uri, new String[]{"_id",
            "title","date","content"}, selection, null, null);
}
private class Myadapter extends  CursorAdapter{
    private LayoutInflater mInflater;
    public Myadapter(Context context, Cursor c) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
        Time time = new Time();
        time.setToNow();
        time.hour = 0;
        time.minute = 0;
        time.second = 0;
        
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.jianbao, parent, false);
        SearchViews views = new SearchViews();
       /* TextView textView=(TextView) view.findViewById(R.id.jianbaotitle);
        textView.setText(Html.fromHtml(  cursor.getString(1)));
       TextView textView2=(TextView) view.findViewById(R.id.date);
       textView2.setText("日期："+cursor.getString(3));
       TextView textView3=(TextView) view.findViewById(R.id.content);
       list.add(textView3);*/
/*        new String[]{"_id",
                "bigtitle","title","date","content"}*/
        TextView textView2=(TextView) view.findViewById(R.id.date);
        views.tv_body =  (TextView) view.findViewById(R.id.content);
     TextView textView=(TextView) view.findViewById(R.id.jianbaotitle);
     TextView textViewget=(TextView) view.findViewById(R.id.get);
		if (width<=480) {
          	 textView.setTextSize(12);
          	textView.setMaxWidth(150);
          	textView.setMaxLines(1);
          	textView2.setTextSize(12);
          	textViewget.setTextSize(12);
			}
		  views.tv_name =textView;
		views.tv_date=textView2;
        view.setTag(views);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
     SearchViews views = (SearchViews) view.getTag();
  String nameString=  cursor.getString(1);
  String body=   cursor.getString(3);
  String tv_date=   cursor.getString(2);
     views.tv_name.setText(Html.fromHtml(nameString));
     views.tv_body.setText(Html.fromHtml( body ));
     views.tv_date.setText("日期"+Html.fromHtml(tv_date));
}
private final class SearchViews{
    TextView tv_name;
    TextView tv_date;
    TextView tv_body;
}
}

private class Queryhandle extends AsyncQueryHandler{
    public Queryhandle(ContentResolver cr) {
        super(cr);
    }
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        if (cursor!=null) {
            if (cursor.getCount()>0) {
                if (cursor.getCount()==1) {
                    cursor.moveToNext();
                    Intent intent=new Intent(SearchableActivity.this,Details.class);
                    intent.putExtra("title",  cursor.getString(1));
                    intent.putExtra("who",  "jianbao");
                    startActivity(intent);
                  /*  Bundle bundle=new Bundle();
                    bundle.putString("bigtitle",cursor.getString(1) );
                    bundle.putString("title", cursor.getString(2));
                    bundle.putString("content", cursor.getString(4));
                    bundle.putString("date", cursor.getString(3));
                    Intent intent=new Intent(SearchableActivity.this,UrlItem.class);
                    intent.putExtras(bundle);       
                   startActivity(intent);*/
                   finish();
                }
                adapter.changeCursor(cursor);
            }else {
                TextView ddTextView =new TextView(SearchableActivity.this);
                ddTextView  .setText("暂无内容");
                 list.setEmptyView(ddTextView);
            }
        }else {
            TextView ddTextView =new TextView(SearchableActivity.this);
           ddTextView  .setText("暂无内容");
            list.setEmptyView(ddTextView);
        }
        changeTitle( cursor);
    }
}
public void changeTitle(Cursor cursor) {
    // TODO Auto-generated method stub
    if(cursor != null){
        if(cursor.getCount() > 0){
            int count = cursor.getCount();
            setTitle("共"+ count+"条查询结果" );
        }else{
            setTitle("没有匹配内容");
        }
    }else{
        setTitle("没有匹配的内容");
    }
}


}
