package com.rmc;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Socket socket;
    private static final int port = 2000;   
    public String ipAddress;
    
    // "192.168.1.109";
	EditText et;
	Button connectButton; 
	
	private boolean connected = false;	 
    private Handler handler = new Handler();
    private Activity currentActivity; 
    
    Thread cThread;
    
    DataInputStream inputStream; 
    PrintWriter outStream; 
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		currentActivity = this; 
		
		et = (EditText)findViewById(R.id.addressField);
		connectButton = (Button)findViewById(R.id.connectButton); 
		connectButton.setOnClickListener(connectListener); 
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
        	Log.wtf("onClick", "in onClick");   
        	
            if (!connected) {
                //ipAddress = et.getText().toString();
            	ipAddress = "192.168.1.109"; 
                if (!ipAddress.equals("")) {                	
                	
                	if(socket != null)
						try {
							Log.wtf("onClick", "Closing socket");
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						} 
                	
                    cThread = new Thread(new ClientThread());
                    cThread.start();
                    Log.wtf("onClick", "thread started"); 
                }
            }
            if(connected)
            	Toast.makeText(currentActivity, "Connected", Toast.LENGTH_SHORT).show();
        }
       
    };    
	
	class ClientThread implements Runnable {	 
		
		@Override
		public void run() {            
			while(true)
			{
				try{
					socket = new Socket(ipAddress, port);
					Log.wtf("ClientThread", "Opened socket");
					break;
				} catch(Exception e)
				{
					Log.wtf("ClientThread", "Failed to open socket", e);
				}		
			}              
                          
			}
		
		}
	
	public void writeString(String message)
	{
		ipAddress = "192.168.1.109";
        
        cThread = new Thread(new ClientThread());
        cThread.start();	
		
        while(true)
		{
			try{
				outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
	                    .getOutputStream())), true); 
				Log.wtf("write", "Initialized outStream");
				break;
			} catch(Exception e)
			{
				//Log.wtf("write", "Failed to initialize outStream");
			}			
		}
        
        while(true)
		{
			try{		
				outStream.println(message);						
				Log.wtf("write", "Sent message");
				break;
			} catch(Exception e)
			  {
				Log.wtf("write", "Failed to send message");
			  }		
		}
        
        while(true)
		{
			try{
				socket.close();
				Log.wtf("Write", "Socket closed");
				break;
			} catch (Exception e)
			{
				Log.wtf("Write", "Failed to close socket"); 
			}	
		}
	}
	
	public void writeFile(byte[] output, long size)
	{
        ipAddress = "192.168.1.109";
        
        cThread = new Thread(new ClientThread());
        cThread.start();		
			
		while(true)
		{
			try{
				outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
	                    .getOutputStream())), true); 
				Log.wtf("write", "Initialized outStream");
				break;
			} catch(Exception e)
			{
				//Log.wtf("write", "Failed to initialize outStream");
			}			
		}
			
		while(true)
		{
			try{
			
			for(int i = 0; i < size; i++)
			{
				//outStream.println(output[i]);				
			}			
	        Log.wtf("write", "Sent file");
	        break;
			} catch(Exception e)
			  {
				Log.wtf("write", "Failed to send file");
			  }		
		}
		
		while(true)
		{
			try{
				socket.close();
				Log.wtf("Write", "Socket closed");
				break;
			} catch (Exception e)
			{
				Log.wtf("Write", "Failed to close socket"); 
			}	
		}
	}
	
	public void read()
    {
		byte[] buffer = new byte[64];
		String input = ""; 
		long currentTime = System.currentTimeMillis(); 
		
		ipAddress = "192.168.1.109";
		
		cThread = new Thread(new ClientThread());
        cThread.start();
        
    	while((System.currentTimeMillis() - currentTime) < 100000)
		{
			try{
				inputStream = new DataInputStream(socket.getInputStream()); 
				Log.wtf("read", "Initialized inputStream");
				break;
			} catch(Exception e)
			{
				Log.wtf("read", "Failed to initialize inputStream");
			}			
		}        	
    	
    	currentTime = System.currentTimeMillis();
    	
    	while((System.currentTimeMillis() - currentTime) < 10000)
    	{ 
    		try{
	    		buffer[0] = inputStream.readByte();  
	    		//input = new String(buffer);
	    		if(buffer[0] != -1)
	    			break; 
	    		
	    	} catch(Exception e)
    		{
	    		Log.wtf("read", "Failed to read bytes");
    		}
    		if(buffer[0] == '*')
    		{
    			Log.wtf("read()", "Exiting read function");
    			break;
    		}    		
    	}  	
    	
    	
		Log.wtf("Bytes read", input);
		Toast.makeText(currentActivity, input, Toast.LENGTH_LONG).show(); 
    	
    	while(true)
		{
			try{
				socket.close();
				Log.wtf("Write", "Socket closed");
				break;
			} catch (Exception e)
			{
				Log.wtf("Write", "Failed to close socket"); 
			}	
		}
        	       	
       
    	}
	
    }

