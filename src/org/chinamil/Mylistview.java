package org.chinamil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
/**
 * 自定义listview 
 * 禁止滚动
 * @author zhang
 *
 */
public class Mylistview extends ListView {

	public Mylistview(Context context) {
		super(context);
	}
	public Mylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
      
    }
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			//System.out.println("手指抬起");
		
			return super.dispatchTouchEvent(ev);
			
	case MotionEvent.ACTION_DOWN:
	//	System.out.println("手指按下");
		
		return super.dispatchTouchEvent(ev);
	case MotionEvent.ACTION_MOVE:
		//禁止 系统滚动处理
		return true;
	}
		
		return true;
	}
}
