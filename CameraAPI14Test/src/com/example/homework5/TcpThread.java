package com.example.homework5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class TcpThread extends AsyncTask<String, String, String> {

	private String recvText = "";
	
	private String ip_addr;
	private int port_num;
	public Socket sock = null;
	
	TextView chatText;

	public TcpThread (String ip_addr, int port_num){
		this.ip_addr = ip_addr;
		this.port_num = port_num;
	}
	
	public TcpThread() {
		this.ip_addr = "127.0.0.1";
		this.port_num = 12345;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			sock = new Socket(ip_addr, port_num);
			
			PrintWriter writer = new PrintWriter(sock.getOutputStream());
			writer.println(params[0]);
			writer.flush();
			
			writer.println("c");
			writer.flush();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//			while (true) {
//				String str = reader.readLine();
//				if (str == null)
//					break;
//				publishProgress(str);
//			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		if (values[0] != null) {
			Log.e("LOG", "msg : " + values[0]);
			
			recvText += values[0] + "\n";
			chatText.setText(recvText);
		}
	}
	
	public String getRecvText() {
		return recvText;
	}
}
