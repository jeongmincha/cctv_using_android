package com.marcodinacci.android.pim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;

/**
 * {@link} http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CameraPreview.html
 */
public class CameraActivity extends Activity {

	/* The surface where the preview is drawn */
	private SurfaceView mCameraView;
	int serverResponseCode=0;
    
    String upLoadServerUri=null;
    
//    String uploadFilePath="/sdcard/";
//    String uploadFileName="image.jpg";
    Handler mHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String ipaddr = intent.getStringExtra("ip_address");
		mCameraView= new Preview(this,ipaddr);
		setContentView(mCameraView);
	}
}