package com.example.homework5;

import com.example.android.BluetoothChat.BluetoothChat;
import com.example.android.BluetoothChat.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("UseValueOf")
public class MainActivity extends Activity {

	private String ipAddress;
	private String userName;
	private int portNum=12345;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// for using view components.
		final EditText editIpAddress = (EditText) findViewById(R.id.edit_ip_addr);
		final Button btn = (Button) findViewById(R.id.btn_login);
		
		// for moving from this activity to ChattingActivity activity.
		final Intent intent = new Intent(this, BluetoothChat.class);
		
		btn.setOnClickListener (new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ipAddress = editIpAddress.getText().toString();
				portNum = new Integer(portNum);
				
				// send input values to ChattingActivity activity.				
				intent.putExtra("ip_address", ipAddress);
				startActivity(intent);
			}
		});
	}
	
}