package com.rmc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)	{
		
		int itemId = item.getGroupId();
		itemId = itemId & 00000011; 
		Log.e("sdfsdfsdfsdf", Integer.toString(itemId));
		
		switch(itemId) {
		case R.id.food: startActivity(new Intent(this, Food.class)); break;
		case R.id.video: startActivity(new Intent(this, Video.class)); break;
		case R.id.audio: startActivity(new Intent(this, Audio.class)); break;
		default: break; 		
		}
		
		return super.onOptionsItemSelected(item);		
	}

}
