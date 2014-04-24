package com.rmc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Food extends MainActivity{
	
	Activity currentActivity = this; 
	Button dispense;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food); 
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		dispense = (Button)findViewById(R.id.dispense); 
		dispense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(currentActivity, "Dispensing Food", Toast.LENGTH_SHORT).show(); 				
			}
		});
		
	}

}
