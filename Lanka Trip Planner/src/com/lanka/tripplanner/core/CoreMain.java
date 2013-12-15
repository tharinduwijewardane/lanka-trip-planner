package com.lanka.tripplanner.core;

import java.util.ArrayList;

public class CoreMain {

	public ArrayList<Locale> searchPath(int noDays, double srcLongi,
			double srcLati, String category, String province) {

		int expectedTime;

		expectedTime = 14 * 60 * noDays;

		// Used to get distance between two locations
		Calculator cal = new Calculator();

		// Sample Location
		Place Anuradhapura = new Place(4, 90, "Ruwanwelisaya", 8.3501184,
				80.3965941, "History", "North-Central");
		Place Peradeniya = new Place(4, 180, "Peradeniya Park", 8.6, 7.2666667,
				"Nature", "Central");
		Place Nilaweli = new Place(4, 90, "Nilaveli", 8.6927331, 81.1885293,
				"Nature", "Eastern");
		Place Polonnaruwa = new Place(4, 180, "Gal Viharaya", 7.9650522,
				81.0055071, "History", "North-Central");
		Place GalleFort = new Place(3, 90, "Galle Fort", 6.0286241, 80.2167942,
				"History", "Southern");
		Place Katharagama = new Place(4, 90, "Kirivehera", 6.4238928,
				81.332206, "History", "Southern");

		// Source and Destination is defined
		Destination dest1 = new Destination(expectedTime, srcLati, srcLongi);
		Source src1 = new Source(srcLati, srcLongi);

		Locale[] places = { Anuradhapura, Peradeniya, Nilaweli, Polonnaruwa,
				GalleFort, Katharagama };

		int[] srcTimeToTravel = new int[places.length];
		int[] destTimeToTravel = new int[places.length];

		// get times to reach Source and Destination from each of above places
		for (int i = 0; i < places.length - 1; i++) {
			srcTimeToTravel[i] = cal.getRoutTime(srcLati, srcLongi,
					places[i].x, places[i].y);
			destTimeToTravel[i] = cal.getRoutTime(srcLati, srcLongi,
					places[i].x, places[i].y);
		}

		// times to reach each place from every other place
		int[][] lowestDurations = { { 0, 150, 107, 112, 288, 295 },
				{ 150, 0, 200, 148, 220, 208 }, { 107, 200, 0, 115, 377, 308 },
				{ 112, 148, 115, 0, 330, 219 }, { 288, 220, 377, 330, 0, 291 },
				{ 295, 208, 308, 219, 291, 0 } };

		for (int i = 0; i < 6; i++) {
			src1.addNeighbour(new Neighbour(places[i], srcTimeToTravel[i]));
		}

		for (int j = 0; j < 6; j++) {
			for (int k = 0; k < 6; k++) {
				if (k == j) {
					continue;
				}
				places[j].addNeighbour(new Neighbour(places[k],
						lowestDurations[j][k]));
			}
			places[j].addNeighbour(new Neighbour(dest1, destTimeToTravel[j]));
		}

		Map aMap = new Map(src1, dest1);
		ArrayList<Locale> road = aMap.getPath(category, province);

		return road;
	}

}
