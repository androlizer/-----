package org.chinamil.ui.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.chinamil.R;

public class WebPlayer extends Activity {
    private  WebView webView;
    private WebSettings webSettings;
    ProgressBar progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     String urlString=  getIntent().getStringExtra(
             "url");
     setContentView(R.layout.webplayer);
     if (urlString!=null) {

         progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
         progressBar1.setMax(100);
         webView=(WebView) findViewById(R.id.webview);
         this.webView.getSettings().setSupportZoom(true);
         this.webView.getSettings().setBuiltInZoomControls(true);
         this.webView.getSettings().setJavaScriptEnabled(true);
         this.webView.getSettings().setAllowFileAccess(true);
         this.webView.getSettings().setPluginsEnabled(true);
         this.webView.setBackgroundColor(0);
         this.webSettings = this.webView.getSettings();
         this.webSettings.setLoadsImagesAutomatically(true);
         this.webSettings.setCacheMode(1);
         this.webSettings.setUseWideViewPort(true);
         this.webSettings.setLoadWithOverviewMode(true);
         this.webSettings.setBlockNetworkImage(false);
         this.webSettings.setBlockNetworkLoads(false);
         this.webSettings.setSaveFormData(false);
         this.webSettings.setSavePassword(false);
         this.webView.setWebChromeClient(new WebChromeClient()
         {
           public void onProgressChanged(WebView paramWebView, int paramInt)
           {
             if (progressBar1.getVisibility() != 0)
               progressBar1.setVisibility(0);
             progressBar1.setProgress(paramInt);
             super.onProgressChanged(paramWebView, paramInt);
           }
         });
        webView.setWebViewClient(new android.webkit.WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                    
                }
                
                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar1.setVisibility(8);
                    super.onPageFinished(view, url);
                  }
            });
           
     
            
           webView.loadUrl(urlString);
  
    }else {
        webView.loadUrl("http://chn.chinamil.com.cn/20113jfjbdzb/paperindex.htm");
    }
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {  
          
            webView.goBack();  
        return true;  
        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP) {
            
        }else{
                finish();
        }
        return super.onKeyDown(keyCode, event);  
        }
@Override
protected void onPause() {
   webView.onPause();
    super.onPause();
}
}
