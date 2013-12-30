package org.chinamil.ui.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyWebview extends Activity
{
  ProgressBar progressBar1;
  WebSettings webSettings;
  WebView webView;

  public void onBackPressed()
  {
    System.exit(0);
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setRequestedOrientation(1);
    setContentView(2130903043);
    String str = getIntent().getStringExtra("url");
    this.webView = ((WebView)findViewById(2131099673));
    this.progressBar1 = ((ProgressBar)findViewById(2131099672));
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
        if (MyWebview.this.progressBar1.getVisibility() != 0)
          MyWebview.this.progressBar1.setVisibility(0);
        MyWebview.this.progressBar1.setProgress(paramInt);
        super.onProgressChanged(paramWebView, paramInt);
      }
    });
    this.webView.setWebViewClient(new WebViewClient()
    {
      public void onPageFinished(WebView paramWebView, String paramString)
      {
        MyWebview.this.progressBar1.setVisibility(8);
        super.onPageFinished(paramWebView, paramString);
      }
    });
    this.webView.loadUrl(str);
  }

  protected void onPause()
  {
    System.exit(0);
    super.onPause();
  }

  protected void onStop()
  {
    System.exit(0);
    super.onStop();
  }
}