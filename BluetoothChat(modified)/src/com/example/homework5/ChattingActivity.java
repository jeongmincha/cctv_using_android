package com.example.homework5;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.android.BluetoothChat.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class ChattingActivity extends Activity{

	private String ipAddress;
	private int portNum;
	private String userName;
	
	TcpThread tcpChat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatting);
		
		// for additional points (the time message sent)
		final SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH:mm");
		
		final TextView textChatting = (TextView) findViewById(R.id.text_view_chatting);
		final EditText editChatting = (EditText) findViewById(R.id.edit_chatting);
		final Button btnSubmit = (Button) findViewById(R.id.btn_submit);
		
		// bring input values from previous activity.
		Intent intent = getIntent();
		ipAddress = intent.getStringExtra("ip_address");
		portNum = intent.getIntExtra("port_number", 12345);
		userName = intent.getStringExtra("user_name");
		
		// create thread for TCP communication.
//		tcpChat = new TcpThread(ipAddress, portNum);
//		tcpChat.execute(userName);
				
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = editChatting.getText().toString();
				
				// clear the chat space after button clicked.
				editChatting.setText("");
				try {
					if (msg.isEmpty() == false && tcpChat.sock != null) {
						PrintWriter writer = new PrintWriter(tcpChat.sock.getOutputStream());
						Date date = new Date(System.currentTimeMillis());
						msg += " (" + CurTimeFormat.format(date) + ")";
						writer.println(msg);
						writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		try {
			tcpChat.sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
