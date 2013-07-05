package org.chinamil.networkerr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.ParserXml;
import org.chinamil.PdfDomin;
import org.chinamil.R;
import org.chinamil.ui.library.WebviewActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ErrorBookShelfActivity extends BaseActivity    {
	private RelativeLayout.LayoutParams lp;
	private Bitmap backagBitmap		 ;
	private LinkedList<PdfDomin>data; 
	private List<GridView> imageViews;
	private ViewPager viewPager;
	private LayoutInflater layoutInflater;//54-47
	private int num, height,width,backgroundhei;
	private ViewGroup relativeLayout;
	private boolean touch=false;//是否在拖动
	boolean iszheng;//是否有余数
	int location[]=new int[2];
	LinearLayout linerlayout;
	LinearLayout.LayoutParams layoutParams;
	float rate;//字體大小
	int eage;
	int page2,pageCount,uppage;
	private	Mytask	taskMytask;
	private List<View> dotViews;
	Cursor cursor;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.erromain);
		linerlayout=(LinearLayout) findViewById(R.id.dot);
		relativeLayout=(ViewGroup) findViewById(R.id.addliner);
		data=new LinkedList<PdfDomin>();
		  //查询  暂不加条件
		cursor=getContentResolver().query(Heibai.MANAGE_URI, new String[] { "_id", 
				"imag", 
				"link", "title","month" }, 
				null,
				null,
				null);
	     if (cursortolist(cursor)) {
		viewPager = (ViewPager) findViewById(R.id.vp);
		 viewPager.setOffscreenPageLimit(1);
			height = getWindowManager().getDefaultDisplay().getHeight();
			width= getWindowManager().getDefaultDisplay().getWidth();
			 rate = (float) width/40;
		 backagBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.bk);
			 eage=height/(backagBitmap.getHeight()+50);//计算界面需要显示多少横向的 书架
			/* android:layout_width="5dip"
	                    android:layout_height="5dip"
	                    android:layout_marginLeft="2dip"
	                    android:layout_marginRight="2dip"*/
			 layoutParams=new LinearLayout.LayoutParams(10, 10);
		 backgroundhei=height/eage; //單個 背景書架 的 高
		 System.out.println("主activiey 信息"+eage+" gdddd"+backgroundhei);
		layoutInflater = getLayoutInflater();
		imageViews = new ArrayList<GridView>();
	
		num = height / backgroundhei - 1;
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		getPage();
		dotViews=new ArrayList<View>();
		 for (int i = 0; i < pageCount; i++) {//添加屏幕小圆点
				View dotviewView=new View(this);
				layoutParams.leftMargin=2;
				layoutParams.rightMargin=2;
				dotviewView.setLayoutParams(layoutParams);
				if (i!=0) {
					dotviewView.setBackgroundResource(R.drawable.dot_normal);
				}else {
					dotviewView.setBackgroundResource(R.drawable.dot_focused);	
				}
				dotViews.add(dotviewView);
				linerlayout.addView(dotviewView);
			}
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) {
				dotViews.get(arg0).setBackgroundResource(R.drawable.dot_focused);
				dotViews.get(uppage).setBackgroundResource(R.drawable.dot_normal);
				uppage=arg0;
				
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	     } else 
	     {
	    	 findViewById(R.id.errempty).setVisibility(View.VISIBLE);
	     }
	     
	
	
	}
	
/*	 "_id", 
		"imag", 
		"link", "title","month" }, */
private boolean cursortolist(Cursor cursor){
	if (cursor!=null&&cursor.getCount()>0) {
		for (int i = 0; i < cursor.getCount(); i++) {
			PdfDomin pdfDomin=new PdfDomin();
			cursor.moveToNext();
			pdfDomin.title=cursor.getString(3);
			pdfDomin.cover=cursor.getString(1);
			data.add(pdfDomin);
		}
		return true;
	}
	return false;
	
}
	/**
	 * 分割总数据  返回每页需要的数据
	 * @param data
	 * @param page
	 * @return
	 */
private LinkedList<PdfDomin> getData(LinkedList<PdfDomin>data,int page,int count,int  pagecount){
	LinkedList<PdfDomin> dataEach=new LinkedList<PdfDomin>();
	if (page>page2-1) {
		  for (int i = data.size(); i > height / (backgroundhei) * 3*page; i--) {
			  dataEach.add(data.get(i-1));
		}	
		  return dataEach;
		}
		else{
			for (int i = 0; i < height / backgroundhei * 3; i++) {
				dataEach.add(
						data.get(page*height / backgroundhei * 3+i)
						
						
						);
			}
			System.out.println(dataEach.size()+"分割数据的大小");
			  return dataEach;
		}
}
	
	
	
	

	/**
	 * gridview 的适配器
	 * 
	 * @author zhang
	 * private int i;
			*/	//System.out.println(height+"bookshelfacti"+backgroundhei+"xxxxxx"+height/(num+1));
		//	lp.topMargin=(height/(num+1)-backagBitmap2.getHeight())-18;
		//	lp.topMargin=139-backagBitmap2.getHeight();
			
			// lp.topMargin=hei-heit2+31;
			// lp.topMargin=hei-heit2;

	class ShlefAdapter extends ArrayAdapter<PdfDomin> {
		private int i;
		private	GridView mGridView;
		private	LinkedList<PdfDomin> mArray;
		private LinkedList<View>views;
		ShlefAdapter(int i,GridView mGridView,LinkedList<PdfDomin>mArray) {// 算出来的--去原始的
			super(ErrorBookShelfActivity.this, mGridView.getId(), mArray);
		/*	int hei = height / (num + 1);
			int heit2 = backgroundhei;
		lp.topMargin = hei - heit2 + 18;//红派
		30 模拟器
*/	lp.topMargin=backgroundhei/3;
			
			this.mGridView=mGridView;
			this.mArray=mArray;
			this.i=i;
			views=new LinkedList<View>();
	}
		
		@Override
		public PdfDomin getItem(int position) {
			// TODO Auto-generated method stub
			return mArray.get(position);
		}
		public View getItemView(int l) {
			return views.get(l);
		}
			public LinkedList<PdfDomin> getList() {
				return mArray;
			}
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView imageView = null;
				if (convertView == null) {
					convertView = layoutInflater.inflate(R.layout.erroitem1, null);
				}
				imageView=(TextView) convertView.findViewById(R.id.imageView1);
				imageView.setLayoutParams(lp);
				imageView.setTextSize(rate);
				PdfDomin pdfDomin=mArray.get(position);
				imageView.setText(pdfDomin.title);
			//	System.out.println(mArray.get(position).title+"我我我"+mArray.get(position).cover);
					taskMytask =new Mytask(imageView); //执行异步加载缩略图
		          //String ssString= cursor.getString(1);
		            taskMytask.execute(pdfDomin.title,pdfDomin.cover);
				/*imageView.setBackgroundResource(
		        			R.drawable.addic_launcher_yuedu
		        			);*/
		        	views.add(convertView);
				return convertView;
			}


		
		}

	/**
	 * 
	 * viewpager 适配器
	 * 
	 * @author zhang
	 * 
	 */
	class MyAdapter extends PagerAdapter {
		
		public int getCount() {
			System.out.println();
			return imageViews.size();
		}
		/*for (int i = 0; i < imageViews.size(); i++) {
			GridView ggGridView=imageViews.get(i);
			ShlefAdapter adapter=(ShlefAdapter) ggGridView.getAdapter();
	int xxx=adapter.views.size();
		int c=adapter.getCount();
			for (int j = 0; j < c; j++) {
				adapter.getItemView(j).getLocationOnScreen(location);
				System.out.println(location[0]+"坐标"+location[1]);
			}
			
		}*/
		// 实例化item
		
		public Object instantiateItem(View arg0, int arg1) {
			// 将每个图片加入到ViewPager里
			System.out.println();
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// 将每个图片在ViewPager里释放掉
			((ViewPager) arg0).removeView((View) arg2);
		}

		
		public boolean isViewFromObject(View arg0, Object arg1) {
			// view 和 Object 是不是一个对象
			return arg0 == arg1;
		}

		
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}
	 private class Mytask extends AsyncTask<String, Void, Drawable> {
	        TextView imageView;
	        public Mytask(TextView imaget) {
	            this.imageView = imaget;
	        }
	        protected void onPostExecute(Drawable result) {
	            super.onPostExecute(result);
	            if (result!=null) {
	            	  imageView.setBackgroundDrawable(result); 
				}
	          
	        }
	        protected Drawable doInBackground(String... params) {
	        	/*  imageView.setBackgroundDrawable(
			        		Tool.getBitmapDrawable(getResources(),
					        		BitmapFactory.decodeResource(getResources(),
					        				R.drawable.test),
					        		90,
					        		backgroundhei/2)
			        		);
			        		
			        		*BitmapFactory.decodeFile(topcache.getAbsolutePath() 
                		+ "/"+MD5.getMD5(params[1])+".png");
			        		*/
	           // Toast.makeText(Mananger.this, topcache.getAbsolutePath() + MD5.getMD5(params[1])+".png", 1).show();
	         //   System.out.println();
	         // System.out.println( "图片路径"+topcache.getAbsolutePath() + MD5.getMD5(params[1])+".png");
	            //创建图片http://wap.chinamil.com.cn/wap-paper/
	            if (params[1].contains(ParserXml.urlhead)) {
	            	System.out.println("hava a url ");
	                return             		
			        		Tool.getBitmapDrawable(getResources(),
					        		BitmapFactory.decodeFile(getCacheDir()+"/"
			        		       +MD5.getMD5(params[0])+".png"),	
			        		       backgroundhei/3,//-20,
			        		           backgroundhei-(backgroundhei/3));
	            }else {
	            	System.out.println("dont hava a url ");
	            	Tool.getBitmapDrawable(getResources(),
			        		BitmapFactory.decodeResource(getResources(),
			        				R.drawable.addic_launcher_yuedu),	
			        			    backgroundhei/3,//-20,
			        		           backgroundhei-(backgroundhei/3));
	                return null;
	            }
	                 
	           
	        }

	    }

	/**
	 * 计算需要多少页面能够完全显示
	 */
	
	private int getPageCount(int count) {
		System.out.println();
		int page = count > (count / (height / backgroundhei * 3)) ? count
				/ (height / backgroundhei * 3) : 1;// 求出是否一页能够显示完
		page = page == 1 ? page : count / (height / backgroundhei * 3);// 如果不能够一页显示完的话就求出需要多少页显示
		page2=page;
	System.out.println(iszheng+"是否有余数");
	page=count <= page * (height / backgroundhei * 3) ? page : page + 1;
	pageCount=page;
		return page; // 看是否有余数
																		// 如果有就再多添加一个页面；
	}
	Animation animation;
	
    TextView textView;
	   private class Myasay extends AsyncTask<String, Void, View> {
	        private TextView imaget;
	        private String path;
	        private String title;
	        public Myasay(TextView imaget) {
	            super();
	            this.imaget = imaget;
	  
	        }
	        protected void onPostExecute(View result) {
	            super.onPostExecute(result);
	             textView=new TextView(ErrorBookShelfActivity.this);
	            textView.setBackgroundResource(R.drawable.addic_launcher_yuedu);
	            textView.setText("测试");
	           animation=new TranslateAnimation(0, 0, 0, 0);
	           animation.setDuration(2000);
	           animation.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}
				public void onAnimationRepeat(Animation animation) {
				}
				public void onAnimationEnd(Animation animation) {
					imaget.setBackgroundResource(R.drawable.addic_launcher_yuedu);	
					textView.setVisibility(View.GONE);
				}
			});
	           relativeLayout.addView(textView);
	           textView.startAnimation(animation);
	        }

	        // 图片
	        protected View doInBackground(String... params) {
	
	            return null;
	        }
	    }
	   /**
		 * 填充gridview的内容 并且添加到viewpage 适配器里面
		 * 
		 * @return
		 */
		private void getPage() {
			System.out.println();
		int coun=getPageCount(data.size());
			for (int i = 0; i < coun; i++) {
				GridView inflate = (GridView) layoutInflater.inflate(R.layout.gridview,
						null);
			//	inflate.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(BookShelfActivity.this, R.drawable.layout_random_fade)));
			//	inflate.setOnTouchListener(BookShelfActivity.this);
				inflate.setNumColumns(3);
				ShlefAdapter adapter = new ShlefAdapter(i,inflate,getData(data,i,data.size(),coun));
				inflate.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ShlefAdapter adapter= (ShlefAdapter) parent.getAdapter();
						   PdfDomin pdf=adapter.getItem(position);

			                Intent intent=new Intent(ErrorBookShelfActivity.
			                		this,WebviewActivity.class);
			                intent.putExtra("who", MD5.getMD5(pdf.title));
			                intent.putExtra("classify","ccc");
			                startActivity(intent);
			            
				
					}
				});
				inflate.setAdapter(adapter);
				imageViews.add(inflate);
			}
		}


	   
}/*private int from = -1;
	private int to = -1;
	private int aniFrom = -1;
	private int aniTo = -1;
	private boolean doingAni = false;
	private TextView mDragView = null;
	private	ShlefAdapter  adapter;
	int clickX;
	int clickY;
	private GridView mGridView;
    public boolean onTouch(View v, MotionEvent event) {
    	mGridView=imageViews.get(viewPager.getCurrentItem());
    	adapter=(ShlefAdapter) mGridView.getAdapter();
		//getRawX()和getRawY()获得的是相对屏幕的位置
		//getX()相对于控件的本身
		float x = event.getX();
		float y = event.getY();
		// 代表当前点击的位置，相对于资源中的位置。产生-1值是超出GridView的范围；
		int position = mGridView.pointToPosition((int) x, (int) y);
		Log.i("ivan", "position = " + position);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//判断是点击位置是否在GridView中
			if (position == -1) {
				break;
			}
			// getChildAt 获取“当前屏幕”上的第几个View 
			View tempView = mGridView.getChildAt(position - mGridView.getFirstVisiblePosition());
			//Toast.makeText(BookShelfActivity.this, "xxx"+(position - mGridView.getFirstVisiblePosition()), 1).show();
			// 获取缓存 要先打开开关
			tempView.setDrawingCacheEnabled(true);
			Bitmap tempBitmap = tempView.getDrawingCache();
			// 自定义一个图片控件
			TextView imageView = new TextView(getApplicationContext());
			imageView.setBackgroundDrawable(new BitmapDrawable(tempBitmap));
			// 任何改变控件在界面上的显示状态 只有通过layoutParams
			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//getLeft , getRight 这一组是获取相对在它父亲里的坐标
			rllp.leftMargin = tempView.getLeft();
			rllp.topMargin = tempView.getTop();
			// 代表down的时候点击的坐标 距此图标左上角的偏移量
			clickX = (int) (x - tempView.getLeft());
			clickY = (int) (y - tempView.getTop());
			relativeLayout.addView(imageView, rllp);
			mDragView = imageView;
			// 设置控件不可见
			tempView.setVisibility(View.INVISIBLE);
			// 保存起点位置
			from = position;
			break;
		// 拖动动作中
		case MotionEvent.ACTION_MOVE:
			if (mDragView == null) {
				break;
			}
			// 从ImageView中获取所有布局参数
			RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) mDragView.getLayoutParams();
			//减去偏移量为了让图片能以触摸点为中心显示
			lp.leftMargin = (int) x - clickX;
			lp.topMargin = (int) y - clickY;
			
			//设置ImageView的布局参数
			mDragView.setLayoutParams(lp);
			//记住要替换的位置，相对于资源位置中
			to = position;
			//判断交换条件，替换位置要在GridView中，原始位置不等于即将替换位置，动画效果是否结束
			if (to != -1 && from != to && !doingAni) {
				// 交换位置的代码
				Integer temp = adapter.getItem(from);
				adapter.remove(temp);
				adapter.insert(temp, to);
				aniFrom = from;
				aniTo = to;
				from = to;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mDragView != null) {
				//移除自定义的ImageView
				relativeLayout.removeView(mDragView);
				View temp = mGridView.getChildAt(from
						- mGridView.getFirstVisiblePosition());
				temp.setVisibility(View.VISIBLE);
			}
			break;
		}
		return true;
	}*/
	
	


    

