package com.lanka.tripplanner.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//represents source location (where the trip starts)
public class Source extends Locale {

	Set<Neighbour> neighbours; // all other locations with time to travel

	// (not the physical neighbours
	// according to the google map)

	public Source() {
		super("source");
		super.name = "source";
		neighbours = new HashSet<Neighbour>();
	}

	public Source(double x, double y) {
		super("source", x, y);
		super.name = "source";
		neighbours = new HashSet<Neighbour>();
	}

	// returns all the neighbours
	@Override
	public Iterator<Neighbour> getNeighbours() {
		return neighbours.iterator();
	}

	@Override
	public void addNeighbour(Neighbour n) {
		neighbours.add(n);
		if (n.loc.type.equals("destination")) {
			timeToDestination = n.timeToTravel;
		}
	}

	@Override
	public String getName() {
		return name;
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
}
