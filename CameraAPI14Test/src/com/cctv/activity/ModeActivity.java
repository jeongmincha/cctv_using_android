package com.cctv.activity;

import com.nhn.android.test.camera.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ModeActivity extends Activity implements OnClickListener{
	
	String ipAddr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode);
		
		// buttons
		Button btnHome = (Button)findViewById(R.id.btnHomeMode);
		Button btnGuard = (Button)findViewById(R.id.btnGuardMode);
		btnHome.setOnClickListener(this);
		btnGuard.setOnClickListener(this);
		
		// intent
		Intent intent = getIntent();
		ipAddr = intent.getStringExtra("ip_address");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnHomeMode:
			intent = new Intent(ModeActivity.this, com.nhn.camera.FaceDetectionActivity.class);
			intent.putExtra("ip_address", ipAddr);
			startActivity(intent);
			break;
		case R.id.btnGuardMode:
			intent = new Intent(ModeActivity.this, com.marcodinacci.android.pim.CameraActivity.class);
			intent.putExtra("ip_address", ipAddr);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
