package com.rmc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Food extends MainActivity{
	
	Thread dispenseThread;
	Activity currentActivity = this; 
	Button dispense;
	Button water;
	
	
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
				dispenseThread = new Thread(new writeThread("*FOOD*", null, 0, false)); 
				dispenseThread.start(); 
				//writeString("*DISPENSE*"); 
				Toast.makeText(currentActivity, "Dispensing Food", Toast.LENGTH_SHORT).show(); 				
			}
		});
		
		water = (Button)findViewById(R.id.water); 
		water.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dispenseThread = new Thread(new writeThread("*WATER*", null, 0, false)); 
				dispenseThread.start(); 
				//writeString("*DISPENSE*"); 
				Toast.makeText(currentActivity, "Dispensing Water", Toast.LENGTH_SHORT).show(); 				
			}
		});
		
	}

}
