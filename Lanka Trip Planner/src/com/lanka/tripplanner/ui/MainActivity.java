package com.lanka.tripplanner.ui;

import java.util.ArrayList;
import java.util.List;

import com.lanka.tripplanner.R;
import com.lanka.tripplanner.util.ConstVals;
import com.lanka.tripplanner.util.PreferenceHelp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Tharindu Wijewardane
 * 
 */
public class MainActivity extends Activity {

	private PreferenceHelp prefHelp;
	private Spinner spNumberOfDays;
	private Spinner spInterests;
	private Spinner spRegion;
	private Button bSelectStartLoc;
	private Button bGeneratePlan;
	private TextView tvLocation;
	private double sourceLatitude;
	private double sourceLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (prefHelp == null) {
			prefHelp = new PreferenceHelp(getApplicationContext());
		}

		initUI();

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		displayValues();
	}



	private void initUI() {
		spNumberOfDays = (Spinner) findViewById(R.id.spNumberOfDays);
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spNumberOfDays.setAdapter(dataAdapter);

		spInterests = (Spinner) findViewById(R.id.spInterests);
		// Create an ArrayAdapter using the string array and a default spinnerLayout
		ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spInterests.setAdapter(adapterCategories);

		spRegion = (Spinner) findViewById(R.id.spRegion);
		// Create an ArrayAdapter using the string array and a default spinnerLayout
		ArrayAdapter<CharSequence> adapterRegions = ArrayAdapter.createFromResource(this,
				R.array.Regions, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterRegions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spRegion.setAdapter(adapterRegions);

		bSelectStartLoc = (Button) findViewById(R.id.bSelectStartLoc);
		bSelectStartLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);
			}
		});

		bGeneratePlan = (Button) findViewById(R.id.bGeneratePlan);
		bGeneratePlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int numberOfDays = (Integer) spNumberOfDays.getSelectedItem();
				String category = (String) spInterests.getSelectedItem();
				String region = (String) spRegion.getSelectedItem();
				
				Intent intent = new Intent(getApplicationContext(), ResultMapActivity.class);
				intent.putExtra("numberOfDays", numberOfDays);
				intent.putExtra("category", category);
				intent.putExtra("region", region);
				startActivity(intent);
			}
		});
		
		tvLocation = (TextView) findViewById(R.id.tvLocation);
	}

	private void saveValues() { //not using yet

		prefHelp.savePref(ConstVals.PREF_KEY_NUMBER_OF_DAYS,
				(Integer) spNumberOfDays.getSelectedItem());
		prefHelp.savePref(ConstVals.PREF_KEY_CATEGORY, (String) spInterests.getSelectedItem());
		prefHelp.savePref(ConstVals.PREF_KEY_REGION, (String) spRegion.getSelectedItem());

	}

	private void displayValues() {
		
		sourceLatitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LAT);
		sourceLongitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LON);

		tvLocation.setText("Start Location:\n"+sourceLatitude+"\n"+sourceLongitude);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
