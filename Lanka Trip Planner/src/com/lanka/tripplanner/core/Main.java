package com.lanka.tripplanner.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) {
		CoreMain aCoreMain = new CoreMain();
		ArrayList<Locale> road = aCoreMain.searchPath(5, 79.999413, 7.089895,
				"History", "North-Central");
		Iterator<Locale> ittr = road.iterator();
		String path = "-";
		while (ittr.hasNext()) {
			Locale roadMark = ittr.next();

			path = path + roadMark.getName() + "-";

			// if a day ends print ***
			if (roadMark.isDestinationForDay()) {
				path = path + " *** ";
			}
		}

		System.out.println(path);
	}

}
