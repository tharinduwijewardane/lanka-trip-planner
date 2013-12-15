package com.lanka.tripplanner.core;

class Neighbour {
	public int timeToTravel; // traveling time from a location
	public Locale loc; // neighbour location

	public Neighbour(Locale aLoc, int time) {
		loc = aLoc;
		timeToTravel = time;
	}
}
