package com.lanka.tripplanner.ui;

import java.util.ArrayList;
import java.util.List;

import com.lanka.tripplanner.R;
import com.lanka.tripplanner.core.CoreMain;
import com.lanka.tripplanner.core.Locale;
import com.lanka.tripplanner.util.ConstVals;
import com.lanka.tripplanner.util.PreferenceHelp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
	private Button bShowDetails;
	private TextView tvLocation;
	private double sourceLatitude;
	private double sourceLongitude;

	int numberOfDays;
	String category;
	String region;

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
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spNumberOfDays.setAdapter(dataAdapter);

		spInterests = (Spinner) findViewById(R.id.spInterests);
		// Create an ArrayAdapter using the string array and a default
		// spinnerLayout
		ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(this, R.array.categories,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spInterests.setAdapter(adapterCategories);

		spRegion = (Spinner) findViewById(R.id.spRegion);
		// Create an ArrayAdapter using the string array and a default
		// spinnerLayout
		ArrayAdapter<CharSequence> adapterRegions = ArrayAdapter.createFromResource(this, R.array.Regions,
				android.R.layout.simple_spinner_item);
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

				readSelectedItems();

				Intent intent = new Intent(getApplicationContext(), ResultMapActivity.class);
				intent.putExtra("numberOfDays", numberOfDays);
				intent.putExtra("category", category);
				intent.putExtra("region", region);
				startActivity(intent);
			}
		});

		bShowDetails = (Button) findViewById(R.id.bShowDetails);
		bShowDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// custom dialog
				// final Dialog dialog = new CustomDialog(MainActivity.this);
				// dialog.setContentView(R.layout.dialog_details_layout);
				// dialog.setTitle("Title...");

				// set the custom dialog components - text, image and button
				// TextView text = (TextView) dialog.findViewById(R.id.text);
				// text.setText("Android custom dialog example!");
				// ImageView image = (ImageView)
				// dialog.findViewById(R.id.image);
				// image.setImageResource(R.drawable.ic_launcher);

				// android.widget.LinearLayout.LayoutParams params = new
				// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				// LayoutParams.WRAP_CONTENT);
				// TextView tv1 = new TextView(MainActivity.this);
				// TextView tv2 = new TextView(MainActivity.this);
				// android.widget.TableRow.LayoutParams trparams = new
				// TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
				// android.widget.TableRow.LayoutParams.WRAP_CONTENT);
				// tv1.setLayoutParams(trparams);
				// tv2.setLayoutParams(trparams);
				// tv1.setText("Hello1!");
				// tv2.setText("Hello2!");
				// TableLayout layoutINNER = new TableLayout(MainActivity.this);
				// layoutINNER.setLayoutParams(params);
				// TableRow tr = new TableRow(MainActivity.this);
				//
				// tr.setLayoutParams(params);
				// tr.addView(tv1);
				// tr.addView(tv2);
				// layoutINNER.addView(tr);
				// LinearLayout main = (LinearLayout)
				// dialog.findViewById(R.id.layout_details);
				// main.addView(layoutINNER);
				//
				// if(main == null){
				// Log.d("-MY-", "main is null");
				// }
				//
				// dialog.setContentView(main);

				// Button dialogButton = (Button)
				// dialog.findViewById(R.id.bDialogOk);
				// // if button is clicked, close the custom dialog
				// dialogButton.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// dialog.dismiss();
				// }
				// });
				//
				// dialog.show();

				new LongOperationFetchPath().execute("");

			}
		});

		tvLocation = (TextView) findViewById(R.id.tvLocation);
	}

	private void saveValues() { // not using yet

		prefHelp.savePref(ConstVals.PREF_KEY_NUMBER_OF_DAYS, (Integer) spNumberOfDays.getSelectedItem());
		prefHelp.savePref(ConstVals.PREF_KEY_CATEGORY, (String) spInterests.getSelectedItem());
		prefHelp.savePref(ConstVals.PREF_KEY_REGION, (String) spRegion.getSelectedItem());

	}

	private void displayValues() {

		sourceLatitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LAT);
		sourceLongitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LON);

		tvLocation.setText("Start Location:\n" + sourceLatitude + "\n" + sourceLongitude);

	}

	private void readSelectedItems() {
		numberOfDays = (Integer) spNumberOfDays.getSelectedItem();
		category = (String) spInterests.getSelectedItem();
		region = (String) spRegion.getSelectedItem();
		if (category.equals(getResources().getStringArray(R.array.categories)[0])) { // -Any-
			category = null;
			Log.d("-MY-", "category set to null");
		}
		if (region.equals(getResources().getStringArray(R.array.Regions)[0])) { // -Any-
			region = null;
			Log.d("-MY-", "region set to null");
		}
	}

	private class LongOperationFetchPath extends AsyncTask<String, Void, List<Locale>> {

		CoreMain coreMain = new CoreMain(getApplicationContext());
		ProgressDialog progressDialog;

		@Override
		protected List<Locale> doInBackground(String... params) {

			Log.d("-MY-", "search path started");

			readSelectedItems();

			List<Locale> pointsList = coreMain.searchPath(numberOfDays, sourceLongitude, sourceLatitude, category,
					region);

			return pointsList;
		}

		@Override
		protected void onPostExecute(List<Locale> pointsList) { // when done

			List<String> details = new ArrayList<String>();
			String tempString = "";
			int dayCounter = 1;

			Log.d("-MY-", "done searching path");
			for (Locale point : pointsList) {
				Log.d("-MY-", point.getName() + " " + point.getLati() + " " + point.getLongi());

				tempString += dayCounter + ":" + point.getName() + ":" + point.getTimeToVisit();

				if (point.isDestinationForDay()) {
					dayCounter++;
				}

				details.add(tempString);
				tempString = ""; // reset the temp string
			}

			progressDialog.dismiss();

			// custom dialog
			final DetailsDialog detailsDialog = new DetailsDialog(MainActivity.this, details);
			detailsDialog.setContentView(R.layout.dialog_details_layout);
			detailsDialog.setTitle("Trip Details");
			detailsDialog.show();
			// if button is clicked, close the custom dialog
			Button dialogButton = (Button) detailsDialog.findViewById(R.id.bDialogOk);
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					detailsDialog.dismiss();
				}
			});

		}

		@Override
		protected void onPreExecute() {
			Log.d("-MY-", "please wait");
			progressDialog = ProgressDialog.show(MainActivity.this, "Please wait !", "Fetching details...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
