package edu.curtin.saed.models;

import java.util.List;

public class Item
{
    // class variables
    private String name;
    private List<Location> locations;
    private String message;

    // constructor
    public Item(String name, List<Location> locations, String message)
    {
        this.name = name;
        this.locations = locations;
        this.message = message;
    }

    // getters
    // gets item name
    public String getName()
    {
        return name;
    }

    // gets item locations
    public List<Location> getLocations()
    {
        return this.locations;
    }

    // gets item's message
    public String getMessage()
    {
        return message;
    }

    // setters
    // sets item's name
    public void setName(String itemName)
    {
        this.name = itemName;
    }

    // sets item's message
    public void setMessage(String itemMessage)
    {
        this.message = itemMessage;
    }

    // sets items locations
    public void setLocations(List<Location> itemLocations)
    {
        this.locations = itemLocations;
    }

    // adds an item location
    public void addLocation(Location itemLocation)
    {
        this.locations.add(itemLocation);
    }

    // remove a specific location from the locaiton list
    public void removeLocation(Location itemLocation)
    {
        locations.remove(itemLocation);
    }

}
