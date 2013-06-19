/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chinamil.downland.fragments;

import java.text.SimpleDateFormat;

import org.chinamil.Heibai;
import org.chinamil.R;
import org.chinamil.ui.library.Details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleFragment extends Fragment implements OnItemClickListener {
    final static String ARG_POSITION = "position";
    public  static String Title = "position";
    int mCurrentPosition = -1;
    View bView;
    LayoutInflater inflater;
    Context context;
    int width;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            Title=savedInstanceState.getString(Title);
        }
        this.inflater=inflater;
        context=inflater.getContext();
   /*     View view01 = getLocalActivityManager().startActivity("activity01",
                new Intent(this, MainActivity1.class)).getDecorView();*/
        // Inflate the layout for this fragment
        /* ActivityGroup activityGroup=new ActivityGroup();
         Intent intent= new Intent(inflater.getContext(), WebviewActivity.class);
         intent.putExtra("who", MD5.getMD5(titleString));
         intent.putExtra("date", titleString);
         bView  =  activityGroup.getLocalActivityManager().startActivity("activity01",
                 intent  ).getDecorView();
         return   bView;
*/
    //    return inflater.inflate(R.layout.article_view, container, false);
      width=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        return inflater.inflate(R.layout.article_view, container, false);
    }

    /**
 * 
 * @param position
 */ ListView listView;
 Mycursoradapter adapter;
    public void updateArticleView(int position,String titleString) {
        TextView article = (TextView) getActivity().findViewById(R.id.article_title);
        
    if (context==null) {
            context=inflater.getContext();
        }
    Cursor  cursor= context.getContentResolver().query(Heibai.JIANBAO_URI, 
              new String[] {
              "_id",  "bigtitle", "title", "date", "content" }, 
               Heibai.DATE+ "=" +"'"+titleString+"'" , null, null);
      if (cursor!=null&&cursor.getCount()>0) {
    	  getActivity().findViewById(R.id.article).setVisibility(View.GONE);
          article.setVisibility(View.GONE);
          if (listView==null) {
              listView=(ListView) getActivity().findViewById(R.id.downlistview);
              listView.setOnItemClickListener(this);
        }
          adapter=new Mycursoradapter(context, cursor);
          listView.setVisibility(View.VISIBLE);
          listView.setAdapter(adapter);
    }else {
    	article.setText(titleString+"期刊暂无收藏内容");
    
        //article.setText(titleString+"暂无剪报内容");
    }
      //  System.out.println("传递标题"+titleString);
     //   article.setText(titleString+"暂无剪报内容");
        mCurrentPosition = position;     
        Title=titleString;
         }

    
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
    private class Mycursoradapter extends CursorAdapter{
        String newdate, olddate;
        @SuppressWarnings("deprecation")
        public Mycursoradapter(Context context, Cursor c) {
            super(context, c);
        }
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final Cace cace = new Cace();
            View view = inflater.inflate(R.layout.jianbao, null);
            TextView textView2 = (TextView) view.findViewById(R.id.date);
            TextView textView3 = (TextView) view.findViewById(R.id.content);
            TextView textViewd = (TextView) view.findViewById(R.id.jianbaodate);
            TextView get = (TextView) view.findViewById(R.id.get);
            TextView textView = (TextView) view.findViewById(R.id.jianbaotitle);
            
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
            textView3.setText(Html.fromHtml(cursor.getString(4)) + "......");
     /*       String content = cursor.getString(4).replaceAll("<p>", "");
            content = content.replaceAll("</p>", "");*/
            
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
        TextView tv_name;
        TextView tv_body;
        TextView tv_date;
        TextView tv_order;
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);
     //   "_id",  "bigtitle", "title", "date", "content"
        if (context!=null) {
        	 Intent intent=new Intent(context,Details.class);
             intent.putExtra("title",  cursor.getString(2));
             intent.putExtra("who",  "jianbao");
             startActivity(intent);
		}else {
	//	context为空的话就什么都不做
		}
       
        
        
       
    } 
   @Override
public void onStart() {
	super.onStart();
	  Bundle args = getArguments();
      if (args != null) {
          // Set article based on argument passed in
          updateArticleView(args.getInt(ARG_POSITION),args.getString(Title));
      } else if (mCurrentPosition != -1&&!Title.equals("position")) {
          // Set article based on saved instance state defined during onCreateView
          updateArticleView(mCurrentPosition,Title);
      }
}
}