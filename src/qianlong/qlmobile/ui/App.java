package qianlong.qlmobile.ui;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

//全局数据存储
public class App extends Application {
    public int a=0;
    private  WindowManager.LayoutParams wmParams;
    private   WindowManager vm;
    public void setAccounts(int accountsNO) {
        this.a = accountsNO;
    }

    public int getAccounts( ) {
      return a;
    }
     public WindowManager.LayoutParams getMywmParams(){
      return wmParams;
     }
     public WindowManager getMywm(){
         return vm;
        }
     @Override
    public void onCreate() {
        super.onCreate();
         wmParams=new WindowManager.LayoutParams();
       vm=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
      
    }
   }
