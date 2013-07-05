package org.chinamil.networkerr;
import org.chinamil.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
public class MyLinearLayout extends FrameLayout {
	private Bitmap background,background2 ,background3;
	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bk);//113 155
		background3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.xiaban);//113 155
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		int top = count > 0 ? getChildAt(0).getTop() : 0;
		int eage=getHeight()/(background.getHeight()+50);//计算界面需要显示多少横向的 书架
		background2=Tool.resizeBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.bk),background.getWidth(),
				getHeight()/eage);//創建適合的背景圖片
		int backgroundWidth = background2.getWidth();
		int backgroundHeight = background2.getHeight()+0;
		int width = getWidth();
		int height = getHeight();// 768 964 3
		for (int y = top; y < height; y += backgroundHeight) {
			for (int x = 0; x < width; x += backgroundWidth) {
				canvas.drawBitmap(background2, x, y, null);
			}
		}
		background3 = Tool.resizeBitmap(background3,
				width,
				background3.getHeight());
		
		for (int y = 0; y < height; y +=height/eage) {
			if (y!=0) {
			if (y>(eage-1)*height/eage) {
					canvas.drawBitmap(background3, 0, height-17, null);
				}else
				canvas.drawBitmap(background3, 0, y-10, null);
			}
		}
		
		super.dispatchDraw(canvas);
	}
	
}
