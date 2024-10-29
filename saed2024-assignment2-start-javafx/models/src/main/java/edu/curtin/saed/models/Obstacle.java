package edu.curtin.saed.models;

import java.util.List;

public class Obstacle
{
    // class variables
    private List<Item> itemsRequired;
    private List<Location> locations;

    // constructor
    public Obstacle(List<Location> locList, List<Item> requiredItems)
    {
        this.itemsRequired = requiredItems;
        this.locations = locList;
    }
    
    // getters
    // gets items required to clear obstacle
    public List<Item> getRequiredItems()
    {
        return this.itemsRequired; 
    }

    // gets obstacle's locations
    public List<Location> getLocation()
    {
        return this.locations;
    }

    // setters
    // sets items required to clear obstacle
    public void setRequiredItems(List<Item> requiredItems)
    {
        this.itemsRequired = requiredItems; 
    }

    // sets obstacles locations
    public void setLocations(List<Location> obstacleLocations)
    {
        this.locations = obstacleLocations;
    }

    // adds a new location for the obstacle
    public void addLocation(Location location)
    {
        this.locations.add(location);
    }
}

