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

import org.chinamil.Heibai;
import org.chinamil.ParserXml;
import org.chinamil.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HeadlinesFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;
    LayoutInflater layout2;
    Context context;
    Myadapter adapter;
    Cursor cursor;
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position,String title);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("暂无下载内容");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : 
                	android.R.layout.simple_list_item_1;
layout2=getLayoutInflater(savedInstanceState);
context=layout2.getContext();
cursor= context.getContentResolver().query(Heibai.MANAGE_URI, 
        new String[]{"_id","title","imag","month"} , null,null, null);
//   setEmptyText("暂无下载内容");  
if (cursor==null||cursor.getCount()<0) {
    setEmptyText("暂无下载内容");  
  }else {
      adapter=new Myadapter(context, cursor );
      setListAdapter(adapter);
}
    }
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                 android.R.layout.simple_list_item_activated_1 :
                 	android.R.layout.simple_list_item_1;
    	 layout2=inflater;
    	 
    //	 imag TEXT," + "month TEXT,"+ "link TEXT,"+"title TEXT)
    	 context=layout2.getContext();
    	 cursor= context.getContentResolver().query(Heibai.MANAGE_URI, 
                 new String[]{"_id","title","imag"} , null,null, null);
      //   setEmptyText("暂无下载内容");  
    	 if (cursor==null||cursor.getCount()<0) {
             setEmptyText("暂无下载内容");  
           }else {
               adapter=new Myadapter(context, cursor );
               setListAdapter(adapter);
        }
    	return super.onCreateView(inflater, container, savedInstanceState);
    }*/
    @Override
    public void onStart() {
        super.onStart();
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        index=0;
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
        }     getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setCacheColorHint(Color.TRANSPARENT);
       
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
             Cursor cursor= (Cursor) adapter.getItem(position);
          String titleString= cursor.getString(1);
        mCallback.onArticleSelected(position,titleString);
        getListView().setItemChecked(position, true);
    }
    int index;
    
private class Myadapter extends CursorAdapter{
    @SuppressWarnings("deprecation")
    public Myadapter(Context context, Cursor c) {
        super(context, c);
    }
    @Override
    public void bindView(View bindView, Context arg1, Cursor arg2) {
TextView  textView=(TextView) bindView.findViewById(R.id.donw_bigtitle);
String title=arg2.getString(1);
textView.setText(title);
/*TextView  textView2=(TextView) bindView.findViewById(R.id.donw_title);
textView.setText(arg2.getString(3));
来源暂且不设置
*/
ImageView imagetImageView=(ImageView) bindView.findViewById(R.id.down_image);
try {
    imagetImageView.setImageURI(Uri.fromFile(ParserXml.getImage(arg2.getString(2), 
            title, context.getCacheDir())
     ));
} catch (Exception e) {
//缩略图   异常不做处理
}    
//默认显示第0个报刊的简报内容
index++;     
         if (getActivity().findViewById(R.id.article_fragment)!=null) {
        	 if (index==cursor.getCount()) {
        			
        		    mCallback.onArticleSelected(0, title);
        		}
		};

   }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
    	
        return layout2.inflate(R.layout.article_item, null);
    }
    }

}