package org.chinamil.networkerr;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.ParserXml;
import org.chinamil.PdfDomin;
import org.chinamil.R;
import org.chinamil.ui.library.WebviewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ErrorBookShelfActivity extends BaseActivity 
implements MyLinearLayout.OndsipatchDraw    {
    
	private RelativeLayout.LayoutParams lp;
	private List<GridView> imageViews;
	private ViewPager viewPager;
	private LayoutInflater layoutInflater;//54-47
	private int  backgroundhei,uppage;// 每个框的高
	private MyLinearLayout relativeLayout;
	private boolean touch=false;//是否在拖动
	int location[]=new int[2];
	LinearLayout linerlayout;
	boolean isFirst;
	LinearLayout.LayoutParams layoutParams;
	float rate;//字體大小
	HashMap<Integer,  LinkedList<PdfDomin>>dataHashMap;
	private List<View> dotViews;
	Cursor cursor;
	Handler mHandler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
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
                    break;
                default:
                    break;
            }
        };
    };
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.erromain);
		linerlayout=(LinearLayout) findViewById(R.id.dot); //园点
		relativeLayout=(MyLinearLayout) findViewById(R.id.addliner);//自定义mylinearlayout
		relativeLayout.setOndsipatchDrawListener(this);//设置监听 获取每个框的高
		  //查询  暂不加条件
		cursor=getContentResolver().query(Heibai.MANAGE_URI, new String[] { "_id", 
				"imag", 
				"link", "title","month" }, 
				null,
				null,
				null);
		dataHashMap=cursortolist(cursor);
	     if (dataHashMap!=null) {
		viewPager = (ViewPager) findViewById(R.id.vp);
		 viewPager.setOffscreenPageLimit(1);
			 rate = (float) getWindowManager().getDefaultDisplay().getWidth()/40;
			 layoutParams=new LinearLayout.LayoutParams(10, 10);
		layoutInflater = getLayoutInflater();
		imageViews = new ArrayList<GridView>();
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		getPage();
		dotViews=new ArrayList<View>();
		 for (int i = 0; i < dataHashMap.size(); i++) {//添加屏幕小圆点
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
		 
	     } else 
	     {
	    	 findViewById(R.id.errempty).setVisibility(View.VISIBLE);
	     }
	     
	
	
	}
	
private HashMap<Integer,  LinkedList<PdfDomin>>  cursortolist(Cursor cursor){
    LinkedList<PdfDomin>data=new LinkedList<PdfDomin>();
	if (cursor!=null&&cursor.getCount()>0) {
		for (int i = 0; i < cursor.getCount(); i++) {
			PdfDomin pdfDomin=new PdfDomin();
			cursor.moveToNext();
			pdfDomin.title=cursor.getString(3);
			pdfDomin.cover=cursor.getString(1);
			data.add(pdfDomin);
		}
		
		
		return dataSlip(data);
	}
	return null;
	
}
	/**
	 * 分割总数据  返回每页需要的数据
	 * @return
	 */
private HashMap<Integer,  LinkedList<PdfDomin>> dataSlip(LinkedList<PdfDomin>data){
    HashMap<Integer, LinkedList<PdfDomin>> dHashMap=new HashMap<Integer, LinkedList<PdfDomin>>();
    int page=0;
    if (data.size()%9!=0) {
        page=data.size()/9+1;
    }
    for (int i = 0; i < page; i++) {
        LinkedList<PdfDomin> dIntegers=new LinkedList<PdfDomin>();
        for (int j = 0; j <9; j++) {
           int index=i*9+j;
            if (index<data.size()) {
                dIntegers.add(data.get(index));
            }else {
                break;
            }
        }
        dHashMap.put(i, dIntegers);
    } 
    return dHashMap;
}
	/**
	 * gridview 的适配器
	 * 
	 * @author zhang
	 * private int i;
			*/


	class ShlefAdapter extends BaseAdapter {
		private	LinkedList<PdfDomin> mArray;
		ShlefAdapter( LinkedList<PdfDomin> mArray) {
	this.mArray=mArray;
		}
        public int getCount() {
            return mArray.size();
        }
        public Object getItem(int position) {
            return mArray.get(position);
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView imageView = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.erroitem1, null);
            }  if (position>=3&&position<=5) {
                lp.topMargin=((backgroundhei-backgroundhei/10)/3)+8; //第2行
            }  else if (position>=6&&position<=8) {
                lp.topMargin=((backgroundhei-backgroundhei/10)/3)+10; //第三行
            }else 
            {
                lp.topMargin=(backgroundhei-backgroundhei/10)/3-10;  //第一行
            }
            imageView=(TextView) convertView.findViewById(R.id.imageView1);
            imageView.setLayoutParams(lp);
            imageView.setTextSize(rate);
            PdfDomin pdfDomin=mArray.get(position);
            imageView.setText(pdfDomin.title);
                if (pdfDomin.cover.contains(ParserXml.urlhead)) {
                    imageView.setBackgroundDrawable(   Tool.getBitmapDrawable(getResources(),
                                    BitmapFactory.decodeFile(getCacheDir()+"/"
                                   +MD5.getMD5(pdfDomin.title)+".png"),  
                                   ( backgroundhei-(backgroundhei/3))-30,
                                   backgroundhei-backgroundhei/3));
                }else {
                    imageView.setBackgroundDrawable(   Tool.getBitmapDrawable(getResources(),
                            BitmapFactory.decodeResource(getResources(),
                                    R.drawable.addic_launcher_yuedu),   
                                      ( backgroundhei-(backgroundhei/3))-30,
                                       backgroundhei-backgroundhei/3));
                }
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
	/* private class Mytask extends AsyncTask<String, Void, Drawable> {
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
	            //创建图片http://wap.chinamil.com.cn/wap-paper/
	            if (params[1].contains(ParserXml.urlhead)) {
	                return             		
			        		Tool.getBitmapDrawable(getResources(),
					        		BitmapFactory.decodeFile(getCacheDir()+"/"
			        		       +MD5.getMD5(params[0])+".png"),	
			        		       ( backgroundhei-(backgroundhei/3))-30,
                                   backgroundhei-backgroundhei/3);
	            }else {
	            	Tool.getBitmapDrawable(getResources(),
			        		BitmapFactory.decodeResource(getResources(),
			        				R.drawable.addic_launcher_yuedu),	
			        				  ( backgroundhei-(backgroundhei/3))-30,
			        	               backgroundhei-backgroundhei/3);
	                return null;
	            }
	           
	        }
	    }*/
	 
	   /**
		 * 填充gridview的内容 并且添加到viewpage 适配器里面
		 * 
		 * @return
		 */
		private void getPage() {
			for (int i = 0; i < dataHashMap.size(); i++) {
				GridView inflate = (GridView) layoutInflater.inflate(R.layout.gridview,
						null);
			//	inflate.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(BookShelfActivity.this, R.drawable.layout_random_fade)));
			//	inflate.setOnTouchListener(BookShelfActivity.this);
				inflate.setNumColumns(3);
				ShlefAdapter adapter = new ShlefAdapter(
				        dataHashMap.get(i));
				inflate.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ShlefAdapter adapter= (ShlefAdapter) parent.getAdapter();
						   PdfDomin pdf=(PdfDomin) adapter.getItem(position);
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
    public void MyDraw(int eage) {
        backgroundhei=eage;
        if (!isFirst && backgroundhei!=0) {
           mHandler.sendEmptyMessage(0);
            isFirst=true;
        }
    }


	   
}
