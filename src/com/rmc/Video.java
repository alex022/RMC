package com.rmc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Video extends MainActivity{
		 
	ImageView picture;
	Button capture;	
    Button viewPicture;
    Button panLeft;
    Button panRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		currentActivity = this;
		  
		capture = (Button)findViewById(R.id.capture); 
		viewPicture = (Button)findViewById(R.id.viewPhoto); 
		panLeft = (Button)findViewById(R.id.panLeft); 
		panRight = (Button)findViewById(R.id.panRight);
		picture = (ImageView)findViewById(R.id.picture);
		picture.setVisibility(ImageView.INVISIBLE);	
		viewPicture.setEnabled(false); 
		pictureReady = false; 
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
				viewPicture.setEnabled(true);
				Toast.makeText(currentActivity, "Taking picture, please wait...", Toast.LENGTH_LONG).show();								
			}
		});
		
		viewPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				if(pictureReady)
				{
					bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
					
					if(bitmap != null)
					{	
						Matrix flipVerticalMatrix = new Matrix();
						flipVerticalMatrix.setScale(1,-1);
						flipVerticalMatrix.postTranslate(bitmap.getWidth(),0);
						
						bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), flipVerticalMatrix, true);
					
						picture.setImageBitmap(bitmap);
						picture.setVisibility(ImageView.VISIBLE);
					}
					else
						Log.wtf("picture", "bitmap is null");		
				}
				else
					Toast.makeText(currentActivity, "Picture still being taken", Toast.LENGTH_LONG).show();
			}
		});
		
		panLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(currentActivity, "Panning camera left", Toast.LENGTH_SHORT).show();
				Thread panThread = new Thread(new writeThread("*PANLEFT*", null, 0, null, null)); 
				panThread.start();	
			}
		});
		
		panRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(currentActivity, "Panning camera right", Toast.LENGTH_SHORT).show();
				Thread panThread = new Thread(new writeThread("*PANRIGHT*", null, 0, null, null)); 
				panThread.start();	
			}
		});
	}

}

