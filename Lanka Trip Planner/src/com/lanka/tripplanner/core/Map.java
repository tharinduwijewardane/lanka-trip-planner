package com.lanka.tripplanner.core;

import java.util.ArrayList;
import java.util.Iterator;

class Map {
	public Locale source;
	public Destination destination;

	public Map(Locale src, Destination dest) {
		source = src;
		destination = dest;
	}

	// this should be called to get the best path
	public ArrayList<Locale> getPath(String aCategory, String aProvince) {

		ArrayList<Locale> path = new ArrayList<Locale>(); // path - Places
		Locale temp = source; // start from source

		// for 'Any' type of category - receives null
		String[] categories;
		if (aCategory != null) {
			if (aCategory.contains(",")) {
				categories = aCategory.split(",");
			} else {
				categories = new String[1];
				categories[0] = aCategory;
			}
		} else {
			categories = new String[1];
			categories[0] = "All";
		}

		// sites in 'Any' area
		if (aProvince == null) {
			aProvince = "All";
		}

		int i = 1;
		while (temp != null) {

			// making sure path traveling time doesn't exceed the expected time
			if (temp.timeOfPath + temp.timeToDestination < destination.expectedTime) {

				// only visiting 14hrs for a day (14 * 60 = 840)
				// checks whether visiting ends for the day
				if (temp.timeOfPath > (840 * i)
						&& path.get(path.size() - 1) != null) {
					Locale dayDestination = path.get(path.size() - 1);
					path.remove(path.size() - 1);
					dayDestination.setDestinationForDay(true);
					path.add(dayDestination);
					i++;
				}
				// add Place to path
				path.add(temp);
				temp.alreadyInPath = true;
			} else {
				break;
			}

			// obtain next best location from the current location
			temp = getNextBestLoc(temp, categories, aProvince);
		}
		destination.timeOfPath = path.get(path.size() - 1).timeOfPath
				+ path.get(path.size() - 1).timeToDestination;
		destination.ratingOfPath = path.get(path.size() - 1).ratingOfPath;
		path.add(destination); // ading last node to the path
		return path;
	}

	// get next best Locale to travel from a Locale
	// out put may be null if a best Locale doesn't exist
	public Locale getNextBestLoc(Locale loc, String[] categories,
			String aProvince) {
		boolean isNotInCategory = true; // not in the required category

		// next best location to visit
		Locale bestLoc = null;

		//
		int bestHeuristic = destination.expectedTime;

		// considers every neighbour of current location
		Iterator<Neighbour> ittr = loc.getNeighbours();
		while (ittr.hasNext()) {
			Neighbour neighbour = ittr.next();

			// destination type is neglected
			if (neighbour.loc.type.equals("destination")) {
				continue;
			}

			// if the neighbour Locale is already in the path
			if (neighbour.loc.alreadyInPath) {
				continue;
			}

			// check whether the site is in the desired province
			if (!((Place) neighbour.loc).getProvince().equals(aProvince)
					&& !aProvince.equals("All")) {
				continue;
			}

			// check whether the site suits the desired category
			if (categories[0].equals("All")) {
				isNotInCategory = false;
			} else {
				for (int i = 0; i < categories.length; i++) {
					isNotInCategory = true;
					if (((Place) neighbour.loc).getCategory().equals(
							categories[i])) {
						isNotInCategory = false;
						break;
					}
				}
			}

			// if site is not in desired category
			if (isNotInCategory) {
				continue;
			}

			// time traveled if the neighbour location is added
			int timeTraveled = loc.timeOfPath + neighbour.timeToTravel
					+ neighbour.loc.timeToVisit;

			// time from source to destination if the neighbour location is
			// added
			int timeToDest = timeTraveled + neighbour.loc.timeToDestination;

			// if time to destination exceeds expected time
			if (timeToDest > destination.expectedTime) {
				continue;
			}

			// heuristic value
			// lowest huristic value is the best one here
			// assumption: 1. rating can be taken only integers 0,1,2,3,4,5
			int heuristicValue = (timeToDest) - (timeToDest - timeTraveled)
					* ((neighbour.loc.rating * 6) / 100);

			// if a better heuristic value is found
			if (heuristicValue < bestHeuristic
					|| (heuristicValue == bestHeuristic && timeTraveled < bestLoc.timeOfPath)) {
				bestHeuristic = heuristicValue;
				bestLoc = neighbour.loc;
				bestLoc.timeOfPath = timeTraveled;
				bestLoc.ratingOfPath = loc.ratingOfPath + bestLoc.rating;
				System.out.println("Selected best : " + neighbour.loc.name);
			}

		}

		return bestLoc;
	}
}
