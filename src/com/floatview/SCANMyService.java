package com.floatview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.activity.CaptureActivity;

import org.chinamil.R;

import qianlong.qlmobile.ui.App;

public class SCANMyService extends Service {
	
	WindowManager.LayoutParams params;
	View floatView;
	TextView powerInfo;
	WindowManager vm;
	boolean isShow;//是否点击过
	boolean init;
//	boolean isme;
    int code=0;
   
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String flag = intent.getStringExtra("show");
		if(!init){
			createFloatView();
			   init=true;
		}
		    if(flag.equals("show")){
	            vm.addView(floatView, params);
	            }else if(flag.equals("unshow")){
	                vm.removeView(floatView);
	            }
	}
/*
	String updateTimes() {
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0) {
			ut = 1;
		}
		return convert(ut);
	}
	private String pad(int n) {
		 if (n >= 10) {
			 return String.valueOf(n);
		 } else {
			 return "0" + String.valueOf(n);
		 }
	 }
	private String convert(long t) {
		int s = (int) (t % 60);
		int m = (int) ((t / 60) % 60);
		int h = (int) ((t / 3600));
		return h + ":" + pad(m) + ":" + pad(s);
	}
	   */ private float ACTION_DOWNX,ACTION_DOWNy;
	void createFloatView() {
		floatView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.floatview, null);
		powerInfo = (TextView)floatView.findViewById(R.id.powerinfo);
		
		floatView.setBackgroundResource(R.drawable.ckt_left_press);
		powerInfo.setText("二维码" );
		
		vm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT ;
		params.format = PixelFormat.RGBA_8888;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;   //调整悬浮窗口至左上角
		floatView.setOnTouchListener(new View.OnTouchListener() {
			int lastx, lasty;
			int paramx, paramy;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				    ACTION_DOWNX=event.getX();
		            ACTION_DOWNy=event.getY();
				    Log.i("xx", "ACTION_DOWN ");
					lastx = (int) event.getRawX();
					lasty = (int) event.getRawY();
					paramx = params.x;
					paramy = params.y;
					break;
				case MotionEvent.ACTION_MOVE:
			         Log.i("xx", code+"ACTION_MOVE");
				    code=1;
				   	int dx = (int) event.getRawX() - lastx;
					int dy = (int) event.getRawY() - lasty;
					params.x = paramx + dx;
					params.y = paramy + dy;
					vm.updateViewLayout(floatView, params);
					
					break;
				 case MotionEvent.ACTION_UP:
				     Log.i("xx", code+"");
				     if (event.getX()==ACTION_DOWNX) {//点击动作
		     Intent intent= new Intent(getApplicationContext(),CaptureActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent); 
			                return false;
			            }
				}

				return true;
			}
		});
		
		//floatView.setOnClickListener(myViewClickListener);
	}

	/*View.OnClickListener myViewClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
Toast.makeText(getApplicationContext(), "xxx", 1).show();            
        }
	};*/
	/*List<String> getHomesApp(){
		List<String> lists = new ArrayList<String>();
		PackageManager pm = getApplicationContext().getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, pm.MATCH_DEFAULT_ONLY);
		for (ResolveInfo info : infos) {
			lists.add(info.activityInfo.packageName);
		}
		return lists;
	}
	
	boolean isHomeApp(){
		ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rtis = am.getRunningTasks(1);
		String topName = rtis.get(0).topActivity.getPackageName();
//		isme = "com.floatview".equals(topName);
		return getHomesApp().contains(topName);
	}*/
	
	/*
	Handler myHandler = new Handler(){
		public void handleMessage(an
		droid.os.Message msg) {
			if(isHomeApp()){
				if(!isShow){
					vm.addView(floatView, params);
					isShow = !isShow;
				}
			}else{
				if(isShow){
						vm.removeView(floatView);
						isShow = !isShow;
				}
			}
			sendEmptyMessageDelayed(0, 500);
		};
	};*/
	
	
 /* 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
    	String result = "N/A";
        try {
        	FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
        	BufferedReader br = new BufferedReader(fr);
        	String text = br.readLine();
        	result = text.trim();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return result;
    }*/

 /*   获取内存 str2 内存
    public String getTotalMemory() {  
    	String str1 = "/proc/meminfo";  
    	String str2="";  
    	try {  
    		FileReader fr = new FileReader(str1);  
    		BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
    		while ((str2 = localBufferedReader.readLine()) != null) {  
    		}  
    	} catch (Exception e) {  
    	}  
    	return str2;
    } */ 
    
/*    String level;
    获取电量 level% 当前电量
    private BroadcastReceiver batteryReceiver= new BroadcastReceiver(){  
    	@Override 
    	public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int lev= intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                level = (lev * 100 / scale)+"";
            }
    	}  
    };*/  
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
