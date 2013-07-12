package org.chinamil.ui.library;

/**
 * 需改动 548行  用到teStrings
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.chinamil.About;
import org.chinamil.Demo;
import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.MultDownloader;
import org.chinamil.ParserXml;
import org.chinamil.PdfDomin;
import org.chinamil.ProgressBarListener;
import org.chinamil.R;
import org.chinamil.Rotate3dAnimation;
import org.chinamil.Tools;
import org.chinamil.downland.fragments.MainActivity;
import org.chinamil.networkerr.ErrorBookShelfActivity;
import org.chinamil.networkerr.ErrorBookShelfActivityTouch;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.karl.reader.BookShelfActivity;

@SuppressWarnings("unused")
public class RecentActivity extends Activity implements OnClickListener {

	private ListView listView; // 自定义listview对象
	private LayoutInflater layout;// 加载器
	private LinkedList<String> list;// 辅助的一个链表集合
	private LinkedList<PdfDomin> realyList;
	private LinkedList<ImageButton> imageList;
	private Mybaseadapterr adapter;
	private Editor ediorr;
	private ImageView imageView, manager, jianbao;// 头
	private Thread thread2;
	float rate;
	private ImageView welcom;// 欢迎图片
	private ViewGroup jfjbk; // 整个布局 jfjbk = (ViewGroup)
								// findViewById(R.id.jfjbk);
	private View library;// 书架显示页面
	// private int date, month;
	private Demo demo;
	private String first;
	private File topcache;// 缓存目录
	private Myclickdown downclick;
	private LinkedList<ProgressBar> progressBars;
	private HashMap<String, ImageButton> whodelent;// 正在下载的谁 用来记录下载按钮

	private ArrayList<String> who;// 是谁在下载
	private LinkedList<Queuedomain> queue;// 是谁在下载
	private LinkedList<String> cace;
	private ExecutorService pool;

	public void sendBroadcast(Intent intent) {
		// TODO Auto-generated method stub
		super.sendBroadcast(intent);
	}
	private SharedPreferences sPreferences;// 用来判断是否是第一次安装程序
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:// 下日载进度条最大值
				ProgressBar progressBar = (ProgressBar) msg.obj;
				progressBar.setVisibility(View.VISIBLE);
				// 40f94cc8
				progressBar.setMax(msg.arg1);

				// Toast.makeText(RecentActivity.this, "下载完毕", 1).show();
				break;
			case 2:// 删除
				Toast.makeText(RecentActivity.this, Heibai.DEL, 1).show();
				break;
			case 0:// 下载 更改进度条
					// 不需要手动控制线程 停止
				/*
				 * Message msg = myHandler.obtainMessage(0); msg.obj =
				 * progressBars.get(c); msg.arg1 = len; if (!queue.isEmpty()) {
				 * queue.remove(0); } msg.arg2 = c;14 13
				 * myHandler.sendMessage(msg);
				 */

				ProgressBar progressBar2 = (ProgressBar) msg.obj;
				/*
				 * if (progressBar2.getMax()==-1) {
				 * progressBar2.setMax(msg.arg1+1); }
				 */
				progressBar2.setProgress(msg.arg1);
				PdfDomin pdfDomin = realyList.get(msg.arg2);
				String titleString = pdfDomin.title;
				if ((progressBar2.getProgress() < progressBar2.getMax())) {// 是否下载完毕
					if (!who.contains(titleString)) {// 添加正在下载对象 屏蔽往期功能
						who.add(titleString);// 添加正在下载的
					}
				} else {
					if (who.contains(titleString)) {// 移除下载对象
						who.remove(titleString);
					}

					// 下载完毕
					// System.out.println("下载文件名" + cach.getAbsolutePath() +
					// "xxxxxxxxx" + name);
					// progressBar2.setProgress(msg.arg1-20);
					// progressBar2.setProgress(progressBar2.getProgress() -
					// 1000);
					/*
					 * boolean success= ZipDecrypter.decrypter( new
					 * File(topcache, MD5.getMD5(cace[msg.arg2]) + ".zip"),
					 * topcache);
					 */
					// System.out.println("解压"+"   "+success);
					progressBar2.setVisibility(View.GONE);
					// progressBar2.setProgress(0);
					Intent intent=new Intent(RecentActivity.this,Myservice.class);
					intent.putExtra("path",topcache.getAbsolutePath());
					intent.putExtra("who",cace.get(msg.arg2)); 
					startService(intent);
					Cursor cursorcCursor = getContentResolver().query(
							Heibai.MANAGE_URI, new String[] { "_id" },
							"title = ?", new String[] { cace.get(msg.arg2) },
							null);
					// Toast.makeText(RecentActivity.this, cace.get(msg.arg2) +
					// Heibai.OVER, 1).show();
					if (cursorcCursor.getCount() <= 0) {
						ContentValues values = new ContentValues();
						values.put(Heibai.TITLE, cace.get(msg.arg2));
						values.put(Heibai.LINK, pdfDomin.link);
						values.put(
								Heibai.IMAG,
								pdfDomin.cover.substring(0,
										pdfDomin.cover.lastIndexOf("."))
										+ ".png");
						values.put(Heibai.MONTH, "待定");
						Uri uri = getContentResolver().insert(
								Heibai.MANAGE_URI, values);
					}

				}
				break;
			case 3:
				asyncloadImage(); // 解析完成 开始下载缩略图
				break;
			case 4:
				/*
				 * //System.out.println(); welcom.setVisibility(View.GONE);
				 */
				applyRotation(1, 0, 90); // 欢迎界面 动画开始 只是现在没用

				break;
			case 5:// pdf下载错误提示

				if (queue.size() > 0) {
					for (int i = 0; i < queue.size(); i++) {
						queue.get(i).view
								.setBackgroundResource(R.drawable.downone);
						queue.remove(i);
					}
				}
				if (who.size() > 0) {
					who.clear();
				}
				/*
				 * if (queue.size()>0) {//移除队列第一个 queue.remove(0); if
				 * (!queue.isEmpty()) { Queuedomain queuedomain = queue.get(0);
				 * downpdf(queuedomain.index, queuedomain.view);//通知下载下一个 } }
				 */
				Toast.makeText(RecentActivity.this, Heibai.DOWNERR,
						Toast.LENGTH_SHORT).show();
				View downView = (View) msg.obj;
				downView.setVisibility(View.VISIBLE);
				progressBars.get(msg.arg1).setVisibility(View.GONE);
				break;
			case 6:// 下日载进度条最大值x.
				Toast.makeText(RecentActivity.this,
						msg.obj + "期" + Heibai.THUMBNAILNO, Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}

		};
	};

	// private Calendar aCalendar;// 日期
	// private String dateString[] = new String[7];

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);// SCREEN_ORIENTATION_LANDSCAPE
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		welcom = (ImageView) findViewById(R.id.rl_splash);
		animation = AnimationUtils.loadAnimation(RecentActivity.this,
				R.anim.wave_scale);
		sPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		first = sPreferences.getString("first", "yes");
		if (first.equals("yes")) {
			sPreferences.edit().putString("first", "no").commit();
			getScreenSize();
		}else {
			getScreenSizeDemo();
		}
		if (demo.imageheight==0) {//如果屏幕尺寸不在预计范围内的话
			startActivity(new Intent(RecentActivity.this,
					BookShelfActivity.class));	
			}
		cace = new LinkedList<String>();
		jfjbk = (ViewGroup) findViewById(R.id.jfjbk);
		/*
		 * if (first.equals("yes")) { //删除以前的缓存目录 delcache(); }else { //更改
		 * first的值 sPreferences.edit().putString("first", "no"); }
		 */
		// 148fc8056613142a65c961e2a5de440e.zip
		pool = Executors.newFixedThreadPool(1);
		queue = new LinkedList<Queuedomain>();
		library = findViewById(R.id.show);
		imageList = new LinkedList<ImageButton>();
		listView = (ListView) findViewById(R.id.library);
		listView.setVisibility(View.VISIBLE);
		imageView = (ImageView) findViewById(R.id.head_image);
		progressBars = new LinkedList<ProgressBar>();
		topcache = getCacheDir();
		/*
		 * topcache = new
		 * File(Environment.getExternalStorageDirectory(),"chiamialpdf"); if
		 * (!topcache.exists()) { topcache.mkdirs(); }
		 */
		who = new ArrayList<String>();
		whodelent = new HashMap<String, ImageButton>();
		/*
		 * aCalendar = Calendar.getInstance(); dateList.add(4); dateList.add(6);
		 * dateList.add(9); dateList.add(11); date =
		 * aCalendar.get(Calendar.DATE); month = aCalendar.get(Calendar.MONTH) +
		 * 1; // 计算 年月日 year(aCalendar.get(Calendar.DAY_OF_WEEK) - 1, date,
		 * month, aCalendar.get(Calendar.YEAR));
		 */
		layout = getLayoutInflater();
		list = new LinkedList<String>();
		realyList = new LinkedList<PdfDomin>();
		for (int i = 0; i < 3; i++) {
			list.add(i + "1");
		}

		downclick = new Myclickdown();
		manager = (ImageView) findViewById(R.id.manage);
		jianbao = (ImageView) findViewById(R.id.jianbao);
		// 往期图片参数 粗略的
		LayoutParams wangqi = manager.getLayoutParams();
		wangqi.height = demo.headimageheight / 2;
		wangqi.width = demo.width / 8;

		LayoutParams jianbaopar = jianbao.getLayoutParams();
		jianbaopar.height = demo.headimageheight / 2;
		jianbaopar.width = demo.width / 8;
		rate = (float) demo.width / 400;
		jianbao.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (who.isEmpty()) {
					// 先查询是否有数据
					myquery = new Myquery(getContentResolver());
					myquery.startQuery(1, null, Heibai.JIANBAO_URI,
							new String[] { "title" }, null, null, null);
				} else {
					Toast.makeText(RecentActivity.this, Heibai.JWNRUN,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 往期监听
		manager.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (who.isEmpty()) {
					// 先查询是否有数据
					myquery = new Myquery(getContentResolver());
					myquery.startQuery(0, null, Heibai.MANAGE_URI,
							new String[] { "_id", "imag", "link", "title" },
							null, null, null);
				} else {

					Toast.makeText(RecentActivity.this, Heibai.DOWNRUN,
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 横向图片
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.heand);

		imageView.setImageBitmap(Tools.resizeBitmap(bitmap2, demo.width,
				demo.headimageheight));

		// xml解析线程
		thread2 = new Thread(new Runnable() {

			public void run() { // 请求网络成功的时候
				try {
					realyList = ParserXml.getPicture(Heibai.URL);
					for (int i = 0; i < realyList.size(); i++) {
						cace.add(realyList.get(i).title);
					}
					// myHandler.sendEmptyMessage(3);
					adapter = new Mybaseadapterr(); // 欢迎就解析 完了适配 完了3秒后再显示
													// 防止线程顺序造成的错误 可能会慢
					listView.setAdapter(adapter);
					myHandler.sendEmptyMessageDelayed(4, 1000L);
				} catch (Exception e) {
					Intent intent = new Intent(RecentActivity.this,
							ErrorBookShelfActivityTouch.class);
					startActivity(intent);
					finish();
					/*
					 * // 请求网络失败的时候 Looper.prepare();
					 * Toast.makeText(RecentActivity.this, Heibai.OFFLINE,
					 * Toast.LENGTH_SHORT).show(); if (realyList.size()>0) { for
					 * (int i = 0; i < realyList.size(); i++) {
					 * realyList.remove(i); } }
					 * 
					 * adapter = new Mybaseadapterr(); // 欢迎就解析 完了适配 完了3秒后再显示
					 * 防止线程顺序造成的错误 可能会慢 listView.setAdapter(adapter);
					 * myHandler.sendEmptyMessageDelayed(4, 1000L);
					 * Looper.loop();
					 */}
			}

		});
		thread2.start();

	}

	// 横向 irem的参数
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);

	// 进度条参数
	FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT,
			FrameLayout.LayoutParams.WRAP_CONTENT);
	// 日期参数
	FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT,
			FrameLayout.LayoutParams.WRAP_CONTENT);

	/**
	 * @author zhang 适配器类 图片等所有组件的 参数 都来至于 demo类
	 */
	private class Mybaseadapterr extends BaseAdapter {

		public int getCount() {

			return list.size();

		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = layout.inflate(R.layout.listitem, null);
			View view = convertView.findViewById(R.id.library23);
			LayoutParams paramsmain = view.getLayoutParams();
			paramsmain.height = demo.rowheight / 3;// item 的高度
			paramsmain.width = demo.width;// item 的宽度
			/*
			 * LayoutParams paramsmain = view.getLayoutParams();
			 * paramsmain.height = demo.imageheight;// item 的高度 paramsmain.width
			 * = demo.width;// item 的宽度
			 */
			View Frameone = (View) convertView.findViewById(R.id.framlayoutone);
			View Frametwo = (View) convertView.findViewById(R.id.framlayouttwo);
			View Framethree = (View) convertView.findViewById(R.id.framlayoutthree);
			lp.gravity = Gravity.BOTTOM;
			lp.bottomMargin = demo.bottomMargin;
			lp.leftMargin = demo.leftMargin;
			Frameone.setLayoutParams(lp);
			Frametwo.setLayoutParams(lp);
			Framethree.setLayoutParams(lp);
			ImageButton one = (ImageButton) convertView.findViewById(R.id.one);
			ImageButton two = (ImageButton) convertView.findViewById(R.id.two);
			ImageButton three = (ImageButton) convertView
					.findViewById(R.id.three);
			LayoutParams params = one.getLayoutParams();
			params.width = demo.imagewidth;
			params.height = demo.imageheight;
			one.setLayoutParams(params);
			// two.setLayoutParams(params);
			three.setLayoutParams(params);
			ImageButton downloadButton = (ImageButton) convertView
					.findViewById(R.id.downButtonone);
			ImageButton downloadButton2 = (ImageButton) convertView
					.findViewById(R.id.downButtontwo);
			ImageButton downloadButton3 = (ImageButton) convertView
					.findViewById(R.id.downButtonthree);
			downloadButton.setOnClickListener(downclick);
			downloadButton2.setOnClickListener(downclick);
			downloadButton3.setOnClickListener(downclick);
			one.setOnClickListener(RecentActivity.this);
			two.setOnClickListener(RecentActivity.this);
			three.setOnClickListener(RecentActivity.this);
			ProgressBar Progressone = (ProgressBar) convertView
					.findViewById(R.id.downbarone);
			ProgressBar progresstwo = (ProgressBar) convertView
					.findViewById(R.id.downbartwo);
			ProgressBar Progressthree = (ProgressBar) convertView
					.findViewById(R.id.downbarthree);
			lp3.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
			lp3.width = demo.imagewidth - 20;
			lp3.height = 20;
			// 设置进度条参数
			Progressone.setLayoutParams(lp3);
			progresstwo.setLayoutParams(lp3);
			Progressthree.setLayoutParams(lp3);

			// 下载按钮参数
			if (demo.width <= 480) {
				LayoutParams dfd = downloadButton.getLayoutParams();
				dfd.width = 32;
				dfd.height = 32;
				downloadButton.setLayoutParams(dfd);
				downloadButton2.setLayoutParams(dfd);
				downloadButton3.setLayoutParams(dfd);
			}

			TextView textViewoneTextView = (TextView) convertView
					.findViewById(R.id.onedate);
			textViewoneTextView.setTextSize(rate * 6);

			TextView textViewoneTextView2 = (TextView) convertView
					.findViewById(R.id.twodate);
			textViewoneTextView2.setTextSize(rate * 6);

			TextView textViewoneTextView3 = (TextView) convertView
					.findViewById(R.id.threedate);
			textViewoneTextView3.setTextSize(rate * 6);

			lp2.bottomMargin = demo.bottomMargin - 10;
			lp2.width = demo.imagewidth;
			// lp2.leftMargin=;//待定
			lp2.gravity = Gravity.RELATIVE_LAYOUT_DIRECTION;
			lp2.height = FrameLayout.LayoutParams.WRAP_CONTENT;

			textViewoneTextView.setLayoutParams(lp2);
			textViewoneTextView2.setLayoutParams(lp2);
			textViewoneTextView3.setLayoutParams(lp2);
			if (position == 0) {
				one.setTag(0);
				two.setTag(1);
				three.setTag(2);
				downloadButton.setId(10);// 0
				downloadButton2.setId(11);// 1
				downloadButton3.setId(12);// 2
				two.setLayoutParams(params);
				// 先看是否解析到数据 -》 然后再 看是否更新 -》 最后看pdf是否下载 -》如果没有进check（）找 缓存
				if (realyList.size() > 0) {
					textViewoneTextView.setText(cace.get(0));
					textViewoneTextView2.setText(cace.get(1));
					textViewoneTextView3.setText(cace.get(2));
					if (new File(topcache, MD5.getMD5(cace.get(0)) + ".zip")
							.exists()) {
						downloadButton.setVisibility(View.GONE);

					}

					if (new File(topcache, MD5.getMD5(cace.get(1)) + ".zip")
							.exists()) {
						downloadButton2.setVisibility(View.GONE);
					}
					if (new File(topcache, MD5.getMD5(cace.get(2)) + ".zip")
							.exists()) {
						downloadButton3.setVisibility(View.GONE);
					}
					whodelent.put(cace.get(0), downloadButton);
					whodelent.put(cace.get(1), downloadButton2);
					whodelent.put(cace.get(2), downloadButton3);
				} else {
					whodelent.put(cace.get(0), downloadButton);
					whodelent.put(cace.get(1), downloadButton2);
					whodelent.put(cace.get(2), downloadButton3);
				}
				Progressone.setId(20);
				progresstwo.setId(21);
				Progressthree.setId(22);
				progressBars.add(Progressone);
				progressBars.add(progresstwo);
				progressBars.add(Progressthree);
				imageList.add(one);
				imageList.add(two);
				imageList.add(three);
				// one.setImageBitmap(Tools.resizeBitmap(bitmap, w, h))
			} else if (position == 1) {
				one.setTag(3);
				two.setTag(4);
				three.setTag(5);
				downloadButton.setId(13);
				downloadButton2.setId(14);
				downloadButton3.setId(15);
				Progressone.setId(23);
				progresstwo.setId(24);
				Progressthree.setId(25);
				progressBars.add(Progressone);
				progressBars.add(progresstwo);
				progressBars.add(Progressthree);
				imageList.add(one);
				imageList.add(two);
				imageList.add(three);
				two.setLayoutParams(params);
				if (realyList.size() > 0) {
					textViewoneTextView.setText(cace.get(3));
					textViewoneTextView2.setText(cace.get(4));
					textViewoneTextView3.setText(cace.get(5));
					if (new File(topcache, MD5.getMD5(cace.get(3)) + ".zip")
							.exists()) {
						downloadButton.setVisibility(View.GONE);
					}
					if (new File(topcache, MD5.getMD5(cace.get(4)) + ".zip")
							.exists()) {
						downloadButton2.setVisibility(View.GONE);
					}
					if (new File(topcache, MD5.getMD5(cace.get(5)) + ".zip")
							.exists()) {
						downloadButton3.setVisibility(View.GONE);
					}
					whodelent.put(cace.get(3), downloadButton);
					whodelent.put(cace.get(4), downloadButton2);
					whodelent.put(cace.get(5), downloadButton3);
				} else {
					whodelent.put(cace.get(3), downloadButton);
					whodelent.put(cace.get(4), downloadButton2);
					whodelent.put(cace.get(5), downloadButton3);
				}

			} else if (position == 2) {
				one.setTag(6);
				two.setTag(7);
				three.setTag(8);
				downloadButton.setId(16);
				downloadButton2.setId(17);
				downloadButton3.setId(18);
				Progressone.setId(26);
				progresstwo.setId(27);
				Progressthree.setId(28);
				progressBars.add(Progressone);
				progressBars.add(progresstwo);
				progressBars.add(Progressthree);
				// Frametwo.setVisibility(View.GONE);
				// textViewoneTextView2.setText("这是个测试");
				imageList.add(one);
				imageList.add(two);
				downloadButton2.setVisibility(View.GONE);
				progresstwo.setVisibility(View.GONE);
				textViewoneTextView2.setVisibility(View.GONE);
				// two.setBackgroundResource(R.drawable.icon);
				LayoutParams layoutParams = two.getLayoutParams();
				layoutParams.width = demo.imagewidth;
				layoutParams.height = demo.imageheight / 2;
				two.setLayoutParams(layoutParams);
				two.setImageResource(R.drawable.old);
				Framethree.setVisibility(View.GONE);
				/*
				 * FrameLayout layuot=(FrameLayout)
				 * convertView.findViewById(R.id.framlayouttwo); ImageButton
				 * button=(ImageButton) layuot.findViewById(R.id.two);
				 * two.setBackgroundResource(R.drawable.add);
				 */
				// . .setVisibility(View.GONE)
				// convertView.findViewById(R.id.framlayoutthree).setVisibility(View.GONE);

				/*
				 * imageList.add(two); imageList.add(three);
				 */
				if (realyList.size() > 0) {
					textViewoneTextView.setText(cace.get(6));
					if (new File(topcache, MD5.getMD5(cace.get(6)) + ".zip")
							.exists()) {
						downloadButton.setVisibility(View.GONE);
					}
					whodelent.put(cace.get(6), downloadButton);
					myHandler.sendEmptyMessage(3); // 适配完后再请求数据 不会因线程先后顺序不确定造成问题
													// getview 初始化会近来这里？？
				} else {
					whodelent.put(cace.get(6), downloadButton);
				}

			}

			return convertView;
		}

		/*
		 * 先用对的日期测试 需要改
		 */
		/*
		 * private void check(ImageButton one, ImageButton downloadButton, int
		 * i) { // System.out.println(cace[i]); whodelent.put(cace[i],
		 * downloadButton); if (new File(topcache, MD5.getMD5(cace[i]) +
		 * ".png").exists()) { // 缓存文件存在sd Bitmap bitmap =
		 * Tools.resizeBitmap(topcache.getAbsolutePath()+"/"+
		 * MD5.getMD5(cace[i])+".png", demo.imagewidth, demo.imageheight);
		 * one.setImageBitmap(bitmap); if (new File(topcache,
		 * MD5.getMD5(cace[i]) + ".zip").exists()) {
		 * downloadButton.setVisibility(View.GONE); } } }
		 */

	}

	/**
	 * 获取屏幕宽 高
	 */
	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		demo = Demo.getData(display.getWidth(), display.getHeight());
		Editor editor = sPreferences.edit();
		/*
		 * public int width;//屏幕宽 public int height;//屏幕高 public int
		 * bottomMargin;//下边距 public int leftMargin;//左边距 public int
		 * imagewidth;//缩略图 宽 public int imageheight;//缩略图高 public int
		 * headimageheight;//标题图片高 public int rowheight;//item 的高度
		 */editor.putInt("width", demo.width);
		editor.putInt("height", demo.height);
		editor.putInt("bottomMargin", demo.bottomMargin);
		editor.putInt("leftMargin", demo.leftMargin);
		editor.putInt("imagewidth", demo.imagewidth);
		editor.putInt("imageheight", demo.imageheight);
		editor.putInt("headimageheight", demo.headimageheight);
		editor.putInt("rowheight", demo.rowheight);
		editor.commit();
	}
	private void getScreenSizeDemo() {
		demo=new Demo();
		demo.width=sPreferences.getInt("width", 0);
		demo.height=sPreferences.getInt("height", 0);
		demo.bottomMargin=sPreferences.getInt("bottomMargin", 0);
		demo.leftMargin	=sPreferences.getInt("leftMargin", 0);
		demo.imagewidth	=sPreferences.getInt("imagewidth", 0);
		demo.imageheight=sPreferences.getInt("imageheight", 0);
		demo.headimageheight=sPreferences.getInt("headimageheight", 0);
		demo.rowheight=sPreferences.getInt("rowheight", 0);
		if (demo.width!=0&&demo.height!=0&&demo.bottomMargin!=0&&demo.leftMargin!=0
				&&demo.imagewidth!=0&&demo.imageheight!=0&&
				demo.headimageheight!=0&&demo.rowheight!=0) {
			//数据正确
		}else {//数据不正确
			getScreenSize();
		}
	}
	public String getNoUpdate(String str) {
		int startandend = str.indexOf("\n");
		String xxString = Heibai.UPDATENO + str.substring(startandend);
		return Heibai.UPDATENO + str.substring(startandend);
	}

	/**
	 * 打开方法 ZipDecrypter.decrypter(new File( new
	 * File(Environment.getExternalStorageDirectory(),
	 * "chinamilpdf"),MD5.getMD5(title)+".zip"), new
	 * File(Environment.getExternalStorageDirectory(), "chinamilpdf"));
	 */

	protected void onRestart() {
		super.onRestart();

	}

	public void onClick(View v) {
		// 打开
		int c = (Integer) v.getTag();
		if (c == 7) {
			startActivityForResult(new Intent(RecentActivity.this,
					BookShelfActivity.class), 1);
		} else if (progressBars.get(c).getVisibility() == View.GONE) {
			if (realyList.size() > 0) {// 解析 集合是否有数据
				String pathString = cace.get(c); // 不支持中文
				File ffFile = new File(topcache, MD5.getMD5(pathString)
						+ ".zip");
				if (!ffFile.exists()) {// 是否存在
					Toast.makeText(RecentActivity.this, Heibai.NODOWN,
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					/*
					 * SimpleDateFormat sdf=new SimpleDateFormat("h:mm:s");
					 * String cdString=sdf.format(System.currentTimeMillis());
					 */
					Intent intent = new Intent(RecentActivity.this,
							WebviewActivity.class);
					intent.putExtra("who", MD5.getMD5(cace.get(c)));
					intent.putExtra("date", cace.get(c));
					/*
					 * if (c<=realyList.size()) {
					 * intent.putExtra("classify",realyList.get(c).channels); }
					 */
					startActivity(intent);
				}

			} else { // 没有数据根据日期找缓存
				File ffFile = new File(topcache, MD5.getMD5(cace.get(c))
						+ ".zip");
				if (ffFile.exists()) {
					Intent intent = new Intent(RecentActivity.this,
							WebviewActivity.class);
					intent.putExtra("who", MD5.getMD5(cace.get(c)));
					/*
					 * SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:s");
					 * String cdString=sdf.format(System.currentTimeMillis());
					 */
					intent.putExtra("date", cace.get(c));
					// intent.putExtra("classify", MD5.getMD5(cace[c]));
					startActivity(intent);
				} else {
					Toast.makeText(RecentActivity.this, Heibai.NODOWN, 1)
							.show();
				}

			}

		} else {
			Toast.makeText(RecentActivity.this, Heibai.UNDONE, 1).show();
		}
		//
	}

	// Thread thread;// 下载线程 与解析线程

	private class Myclickdown implements OnClickListener {
		// 下载按钮处理事件
		public void onClick(View v) {
			final int c = v.getId() - 10;
			final View downView = v;
			if (realyList.size() > 0) {
				v.setVisibility(View.GONE);
				progressBars.get(c).setVisibility(View.VISIBLE);
				pool.execute(new Mythread(c, v));
				/*
				 * // 网通 直接下载 if (who.isEmpty()) { downpdf(c, downView);ddd }
				 * else { // 加入队列 int cc=0; for (int i = 0; i < queue.size();
				 * i++) { if (queue.get(i).index==c) { //保证不会重复添加 cc=1; } } if
				 * (cc==0) { queue.add(new Queuedomain(c, downView));
				 * v.setBackgroundResource(R.drawable.downtwo);
				 * Toast.makeText(RecentActivity.this, Heibai.QUEUE,
				 * Toast.LENGTH_SHORT).show(); }
				 * 
				 * }
				 */} else { // 不存在网络
				Toast.makeText(RecentActivity.this, Heibai.AGAIN,
						Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					public void run() {
						try {// 再次请求网络
							realyList = ParserXml.getPicture(Heibai.URL);
						} catch (Exception e) {
							Message msg = new Message();
							msg.what = 5;
							msg.obj = downView;
							myHandler.sendMessage(msg);
						}
					}
				}).start();
			}

		}

	}

	/**
	 * * @param c——》索引参数
	 * 
	 * @param down
	 *            , ：-》 下载按钮 下载方法
	 * 
	 * 
	 */
	/*
	 * private void downpdf(final int c, final View down) {
	 * down.setVisibility(View.GONE);// 隐藏 //
	 * progressBars.get(c).setVisibility(View.VISIBLE); new Thread(new
	 * Runnable() { public void run() { String title
	 * =cace.get(c);//ProgressBarListener try { MultDownloader multDownloader =
	 * new MultDownloader(new ProgressBarListener() {
	 *//**
	 * 下载回调到这里 通知进度条改变 以及设置进度条最大值
	 */
	/*
	 * public void getMax(int len) throws NetworkErrorException{
	 * //progressBars.get(c).setVisibility(View.VISIBLE); Message msg =
	 * myHandler.obtainMessage(1); msg.obj = progressBars.get(c); msg.arg1 =
	 * len; myHandler.sendMessage(msg); }
	 * 
	 * public void downloadsize(int len) throws NetworkErrorException { //
	 * cc=cc+3; // 不需要手动控制线程 停止 Message msg = myHandler.obtainMessage(0);
	 * msg.obj = progressBars.get(c); msg.arg1 = len; msg.arg2 = c;
	 * myHandler.sendMessage(msg); } }, topcache, title); // 真正下载方法
	 * multDownloader.download(realyList.get(c).link, RecentActivity.this); }
	 * catch (Exception e) { e.printStackTrace(); new File(topcache,
	 * MD5.getMD5(title) + ".zip").delete();// 删除下载一半的文件 Message msg = new
	 * Message(); msg.obj = down; msg.arg1 = c; msg.what = 5;
	 * myHandler.sendMessage(msg); } }
	 * 
	 * }).start();
	 * 
	 * }
	 */
	class Mythread extends Thread {
		int c;
		View down;

		Mythread(int c, View down) {
			this.c = c;
			this.down = down;
		}

		public void run() {
			String title = cace.get(c);// ProgressBarListener
			try {
				MultDownloader multDownloader = new MultDownloader(
						new ProgressBarListener() {
							/**
							 * 下载回调到这里 通知进度条改变 以及设置进度条最大值
							 */
							public void getMax(int len)
									throws NetworkErrorException {
								// progressBars.get(c).setVisibility(View.VISIBLE);
								Message msg = myHandler.obtainMessage(1);
								msg.obj = progressBars.get(c);
								msg.arg1 = len;
								myHandler.sendMessage(msg);
							}

							public void downloadsize(int len)
									throws NetworkErrorException {
								// cc=cc+3;
								// 不需要手动控制线程 停止
								Message msg = myHandler.obtainMessage(0);
								msg.obj = progressBars.get(c);
								msg.arg1 = len;
								msg.arg2 = c;
								myHandler.sendMessage(msg);
							}
						}, topcache, title);
				// 真正下载方法
				multDownloader.download(realyList.get(c).link,
						RecentActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
				new File(topcache, MD5.getMD5(title) + ".zip").delete();// 删除下载一半的文件
				Message msg = new Message();
				msg.obj = down;
				msg.arg1 = c;
				msg.what = 5;
				myHandler.sendMessage(msg);
			}
		}

	}

	/*
	 * private void downpdf(final int c, final View down) { if
	 * (zipDown.getinstace()!=null) {//正在下载 zipDown.addTask(c);//添加要下载的title
	 * Toast.makeText(RecentActivity.this,"已加入下载队列",0).show(); } else {
	 * zipDown.addTask(c);//添加要下载的title down.setVisibility(View.GONE);// 隐藏
	 * zipDown.getinstace(new Runnable() {// 加入到单线程队列中去 下载对象在who里面 public void
	 * run() { final int index; index=zipDown.getIndex(); String title =
	 * cace[index]; System.out.println(c+"下载文件名"); MultDownloader multDownloader
	 * = new MultDownloader(new ProgressBarListener() {
	 *//**
	 * 下载回调到这里 通知进度条改变 以及设置进度条最大值
	 */
	/*
	 * public void getMax(int len) { Message msg = myHandler.obtainMessage(1);
	 * msg.obj = progressBars.get(index); msg.arg1 = len;
	 * myHandler.sendMessage(msg); } public void downloadsize(int len) { //
	 * cc=cc+3; // 不需要手动控制线程 停止 Message msg = myHandler.obtainMessage(0);
	 * msg.obj = progressBars.get(index); msg.arg1 = len; msg.arg2 = index;
	 * myHandler.sendMessage(msg); } }, topcache, title);
	 * 
	 * try { // 真正下载方法 multDownloader.download(realyList.get(index).link,
	 * RecentActivity.this); } catch (Exception e) { new File(topcache,
	 * MD5.getMD5(title) + ".zip").delete();// 删除下载一半的文件 Message msg = new
	 * Message(); msg.obj = down; msg.arg1 = index; msg.what = 5;
	 * myHandler.sendMessage(msg); } }
	 * 
	 * }).start();
	 * 
	 * } }
	 */
	/**
	 * 是否是闰年 计算2月份的天数 看是否是 2 4 6 9 11
	 */

	/*
	 * String bucongString[] = { "31", "30", "29", "28", "27", "26" }; String
	 * bucongString2[] = { "30", "29", "28", "27", "26", "25" }; String
	 * bucongString3[] = { "28", "27", "26", "25", "24", "23" };// 闰年2月 String
	 * bucongString4[] = { "29", "28", "27", "26", "25", "24" };// 非闰年2月
	 * List<Integer> dateList = new ArrayList<Integer>();
	 * 
	 * public void year(int xingqin, int date, int month, int year) { int today
	 * = 0; int index = 0;
	 * 
	 * if (date < 7) {// 本月的前7天内 for (int i = 0; i < date; i++) { dateString[i]
	 * = year + "/" + month + "/" + (date - i); today = i; } if (date != 7) {//
	 * 还不够7天 加入上一个月的 最后几天 month = month - 1; for (int i = 0; i < 7 - date; i++)
	 * {// 少了几天 String monthdate = null; if (month != 1 &&
	 * dateList.contains(month)) {// 不是一月 // 且都是30天的情况下 monthdate =
	 * String.valueOf(bucongString2[i]); } else if (month == 1) {// 加上一年的 year =
	 * year + 1; month = 12; monthdate = String.valueOf(bucongString[i]); } else
	 * if (month != 2) { monthdate = String.valueOf(bucongString[i]); } if
	 * (month == 2) { if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
	 * monthdate = String.valueOf(bucongString3[i]);
	 * 
	 * } else { monthdate = String.valueOf(bucongString4[i]);
	 * 
	 * } } dateString[today + 1 + i] = year + "/" + month + "/" + monthdate; } }
	 * 
	 * } else { for (int i = 0; i < dateString.length; i++) { dateString[i] =
	 * year + "/" + month + "/" + (date - i);
	 * 
	 * } } cace = getStrings(cace);// sds 设置cace
	 * 
	 * for (int i = 0; i < cace.length; i++) {// 获取对应的星期 Date date2 = new
	 * Date(System.currentTimeMillis() - i * 1000 * 60 * 60 * 24); dateString[i]
	 * = getWeekOfDate(date2) + "\n" + dateString[i]; }
	 * 
	 * }
	 */

	/**
	 * 获取 日期对应的星期
	 * 
	 * @param date
	 * @return
	 */
	public String getWeekOfDate(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	/**
     *
     */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.browsermenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 替换/为对应的年月日
	 * 
	 * @param string
	 * @return
	 */
	/*
	 * public String[] getStrings(String[] string) { string = new String[7]; for
	 * (int i = 0; i < string.length; i++) { string[i] =
	 * dateString[i].replaceFirst("/", "年"); string[i] =
	 * string[i].replaceFirst("/", "月"); string[i] = string[i] + "日"; } return
	 * string;
	 * 
	 * }
	 */

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mainmenu_settings:
			System.exit(0);
			return true;
		case R.id.mainmenu_about:
			about();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	// 关于对话框
	public void about() {
		startActivity(new Intent(RecentActivity.this, About.class));
	}

	int i = 0;// 返回键按下次数计算 辅助变量

	Timer aTimer = new Timer();

	public void onBackPressed() {
		ErrorDialog(Heibai.EXIT);
	}

	protected void onDestroy() {
		super.onDestroy();
		index = 0;
		if (aTimer != null) {
			aTimer = null;
		}
		who = null;
		whodelent = null;

	}

	int index = 0;// 记录运行异步加载到那个imagebutton 对象

	private void asyncloadImage() {// 发送异步任务.
		String paString = null, title = null;
		if (realyList.size() > 0) {// 网有
			// 判断无用
			if (index < realyList.size()) {
				title = cace.get(index);
				for (int i = 0; i < realyList.size(); i++) {
					if (title.equals(realyList.get(i).title)) {
						paString = realyList.get(i).cover;
						break;
					}
				}
				// if (title!=null&&paString!=null) {
				Myasay task = new Myasay(imageList.get(index), paString, title);
				task.execute(paString);
				// }
				index++;
			} else {
				index = 0;
			}

			// "_id", "imag", "link", "title"
		} else {

			// if(){//网无 使用cursor 设置
			// dateString
			/*
			 * Myasay task = new Myasay(imageList.get(index), paString, title);
			 * task.execute(paString);
			 * 
			 * if (index < 7) { Mycursor.moveToLast(); int
			 * count=Mycursor.getCount(); paString=Mycursor.getString(1);
			 * title=Mycursor.getString(3); Myasay task = new
			 * Myasay(imageList.get(index), paString, title);
			 * task.execute(paString); index++;
			 * Mycursor.moveToPosition(count--); }else{ index = 0; }
			 */

		}

	}

	Animation animation;
	Animation testanimation;

	/**
	 * 异步任务类
	 * 
	 * @author zhang
	 * 
	 */
	private class Myasay extends AsyncTask<String, Void, Bitmap> {

		private ImageButton imaget;
		private String path;
		private String title;

		public Myasay(ImageButton imaget, String path, String title) {
			super();
			this.imaget = imaget;
			this.path = path;
			this.title = title;
		}

		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				imaget.setImageBitmap(result);
				imaget.startAnimation(animation);

			} else {
				/*
				 * try { String name = MD5.getMD5(title) + ".png"; new
				 * File(topcache, name).delete();
				 * imaget.setImageBitmap(Tools.resizeBitmap
				 * (ParserXml.getImage(path, title, topcache).getPath(),
				 * demo.imagewidth, demo.imageheight)); } catch (Exception e) {
				 */
				Message msg = new Message();
				msg.what = 6;
				msg.obj = title;
				myHandler.sendMessage(msg);
				// }// 缩略图
			}

			if (index < realyList.size()) {
				asyncloadImage();
			}

		}

		// 图片
		protected Bitmap doInBackground(String... params) {
			try {

				return Tools.resizeBitmap(
						ParserXml.getImage(path, title, topcache).getPath(),
						demo.imagewidth, demo.imageheight);// 缩略图
			} catch (Exception e) {
				// 缩略图获取错误

			}
			return null;
		}
	}

	/**
	 * 退出提示
	 * 
	 * @param context
	 * @param what
	 */
	public void ErrorDialog(String title) {

		new AlertDialog.Builder(this)
				.setTitle(title)
				.setIcon(R.drawable.imageback)
				.setMessage("确定退出程序？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						if (!who.isEmpty()) {

							// new File(topcache, MD5.getMD5(who.get(0)) +
							// ".zip").delete();
							/*
							 * if (0!=update) { String dateString=
							 * getDateString(who.get(0)); new File(topcache,
							 * MD5.getMD5(dateString) + ".zip").delete(); }else
							 * {
							 */
							new File(topcache, MD5.getMD5(who.get(0)) + ".zip")
									.delete();
							// }

						}
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	/*
	 * public String getDateString(String str){ String
	 * riString=str.substring(str.indexOf("月")+1,str.indexOf("日")); String
	 * yueString=str.substring(0,str.indexOf("月")); int
	 * index=Integer.parseInt(riString)+update; return yueString+"月"+index+"日";
	 * }
	 */
	/**
	 * 、 作用主要为 当用户进入管理删除一些文件时 主ui对应的 下载按钮会重新显示
	 */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 更多回传数据
		if (data != null) {
			ArrayList<String> array = data.getStringArrayListExtra("me");
			for (int i = 0; i < array.size(); i++) {
				String naString = array.get(i);// 联网与否
				for (int j = 0; j < cace.size(); j++) {
					String twoString = cace.get(j);
					if (naString.equals(twoString)) {
						ImageButton button = whodelent.get(array.get(i));
						if (button != null) {
							// 存在不确定的问题 收藏与更多 就是这个不一样 一个是显示 一个是隐藏
							if (requestCode == 1) {
								button.setVisibility(View.GONE);
							} else {
								button.setVisibility(View.VISIBLE);
							}

							button.setBackgroundResource(R.drawable.downone);
						}
					}
				}
			}
		}

		// System.out.println("接受完毕dfdd");
	}

	private Myquery myquery;

	private class Myquery extends AsyncQueryHandler {

		public Myquery(ContentResolver cr) {

			super(cr);
		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			switch (token) {
			case 0:
				if (cursor != null) { // 管理功能查询完毕后 有数据跳转
					if (cursor.getCount() > 0) {
						startActivityForResult(new Intent(RecentActivity.this,
								Mananger.class), 9);
						// finish();
					} else {
						Toast.makeText(RecentActivity.this, Heibai.NO, 1)
								.show();
					}
				} else {
					Toast.makeText(RecentActivity.this, Heibai.NO, 1).show();

				}

				break;
			case 1:

				if (cursor != null) { // 查询完毕后 有数据跳转
					if (cursor.getCount() > 0) {
						// Intent intent= new Intent(RecentActivity.this,
						// UrlItem.class);
						// startActivity(intent);
						Intent intent = new Intent(RecentActivity.this,
								Shears.class);
						startActivity(intent);
						// finish();
					} else {
						Toast.makeText(RecentActivity.this, Heibai.NO, 1)
								.show();
					}
				} else {
					Toast.makeText(RecentActivity.this, Heibai.NO, 1).show();

				}
				break;
			default:
				break;
			}

		}
	}

	private void applyRotation(int position, float start, float end) {
		/*
		 * welcom.setVisibility(View.GONE); library.setVisibility(View.VISIBLE);
		 * library.requestFocus();
		 */
		final float centerX = jfjbk.getWidth() / 2.0f;
		final float centerY = jfjbk.getHeight() / 2.0f;
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310f, false);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));
		welcom.startAnimation(rotation);

	}

	/**
	 * 动画类
	 * 
	 * @author zhang
	 * 
	 */
	private final class DisplayNextView implements Animation.AnimationListener {

		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			jfjbk.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private final class SwapViews implements Runnable {

		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}
		public void run() {
			final float centerX = jfjbk.getWidth() / 2.0f;
			final float centerY = jfjbk.getHeight() / 2.0f;
			Rotate3dAnimation rotation = null;
			if (mPosition > -1) {
				welcom.setVisibility(View.GONE);
				library.setVisibility(View.VISIBLE);
				library.requestFocus();
				rotation = new Rotate3dAnimation(270, 360, centerX, centerY,
						310.0f, false);
			} /*
			 * else {//重新转回去不做处理 mImageView.setVisibility(View.GONE);
			 * mPhotosList.setVisibility(View.VISIBLE);
			 * mPhotosList.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f,
			 * false); }
			 */
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			jfjbk.startAnimation(rotation);
		}
	}

	/**
	 * 看是否更新
	 * 
	 * @param name
	 * @return
	 */
	public boolean isupadate(String name) {
		boolean a = false;
		for (int i = 0; i < realyList.size(); i++) {
			if (realyList.get(i).title.equals(name)) {
				a = true;
				break;
			}
		}

		return a;

	}
	/**
	 * 删除 缓存
	 */
	/*
	 * public void delcache() { File[] files = new
	 * File(Environment.getExternalStorageDirectory(),
	 * "chinamilpdf").listFiles(); for(File file :files){ file.delete(); }
	 * 
	 * }
	 */

}
