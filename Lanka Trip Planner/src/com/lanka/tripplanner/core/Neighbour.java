package com.lanka.tripplanner.core;

class Neighbour {
	public int timeToTravel; // traveling time from a location
	public Location loc; // neighbour location

	public Neighbour(Location aLoc, int time) {
		loc = aLoc;
		timeToTravel = time;
	}
}
