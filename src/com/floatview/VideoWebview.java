package com.floatview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class VideoWebview  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
String url=getIntent().getStringExtra("url");
         WebView webView=new WebView(this);
        setContentView(webView);
    webView.loadUrl(url);
    
    }
}
