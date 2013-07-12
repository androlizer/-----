package org.chinamil.networkerr;
/**
 * 能够触摸拖动以及删除的版本
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.ParserXml;
import org.chinamil.PdfDomin;
import org.chinamil.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
public class ErrorBookShelfActivityTouch extends BaseActivity 
implements MyLinearLayout.OndsipatchDraw  ,OnTouchListener  {
             RelativeLayout rl ;
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
                            for (GridView element :imageViews) {
                            }
                        }
                        public void onPageScrollStateChanged(int arg0) {
                            for (GridView element :imageViews) {
                            }
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
		rl=(RelativeLayout) findViewById(R.id.viewpager_gridview);
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
	
private class Myarrayadapter extends ArrayAdapter<PdfDomin>{
  
    List<PdfDomin> objects;
    public Myarrayadapter(Context context, int resource, 
            List<PdfDomin> objects) {
        super(context, resource, objects);
        this.objects=objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView imageView = null;
            convertView = layoutInflater.inflate(R.layout.erroitem1, null);
       if (position>=3&&position<=5) {
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
        PdfDomin pdfDomin=objects.get(position);
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
         if (position == from) {
                imageView.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.VISIBLE);
            }

            // 给View添加animation
            Animation an = null;
            if (position > aniTo && position <= aniFrom) {
                if (position % 5 == 0) {
                    //设置位移动画的参数
                    an = new TranslateAnimation(255, 0, -85, 0);
                } else {
                    //设置位移动画的参数
                    an = new TranslateAnimation(-60, 0, 0, 0);
                }
            } else if (position < aniTo && position >= aniFrom) {
                if (position % 5 == 4) {
                    //设置位移动画的参数
                    an = new TranslateAnimation(-255, 0, 85, 0);
                } else {
                    //设置位移动画的参数
                    an = new TranslateAnimation(60, 0, 0, 0);
                }
            }
            if (an != null) {
                an.setDuration(300);
                // 做动画的时候不能做位置交换
                an.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        doingAni = true;
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        doingAni = false;
                    }
                });
                      imageView.setAnimation(an);
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
	 
	   /**
		 * 填充gridview的内容 并且添加到viewpage 适配器里面
		 * 
		 * @return
		 */
		private void getPage() {
			for (int i = 0; i < dataHashMap.size(); i++) {
				 final GridView inflate = (GridView) layoutInflater.inflate(R.layout.gridview,
						null);
			//	inflate.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(BookShelfActivity.this, R.drawable.layout_random_fade)));
			//	inflate.setOnTouchListener(BookShelfActivity.this);
				inflate.setNumColumns(3);
				/*ShlefAdapter adapter = new ShlefAdapter(
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
				});*/
				
	
				inflate.setOnItemLongClickListener(new OnItemLongClickListener( ) {
                            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                                Log.i("xx", position+"xx");
                                if (parent instanceof GridView) { //缩小grdiiew的item
                                    ScaleAnimation animation= new ScaleAnimation(1.0f,0.8f, 1.0f, 0.8f);
                                    animation.setDuration(700);
                                    animation.setFillAfter(false);
                                 for (int j = 0; j < parent.getChildCount(); j++) {
                                     if (j!=position) {
                                       View view2=  parent.getChildAt(j);
                                       view2.startAnimation(animation);
                                    }
                                  
                                }   
                                    
                                }
                                int []location=new int[2];
                                view.getLocationOnScreen(location);
                                MotionEvent event=MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, 
                                        location[0], location[1], 0);
                               onTouch(view, event);
                               inflate.setOnTouchListener(ErrorBookShelfActivityTouch.this);
                                inflate.setTag("zhuce");
                                return true;
                            }
                });
		   inflate.setTag("no");//用来判断是否启用viewpager的滑动
				inflate.setAdapter(new Myarrayadapter(ErrorBookShelfActivityTouch.this, inflate.getId(), dataHashMap.get(i)));
				imageViews.add(inflate);
			}
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
		}
    public void MyDraw(int eage) {
        backgroundhei=eage;
        if (!isFirst && backgroundhei!=0) {
            mHandler.sendEmptyMessageDelayed(0, 1000L);
            isFirst=true;
        }
    }
    private ImageView mDragView;
    // 代表down的时候点击的坐标 距此图标左上角的偏移量
    private int clickX, clickY;
private float ACTION_DOWNX,ACTION_DOWNy;
    private int from = -1;
    private int to = -1;
    private int aniFrom = -1;
    private int aniTo = -1;
    private boolean doingAni = false;
    int position,myposition;

    public boolean onTouch(View v, MotionEvent event) {
        //getRawX()和getRawY()获得的是相对屏幕的位置
        //getX()相对于控件的本身
        GridView mGridView=imageViews.get(viewPager.getCurrentItem());
        float x = event.getX();
        float y = event.getY();
        // 代表当前点击的位置，相对于资源中的位置。产生-1值是超出GridView的范围；
         position = mGridView.pointToPosition((int) x, (int) y);
         myposition=position;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            System.out.println("ACTION_DOWNlonglllllllllllllllllllllll");
            //判断是点击位置是否在GridView中
            ACTION_DOWNX=event.getX();
            ACTION_DOWNy=event.getY();
            if (position == -1) {
                break;
            }
            System.out.println(position+"position");
            // getChildAt 获取“当前屏幕”上的第几个View 
            View tempView = mGridView.getChildAt(position - mGridView.getFirstVisiblePosition());
            // 获取缓存 要先打开开关
            tempView.setDrawingCacheEnabled(true);
            Bitmap tempBitmap = tempView.getDrawingCache();
            // 自定义一个图片控件
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageBitmap(tempBitmap);
            // 任何改变控件在界面上的显示状态 只有通过layoutParams
            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            //getLeft , getRight 这一组是获取相对在它父亲里的坐标
            rllp.leftMargin = tempView.getLeft();
            rllp.topMargin = tempView.getTop();
            // 代表down的时候点击的坐标 距此图标左上角的偏移量
            clickX = (int) (x - tempView.getLeft());
            clickY = (int) (y - tempView.getTop());
            rl.addView(imageView, rllp);
            mDragView = imageView;
            // 设置控件不可见
            tempView.setVisibility(View.INVISIBLE);
            // 保存起点位置
            from = position;
            break;
        // 拖动动作中
        case MotionEvent.ACTION_MOVE:
            System.out.println("ACTION_MOVElonglllllllllllllllllllllll");
            if (mDragView == null) {
                break;
            }
            // 从ImageView中获取所有布局参数
            RelativeLayout.LayoutParams lp = (LayoutParams) mDragView.getLayoutParams();
            //减去偏移量为了让图片能以触摸点为中心显示
           lp.leftMargin = (int) x - clickX;
            lp.topMargin = (int) y - clickY+(backgroundhei-backgroundhei/10)/3;
            //设置ImageView的布局参数
            mDragView.setLayoutParams(lp);
            //记住要替换的位置，相对于资源位置中
            to = position;
            
            //判断交换条件，替换位置要在GridView中，原始位置不等于即将替换位置，动画效果是否结束
         /*   if (to != -1 && from != to && !doingAni) {
               Myarrayadapter  madapter=(Myarrayadapter)mGridView.getAdapter();
                // 交换位置的代码
           PdfDomin temp = madapter.getItem(from);
           madapter.remove(temp);
           madapter.insert(temp, to);
                aniFrom = from;
                aniTo = to;
                from = to;
            }*/
            break;
        case MotionEvent.ACTION_UP:
            System.out.println("ACTION_UPlllllllllllllllllllllll");
            if (mDragView != null) {
                //移除自定义的ImageView
                rl.removeView(mDragView);
                View temp = mGridView.getChildAt(from
                        - mGridView.getFirstVisiblePosition());
                temp.setVisibility(View.VISIBLE);
            }
        /*    for (GridView element : imageViews) {
                if (element.getTag().equals("zhuce")) {
                    element.setOnTouchListener(null);
                    element.setTag("no");
                }
            }*/
            if (event.getX()==ACTION_DOWNX) {//点击动作
                System.out.println(mGridView.pointToPosition((int) event.getX(), (int)event.getY()));
                return false;
            }
        
        }
        return true;
    }
 
@Override
public void onBackPressed() {
    finish();
super.onBackPressed();
}
	   
}
