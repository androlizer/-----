package cn.com.karl.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.chinamil.Heibai;
import org.chinamil.MD5;
import org.chinamil.MultDownloader;
import org.chinamil.ParserXml;
import org.chinamil.PdfDomin;
import org.chinamil.ProgressBarListener;
import org.chinamil.R;
import org.chinamil.ui.library.RecentActivity;
import org.chinamil.ui.library.WebviewActivity;
import org.chinamil.ui.library.ZipDecrypter;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class BookShelfActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    private GridView bookShelf;
    private Button more, back;
    private ShlefAdapter adapter;
    private ProgressBar progressBar;
    private Mytask tashMytask;
    private File topcache;
    /*private LinearLayout linearLayout;
    private int clickX, clickY;*/
    private HashMap<PdfDomin, View> queue;// 下载队列
    private boolean working = false;// 是否在下载
    private   ArrayList<String> weList;//记录下载了那些
    Handler myHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(android.os.Message msg) {
        	  ProgressBar progressBar2 = null;
        	  View bView=null;
        	  if (msg.what==4) {
        		  progressBar.setVisibility(View.GONE);
 			}
            // 设置进度跳最大值
        	if (msg.what!=0&&msg.what!=4) {
        		 bView=(View) msg.obj;
                 progressBar2 = (ProgressBar)bView.findViewById(R.id.adddownprogressBar1);;
			}
        	   switch (msg.what) {
                case 0:// 20条数据解析完后初始化控件
                    init();
                    bookShelf.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    new Mytask().execute(Heibai.URL, "xx");
                    
                    break;
                case 1:
                	if (progressBar2!=null) {
                		  progressBar2.setMax(msg.arg1);
                          progressBar2.setVisibility(View.VISIBLE);
					}
                    // 设置进度跳最大值
                  
                    break;
                case 2:if (progressBar2!=null) {
					
			
                    // 下载进度通知
                    progressBar2.setProgress(msg.arg1);
                    if (progressBar2.getProgress() < progressBar2.getMax()) {
                        // 没有下载完的情况下 不做

                    } else {
                        // 下载完的时候
                        working = false;
                        Toast.makeText(BookShelfActivity.this, "下载完成", 0).show();
                        progressBar2.setVisibility(View.GONE);
                        if (myTextView!=null) {
                        	myTextView.setTextColor(Color.RED);
                        myTextView.setTypeface(null, Typeface.BOLD_ITALIC);
						}
                        synchronized (BookShelfActivity.class) {
                            if (!queue.isEmpty()) {
                                Iterator<PdfDomin> iterator = queue.keySet().iterator();
                                iterator.hasNext();
                                PdfDomin next = iterator.next();
                                boolean success = ZipDecrypter.decryptercrypto(
                                        new File(topcache, MD5.getMD5(next.title) + ".zip"), topcache);
                                if (!weList.contains(next.title)) {
                                    weList.add(next.title);
                                }
                                ContentValues values = new ContentValues();
                                values.put(Heibai.TITLE, next.title);
                                values.put(Heibai.LINK, next.link);
                                values.put(Heibai.IMAG, next.cover.substring(0, next.cover.lastIndexOf(".")) + ".png");
                                values.put(Heibai.MONTH, "待定");
                                Uri uri = getContentResolver().insert(Heibai.MANAGE_URI, values);
                                queue.remove(next);
                                if (!queue.isEmpty()) {
                                    downpdf(next, queue.get(next));
                                }
                            }
                            System.out.println("完成后的队列大小" + queue.size());
                        }

                    }	}

                    break;
                case 3:
             
                if (progressBar2!=null) {
					
				
                    // 下载错误通知
                    progressBar2.setVisibility(View.GONE);
                    if (!queue.isEmpty()) {
                        queue.clear();
                    }
                    working = false;
                    Toast.makeText(BookShelfActivity.this, "下载错误", 0).show();
                	ImageView donwButton=(ImageView) bView.findViewById(R.id.down);
                	donwButton.setBackgroundResource(R.drawable.downselect);
                	donwButton.setVisibility(View.VISIBLE);
                }
                    break;
              
                default:
                    break;
            }

        };
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addmain);
       // linearLayout=(LinearLayout) findViewById(R.id.addliner);
        topcache = getCacheDir();
        tashMytask = new Mytask();
        tashMytask.execute(Heibai.URL);
        weList=new ArrayList<String>();
        progressBar = (ProgressBar) findViewById(R.id.addprogressBar);
    }

    private void init() {
        queue = new HashMap<PdfDomin, View>();
        more = (Button) findViewById(R.id.progressBarmore);
        more.setOnClickListener(this);
        back = (Button) findViewById(R.id.btn_leftTop);
        back.setOnClickListener(this);
      
        bookShelf = (GridView) findViewById(R.id.bookShelf);
        bookShelf.setOnItemClickListener(this);
        adapter = new ShlefAdapter();
        alllist = new LinkedList<PdfDomin>();
        bookShelf.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (alllist.size() > 0) {
                    if (view.getLastVisiblePosition() == list.size() - 1) {
                        more.setVisibility(View.VISIBLE);
                    } else {
                        more.setVisibility(View.GONE);
                    }
                } else {
                    more.setVisibility(View.GONE);
                }
            }
        });
    }

   

    class ShlefAdapter extends BaseAdapter {

        
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return super.getItemViewType(position);
        }
        
        
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return super.getViewTypeCount();
        }
        
        
        
        public int getCount() {
            return list.size();
        }

        
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        
        public long getItemId(int arg0) {
            return arg0;
        }

        
        public View getView(int position, View contentView, ViewGroup arg2) {
                contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item1, null);
            TextView view = (TextView) contentView.findViewById(R.id.imageView1);
        
            ImageView button=(ImageView) contentView.findViewById(R.id.down);
              Cursor cursor= getContentResolver().query(Heibai.MANAGE_URI, new String[]{"_id",  "title"},
                        "title = ?",new String[]{list.get(position).title} , null);
                if (cursor!=null&&cursor.getCount()>0) {
                    view.setTextColor(Color.RED); 
                    button.setVisibility(View.GONE);
                    view.setTypeface(null, Typeface.BOLD_ITALIC);
                }
            view.setText(list.get(position).title);
            PdfDomin pdfDomin = list.get(position);
            new Mytaskdown(view).execute(pdfDomin.cover, pdfDomin.title);
            /*    if (position == from) {
            	view.setVisibility(View.INVISIBLE);
			} else {
				view.setVisibility(View.VISIBLE);
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
				view.setAnimation(an);
			}*/
            return contentView;
        }

    }

    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_leftTop:
                finish();
                break;
            case R.id.progressBarmore:
                progressBar.setVisibility(View.VISIBLE);
                if (alllist != null && alllist.size() > 0) {
                    for (int i = 0; i < 20; i++) {
                        if (alllist.size() > 0) {
                            if (!list.contains(alllist.get(0))) {
                                list.add(alllist.get(0));
                                alllist.remove(0);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.GONE);

                break;
            default:
                break;
        }

    }

    /**
     * 文件异步下载类
     * 
     * @author Administrator
     * 
     */
    private class Mytaskdown extends AsyncTask<String, Void, File> {

        private TextView imaget;

        public Mytaskdown(TextView textView) {
            super();
            this.imaget = textView;

        }

        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            System.out.println();
            if (result != null) {
                imaget.setBackgroundDrawable(Drawable.createFromPath(result.getAbsolutePath()));
            } else {

                // 缩略图下载错误
            }

        }

        // 图片
        
        protected File doInBackground(String... params) {
            try {
                File file = ParserXml.getImage(params[0], params[1], topcache);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    LinkedList<PdfDomin> list, alllist;

    /**
     * 缩略图异步下载类
     * 
     * @author Administrator
     * 
     */

    @SuppressWarnings("rawtypes")
    private class Mytask extends AsyncTask<String, Void, LinkedList> {

        
        protected LinkedList doInBackground(String... params) {
            System.out.println();
            if (params.length == 1) {
                System.out.println("传递了一个参数");
                try {
                    list = ParserXml.getPictureall(params[0]);
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(BookShelfActivity.this, "网络错误", 0).show();
                    myHandler.sendEmptyMessage(4);
                    Looper.loop();
               
                }
                return list;
            } else {
                try {
                    alllist = ParserXml.getPictureallreal(params[0]);
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(BookShelfActivity.this, "网络错误", 0).show();
                    myHandler.sendEmptyMessage(4);
                    Looper.loop();
                   
                }
                return alllist;
            }

        }

        
        protected void onPostExecute(LinkedList result) {
            super.onPostExecute(result);
            if (alllist == null) {
                myHandler.sendEmptyMessage(0);
            }
            if (alllist != null) {
                for (int i = 0; i < list.size(); i++) {
                    PdfDomin pdfDomin = list.get(i);
                    for (int j = 0; j < alllist.size(); j++) {
                        if (alllist.get(j).title.equals(pdfDomin.title)) {
                            alllist.remove(j);
                        }
                    }

                }
            }

        }
    }

    @SuppressWarnings("unused")
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    System.out.println("来了么");
        if (!working) {
            if (position < list.size()) {
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.adddownprogressBar1);
                PdfDomin pdfDomin = list.get(position);
                File ffFile = new File(topcache, MD5.getMD5(pdfDomin.title) + ".zip");
                if (!ffFile.exists()) {// 是否存在
                    System.out.println("ffFile点击事件");
                    downshow(view, pdfDomin);
                } else {
                    Intent intent = new Intent(BookShelfActivity.this, 
                    		WebviewActivity.class);
                    intent.putExtra("who", MD5.getMD5(pdfDomin.title));
                    intent.putExtra("date", pdfDomin.title);
                    startActivity(intent);
                }
            }

        } else {
            Toast.makeText(BookShelfActivity.this, "下载中请稍等", 1).show();
        }

    }

    String[] nameStrings = { "下载" };

    /**
     * 显示下载按钮
     * 
     */
    private void downshow(final View v, final PdfDomin title) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(BookShelfActivity.this);
        aBuilder.setItems(nameStrings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.out.println();
                if (queue.isEmpty()) {
                    queue.put(title, v);
                } else {
                    if (!queue.containsKey(title)) {
                        queue.put(title, v);
                    }
                }
                System.out.println(queue.size() + "队列的大小");
                if (!working) {
                    downpdf(title, v);
                }
            }
        });
        aBuilder.show();

    }

    /**
     * 下载方法
     * 
     */
    TextView myTextView;
    private void downpdf(final PdfDomin title, final View down) {
        working = true;
        ImageView donwButton=(ImageView) down.findViewById(R.id.down);
    	donwButton.setVisibility(View.GONE);
        myTextView=(TextView) down.findViewById(R.id.imageView1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    MultDownloader multDownloader = new MultDownloader(new ProgressBarListener() {
                        public void getMax(int len) throws NetworkErrorException {
                            Message msg = myHandler.obtainMessage(1);
                            msg.arg1 = len;
                            msg.obj = down;
                            myHandler.sendMessage(msg);
                        }
                        public void downloadsize(int len) throws NetworkErrorException {
                            Message msg = myHandler.obtainMessage(2);
                            msg.arg1 = len;
                            msg.obj = down;
                            myHandler.sendMessage(msg);
                        }
                    }, topcache, title.title);
                    multDownloader.download(title.link, BookShelfActivity.this);
                } catch (Exception e) {
                    new File(topcache, MD5.getMD5(title.title) + ".zip").delete();// 删除下载一半的文件
                    Message msg = new Message();
                    msg.obj = down;
                    msg.what = 3;
                    myHandler.sendMessage(msg);
                }
            }
        }).start();
    }
    
/*    Intent intent = new Intent();
    intent.putStringArrayListExtra("me", weList);
    setResult(9, intent);*/
    
    public void onBackPressed() {
        System.out.println();
        if (!working) {
            //返回已经下载的
            Intent intent = new Intent(BookShelfActivity.this,RecentActivity.class);
            intent.putStringArrayListExtra("me", weList);
            setResult(1, intent);
        }else {
            Intent intent = new Intent(BookShelfActivity.this,RecentActivity.class);
            intent.putStringArrayListExtra("me", weList);
            setResult(1, intent);
            //清楚未下载完的 
            Iterator<PdfDomin> iterator = queue.keySet().iterator();
         while (iterator.hasNext()) {
             PdfDomin next = iterator.next();
             new File(topcache, MD5.getMD5(next.title) + ".zip").delete();
        }   
       
   
        }      super.onBackPressed();
    }

/*	private int from = -1;
	private int to = -1;
	private int aniFrom = -1;
	private int aniTo = -1;
	private boolean doingAni = false;
	private ImageView mDragView;
    public boolean onTouch(View v, MotionEvent event) {
	//getRawX()和getRawY()获得的是相对屏幕的位置
	//getX()相对于控件的本身
	float x = event.getX();
	float y = event.getY();
	// 代表当前点击的位置，相对于资源中的位置。产生-1值是超出GridView的范围；
	int position = bookShelf.pointToPosition((int) x, (int) y);
	Log.i("ivan", "position = " + position);

	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
		
		//判断是点击位置是否在GridView中
		if (position == -1) {
			break;
		}
		// getChildAt 获取“当前屏幕”上的第几个View 
		View tempView = bookShelf.getChildAt(position - bookShelf.getFirstVisiblePosition());
//item 索引
		//	Toast.makeText(GridViewDemo.this, "xxx"+(position - bookShelf.getFirstVisiblePosition()), 1).show();
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
		linearLayout.addView(imageView, rllp);
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
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mDragView.getLayoutParams();
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
			PdfDomin  cc= (PdfDomin) adapter.getItem(from);
			list.remove(cc);
			list.add(to,cc);
			adapter.notifyDataSetChanged();
			aniFrom = from;
			aniTo = to;
			from = to;
		}
		break;
	case MotionEvent.ACTION_UP:
		if (mDragView != null) {
			//移除自定义的ImageView
			linearLayout.removeView(mDragView);
			View temp = bookShelf.getChildAt(from
					- bookShelf.getFirstVisiblePosition());
			temp.setVisibility(View.VISIBLE);
		}
		break;
	}
	return true;
}*/
}
