package com.rmc;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Video extends MainActivity{
		 
	ImageView picture;
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
		picture = (ImageView)findViewById(R.id.picture);
		picture.setVisibility(ImageView.INVISIBLE); 
		
		//view.setEnabled(false); 
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
				/*if(bitmap != null)
				{					
					if(pictureReady)
						startActivity(new Intent(currentActivity, Picture.class)); 
					else
						Toast.makeText(currentActivity, "Please wait for picture acquisition to finish", Toast.LENGTH_SHORT).show();						
					
				}
				else
					Toast.makeText(currentActivity, "bitmap is null", Toast.LENGTH_SHORT).show(); 						
				
				pictureReady = false;*/
				//Log.wtf("read", "Attempting to view photo"); 
				//File root = Environment.getExternalStorageDirectory();
				//String fileName = root + "/images/photoFile.jpg";
									
				//BitmapFactory.Options options = new BitmapFactory.Options();
				//  options.inSampleSize = 2;
				
				//bitmap = BitmapFactory.decodeFile(fileName);
				
				File photoFile;
				String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				fileName+= "/photoFile.jpg";
				
				bitmap = BitmapFactory.decodeFile(fileName);
				
				if(bitmap != null)
				{
					picture.setImageBitmap(bitmap);
					picture.setVisibility(ImageView.VISIBLE);
				}
				else
					Log.wtf("picture", "bitmap is null");
				
				//picture.setVisibility(ImageView.VISIBLE); 
				//startActivity(new Intent(currentActivity, Picture.class));				
			}
		});
	}

}

