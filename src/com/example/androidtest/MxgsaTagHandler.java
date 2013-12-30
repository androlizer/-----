package com.example.androidtest;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import org.chinamil.ui.library.WebPlayer;
import org.xml.sax.XMLReader;

public class MxgsaTagHandler implements TagHandler{
    private int sIndex = 0;  
    private  int eIndex=0;
    private final Context mContext;
    String output;
    int d=0;
    public MxgsaTagHandler(Context context,String output ){
        mContext=context;
        this.output=output;
    }
    public void handleTag(boolean opening,String tag, Editable output, XMLReader xmlReader) {
       
            if (opening) {
                sIndex=output.length();
            }else {
                eIndex=output.length();
                output.setSpan(new MxgsaSpan(), 
                        sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        
    }
   
    private class MxgsaSpan extends ClickableSpan  {
        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            //具体代码，可以是跳转页面，可以是弹出对话框，下面是跳转页面
         /*   Intent intent= new Intent(mContext,VideoBuffer.class);
            intent.putExtra("url","http://vv.chinamil.com.cn/asset/category3/2013/08/28/asset_106628.mp4");
          mContext.startActivity(intent);*/
            Intent intent= new Intent(mContext,WebPlayer.class);
            intent.putExtra("url",output);
          mContext.startActivity(intent);
           /*if (output.contains("mp4")) {
                Intent intent= new Intent(mContext,VideoBuffer.class);
                intent.putExtra("url",output);
              mContext.startActivity(intent);
            }else {
                Intent intent = new Intent();        
                intent.setAction("android.intent.action.VIEW");    
                Uri content_url = Uri.parse(output);   
                intent.setData(content_url);  
                mContext.startActivity(intent);
            }*/
        /* Intent intent = new Intent();        
         intent.setAction("android.intent.action.VIEW");    
         Uri content_url = Uri.parse(output);   
         intent.setData(content_url);  
         mContext.startActivity(intent);*/
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
        
        
    }

   }

