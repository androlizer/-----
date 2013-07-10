package org.chinamil.ui.library;

/**
 * 连续翻页
 */
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;

import org.chinamil.Heibai;
import org.chinamil.R;
import org.chinamil.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Details5 extends Activity implements OnClickListener ,OnItemClickListener{
	private FlipViewController flipView;
	/**
	 * Called when the activity is first created.
	 */
	private MyBaseAdapter adapter;
	private LinkedList<View> IMG_DESCRIPTIONS = new LinkedList<View>();
	private String LISTVIEW_DESCRIPTIONS;// ///////
	// private ImageGetter imageGetter;
	private String image;
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
			Heibai.DATE, Heibai.PATH ,Heibai.TITLE};
	private Float textsize;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
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
			adapter = new MyBaseAdapter(Details5.this);
	//		android:descendantFocusability="blocksDescendants"
			flipView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
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
		/*
		 * imageGetter = new ImageGetter() { public Drawable getDrawable(String
		 * source) { source = pathString + "/" + source; Drawable drawable =
		 * null; if (source != null) { Log.i("xx", source);
		 * 
		 * drawable 图片 int rId=Integer.parseInt(source);
		 * drawable=getResources().getDrawable(rId); drawable.setBounds(0, 0,
		 * drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); return
		 * drawable;
		 * 
		 * // sd卡图片 try { drawable = Drawable.createFromPath(source);
		 * drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		 * drawable.getIntrinsicHeight()); return drawable; // 网络图片 需要另开线程
		 * 
		 * try { url = new URL(source); drawable =
		 * Drawable.createFromStream(url.openStream(), ""); } catch (Exception
		 * e) { e.printStackTrace(); return null; } drawable.setBounds(0, 0,
		 * drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); return
		 * drawable;
		 * 
		 * } catch (Exception e) { return null; } } else { Log.i("xx", "空"); }
		 * return null; } };
		 */
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

			setViewData(mcursor.getString(1),
				    	mcursor.getString(4));
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
			NoMd5date = cursor.getString(2);
			String tempPath = cursor.getString(3);
			cursor.move(-1);
			String de = "_id  DESC  LIMIT 7 OFFSET  0"; // 取前7个;
			String where2 = Heibai.DATE + " = ? and " + Heibai.PATH + "= ?";
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
				setViewData(mergeCursor.getString(1),mergeCursor.getString(4));
			}
			return true;
		} else
			return false;
	}

	private void setViewData(String context,String title) {
		View headView = getLayoutInflater().inflate(R.layout.html2, null);
		ImageButton big = (ImageButton) headView
				.findViewById(R.id.html_big_button2);
		ImageButton min = (ImageButton) headView
				.findViewById(R.id.html_min_button);
		ImageButton backButton = (ImageButton) headView
				.findViewById(R.id.btn_back);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editor = sp.edit();
				editor.putFloat("textsize", textsize);
				editor.commit();
				getContentResolver().delete(Heibai.TEMP_URI, null, null);
				finish();
			}
		});
		ImageButton cut = (ImageButton) headView.findViewById(R.id.html_shears);
		ListView mListView = (ListView) headView
				.findViewById(R.id.html2_listview);
		min.setOnClickListener(this);
		cut.setOnClickListener(this);
		big.setOnClickListener(this);
		mListView.setAdapter(new LIstviewAdaoter(context,title));
		IMG_DESCRIPTIONS.add(headView);
	}

	/**
	 * 整体适配
	 * 
	 * @author zhang
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
	 */
	private class LIstviewAdaoter extends BaseAdapter {
		String text,mytitle;

		public LIstviewAdaoter(String text,String title) {
			this.text = text;
			this.mytitle=title;
			System.out.println("LIstviewAdaoter"+title);
		}
		public int getCount() {
			return 2;
		}

		public Object getItem(int position) {
			return text;
		}
		public String getMytitle( ) {
			return mytitle;
		}
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				ArrayList imageUrl = new ArrayList<String>();
				Gallery gallery = new Gallery(Details5.this);
				gallery.setBackgroundColor(Color.WHITE);
				Document dc = Jsoup.parse(text);
				Elements essElements = dc.select("img[src$=.jpg]");
				for (Element element : essElements) {
					imageUrl.add(pathString + "/" + element.attr("src"));
				}
				if (imageUrl.size() > 1)
					gallery.setAdapter(new MgallayAdapter(imageUrl, true));
				else
					gallery.setAdapter(new MgallayAdapter(imageUrl, false));
				gallery.setOnItemClickListener(Details5.this);
				return gallery;
			} else {
				if (convertView == null)
					convertView=getLayoutInflater().inflate(R.layout.twotixtview, null);
				TextView	title =(TextView) convertView.findViewById(R.id.title);
				TextView 	content =(TextView) convertView.findViewById(R.id.content);
				content.setText(Html.fromHtml(text));
				title.setText(Html.fromHtml(mytitle));
				return convertView;
			}
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
			if (textsize <= 30F) {
				textsize = textsize + 5f;
				View ss = (View) adapter.getItem(flipView
						.getSelectedItemPosition());
				ListView listView = (ListView) ss
						.findViewById(R.id.html2_listview);
				View view=listView.getChildAt(1);
				TextView titleview = (TextView) view.findViewById(R.id.content);
				titleview.setTextSize(textsize);

			}
			break;
		case R.id.html_min_button:
			if (textsize > 15f) {
				textsize = textsize - 5f;
				View ss = (View) adapter.getItem(flipView
						.getSelectedItemPosition());
				ListView listView = (ListView) ss
						.findViewById(R.id.html2_listview);
				View view=listView.getChildAt(1);
				TextView titleview = (TextView) view.findViewById(R.id.content);
				titleview.setTextSize(textsize);
			}
			break;
		case R.id.html_shears:
			
			View ss = (View) adapter
					.getItem(flipView.getSelectedItemPosition());
			ListView listView = (ListView) ss.findViewById(R.id.html2_listview);
			String context = (String) listView.getAdapter().getItem(0);
				ContentValues initialValues = new ContentValues();
				   LIstviewAdaoter adaoter=(LIstviewAdaoter) listView.getAdapter();
			    	initialValues.put(Heibai.TITLE, (String)adaoter.getMytitle());
			       initialValues.put(Heibai.CONTENT, (String)adaoter.getItem(0));
		        	initialValues.put(Heibai.DATE, NoMd5date);
			getContentResolver().insert(Heibai.JIANBAO_URI, initialValues);
			Toast.makeText(Details5.this, "已成功裁剪", 1).show();
			/*
			 * View ss = (View) adapter
			 * .getItem(flipView.getSelectedItemPosition()); ListView listView =
			 * (ListView) ss.findViewById(R.id.html2_listview); ContentValues
			 * initialValues = new ContentValues();
			 * initialValues.put(Heibai.TITLE, titleString);
			 * initialValues.put(Heibai.CONTENT, (String) listView.getAdapter()
			 * .getItem(1)); initialValues.put(Heibai.DATE, NoMd5date);
			 * getContentResolver().insert(Heibai.JIANBAO_URI, initialValues);
			 * Toast.makeText(Details4.this, "操作已完成", 1).show();
			 */
			break;
		default:
			break;
		}
	}

	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);

	/**
	 * gallay 适配
	 * 
	 * @author zhang
	 * 
	 */
	private class MgallayAdapter extends BaseAdapter {
		ArrayList<String> urlArrayList;
		boolean ismore;

		MgallayAdapter(ArrayList<String> urlArrayList, boolean ismore) {
			this.urlArrayList = urlArrayList;
			this.ismore = ismore;
		}

		public int getCount() {
			return urlArrayList.size();
		}

		public Object getItem(int position) {
			return urlArrayList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.gallyitem,
						null);
			}
			ImageView text = (ImageView) convertView
					.findViewById(R.id.gally_item);
			try {
				if (ismore) {// 有多张的话 统一 大小
					lp.height = 200;
					text.setLayoutParams(lp);
					text.setImageBitmap(Tools.resizeBitmap(
							urlArrayList.get(position), 400, 200));
				} else {// 单张的话 显示原始大小
					text.setImageBitmap(BitmapFactory
							.decodeStream(new FileInputStream(urlArrayList
									.get(position))));// 这个可能会快点
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				finish();
			}
			return convertView;
		}

	}

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MgallayAdapter dapter =(MgallayAdapter) parent.getAdapter();
        if (dapter.ismore) {
            Intent intent= new Intent(Details5.this, ImageDialog2.class);
            intent.putStringArrayListExtra("url", dapter.urlArrayList);
           startActivity(intent); 
        }
      
    }

}
