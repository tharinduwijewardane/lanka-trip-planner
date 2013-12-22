package com.lanka.tripplanner.ui;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lanka.tripplanner.R;
import com.lanka.tripplanner.core.CoreMain;
import com.lanka.tripplanner.core.Locale;
import com.lanka.tripplanner.util.ConstVals;
import com.lanka.tripplanner.util.PreferenceHelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Tharindu Wijewardane
 * 
 */
public class ResultMapActivity extends FragmentActivity implements
		OnMyLocationButtonClickListener, LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener, OnMapLongClickListener,
		OnMarkerClickListener, OnMarkerDragListener {

	GoogleMapV2Direction googleMapV2Direction;

	private static final LatLng NORTH = new LatLng(9.8188, 80.2060);
	private static final LatLng SOUTH = new LatLng(5.9287, 80.6039);
	private static final LatLng WEST = new LatLng(8.1807, 79.7074);
	private static final LatLng EAST = new LatLng(7.2935, 81.8622);

	double sourceLatitude;
	double sourceLongitude;
	int numberOfDays;
	String category;
	String region;
	CoreMain coreMain;

	private LatLng markerLocation;

	private GoogleMap mMap;
	private LocationClient mLocationClient; // to get user location
	PreferenceHelp prefHelp;
	private boolean isMyLocInfoEnabled;
	private String msgText;

	private TextView mMessageView;
	
	private Button bShowDetails;

	// private Marker mBrisbane;
	// private Marker mMelbourne;
	private Marker mMarker;

	private AsyncTask longOperation1;
	private AsyncTask longOperation2;

	private List<String> details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result_map_layout);

		setUpMapIfNeeded();

		initVals(); // initialize variables

		numberOfDays = getIntent().getIntExtra("numberOfDays", 1);
		category = getIntent().getStringExtra("category");
		region = getIntent().getStringExtra("region");

		Log.d("-MY-", "" + numberOfDays);
		Log.d("-MY-", "" + category);
		Log.d("-MY-", "" + region);

		longOperation1 = new LongOperation1().execute("");

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();

		setUpLocationClientIfNeeded(); // to get user location
		mLocationClient.connect(); // to get user location
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// longOperation1.cancel(false);
	// longOperation2.cancel(false);
	// }

	private class LongOperation1 extends AsyncTask<String, Void, List<Locale>> {

		@Override
		protected List<Locale> doInBackground(String... params) {

			Log.d("-MY-", "search path started");
			List<Locale> pointsList = coreMain.searchPath(numberOfDays,
					sourceLongitude, sourceLatitude, category, region);

			return pointsList;
		}

		@Override
		protected void onPostExecute(List<Locale> pointsList) { // when done

			mMessageView.setText("Path info fetched");

			details = new ArrayList<String>();
			String tempString = "";
			int dayCounter = 1;
			int i = 0; // to change markers

			Log.d("-MY-", "done searching path");
			for (Locale point : pointsList) {
				Log.d("-MY-", point.getName() + " " + point.getLati() + " "
						+ point.getLongi());

				tempString += dayCounter + ":" + point.getName() + ":"
						+ point.getTimeToVisit();

				if (point.isDestinationForDay()) {
					dayCounter++;
				}

				details.add(tempString);
				
				tempString = ""; //reset temp string

				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions
						.position(new LatLng(point.getLati(), point.getLongi()))
						.title(point.getName()).snippet("Day "+dayCounter).draggable(false);

				switch (i) { //to change marker icon
				case 0:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mr));
					break;
				case 1:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m1));
					break;
				case 2:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m2));
					break;
				case 3:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m3));
					break;
				case 4:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m4));
					break;
				case 5:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m5));
					break;
				case 6:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m6));
					break;
				case 7:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.m7));
					break;
				default:
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.me));
					break;
				}

				i++;

				if (!point.getName().equalsIgnoreCase("destination")) {
					// avoid adding a marker to destination bcoz its same as
					// source
					mMarker = mMap.addMarker(markerOptions);
				}

				// private void addMarkersToMap() {
				// // Uses a colored icon.
				// mBrisbane = mMap.addMarker(new MarkerOptions()
				// .position(BRISBANE)
				// .title("Brisbane")
				// .snippet("Population: 2,074,200")
				// .icon(BitmapDescriptorFactory
				// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				//
				// // Creates a draggable marker. Long press to drag.
				// mMelbourne = mMap.addMarker(new
				// MarkerOptions().position(MELBOURNE)
				// .title("Melbourne").snippet("Population: 4,137,400")
				// .draggable(true));
			}
			
			bShowDetails.setEnabled(true); //enable show details button

			longOperation2 = new LongOperation2().execute(pointsList);

		}

		@Override
		protected void onPreExecute() {
			mMessageView.setText("Please wait.. Searching for path");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	// to call the async method to get directions from google API
	private class LongOperation2 extends
			AsyncTask<List<Locale>, Void, List<Document>> {

		List<Locale> pontList;

		@Override
		protected List<Document> doInBackground(List<Locale>... params) {

			Log.d("-MY-", "before doc ");

			mMessageView
					.setText("Path info fetched. Waiting for directions...");

			pontList = params[0];
			Locale previousPoint = null;
			Document doc;
			List<Document> docs = new ArrayList<Document>();
			boolean isFirst = true;

			for (Locale point : pontList) {

				if (isFirst) {
					isFirst = false;
					previousPoint = point;
					continue;
				}

				doc = googleMapV2Direction.getDocument(
						new LatLng(previousPoint.getLati(), previousPoint
								.getLongi()),
						new LatLng(point.getLati(), point.getLongi()),
						GoogleMapV2Direction.MODE_OF_DRIVING);
				docs.add(doc);
				
				previousPoint = point;

			}

			Log.d("-MY-", "after doc ");

			return docs;
		}

		@Override
		protected void onPostExecute(List<Document> docs) { // when done

			mMessageView.setText("Directions are fetched successfully.");

			// Color.RED, Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN
			int[] colors = { Color.RED, Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN };
			int i = 0;

			for (Document doc : docs) {
				// draws the line on map
				ResultMapActivity.this.drawLine(doc, colors[i % colors.length]);
				i++;
			}

		}

		@Override
		protected void onPreExecute() {
			mMessageView.setText("Please wait... Directions are being fetched");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	/**
	 * draws the line segments on map. should be called when Document from
	 * google api is returned
	 */
	private void drawLine(Document doc, int clr) {

		if (doc == null) {
			Log.d("-MY-", "doc is null");
			return;
		}
		ArrayList<LatLng> directionPoint = googleMapV2Direction
				.getDirection(doc);

		PolylineOptions rectLine = new PolylineOptions().width(4).color(clr);

		for (int i = 0; i < directionPoint.size(); i++) {

			rectLine.add(directionPoint.get(i));// add each point of the path to
												// the polyline

		}
		mMap.addPolyline(rectLine);

	}

	private void initVals() {

		if (prefHelp == null) {
			prefHelp = new PreferenceHelp(getApplicationContext());
		}
		// retrieving values from prefs
		sourceLatitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LAT);
		sourceLongitude = prefHelp.getPrefDouble(ConstVals.PREF_KEY_LON);

		googleMapV2Direction = new GoogleMapV2Direction();
		coreMain = new CoreMain(getApplicationContext());

		// to get the previously saved position to initially pan to it
		// markerLocation = new LatLng(latitude, longitude);
		// markerLocation = PLACE2;

		// Creates a draggable marker. Long press to drag.
		// mMarker = mMap.addMarker(new
		// MarkerOptions().position(markerLocation).title("Marker")
		// .snippet("testing").draggable(true));

		mMessageView = (TextView) findViewById(R.id.tvMapMsg);
		// tvLat = (TextView) findViewById(R.id.tvLat);
		// tvLon = (TextView) findViewById(R.id.tvLon);

		mMessageView.setText("-");
		// tvLat.setText(Double.toString(sourceLatitude));
		// tvLon.setText(Double.toString(sourceLongitude));

		bShowDetails = (Button) findViewById(R.id.bShowDetailsMap);
		bShowDetails.setEnabled(false);
		bShowDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// custom dialog
				final DetailsDialog detailsDialog = new DetailsDialog(ResultMapActivity.this, details);
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
		});
		
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {

				setUpMap(); // for markers

				mMap.setMyLocationEnabled(true);
				mMap.setOnMyLocationButtonClickListener(this);
				mMap.setOnMapLongClickListener(this);
			}
		}
	}

	/* start of obtaining my location methods */

	// These settings are the same as the settings for the map. They will in
	// fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(10000) // 10 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
				.show();
		// Return false so that we don't consume the event and the default
		// behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	// Implementation of {@link LocationListener}.
	@Override
	public void onLocationChanged(Location location) {
		msgText = "Location = " + location;
		if (isMyLocInfoEnabled) {
			mMessageView.setText(msgText);
		}
	}

	// Implementation of {@link OnConnectionFailedListener}.
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// do nothing
	}

	// Callback called when connected to GCore. Implementation of {@link
	// ConnectionCallbacks}.
	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	// Callback called when disconnected from GCore. Implementation of {@link
	// ConnectionCallbacks}.
	@Override
	public void onDisconnected() {
		// do nothing
	}

	/**
	 * Button to get current Location. This demonstrates how to get the current
	 * Location as required without needing to register a LocationListener.
	 */
	// public void showMyLocation(View view) {
	// if (mLocationClient != null && mLocationClient.isConnected()) {
	// String msg = "Location = " + mLocationClient.getLastLocation();
	// Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
	// .show();
	// }
	// }

	/* end of obtaining my location methods */

	// my
	@Override
	public void onMapLongClick(LatLng location) {
		// String text = point.latitude + ", " + point.longitude;
		// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

		if (!checkReady()) {
			return; // return if map not ready
		}

		// mMap.clear(); // to clear the existing marker

		// Creates a draggable marker. Long press to drag.
		// mMarker = mMap.addMarker(new
		// MarkerOptions().position(location).title("Marker")
		// .snippet("testing").draggable(true));

		// showPosition(location);
		// savePosition(location);

	}

	/* start of marker methods */

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		// mMap.getUiSettings().setZoomControlsEnabled(false);

		// Add lots of markers to the map.
		// addMarkersToMap();

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		// mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		// mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							LatLngBounds bounds = new LatLngBounds.Builder()
									.include(NORTH).include(SOUTH)
									.include(WEST).include(EAST)
									// .include(markerLocation) // saved marker
									// pos

									.build();
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
						}
					});
		}
	}

	// private void addMarkersToMap() {
	// // Uses a colored icon.
	// mBrisbane = mMap.addMarker(new MarkerOptions()
	// .position(BRISBANE)
	// .title("Brisbane")
	// .snippet("Population: 2,074,200")
	// .icon(BitmapDescriptorFactory
	// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	//
	// // Creates a draggable marker. Long press to drag.
	// mMelbourne = mMap.addMarker(new MarkerOptions().position(MELBOURNE)
	// .title("Melbourne").snippet("Population: 4,137,400")
	// .draggable(true));
	// }

	private boolean checkReady() {
		if (mMap == null) {
			Toast.makeText(this, "Map not ready! ", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// /** Called when the Clear button is clicked. */
	// public void onClearMap(View view) {
	// if (!checkReady()) {
	// return;
	// }
	// mMap.clear();
	// }
	//
	// /** Called when the Reset button is clicked. */
	// public void onResetMap(View view) {
	// if (!checkReady()) {
	// return;
	// }
	// // Clear the map because we don't want duplicates of the markers.
	// mMap.clear();
	// // addMarkersToMap();
	// }

	@Override
	public boolean onMarkerClick(final Marker marker) {

		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		LatLng location = marker.getPosition();
		// showPosition(location);
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		LatLng location = marker.getPosition();
		// showPosition(location);
		// savePosition(location);
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub

	}

	/* end of marker methods */

	// // save location in prefs
	// private void savePosition(LatLng location) {
	//
	// prefHelp.savePref(ConstVals.PREF_KEY_LAT, location.latitude);
	// prefHelp.savePref(ConstVals.PREF_KEY_LON, location.longitude);
	//
	// }
	//
	// // show lat and lon in text views
	// private void showPosition(LatLng location) {
	//
	// tvLat.setText(Double.toString(location.latitude));
	// tvLon.setText(Double.toString(location.longitude));
	//
	// }

}
