package org.chinamil;

import android.accounts.NetworkErrorException;


public interface ProgressBarListener {

		public void getMax(int len)throws NetworkErrorException;
	
	
	   public void downloadsize(int len)throws NetworkErrorException;
}
