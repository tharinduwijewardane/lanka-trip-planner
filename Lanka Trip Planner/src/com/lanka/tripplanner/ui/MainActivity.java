package com.lanka.tripplanner.ui;

import com.lanka.tripplanner.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Tharindu Wijewardane
 *
 */
public class MainActivity extends Activity {
	
	Button bSelectStartLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bSelectStartLoc = (Button) findViewById(R.id.bSelectStartLoc);
		bSelectStartLoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
