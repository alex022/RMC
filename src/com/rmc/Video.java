package com.rmc;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Video extends MainActivity{
	
	Button capture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		
		capture = (Button)findViewById(R.id.capture); 
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				read(); 				
			}
		});
	}

}

