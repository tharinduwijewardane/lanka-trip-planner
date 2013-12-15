package com.lanka.tripplanner.core;

import java.util.Iterator;

public class Locale{
	public double x, y; // x,y coordinates
	public String type; // Eg: source, destination, place
	public String name;
	public String province;
	public int rating;
	public int ratingOfPath; // needed when path is calculated
	public int timeToVisit; // amount of time a visitor spends in the location
	public int timeOfPath; // needed when path is calculated
	public int timeToDestination; // minimum time from this location to
									// destination
	public Boolean alreadyInPath; // needed when path is calculated
	public Boolean destinationForDay; // last location visiting within the day

	public Locale(String aType, double xCor, double yCor, String aProvince) {
		type = aType;
		x = xCor;
		y = yCor;
		rating = 0;
		ratingOfPath = 0;
		timeToVisit = 0;
		timeOfPath = 0;
		province = aProvince;
		alreadyInPath = false;
		destinationForDay = false;
	}

	public Locale(String aType, double xCor, double yCor) {
		type = aType;
		x = xCor;
		y = yCor;
		rating = 0;
		ratingOfPath = 0;
		timeToVisit = 0;
		timeOfPath = 0;
		province = "None";
		alreadyInPath = false;
		destinationForDay = false;
	}

	public Locale(String aType) {
		type = aType;
		rating = 0;
		ratingOfPath = 0;
		timeToVisit = 0;
		timeOfPath = 0;
		province = "None";
		alreadyInPath = false;
		destinationForDay = false;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Boolean isDestinationForDay() {
		return false;
	}

	public void setDestinationForDay(Boolean destinationForDay) {
	}

	// functions to implement polimorphism
	public Iterator<Neighbour> getNeighbours() {
		return null;
	}

	public Integer getTimeTraveled() {
		return null;
	}

	public Integer getExpTime() {
		return null;
	}

	public Integer getTimeToVisit() {
		return null;
	}

	public Integer getRating() {
		return null;
	}

	public Integer getPathRating() {
		return null;
	}

	public String getName() {
		return null;
	}

	public double getLati() {
		return x;
	}

	public double getLongi() {
		return y;
	}

	public void addNeighbour(Neighbour n) {
	}

}
