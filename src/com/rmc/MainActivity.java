package com.rmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public static String ipAddress;
	private EditText ipEntry; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			getUrl();
		} catch (ConnectException e) {
			
			e.printStackTrace();
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		
		int itemId = item.getItemId();
		
		switch(itemId) {
		case R.id.food: 
			startActivity(new Intent(this, Food.class));
			break;
		case R.id.video: 
			startActivity(new Intent(this, Video.class)); 
			break;
		case R.id.audio: 
			startActivity(new Intent(this, Audio.class)); 
			break;	
		default: 
			Log.e("onOptionsItemSelected", Integer.toString(itemId)); 
			break;
		}
		
		return super.onOptionsItemSelected(item);		
	}		
}
