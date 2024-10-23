package edu.curtin.saed.assignment1;

import java.util.HashMap;
import java.util.Map;

public class Player
{
    private Location location;
    private Map<String, Integer> inventory = new HashMap<>();

    // constructor
    public Player(Location location)
    {
        this.location = location;
        this.inventory = new HashMap<>();
    }

    // getters
    public Location getLocation()
    {
        return location;
    }

    public Map<String, Integer> getInventory()
    {
        return inventory;
    }

    // see if player has specific item in inventory
    public boolean hasItem(String itemName)
    {
        return inventory.containsKey(itemName) && inventory.get(itemName) > 0;
    }

    // setters
    public void setLocation(Location location)
    {
        this.location = location;
    }

    // add an item to the player's inventory OR increment that items count if it already exists in inventory
    public void addItemToInventory(Item item)
    {
        inventory.put(item.getName(), inventory.getOrDefault(item.getName(), 0) + 1);
    }

    public void removeItemFromInventory(String itemName)
    {
        if (inventory.containsKey(itemName))
        {
            int count = inventory.get(itemName);
            if (count > 1)
            {
                inventory.put(itemName, count - 1);
            }
            else
            {
                inventory.remove(itemName);
            }
        }
    }

}
