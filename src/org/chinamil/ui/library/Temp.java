package org.chinamil.ui.library;


import org.chinamil.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class Temp  extends Activity{
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
setContentView(R.layout.temp);
ErrorNetwork();
}
/**
 * 无网络退出 
 * @param title
 */
public void ErrorNetwork() {

    new AlertDialog.Builder(this).setTitle("错误").setIcon(R.drawable.imageback).setMessage("网络不可用，程序退出！")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                  
                    System.exit(0);
                }
            }).show();

}
}