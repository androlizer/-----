package org.chinamil.ui.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.List;

import org.chinamil.Heibai;
import org.chinamil.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
@SuppressWarnings("unused")
public class WebviewActivity extends Activity implements OnDrawerCloseListener, 
OnDrawerOpenListener {
    private WebView webView;
    @SuppressWarnings("deprecation")
	private Gallery gallery;
    private Myadapter adaMyadapter;
    private LayoutInflater layout;
    private SlidingDrawer sliding;
    private boolean isload = false;;
    private int index = 0;
    private  ProgressDialog progressDialog;
  private ProgressBar mypDialog;
    private WebSettings webSettings;
    private Button openImageView;
    private  String  currentIndex="01";
    /*
     * ProgressDialog mypDialog=new ProgressDialog(RecentActivity.this);
     * mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     * mypDialog.setMessage(Heibai.LOADING);
     * mypDialog.setIndeterminate(false);
     * o * mypDialog.show();
     * System.out.println("开始解压");
     * boolean success=ZipDecrypter.decrypter(new File(
     * topcache,MD5.getMD5(cace[c])+".zip"),
     * topcache);
     */
    private Handler myhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    new File(root,whoString+".zip").delete();
                    Toast.makeText(WebviewActivity.this, Heibai.LOADINGERR, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    //
                    // /data/data/org.chinamil/cache/927df7d56d1cf7225cb9f426d859a62f/thumbs/09.jpg解压路径
                    if (root!=null&&webView!=null&&url!=null) {
                        loadurl=root.getAbsolutePath() + "/" + whoString + "/" + url.get(0);
                                webView.loadUrl("file:///" + root.getAbsolutePath() + "/" + whoString + "/" + url.get(0));
                                adaMyadapter = new Myadapter(new File(root, whoString + ".zip"));
                                gallery.setAdapter(adaMyadapter);
                    }else {
                            Toast.makeText(WebviewActivity.this, Heibai.LOADINGERR, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    break;
                case 2://显示
                	mypDialog.setVisibility(View.GONE);	
                	/* if (progressDialog==null) {
                        progressDialog = new ProgressDialog(
                                WebviewActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // progressDialog.setView(view) 不设置这个了
                        progressDialog.setMessage("提取中...");
                        progressDialog.setCancelable(false);// 设置回退键失效
                    }
                    progressDialog.show();*/
                    break;
                case 3:
                 /* if (progressDialog!=null) {
                      progressDialog.dismiss();
                      progressDialog=null;
                } */      
                	 String imageStringf= loadurl.substring(0,loadurl.indexOf("ind"));
                	 Log.i("xx","handle"+imageStringf);
                 Intent intent=new Intent(WebviewActivity.this,Details4.class);
                     intent.putExtra("image", imageStringf);
                      intent.putExtra("title",  (String) msg.obj);
                      intent.putExtra("who",  "bushi");
                         startActivity(intent);
                    break;
                case 4:
                    if (mypDialog != null) {
                        if (mypDialog.getVisibility()==View.VISIBLE) {
                            mypDialog.setVisibility(View.GONE);
                        }
                    }
                    break;
                default:
                    break;
            }

        };
    };
    private String whoString,date;// 将要显示谁
    private RotateAnimation animation, openanimation;
    File root;
    private List<String> url, thumbs;// html实体于缩略图实体
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.readeui);
        root = getCacheDir();
        mypDialog=(ProgressBar) findViewById(R.id.progressBar1);
      /*  mypDialog = new ProgressDialog(WebviewActivity.this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setMessage(Heibai.LOADING);
        mypDialog.setIndeterminate(false);
        mypDialog.setCancelable(false);
        mypDialog.show();*/
        whoString = getIntent().getStringExtra("who");
        date = getIntent().getStringExtra("date");
       // classift = getIntent().getStringExtra("classify").split("/");
        url = ZipDecrypter.getList(new File(root, whoString + ".zip"));
        if (url == null) {
            Toast.makeText(WebviewActivity.this, Heibai.LOADINGERR, 1).show();
            new File(root,whoString+"/.zip").delete();
            finish();
            
        }
        new Thread(new Runnable() {
            public void run() {
                if (new File(root, whoString + ".zip").exists()) {// 如果存在 zip文件 采取看是否解压过
                    if (!new File(root, whoString + "/01").exists()) {// 如果从未解压 就解压 否则不解压
                      //  boolean success = ZipDecrypter.decrypter(new File(root, whoString + ".zip"), root);
                        boolean success =  ZipDecrypter.decryptercrypto(      new File(root,  whoString + ".zip"),
                                root);
                        if (success) {
                            myhandler.sendEmptyMessage(1);
                        } else {
                            myhandler.sendEmptyMessage(0);
                        }
                    } else {
                        myhandler.sendEmptyMessage(1);
                    }
                } else {
             
                    myhandler.sendEmptyMessage(0);
                }

            }
        }).start();

        setView();

    }
    
    private String loadurl;

    @SuppressLint({ "NewApi", "NewApi" })
	private void setView() {
        openImageView = (Button) findViewById(R.id.imageViewIcon);
        gallery = (Gallery) findViewById(R.id.webviewgallery);
 
        
        
        sliding = (SlidingDrawer) findViewById(R.id.sliding);
        /*
         * RotateAnimation rAnimation = new RotateAnimation(0,360,
         * Animation.RELATIVE_TO_PARENT,1f,Animation.RELATIVE_TO_PARENT,0f);
         * nimation a = new RotateAnimation(0.0f, 360.0f,
         * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
         * 0.5f);
         * animation= new RotateAnimation(0, 180,
         * Animation.RELATIVE_TO_PARENT,
         * Animation.RELATIVE_TO_PARENT);
         */
        animation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        openanimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        openanimation.setDuration(100);
        openanimation.setFillAfter(true);
        animation.setDuration(100);
        animation.setFillAfter(true);
        sliding.open();
        // sliding.animateOpen();
        sliding.setOnDrawerCloseListener(this);
        sliding.setOnDrawerOpenListener(this);
        gallery.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (adaMyadapter!=null) {
					adaMyadapter.setSelectedTab(position);
				}
                synchronized (WebviewActivity.class) {
                    if (!isload) {// "file:///mnt/sdcard/01/index.htm",
                        index = position;
                        // "file:///mnt/sdcard/01/index.htm",
                        // 拼路径
                        // 三种方式,一种是loadRequest,还有个就是loadHTMLString,最后loadData
                        loadurl=root.getAbsolutePath() + "/" + whoString + "/" + url.get(position);
                        webView.loadUrl("file:///" + root.getAbsolutePath() + "/" + whoString + "/" + url.get(position));
                        currentIndex=url.get(position).substring(0,2);
                       
                        view.startAnimation(AnimationUtils.loadAnimation(WebviewActivity.this, R.anim.fangda));
                    } else {
                        Toast.makeText(WebviewActivity.this, "上版加载中...", 1).show();
                    }
                }
                // Toast.makeText(WebviewguiActivity.this, position+"", 1).show();
            }
        });
        layout = getLayoutInflater();
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginsEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT); // 背景透明
      
        webView.getSettings().setDefaultTextEncodingName("GBK");// 防止乱码 可能会不对
        webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setUseWideViewPort(true);
        //webSettings.setUseDoubleTree(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        /*
         * webView.setWebViewClient(new WebViewClient(){
         * 
         * 
         * public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
         * webView.loadUrl("file:///android_asset/error.htm");
         * super.onReceivedError(view, errorCode, description, failingUrl);
         * }
         * });
         */
        // 默认为第一版
       // webView.loadUrl("file:///android_asset/error.html");
        // webView.loadUrl("file:///" + root.getAbsolutePath() + "/" + whoString + "/" + url.get(0).getName());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                if (progress != 100) {
                    setTitle("加载中，请稍候..." + progress + "%");
                    setProgress(progress * 100);
                    isload = true;
                } else {
                    setTitle("解放军报");
myhandler.sendEmptyMessage(4);
                    isload = false;
                    webView.startAnimation(AnimationUtils.loadAnimation(WebviewActivity.this, R.anim.suoxiao));
                }

            }

            // onReceivedError

        });

        webView.addJavascriptInterface(new JavaScript(), "demo");
        /*
         * webView.setOnKeyListener(new View.OnKeyListener() {
         * 
         * 
         * 
         * public boolean onKey(View v, int keyCode, KeyEvent event) {
         * 
         * if (event.getAction() == KeyEvent.ACTION_DOWN) {
         * if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { //表示按返回键时的操作
         * 
         * if (!webView.getUrl().contains("01")) {
         * webView.goBack(); //后退
         * }
         * //webview.goForward();//前进
         * return true; //已处理
         * }
         * }
         * return false;
         * 
         * }
         * });
         */

        /*
         * // 防止乱码 终极方法
         * try {
         * InputStreamReader read = new InputStreamReader(
         * 
         * new FileInputStream(new File("mnt/sdcard/01/index.txt")), "GBK");
         * BufferedReader in = new BufferedReader(read);
         * String c = null;
         * sBuilder = new StringBuilder();
         * while ((c = in.readLine()) != null) {
         * sBuilder.append(c);
         * 
         * }
         * System.out.println(sBuilder.toString());
         * in.close();
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         * 
         * System.out.println(sBuilder.toString());
         */

        // webView.loadData(sBuilder.toString(), "text/html", "GBK");
        // webView.loadDataWithBaseURL("file:///mnt/sdcard/01/pg_0001.jpg", sBuilder.toString(), "text/html", "UTF-8",
        // null);
 /*       webView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
					System.out.println();
				if (event.getAction()==MotionEvent.ACTION_UP) {
					mypDialog.setVisibility(View.VISIBLE);	
					// myhandler.sendEmptyMessage(2);		
							}
				return false;
			}
		}) ;*/
    }

    private class Myadapter extends BaseAdapter {
int mSelectedTab=0;
        public Myadapter(File file) {
            thumbs = ZipDecrypter.getThumb(file);
            if ( thumbs.size()<=0) {
                myhandler.sendEmptyMessage(0);
                finish();
            }
        }
        public void setSelectedTab(int tab) {
                 if (tab != mSelectedTab) {
            mSelectedTab = tab;
             notifyDataSetChanged();
        	 }
        	}
        
        public int getCount() {
            return url.size();
        }
        public Object getItem(int position) {
            return thumbs.get(position);
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layout.inflate(R.layout.readuiitem, null);
            }
            TextView text = (TextView) convertView.findViewById(R.id.webviewtext);
            if (position==mSelectedTab) {
            	text.setTextColor(Color.RED);
			}
            text.setText(position+1+"");
           /* if (classift != null) {
                if (classift.length >= 8) {
                    if (position <= classift.length) {
                        text.setText(classift[position]);
                    }
                }
            }*/
            ImageView iamge2 = (ImageView) convertView.findViewById(R.id.webviewimage);
            // "file:///"+root.getAbsolutePath()+"/"+whoString+"/"+url.get(0).getName()
            // stream = new FileInputStream(pathName);
            try {
                iamge2.setImageBitmap(BitmapFactory.decodeStream( // 这个可能会快点
                        new FileInputStream(root.getAbsolutePath() + "/" + whoString + "/" + thumbs.get(position))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                finish();
            }
            return convertView;
        }

    }

    int screenwidht, screenheight;

    
    public void onDrawerOpened() {

        openImageView.startAnimation(openanimation);
    }

    
    public void onDrawerClosed() {

        openImageView.startAnimation(animation);
    }

    /**
     * 接数据
     * 
     * @author Administrator
     * 
     */
   boolean click=false;//是否点击过
   
protected void onStart() {
       if(progressDialog!=null&&progressDialog.isShowing()){
           progressDialog.cancel();
       }
       click=false;
    super.onStart();
   
}

protected void onRestart() {
    super.onRestart();
    if(progressDialog!=null&&progressDialog.isShowing()){
        progressDialog.cancel();
    }
    if (click) {
    	if (mypDialog==null) {
			
		}
		
	}
    click=false;
}


    final class JavaScript {// 这个Java 对象是绑定在另一个线程里的，
    public void clickOnAndroid(final String str, final String id, 
    		final String name, final String title) {
    	/*if (isTabletDevice()) 
    	{*/	
            if(!isload){//正在加载的时候禁止  与js交互
            if (!click) {
            	   click=true;
            	 ContentValues initialValues = new ContentValues();
                 initialValues.put(Heibai.TITLE, title);
                 initialValues.put(Heibai.CONTENT, str);
                 initialValues.put(Heibai.DATE, date);
                initialValues.put(Heibai.PATH,  currentIndex);
              
            	getContentResolver().insert(Heibai.TEMP_URI, initialValues);
            	myhandler.sendEmptyMessage(2);
            	Message msg=new Message();
                 msg.what=3;
                 msg.obj=title;
                  myhandler.sendMessage(msg);
            	/*//为了防止多次点击
                String pathString="no";
                String head=null;
                String content=null;
                String bigtitle =null;
                System.out.println();
                if (str!=null) {
                int index=str.indexOf("<table>");
                   if (index!=-1) {
                     head = str.substring(0,index);
                }
                if (head!=null) {
                     bigtitle = main(head);
                }
                if (index!=-1) {
                    content=str.substring(index);
               }
           if ( content!=null) {//抓取图片
            String imageString=content.substring(content.indexOf("=")+2,content.indexOf("\">"));
            String imageStringf= loadurl.substring(0,loadurl.indexOf("ind"));
            pathString=imageStringf+imageString;
           }
                Bundle bundle=new Bundle();
                if (bigtitle==null) {
                	  bundle.putString("bigtitle", name);
				}else {
					  bundle.putString("bigtitle", bigtitle);
				}
              
                bundle.putString("title", title);
                //抓取内容
               
                bundle.putString("content", str);
               System.out.println("提取的内容"+str);
                bundle.putString("date", date);
                bundle.putString("image", pathString);
                Message msg=new Message();
                msg.what=3;
                msg.obj=bundle;
                 myhandler.sendMessage(msg);
            } */}
            }else {
            }
            // Html.toHtml(Html.fromHtml(str)) 另一种解决乱码的方法
            // 替换图片路径
            
            /*    * if (str.contains("src")) {
             * int index = str.indexOf("\"", str.indexOf("src"));// src 后面第一个 " 符号的位置
             * int end = str.indexOf("\"", index + 1);
             * if (url.size() >= index) {
             * // /data/data/org.chinamil/cache/e461cff17282dad41e589b62974a29c3/02/index.htm
             * String imageString = "file:///" + root.getAbsolutePath() + "/" + whoString + "/" + url.get(index);
             * str.replace(str.substring(index, end + 1), imageString.substring(0, imageString.lastIndexOf("/")));
             * }
             * }
             
            // str.replace("left", "center");
            
             * String htmString=
             * "<span style=\"float:center; width:522px; font-size:24px; line-height:140%; font-weight: normal\"></span> "
             * ;
             
           // System.out.println();
          
            System.out.println(content);
            System.out.println(title);
            System.out.println(bigtitle);
          
           // myhandler.sendEmptyMessage(2);
            .post(new Runnable(){
                
                public void run() {//这里应该特别注意的
                    //  Intent intent=new Intent(WebviewActivity.this,UrlItem.class);
                    //  startActivity(intent);
                }
                
               });
            // StringBuilder quanbuString=new StringBuilder();
            // quanbuString.append(htmString).append(bigtitle).append("<br>").append(htmString).append(title).append(
            // htmString).append(content);
            // System.out.println(quanbuString.toString());
            // webView.loadData(Html.toHtml(Html.fromHtml(quanbuString.toString())), "text/html", "GBK");
            // webView.loadUrl("file:///android_asset/test.htm");
        View view = layout.inflate(R.layout.urlitem, null);
            TextView bigtTextView = (TextView) view.findViewById(R.id.bigtitle);
            TextView titletext = (TextView) view.findViewById(R.id.title);
            TextView contenttext = (TextView) view.findViewById(R.id.content);
            bigtTextView.setText(Html.fromHtml(bigtitle));
            contenttext.setText(Html.fromHtml(content));
            titletext.setText(Html.fromHtml(title));
            new AlertDialog.Builder(WebviewActivity.this).setView(view).show();
    
            
             * String title2=getBigTitle(str);
             * String content=getBigTitle(str);
             * System.out.println(bigtitle);
             * System.out.println(title2);
             * System.out.println(content);
             
            // webView.loadData(Html.toHtml(Html.fromHtml(str)), "text/html", "GBK");
            // intent.putExtra("quanbu", str);
            
             * Intent intent=new Intent(WebviewActivity.this,UrlItem.class);
             * intent.putExtra("bigtitle", getBigTitle(str));
             * intent.putExtra("title", getTitle(str));
             * intent.putExtra("content", getContent(str));
             * startActivity(intent);
             
        */
              }
        public String main(String str) {
            StringBuffer stringBuffer = new StringBuffer();
            boolean one = true;
            
            for (int i = 0; i < str.length() && one; i++) {
                if (!isChinese(str.charAt(i))) {// 不是汉字
                    if (stringBuffer.length() > 0) {// 看是否添加过
                        one = false;
                    }

                } else {// 是汉字
                    if (isChinese(str.charAt(i))) {
                        stringBuffer.append(str.charAt(i));
              
                    }
                }

            }
            return stringBuffer.toString();

        }

        public boolean isChinese(char a) {
            int v = (int) a;
            return (v >= 19968 && v <= 171941);
        }
    }
    /*检测时pad 还是手机
     * 
     */
    @SuppressLint({ "NewApi", "NewApi" })
    private boolean isTabletDevice() {	
    	if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb	
    		// test screen size, use reflection because isLayoutSizeAtLeast is only available since 11	    
    		Configuration con = getResources().getConfiguration();	        
    		try {	  
    			Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast", int.class);	     
    			Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004); 
    			// Configuration.SCREENLAYOUT_SIZE_XLARGE	     
    			System.err.println("pad");	      
    			return r;	   
    			} catch (Exception x) {	       
    				x.printStackTrace();	 
    			System.err.println("not pad");	     
    			return false;	 
    			}	    }	 
    	  return false;}
    
}


