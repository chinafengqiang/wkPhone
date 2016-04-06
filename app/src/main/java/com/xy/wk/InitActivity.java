package com.xy.wk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import xy.com.utils.AppConstants;
import xy.com.utils.FileUtils;

public class InitActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					String rootPath = Environment.getExternalStorageDirectory().getPath()
							+ "/"+ AppConstants.COURSE_DOWNLOAD_URL;
					FileUtils.getFileDir(rootPath);

					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				startActivity(intent);
				finish();
			}
		}).start();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		this.finish();
	}
	
	
}
