package com.rmc;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Food extends MainActivity{
	
	Thread dispenseThread;
	Activity currentActivity = this; 
	Button dispense;
	Button water;
	Button schedule;
	EditText timeText;	
	String time;
	String currentTime;
	
	Time now = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food); 
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		timeText = (EditText)findViewById(R.id.time); 
		
		dispense = (Button)findViewById(R.id.dispense); 
		dispense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dispenseThread = new Thread(new writeThread("*FOOD*", null, 0, null, null)); 
				dispenseThread.start(); 
				Toast.makeText(currentActivity, "Dispensing Food", Toast.LENGTH_LONG).show(); 				
			}
		});
		
		water = (Button)findViewById(R.id.water); 
		water.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dispenseThread = new Thread(new writeThread("*WATER*", null, 0, null, null)); 
				dispenseThread.start();  
				Toast.makeText(currentActivity, "Dispensing Water", Toast.LENGTH_LONG).show(); 				
			}
		});
				
		schedule = (Button)findViewById(R.id.schedule);
		schedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				time = timeText.getText().toString();
				
				now.setToNow(); 
				currentTime = "";
				if(now.hour < 10)				
					currentTime = currentTime.concat("0");
				currentTime = currentTime.concat(Integer.toString(now.hour));					
				
				if(now.minute < 10)
					currentTime = currentTime.concat("0");
				currentTime = currentTime.concat(Integer.toString(now.minute));
				
				if(!(time.length() < 4))
				{
					String h = Character.toString(time.charAt(0)) + Character.toString(time.charAt(1));
					String m = Character.toString(time.charAt(2)) + Character.toString(time.charAt(3));
					
					int hour = Integer.valueOf(h);
					int minute = Integer.valueOf(m);
					
					if((hour > 23) || (minute > 59))
					{
						Toast.makeText(currentActivity, "Not a valid time", Toast.LENGTH_SHORT).show();
						
					}
					else
					{
						Toast.makeText(currentActivity, "Setting schedule", Toast.LENGTH_LONG).show();
						dispenseThread = new Thread(new writeThread("*SCHEDULE*", null, 0, time, currentTime)); 
						dispenseThread.start();
					}
				}
				else
				{
					Toast.makeText(currentActivity, "Not a valid time", Toast.LENGTH_SHORT).show();
				}
					
				
				 
			}
		});
	}

}
