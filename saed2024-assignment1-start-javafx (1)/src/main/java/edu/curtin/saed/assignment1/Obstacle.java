package edu.curtin.saed.assignment1;

import java.util.List;

public class Obstacle
{
    private List<Item> itemsRequired;
    private List<Location> locations;

    public Obstacle(List<Location> locList, List<Item> requiredItems)
    {
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

    // setters
    public void setRequiredItems(List<Item> requiredItems)
    {
        this.itemsRequired = requiredItems; 
    }

    public void setLocations(List<Location> obstacleLocations)
    {
        this.locations = obstacleLocations;
    }

    public void addLocation(Location location)
    {
        this.locations.add(location);
    }

    // TO DO: logic to find and remove location

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Obstacle at ").append(locations)
          .append(" requires: ");
        
        if (itemsRequired.isEmpty()) {
            sb.append("none");
        } else {
            for (Item item : itemsRequired) {
                sb.append(item.getName()).append(", ");
            }
            sb.setLength(sb.length() - 2); 
        }
        
        return sb.toString();
    }

}

