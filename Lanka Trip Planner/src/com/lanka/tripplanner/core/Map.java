package com.lanka.tripplanner.core;

import java.util.Iterator;
import java.util.LinkedList;

class Map {
	public Location source;
	public Destination destination;

	public Map(Location src, Destination dest) {
		source = src;
		destination = dest;
	}

	// this should be called to get the best path
	public LinkedList<Location> getPath(String aCategory, String aProvince) {

		LinkedList<Location> path = new LinkedList<Location>(); // path
		Location temp = source;
		int i = 1;
		while (temp != null) {

			// making sure path traveling time doesn't exceed the expected time
			if (temp.timeOfPath + temp.timeToDestination < destination.expectedTime) {

				// only visiting 14hrs for a day
				// checks whether visiting ends for the day
				if (temp.timeOfPath > (840 * i) && path.getLast() != null) {
					Location dayDestination = path.getLast();
					path.removeLast();
					dayDestination.setDestinationForDay(true);
					path.add(dayDestination);
					i++;
				}

				path.add(temp);
				temp.alreadyInPath = true;
			} else {
				break;
			}

			temp = getNextBestLoc(temp, aCategory, aProvince);
		}
		destination.timeOfPath = path.getLast().timeOfPath
				+ path.getLast().timeToDestination;
		destination.ratingOfPath = path.getLast().ratingOfPath;
		path.add(destination); // ading last node to the path
		return path;
	}

	// get next best location to travel from a location
	// out put may be null if a best location doesn't exsist
	public Location getNextBestLoc(Location loc, String aCategory,
			String aProvince) {
		boolean isNotInCategory = false;

		Location bestLoc = null;

		String[] categories;
		if (aCategory != null) {
			categories = aCategory.split(",");
		} else {
			categories = new String[1];
			categories[0] = "All";
		}

		if (aProvince == null) {
			aProvince = "All";
		}

		int bestHeuristic = destination.expectedTime;
		Iterator<Neighbour> ittr = loc.getNeighbours();
		while (ittr.hasNext()) {
			Neighbour neighbour = ittr.next();

			// destination type is neglected
			if (neighbour.loc.type.equals("destination")) {
				continue;
			}

			for (int i = 0; i > categories.length; i++) {
				if (neighbour.loc.type.equals("place")) {
					if (!((Place) neighbour.loc).getCategory().equals(
							categories[i])
							&& !categories[0].equals("All")) {
						isNotInCategory = true;
						break;
					}
				}
			}

			if (isNotInCategory) {
				continue;
			}

			if (!((Place) neighbour.loc).getProvince().equals(aProvince)
					&& !aProvince.equals("All")) {
				continue;
			}

			// if the neighbour location is already in the path
			if (neighbour.loc.alreadyInPath) {
				continue;
			}

			int timeTraveled = loc.timeOfPath + neighbour.timeToTravel
					+ neighbour.loc.timeToVisit; // time traveled if the
													// neighbour loc is added
			int timeToDest = timeTraveled + neighbour.loc.timeToDestination;
			// time from source to destination if the neighbour loc is added

			// if time to destination exceeds expected time
			if (timeToDest > destination.expectedTime) {
				continue;
			}

			// heuristic value
			// lowest huristic value is the best one here
			// assumption: 1. rating can be taken only integers 0,1,2,3,4,5
			// (timeToDest - timeTraveled) was the first part
			int heuristicValue = (timeToDest) // TIME TO TRAVEL MUST BE REMOVED
												// FOR A*
					- (timeToDest - timeTraveled)
					* ((neighbour.loc.rating * 6) / 100);

			// if a better heuristic value is found
			if (heuristicValue < bestHeuristic
					|| (heuristicValue == bestHeuristic && timeTraveled < bestLoc.timeOfPath)) {
				bestHeuristic = heuristicValue;
				bestLoc = neighbour.loc;
				bestLoc.timeOfPath = timeTraveled;
				bestLoc.ratingOfPath = loc.ratingOfPath + bestLoc.rating;
			}

		}

		return bestLoc;
	}
}
