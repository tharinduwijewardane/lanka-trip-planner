package com.lanka.tripplanner.core;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

//import android.widget.Toast;

public class Calculator {

	public String getRoutDistane(double startLat, double startLong,
			double endLat, double endLong) {
		String Distance = "error";
		String Status = "error";
		try {

			JSONParser PJ = new JSONParser();
			JSONObject jsonObj = PJ
					.getJSONFromUrl("http://maps.googleapis.com/maps/api/directions/json?origin="
							+ startLat
							+ ","
							+ startLong
							+ "&destination="
							+ endLat + "," + endLong + "&sensor=true");
			Status = jsonObj.getString("status");
			// Toast.makeText(mContext,
			// "status.."+Status,Toast.LENGTH_SHORT).show();
			if (Status.equalsIgnoreCase("OK")) {
				JSONArray routes = jsonObj.getJSONArray("routes");
				JSONObject zero = routes.getJSONObject(0);
				JSONArray legs = zero.getJSONArray("legs");
				JSONObject zero2 = legs.getJSONObject(0);
				JSONObject dist = zero2.getJSONObject("distance");
				Distance = dist.getString("text");
			} else {
				Distance = "Too Far";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Distance;
	}

	public int getRoutTime(double startLat, double startLong, double endLat,
			double endLong) {
		double duration = 0.0;
		int minuteDuration = 0;
		String Status = "error";
		try {

			JSONParser PJ = new JSONParser();
			JSONObject jsonObj = PJ
					.getJSONFromUrl("http://maps.googleapis.com/maps/api/directions/json?origin="
							+ startLat
							+ ","
							+ startLong
							+ "&destination="
							+ endLat + "," + endLong + "&sensor=true");
			Status = jsonObj.getString("status");
			// Toast.makeText(mContext,
			// "status.."+Status,Toast.LENGTH_SHORT).show();
			if (Status.equalsIgnoreCase("OK")) {
				JSONArray routes = jsonObj.getJSONArray("routes");
				JSONObject zero = routes.getJSONObject(0);
				JSONArray legs = zero.getJSONArray("legs");
				JSONObject zero2 = legs.getJSONObject(0);
				JSONObject dist = zero2.getJSONObject("duration");
				duration = dist.getDouble("value");
				minuteDuration = (int) duration / 60;
			} else {
				minuteDuration = 0;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return minuteDuration;
	}

	public LatLng getLatLong(String address) {

		double longitude = 0.0;
		double latitude = 0.0;
		LatLng ltn;

		JSONParser PJ = new JSONParser();
		JSONObject jsonObject = PJ.getLocationInfo(address);

		try {

			longitude = ((JSONArray) jsonObject.get("results"))
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lng");

			latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

			System.out.println(latitude + " " + longitude);

			ltn = new LatLng(latitude, longitude);

		} catch (JSONException e) {
			return null;

		}

		return ltn;
	}

	public LatLng getCordinates(String locName, Context cntx) {
		Geocoder coder = new Geocoder(cntx, Locale.getDefault());
		List<Address> address;
		double longitude, latitude;

		longitude = 0;
		latitude = 0;

		try {
			address = coder.getFromLocationName(locName, 5);
			Address location = address.get(0);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.v("Calculator", latitude + " " + longitude);

		} catch (IOException e) {
			e.printStackTrace();
		}

		LatLng crdt = new LatLng(longitude, latitude);

		return crdt;
	}

}
