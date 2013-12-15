package com.lanka.tripplanner.core;

//represents the destination (where the trip ends)
class Destination extends Locale {

    public int expectedTime; // the time allocated for the trip by user

    public Destination(int time) {
        super("destination");
        super.name = "destination";
        expectedTime = time;
        timeToDestination = 0;
    }

    public Destination(int time, double x, double y) {
        super("destination", x, y);
        super.name = "destination";
        expectedTime = time;
        timeToDestination = 0;
    }

    @Override
    public Integer getExpTime() {
        return expectedTime;
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
