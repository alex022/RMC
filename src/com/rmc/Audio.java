package com.rmc;

import android.os.Bundle;
import android.widget.Toast;

public class Audio extends MainActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(this, "Audio", Toast.LENGTH_LONG).show(); 
	}

}
