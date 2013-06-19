package org.chinamil.ui.library;

import java.util.LinkedList;

import org.chinamil.Heibai;
import org.chinamil.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;

public class Details3 extends Activity implements OnClickListener {
	private FlipViewController flipView;
	/**
	 * Called when the activity is first created.
	 */
	private MyBaseAdapter adapter;
	private LinkedList<View> IMG_DESCRIPTIONS = new LinkedList<View>();
	private String LISTVIEW_DESCRIPTIONS;// ///////
	private ImageGetter imageGetter;
	private String image, titleString;
	private MergeCursor mergeCursor;
	int index = 1;
	private SharedPreferences sp;
	private String imagePath;
	private String dString;
	private String NoMd5date;
    private Editor editor;
	boolean end = false; // 用来标记是否已经加载完
	Cursor[] cursors;
	ContentResolver contentResolver;
	String dateString;
	String[] clounm = new String[] { Heibai.PUBLIC, Heibai.CONTENT,
			Heibai.DATE, Heibai.PATH };
    private Float textsize;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		titleString = intent.getStringExtra("title");
		image = intent.getStringExtra("image");
		contentResolver = getContentResolver();
		  sp = getSharedPreferences("text", Context.MODE_PRIVATE);
	        textsize = sp.getFloat("textsize", 12);
		dString = image.substring(image.indexOf("he") + 3);
		dString = dString.substring(0, dString.indexOf("/"));
		image = image.substring(0, image.length() - 3);
		// /data/data/org.chinamil/cache/b4dfb9f9aa7c45de6c0c868b2aacd2da/01/
		// String where = Heibai.CONTENT + " like '%" + titleString + "%'";
		if (getData(dString)) {
			flipView = new FlipViewController(this,
					FlipViewController.HORIZONTAL);
			adapter = new MyBaseAdapter(Details3.this);
			flipView.setAdapter(adapter);
			setContentView(flipView);
			flipView.setOnViewFlipListener(new ViewFlipListener() {
				public void onViewFlipped(View view, int position) {
					if (position != 0) {
						if (position % 7 == 0) {
							getData2(dString, index);
							index++;
							adapter.notifyDataSetChanged();
						}
					}
				}
			});
		} else {
			TextView xxTextView = new TextView(this);
			xxTextView.setText("数据为空");
			setContentView(xxTextView);
		}
		imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				source = pathString + "/" + source;
				Drawable drawable = null;
				if (source != null) {
					Log.i("xx",source);
					/*
					 * drawable 图片 int rId=Integer.parseInt(source);
					 * drawable=getResources().getDrawable(rId);
					 * drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					 * drawable.getIntrinsicHeight()); return drawable;
					 */
					// sd卡图片
					try {
						drawable = Drawable.createFromPath(source);
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
								drawable.getIntrinsicHeight());
						return drawable;
						// 网络图片 需要另开线程
						/*
						 * try { url = new URL(source); drawable =
						 * Drawable.createFromStream(url.openStream(), ""); }
						 * catch (Exception e) { e.printStackTrace(); return
						 * null; } drawable.setBounds(0, 0,
						 * drawable.getIntrinsicWidth(),
						 * drawable.getIntrinsicHeight()); return drawable;
						 */
					} catch (Exception e) {
						return null;
					}
				}else {
					Log.i("xx","空");
				}
				return null;
			}
		};
	}

	/**
	 * 获取 报纸的所有新闻
	 * 
	 * @param dString
	 * @param page
	 * @return
	 */
	private boolean getData2(String dString, int page) {
		indexString = null;
		pathString = null;
		String de = "_id  DESC  LIMIT 7 OFFSET " + page * 7;
		String where2 = Heibai.DATE + " = ?";
		Cursor mcursor = contentResolver.query(Heibai.DEAIL_URI, clounm,
				where2, new String[] { dString }, de);
		while (mcursor.moveToNext()) {
			String tempathString = mcursor.getString(3);
			if (indexString == null) {
				indexString = tempathString;
				pathString = image + indexString;
			}
			if (!indexString.equals(tempathString)) {
				indexString = tempathString;
				pathString = image + indexString;
			}

			setViewData(mcursor);
		}
		return false;
	}

	String pathString = null;
	String indexString = null;

	/**
	 * 获取报纸的所有新闻 包括用户点击的
	 * 
	 * @param dString
	 * @param page
	 * @return
	 */
	private boolean getData(String dString) {
		Cursor cursor = contentResolver.query(Heibai.TEMP_URI, clounm, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToNext();
			NoMd5date=cursor.getString(2);
			String tempPath=cursor.getString(3);
			cursor.move(-1);
			String de = "_id  DESC  LIMIT 7 OFFSET  0"; // 取前7个;
			String where2 = Heibai.DATE + " = ? and "+Heibai.PATH+"= ?";;
			Cursor mcursor = contentResolver.query(Heibai.DEAIL_URI, clounm,
					where2, new String[] { dString, tempPath }, de);
			Cursor[] cursors = new Cursor[] { cursor, mcursor }; // 简单的合并2个cursor
			mergeCursor = new MergeCursor(cursors);
			while (mergeCursor.moveToNext()) {
				String tempathString = mergeCursor.getString(3);
				if (indexString == null) {
					indexString = tempathString;
					pathString = image + indexString;
				}
				if (!indexString.equals(tempathString)) {
					indexString = tempathString;
					pathString = image + indexString;
				}
				// 缓存不行 只加载一次不行
				setViewData(mergeCursor);
			}
			return true;
		} else
			return false;
	}

	private void setViewData(Cursor mCursor) {
		View headView = getLayoutInflater().inflate(R.layout.html2, null);
		ImageButton big = (ImageButton) headView
				.findViewById(R.id.html_big_button2);
		ImageButton min = (ImageButton) headView
				.findViewById(R.id.html_min_button);
		ImageButton cut = (ImageButton) headView.findViewById(R.id.html_shears);
		ListView mListView = (ListView) headView
				.findViewById(R.id.html2_listview);
		min.setOnClickListener(this);
		cut.setOnClickListener(this);
		big.setOnClickListener(this);
		mListView.setAdapter(new LIstviewAdaoter(mCursor.getString(1)));
		IMG_DESCRIPTIONS.add(headView);
	}

	/**
	 * 整体适配
	 * 
	 * @author zhang
	 * 
	 */
	private class MyBaseAdapter extends BaseAdapter {

		private MyBaseAdapter(Context context) {
		}

		public int getCount() {
			return IMG_DESCRIPTIONS.size();
		}

		public Object getItem(int position) {
			return IMG_DESCRIPTIONS.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return IMG_DESCRIPTIONS.get(position);
		}
	}
	/**
	 * 单页适配
	 * 
	 * @author zhang
	 * 
	 */
	private class LIstviewAdaoter extends BaseAdapter {
		String text;
		TextView 	 mconvertView = null;
		public LIstviewAdaoter(String text) {
			this.text = text;
		}

		public int getCount() {
			return 1;
		}

		public Object getItem(int position) {
			return text;
		}
		public TextView getItemVIew() {
			return mconvertView;}
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (mconvertView == null)
				mconvertView = new TextView(getApplicationContext());
			mconvertView.setTextColor(Color.BLACK);
			mconvertView.setTextSize(textsize);
			mconvertView.setText(Html.fromHtml(text, imageGetter, null));
			return mconvertView;
		}
	}
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	protected void onPause() {
		super.onPause();
		flipView.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	      editor = sp.edit();
	        editor.putFloat("textsize", textsize);
	        editor.commit();
		getContentResolver().delete(Heibai.TEMP_URI, null, null);
		finish();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.html_big_button2:
			 if (textsize <=30F ) {
              	textsize=textsize + 5f;
              	View ss = (View) adapter	.getItem(flipView.getSelectedItemPosition());
    			ListView listView = (ListView) ss.findViewById(R.id.html2_listview);       
    			TextView view = (TextView) listView.getChildAt(0);
    			view.setTextSize(textsize);
    		
			 }
			break;
		case R.id.html_min_button:
		    if (textsize > 15f) {
            	textsize=textsize - 5f;
            	View ss = (View) adapter
    					.getItem(flipView.getSelectedItemPosition());
    			ListView listView = (ListView) ss.findViewById(R.id.html2_listview);       
    			TextView view = (TextView) listView.getChildAt(0);
    			view.setTextSize(textsize);
		    }
			break;
		case R.id.html_shears:
			View ss = (View) adapter
					.getItem(flipView.getSelectedItemPosition());
			ListView listView = (ListView) ss.findViewById(R.id.html2_listview);
			ContentValues initialValues = new ContentValues();
			initialValues.put(Heibai.TITLE, titleString);
			initialValues.put(Heibai.CONTENT, (String) listView.getAdapter()
					.getItem(0));
			 initialValues.put(Heibai.DATE, NoMd5date);
			getContentResolver().insert(Heibai.JIANBAO_URI, initialValues);
			Toast.makeText(Details3.this, "已成功裁剪", 1).show();
			break;
		default:
			break;
		}
	}
}
