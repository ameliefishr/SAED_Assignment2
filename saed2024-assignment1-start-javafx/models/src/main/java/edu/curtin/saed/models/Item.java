package edu.curtin.saed.models;

import java.util.List;

public class Item
{
    private String name;
    private List<Location> locations;
    private String message;

    public Item(String name, List<Location> locations, String message)
    {
        this.name = name;
        this.locations = locations;
        this.message = message;
    }

    // getters
    public String getName()
    {
        return name;
    }

    public List<Location> getLocations()
    {
        return this.locations;
    }

    public String getMessage()
    {
        return message;
    }

    // setters
    public void setName(String itemName)
    {
        this.name = itemName;
    }

    public void setMessage(String itemMessage)
    {
        this.message = itemMessage;
    }

    public void setLocations(List<Location> itemLocations)
    {
        this.locations = itemLocations;
    }

    public void addLocation(Location itemLocation)
    {
        this.locations.add(itemLocation);
    }

    // remove a specific location from the locaiton list
    public void removeLocation(Location itemLocation)
    {
        locations.remove(itemLocation);
    }

    @Override
    public String toString()
    {
        return "Item [name=" + name + ", locations=" + locations + ", message=" + message + "]";
    }
}
