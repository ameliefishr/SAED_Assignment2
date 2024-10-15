package edu.curtin.saed.assignment1;

import java.util.List;

public class Obstacle
{
    private String name;
    private List<Item> itemsRequired;
    private List<Location> locations;

    public Obstacle(List<Location> locList, String name, List<Item> requiredItems)
    {
        this.name = name;
        this.itemsRequired = requiredItems;
        this.locations = locList;
    }
    
    // getters
    public List<Item> getRequiredItems()
    {
        return this.itemsRequired; 
    }

    public List<Location> getLocation()
    {
        return this.locations;
    }

    public String getObstacleName()
    {
        return this.name;
    }

    // setters
    public void setRequiredItems(List<Item> required_items)
    {
        this.itemsRequired = required_items; 
    }

    public void setLocations(List<Location> obstacle_locations)
    {
        this.locations = obstacle_locations;
    }

    public String setObstacleName(String obstacle_name)
    {
        return this.name;
    }

    public void addLocation(Location location)
    {
        this.locations.add(location);
    }

    // TO DO: logic to find and remove location

}

