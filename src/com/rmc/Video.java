package com.rmc;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Video extends MainActivity{
		 
	Button capture;	
	Button view;
	int timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		currentActivity = this;
		timer = 0;
		
		pictureReady = false;  
		capture = (Button)findViewById(R.id.capture); 
		view = (Button)findViewById(R.id.viewPhoto); 
		
		view.setEnabled(false); 
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
					Thread readThread = new Thread(new readThread()); 
					readThread.start();	
					
					Toast.makeText(currentActivity, "Taking picture, please wait...", Toast.LENGTH_LONG).show();
					
					view.setEnabled(true);								
			}
		});
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
							
					if(bitmap != null)
					{					
						if(pictureReady)
							startActivity(new Intent(currentActivity, Picture.class)); 
						else
							Toast.makeText(currentActivity, "Please wait for picture acquisition to finish", Toast.LENGTH_SHORT).show();						
						
					}
					else
						Toast.makeText(currentActivity, "bitmap is null", Toast.LENGTH_SHORT).show(); 
						
							
					
					pictureReady = false;
			}
		});
	}

}

