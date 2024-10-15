package edu.curtin.saed.assignment1;

import java.util.List;

public class Player
{
    private Location location;
    private List<Item> inventory; 

    // constructor
    public Player(Location playerLocation)
    {
        this.location = playerLocation;
    }
    
    // setters
    public void setLocation(Location newLocation)
    {
        this.location = newLocation;
    }

    public void addItemToInventory(Item item)
    {
        inventory.add(item);
    }

    // Assumption: all items of the same name will be the same item (name is unqiue identifier)
    public void removeItemFromInventory(String itemName)
    {
        // if more than one of an item exists and you want to delete entire stock of that item, will need to loop through until none remain
        // TO DO: getItemToRemove(itemName); -> returns index of item you want to remove
        int index = 0; // placeholder until proper logic is set up
        inventory.remove(index);
    }
    // getters
    public Location getLocation()
    {
        return this.location;
    }
    
    public List<Item> getInventory()
    {
        return this.inventory;
    }
}

