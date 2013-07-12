package org.chinamil.networkerr;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class Myviewpager extends ViewPager {
	public Myviewpager(Context context) {
		super(context);
	}
	public Myviewpager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	View  v = this.getChildAt(0);
	if (v instanceof GridView) {
	    GridView gridView= (GridView) v;
	    if ( gridView.getTag().equals("zhuce")) {
	        return    gridView.dispatchTouchEvent(ev);
        }       else 
            
           return super.dispatchTouchEvent( ev);
    }
    return false;
    //return super.dispatchTouchEvent( ev);
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
	    // TODO Auto-generated method stub
	    return super.onTouchEvent(arg0);
	}
	
	
}
