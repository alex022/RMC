package com.rmc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Picture extends Video{
	
	ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		imageView = (ImageView)findViewById(R.id.photo); 
		
		if(bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
			Log.wtf("picture", "bitmap is null");
		
		if(imageView != null){
			Log.wtf("picture", "imageView is not null");
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.wtf("picture", "clicked imageView"); 
					
				}
				
			});
			
		}			
		else
			Log.wtf("picture", "imageView is null");
	    
	    setContentView(R.layout.picture); 	
		
	}

}
