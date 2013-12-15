package com.lanka.tripplanner.core;

import java.util.Iterator;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		CoreMain aCoreMain = new CoreMain();
		LinkedList<Location> road = aCoreMain.searchPath(5, 79.999413,
				7.089895, "History", "North-Central");
		Iterator<Location> ittr = road.iterator();
		String path = "-";
		while (ittr.hasNext()) {
			Location roadMark = ittr.next();

			path = path + roadMark.getName() + "-";

			// if a day ends print ***
			if (roadMark.isDestinationForDay()) {
				path = path + " *** ";
			}
		}

		System.out.println(path);
	}

}
