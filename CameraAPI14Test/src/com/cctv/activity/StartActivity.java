package com.cctv.activity;

import com.nhn.android.test.camera.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends Activity {

	private String ipAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// for using view components.
		final EditText editIpAddress = (EditText) findViewById(R.id.edit_ip_addr);
		final Button btn = (Button) findViewById(R.id.btn_login);
		
		// for moving from this activity to ChattingActivity activity.
		final Intent intent = new Intent(this, ModeActivity.class);
		
		btn.setOnClickListener (new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ipAddress = editIpAddress.getText().toString();
				
				// send input values to ChattingActivity activity.				
				intent.putExtra("ip_address", ipAddress);
				startActivity(intent);
			}
		});
	}
}