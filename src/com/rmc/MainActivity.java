package com.rmc;
	
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
	
public class MainActivity extends Activity {
	
	private Socket socket;
    private static final int port = 2000;   
    public String ipAddress = "192.168.1.109";
    
    public byte[] photoData;
    public static Bitmap bitmap; 
        
    private static final int ATTEMPTS = 5;
    
    // "192.168.1.109";
	EditText et;
	Button connectButton; 
	
	private boolean connected = false;	 
    private Handler handler = new Handler();
    public Activity currentActivity;   
     
    PrintWriter outStream;   
    public static boolean pictureReady;      
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		currentActivity = this; 
		
		et = (EditText)findViewById(R.id.addressField);
		connectButton = (Button)findViewById(R.id.connectButton); 
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
	
	class readThread implements Runnable {
		
		@Override
		public void run() {
			
			String size = "";
			int numBytes = 0;
			int attempts = 0; 
			byte[] byteBuffer = new byte[100]; 
									
			InputStream reader = null;
			
			while(attempts < ATTEMPTS)
			{
				try{
					socket = new Socket(ipAddress, port);
					Log.wtf("read", "Opened socket");
					break;
				} catch(Exception e)
				{
					Log.wtf("read", "Failed to open socket", e);
				}		
				
				attempts++;
			}   
			
			if(attempts == ATTEMPTS)
			{
				Log.wtf("read", "Connection failed");
				return; 
			}
			
			attempts = 0;
			
			while(attempts < ATTEMPTS)
			{
				try{
					outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
		                    .getOutputStream())), true); 
					Log.wtf("write", "Initialized outStream");
						
					reader = socket.getInputStream(); 	
					Log.wtf("read", "Initialized inputStream");
					break;
				} catch(Exception e)
				{
					Log.wtf("read", "Failed to initialize inputStream");
				}	
				
				attempts++;
			}   
			
			attempts = 0;
			
			while(attempts < ATTEMPTS)
			{
				try{
					outStream.println("*PICTURE*");						
					Log.wtf("write", "Sent message");
					break;
				} catch(Exception e)
				{
					Log.wtf("read", "Failed to send PICTURE message"); 
				}
				
				attempts++;
			}
			
	    	
			//This loop reads *HELLO*
    		try{   			
				while(true)
	    		{
					reader.read(byteBuffer, numBytes, 1);
					Log.wtf("read", Character.toString((char)byteBuffer[numBytes]));
										 
					numBytes++;
					
					if(numBytes == 7)
						break; 
	    		}
	    			
	    		} catch(Exception e)
    		{
	    		Log.wtf("read", "Failed to read bytes");
    		}
    			    	
    		//Read buffer size
    		for(int i = 0; i < 5; i++)
    		{
    			try {
					reader.read(byteBuffer, i, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}   	    			
    		}
    		for(int i = 4; i >= 0; i--){
    			size = size.concat(Byte.toString((byte)byteBuffer[i]));    			
    		}
	    		
    		
    		Log.wtf("read", "size of buffer is " + size);   
    		    		
    		photoData = new byte[Integer.valueOf(size)];
    		
    		boolean end = false;
    		numBytes = 0;
    		while(!end)
    		{    			
    			try {
					reader.read(photoData, numBytes, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
    			numBytes++;
    			
    			if((numBytes % 500) == 0)
    				Log.wtf("read", Integer.toString(numBytes) + " bytes read");    				
    			
    			if(numBytes == Integer.valueOf(size)){
    				end = true; 
    				Log.wtf("read", "Read " + numBytes + " bytes");
    				break; 
    			}    			 			    			
    		}    			    	
	    	
			try{
				if(socket != null)
				{
					socket.close(); 
				    reader.close();
				    outStream.close();
				}					
				
				Log.wtf("Write", "Socket closed");				
			} catch (Exception e)
			{
				Log.wtf("Write", "Failed to close socket"); 
			}	
	    	
	    	 pictureReady = true; 
		}
    	
    }
    
    class writeThread implements Runnable	{

    	String message, time, currentTime;
    	byte[] output;
    	long size;
    	
    	public writeThread(String message, byte[] output, long size, String time, String currentTime)
    	{
    		this.message = message; 
    		this.output = output;
    		this.size = size; 
    		this.time = time; 
    		this.currentTime = currentTime; 
    	}
    	
		@Override
		public void run() {
			
			int attempts = 0;
			
			Log.wtf("write", "Attempting to open socket"); 
			
			while(attempts < ATTEMPTS)
			{
				try{
					socket = new Socket(ipAddress, port);
					Log.wtf("ClientThread", "Opened socket");
					break;
				} catch(Exception e)
				{
					Log.wtf("write", "Failed to open socket", e);
				}	
				attempts++;
			}
			
			if(attempts == ATTEMPTS)
			{
				Log.wtf("write", "Connection failed");
				return; 
			}
			
			attempts = 0;
			
			while(attempts < ATTEMPTS)
			{
				try{
					outStream = new PrintWriter(socket
		                    .getOutputStream(), true); 
					Log.wtf("write", "Initialized outStream");
					break;
				} catch(Exception e)
				{
					Log.wtf("write", "Failed to initialize outStream");
				}	
				attempts++;
			}
			
			attempts = 0;
			
			while(attempts < ATTEMPTS)
			{					
				try{	
					if(message.equals("*SCHEDULE*"))
						outStream.print(message);	
					else
						outStream.println(message);	
					Log.wtf("write", "Sent message");
					break;
				} catch(Exception e)
				  {
					Log.wtf("write", "Failed to send message");
				  }	
					
				attempts++;				
			}
			
			if(message.equals("*FOOD*") || message.equals("*WATER*") || message.equals("*SCHEDULE*"))
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} 			
			else if(message.equals("*PANLEFT*") || message.equals("*PANRIGHT*"))
				try {
					Thread.sleep(750);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} 
			else if(message.equals("*AUDIO1*") || message.equals("*AUDIO2") || message.equals("*AUDIO3*"))
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			
			attempts = 0;			
//			if(message.equals("*AUDIO*"))
//			while(attempts < ATTEMPTS)
//			{					
//				try{					
////					for(int i = 0; i < size; i++)
////					{
////						outStream.println(output[i]);
////						Log.wtf("byte", Integer.toHexString((int)output[i]));
////					}
//					Log.wtf("Size", Long.toString(size));	
//					
//					String dataString = output.toString(); 
//					outStream.println(dataString); 
//					
//						
//			        Log.wtf("write", "Sent file");
//			        break;
//				} catch(Exception e)
//				  {
//					Log.wtf("write", "Failed to send file");
//				  }		
//				
//				attempts++;
//			}
			
			if(message.equals("*SCHEDULE*"))
			{
				Log.wtf("sending", "current time is " + currentTime);
				Log.wtf("sending", "schedule time is " + time);
				outStream.println(currentTime + time);				
			}
			
			attempts = 0;
			
			while(true)
			{
				try{					
					if(socket != null)
					{
						socket.close();
					    outStream.close();
					}
					
					Log.wtf("Write", "Socket closed");
					break;
				} catch (Exception e)
				{
					Log.wtf("Write", "Failed to close socket"); 
				}	
			} 			
		}
    	
    	}
	
    }

