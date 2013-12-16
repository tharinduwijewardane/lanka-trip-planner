package com.lanka.tripplanner.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//represents places on the way
public class Place extends Locale {

	public Set<Neighbour> neighbours;
	public String category;

	public Place(int aRating, int aTimetoVisit, String name, String aCategory) {
		super("place"); // type is "place"
		super.name = name;
		rating = aRating;
		timeToVisit = aTimetoVisit;
		neighbours = new HashSet<Neighbour>();
		category = aCategory;
	}

	public Place(int aRating, int aTimetoVisit, String name, double x,
			double y, String aCategory, String aProvince) {
		super("place", x, y, aProvince); // type is "place"
		super.name = name;
		rating = aRating;
		timeToVisit = aTimetoVisit;
		neighbours = new HashSet<Neighbour>();
		category = aCategory;
	}

	public Boolean isDestinationForDay() {
		return destinationForDay;
	}

	public void setDestinationForDay(Boolean destinationForDay) {
		this.destinationForDay = destinationForDay;
	}

	@Override
	public void addNeighbour(Neighbour n) {
		neighbours.add(n);
		if (n.loc.type.equals("destination")) {
			timeToDestination = n.timeToTravel;
		}
	}

	@Override
	public Iterator<Neighbour> getNeighbours() {
		return neighbours.iterator();
	}

	@Override
	public String getName() {
		return name;
	}

	public double[] getCoordinates() {
		double[] coordinates = { super.x, super.y };
		return coordinates;
	}

	@Override
	public double getLati() {
		// TODO Auto-generated method stub
		return super.getLati();
	}

	@Override
	public double getLongi() {
		// TODO Auto-generated method stub
		return super.getLongi();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getTimeToVisit() {
		return timeToVisit;
	}

	public Integer getRating() {
		return rating;
	}
}
