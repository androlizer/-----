package org.chinamil.ui.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.*;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import org.chinamil.Heibai;
/**
 *查询建议列表 以标题作为目标条件来查
 * @author zhang
 *
 */
public class Mysugessprovider extends SearchRecentSuggestionsProvider {
	public final static String AUTHORITY = " org.chinamil.ui.library.Mysugessprovider";
	public final static int MODE = DATABASE_MODE_QUERIES;
	private final static String[] sms_projection = new String[] {
			Heibai.PUBLIC, Heibai.TITLE, Heibai.DATE };
	private final static String[] columnNames = new String[] {
		     BaseColumns._ID,
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			SearchManager.SUGGEST_COLUMN_QUERY };
	public Mysugessprovider() {
		setupSuggestions(AUTHORITY, MODE);
	}
	String query;
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		// 拦截查询操作，
		query = selectionArgs[0];
		// 没有输入不就显示建议下拉列表
		if (TextUtils.isEmpty(query)) {
			return null;
		}
		Uri uri1 = Heibai.JIANBAO_URI;
		String where = Heibai.CONTENT + " like '%" + query + "%'";
		Cursor cursor = getContext().getContentResolver().query(uri1,
				sms_projection, where, null, null);
		return changeCursor(cursor);
	}
	/*
	 * Heibai.PUBLIC, Heibai.TITLE, Heibai.DATE, Heibai.CONTENT};
	 */
	private Cursor changeCursor(Cursor cursor) {
		MatrixCursor result = new MatrixCursor(columnNames);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Object[] objs = new Object[] {
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(1) };
				result.addRow(objs);
			}
		}
		return result;
	}

	/**
	 * 关键字高亮显示
	 * 
	 * @param target
	 *            需要高亮的关键字
	 */
	public SpannableStringBuilder highlight(String target, String text) {

		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		CharacterStyle span = null;

		Pattern p = Pattern.compile(target);
		Matcher m = p.matcher(text);
		while (m.find()) {
			span = new ForegroundColorSpan(Color.RED);// 需要重复！
			// span = new ImageSpan(drawable,ImageSpan.XX);//设置现在图片
			spannable.setSpan(span, m.start(), m.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannable;

	}
}
