package com.rmc;
	
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
    
    public static boolean pictureReady;
    
    private static final int ATTEMPTS = 10 ;
    
    // "192.168.1.109";
	EditText et;
	Button connectButton; 
	
	private boolean connected = false;	 
    private Handler handler = new Handler();
    public Activity currentActivity;   
     
    PrintWriter outStream;    
    
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
			int numStars = 0;
			int numBytes = 0;
			int attempts = 0; 
			byte[] byteBuffer = new byte[100]; 
			
			
			File photoFile;
			FileOutputStream fos = null;
			String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			fileName+= "/photoFile.jpg";
						
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
    			    	
    		try {	    		
	    		photoFile = new File(fileName);

	    		if (photoFile.exists()) {
	                photoFile.delete();
	    		}

	    		fos = new FileOutputStream(photoFile.getPath());

	            fos.write(photoData);
	            fos.close();	            
	        } catch (Exception e) {
	        }    	
	    	
	    	bitmap = BitmapFactory.decodeFile(fileName);	    	
	    	
	    	if(bitmap != null)
	    		Log.wtf("read", "bitmap conversion successful");
	    	else
	    		Log.wtf("read", "failed to convert bitmap"); 
	    	
	    	pictureReady = true;
	    	
	    	while(true)
			{
				try{
					if(socket != null)
					{
						socket.close(); 
					    reader.close();
					    fos.close();
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
    
    class writeThread implements Runnable	{

    	String message;
    	byte[] output;
    	long size;
    	boolean isAudio;
    	
    	public writeThread(String message, byte[] output, long size, boolean isAudio)
    	{
    		this.message = message; 
    		this.output = output;
    		this.size = size; 
    		this.isAudio = isAudio; 
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
				Toast.makeText(currentActivity, "Failed to connect, please try again", Toast.LENGTH_LONG).show();
				return; 
			}
			
			attempts = 0;
			
			while(attempts < ATTEMPTS)
			{
				try{
					outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
		                    .getOutputStream())), true); 
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
					outStream.println(message);						
					Log.wtf("write", "Sent message");
					break;
				} catch(Exception e)
				  {
					Log.wtf("write", "Failed to send message");
				  }	
					
				attempts++;				
			}
			
			attempts = 0;
			
			if(isAudio)
			while(attempts < ATTEMPTS)
			{			
				try{						
					for(int i = 0; i < size; i++)
					{
						Log.wtf("Sending byte", Byte.toString(output[i])); 
						outStream.println(output[i]);				
					}			
			        Log.wtf("write", "Sent file");
			        break;
				} catch(Exception e)
				  {
					Log.wtf("write", "Failed to send file");
				  }		
				
				attempts++;
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

