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
				80.3965941, categories[2], regions[2]);
		Place Horton = new Place(5, 160, "Horton_Plains", 6.811306, 80.787941,
				categories[1], regions[1]);
		Place Nilaweli = new Place(3, 90, "Nilaveli", 8.6927331, 81.1885293,
				categories[1], regions[5]);
		Place Polonnaruwa = new Place(4, 180, "Gal_Viharaya", 7.9650522,
				81.0055071, categories[2], regions[2]);
		Place GalleFort = new Place(3, 90, "Galle_Fort", 6.0286241, 80.2167942,
				categories[2], regions[3]);
		Place Katharagama = new Place(4, 90, "Kirivehera", 6.4238928,
				81.332206, categories[2], regions[3]);
		Place Hanthana = new Place(4, 130, "Hanthana", 7.228713, 80.638361,
				categories[1], regions[1]);
		Place Abhayagiriya = new Place(3, 70, "Abhayagiriya", 8.371134,
				80.395181, categories[2], regions[2]);
		Place Dambulla = new Place(4, 80, "Dambulu_Viharaya", 7.856667,
				80.649167, categories[2], regions[2]);

		Place[] places = { Anuradhapura, Horton, Nilaweli, Polonnaruwa,
				GalleFort, Katharagama, Hanthana, Abhayagiriya, Dambulla };

		return places;
	}

	public int[][] getInitDurations() {

		// minimum time durations to travel between above locations
		int[][] lowestDurations = { { 0, 255, 107, 112, 288, 295, 157, 6, 75 },
				{ 255, 0, 301, 202, 231, 131, 106, 253, 186 },
				{ 107, 301, 0, 115, 377, 308, 206, 106, 123 },
				{ 112, 202, 115, 0, 330, 219, 153, 112, 78 },
				{ 288, 231, 377, 330, 0, 189, 212, 292, 263 },
				{ 295, 115, 308, 219, 205, 0, 214, 296, 229 },
				{ 157, 106, 206, 153, 212, 214, 0, 165, 96 },
				{ 6, 253, 106, 112, 292, 296, 165, 0, 73 },
				{ 75, 186, 123, 78, 263, 229, 96, 73, 0 } };

		return lowestDurations;
	}
}