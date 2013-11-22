package org.chinamil.networkerr;
import org.chinamil.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
public class MyLinearLayout extends FrameLayout {
    private Bitmap background,background2 ,background3;
    OndsipatchDraw onDraw;
    FrameLayout.LayoutParams layoutParams;
public  int eage,nogengg;
    public interface OndsipatchDraw{
        public  void MyDraw(int eage);
    }
    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
                  init();
                
    }
    public void setOndsipatchDrawListener(OndsipatchDraw draw) {
        this.onDraw=draw;
    }   
private void init() {
    background = BitmapFactory.decodeResource(getResources(),
            R.drawable.bk);//113 155
    background3 = BitmapFactory.decodeResource(getResources(),
            R.drawable.bai);//113 155
          this.onDraw=onDraw;
}   
    @Override
    protected void dispatchDraw(Canvas canvas) {
       
        int count = getChildCount();
        int width = getWidth();
        int height = getHeight();// 768 964 3
        nogengg= eage=getHeight()/3;//每个书架的高
            onDraw.MyDraw(eage);
        background2=Tool.resizeBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.bk),width,
                eage);//創建適合的背景圖片
        int backgroundWidth = background2.getWidth();
        int backgroundHeight = background2.getHeight()+0;
        for (int y = 0; y < height; y += backgroundHeight) {
            for (int x = 0; x < width; x += backgroundWidth) {
                canvas.drawBitmap(background2, x, y-5, null);
            }
        }
        background3 = Tool.resizeBitmap(background3,
                width,
                eage/10);
        for (int y = 0; y <=3 ; y ++,eage=eage*y) {
            if (y!=0) {
                if (y==3) 
                    canvas.drawBitmap(background3, 0, height-background3.getHeight(), null);
               else
                canvas.drawBitmap(background3, 0,eage-nogengg/10, null);
            }
        }
    /*  
        int count = getChildCount();
        int top = count > 0 ? getChildAt(0).getTop() : 0;
        int backgroundWidth = background.getWidth();
        int backgroundHeight = background.getHeight()+2;
        int width = getWidth();
        int height = getHeight();

        for (int y = top; y < height; y += backgroundHeight) {
            for (int x = 0; x < width; x += backgroundWidth) {
                canvas.drawBitmap(background, x, y, null);
            }
        }
        super.dispatchDraw(canvas);*/
        super.dispatchDraw(canvas);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    
}
