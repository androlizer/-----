package qianlong.qlmobile.ui;
import qianlong.qlmobile.ui.CrashHandler;
import android.app.Application;

//全局数据存储
public class App extends Application {
   @Override
   public void onCreate() {
    super.onCreate();
        
    // 异常处理，不需要处理时注释掉这两句即可！
    CrashHandler crashHandler = CrashHandler.getInstance(); 
    // 注册crashHandler 
    crashHandler.init(getApplicationContext()); 
   }}
