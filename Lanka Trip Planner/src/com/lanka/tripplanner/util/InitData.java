package com.lanka.tripplanner.util;

import com.lanka.tripplanner.R;
import com.lanka.tripplanner.core.Place;

import android.content.Context;

public class InitData {

	public Place[] getInitPlaces(Context cnt) {

		String[] regions = cnt.getResources().getStringArray(R.array.Regions);
		String[] categories = cnt.getResources().getStringArray(
				R.array.categories);

		// Sample Locations
		Place Anuradhapura = new Place(4, 90, "Ruwanwelisaya", 8.3501184,
				80.3965941, categories[1], regions[1]);
		Place Peradeniya = new Place(4, 180, "Peradeniya_Park", 8.6, 7.2666667,
				categories[0], regions[0]);
		Place Nilaweli = new Place(4, 90, "Nilaveli", 8.6927331, 81.1885293,
				categories[0], regions[4]);
		Place Polonnaruwa = new Place(4, 180, "Gal_Viharaya", 7.9650522,
				81.0055071, categories[1], regions[1]);
		Place GalleFort = new Place(3, 90, "Galle_Fort", 6.0286241, 80.2167942,
				categories[1], regions[2]);
		Place Katharagama = new Place(4, 90, "Kirivehera", 6.4238928,
				81.332206, categories[1], regions[2]);

		Place[] places = { Anuradhapura, Peradeniya, Nilaweli, Polonnaruwa,
				GalleFort, Katharagama };

		return places;
	}

	public int[][] getInitDurations() {

		// minimum time durations to travel between above locations
		int[][] lowestDurations = { { 0, 150, 107, 112, 288, 295 },
				{ 150, 0, 200, 148, 220, 208 }, { 107, 200, 0, 115, 377, 308 },
				{ 112, 148, 115, 0, 330, 219 }, { 288, 220, 377, 330, 0, 291 },
				{ 295, 208, 308, 219, 291, 0 } };

		return lowestDurations;
	}
}
