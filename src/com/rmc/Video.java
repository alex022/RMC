package com.rmc;

import android.os.Bundle;
import android.widget.Toast;

public class Video extends MainActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(this, "Video", Toast.LENGTH_LONG).show(); 
	}

}

