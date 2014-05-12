package com.rmc;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Socket socket;
    private static final int port = 2000;   
	//public static String ipAddress = "128.111.58.37";
    public static String ipAddress = "192.168.1.109";
	EditText et;
	
	private boolean connected = false;	 
    private Handler handler = new Handler();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		et = (EditText)findViewById(R.id.addressField);
		
		new Thread(new ClientThread()).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		
		int itemId = item.getItemId();
		
		switch(itemId) {
		case R.id.food: 
			startActivity(new Intent(this, Food.class));
			break;
		case R.id.video: 
			startActivity(new Intent(this, Video.class)); 
			break;
		case R.id.audio: 
			startActivity(new Intent(this, Audio.class)); 
			break;	
		default: 
			Log.e("onOptionsItemSelected", Integer.toString(itemId)); 
			break;
		}		
		return super.onOptionsItemSelected(item);		
	}	
	
	private OnClickListener connectListener = new OnClickListener() {
		 
        @Override
        public void onClick(View v) {
        	Log.d("onClick", "in onClick"); 
            if (!connected) {
                ipAddress = et.getText().toString();
                if (!ipAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };
	
	class ClientThread implements Runnable {	 
		
		@Override
		public void run() {
            try {
                Log.d("ClientActivity", "C: Connecting...");
                while(!connected){
                socket = new Socket(ipAddress, port);
                connected = true;
                }
               // while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                    .getOutputStream())), true);                           
                            out.println("Hey Server!");
                            Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                //}
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
	}
}