package edu.curtin.saed.assignment1;

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
    public void setName(String item_name)
    {
        this.name = item_name;
    }

    public void setMessage(String item_message)
    {
        this.message = item_message;
    }

    public void setLocations(List<Location> item_locations)
    {
        this.locations = item_locations;
    }

    public void addLocation(Location item_location)
    {
        this.locations.add(item_location);
    }

    // remove a specific location from the locaiton list
    public void removeLocation(Location item_location)
    {
        locations.remove(item_location);
    }

    @Override
    public String toString()
    {
        return "Item [name=" + name + ", locations=" + locations + ", message=" + message + "]";
    }
}
