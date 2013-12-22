package com.lanka.tripplanner.core;

import java.util.Iterator;

public class Locale {

	double lati; // x coordinate
	double longi;// y coordinate
	String type; // Eg: source, destination, place
	String name;
	String province;
	int rating;
	int ratingOfPath; // needed when path is calculated
	int timeToVisit; // amount of time a visitor spends in the location
	int timeOfPath; // needed when path is calculated
	int timeToDestination; // minimum time from this location to
							// destination
	Boolean alreadyInPath; // needed when path is calculated
	Boolean destinationForDay; // last location visiting within the day

	public Locale(String aType, double xCor, double yCor, String aProvince) {
		type = aType;
		lati = xCor;
		longi = yCor;
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
		lati = xCor;
		longi = yCor;
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
		return lati;
	}

	public double getLongi() {
		return longi;
	}

	public void addNeighbour(Neighbour n) {
	}

}
