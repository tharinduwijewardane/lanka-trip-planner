package com.lanka.tripplanner.core;

import java.util.ArrayList;

import com.lanka.tripplanner.util.Database;

import android.content.Context;

public class CoreMain {

	private Context mContext;

	public CoreMain(Context context) {
		mContext = context;
	}

	public ArrayList<Locale> searchPath(int noDays, double srcLongi,
			double srcLati, String category, String province) {

		int expectedTime;

		expectedTime = 14 * 60 * noDays;

		// Used to get distance between two locations
		Calculator cal = new Calculator();

		// Source and Destination is defined
		Destination dest1 = new Destination(expectedTime, srcLati, srcLongi);
		Source src1 = new Source(srcLati, srcLongi);

		// Get data from DB
		Database appDB = new Database(mContext);
		appDB.openForWrite();
		ArrayList<Place> sites = appDB.readData();
		Place[] places = (Place[]) sites.toArray(new Place[sites.size()]);
		appDB.close();

		int[] srcTimeToTravel = new int[places.length];
		int[] destTimeToTravel = new int[places.length];

		// get times to reach Source and Destination from each of above places
		for (int i = 0; i < places.length - 1; i++) {
			srcTimeToTravel[i] = cal.getRoutTime(srcLati, srcLongi,
					places[i].x, places[i].y);
			destTimeToTravel[i] = srcTimeToTravel[i];
		}

		appDB.openForWrite();

		for (int i = 0; i < places.length; i++) {
			src1.addNeighbour(new Neighbour(places[i], srcTimeToTravel[i]));
		}

		for (int j = 0; j < places.length; j++) {
			for (int k = 0; k < places.length; k++) {
				if (k == j) {
					continue;
				}
				places[j].addNeighbour(new Neighbour(places[k], appDB
						.getTimeToTravel(places[j].getName(),
								places[k].getName())));

			}
			places[j].addNeighbour(new Neighbour(dest1, destTimeToTravel[j]));
		}

		appDB.close();

		Map aMap = new Map(src1, dest1);
		ArrayList<Locale> road = aMap.getPath(category, province);

		return road;
	}
}
