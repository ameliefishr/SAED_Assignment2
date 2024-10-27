package edu.curtin.saed.assignment1;

import java.util.LinkedHashMap;

public class Player
{
    private Location location;
    private LinkedHashMap<Item, Integer> inventory; // switched to linked hashmap to easily get most recently added item

    // constructor
    public Player(Location location)
    {
        this.location = location;
        this.inventory = new LinkedHashMap<>();
    }

    // getters
    public Location getLocation()
    {
        return location;
    }

    // returns players inventory
    public LinkedHashMap<Item, Integer> getInventory()
    {
        return inventory;
    }

    // returns most recently added item to inventory
    public Item getMostRecentlyAddedItem() 
    {
        if (inventory.isEmpty())
        {
            return null; 
        }

        Item recentlyAdded = null;
        for (Item item : inventory.keySet()) // iterates through the whole inventory
        {
            recentlyAdded = item; // after iterations are complete the value of recently added will be the most recent item
                                  // (last item in the LinkedHashMap)
        }
        return recentlyAdded; 
    }

    // see if player has specific item in inventory
    public boolean hasItem(String itemName)
    {
        for (Item item : inventory.keySet())
        {
            if (item.getName() == itemName && inventory.get(item) > 0)
            {
                return true;
            }
        }
        return false; 
    }
    
    // setters
    public void setLocation(Location location)
    {
        this.location = location;
    }

    // add an item to the player's inventory OR increment that items count if it already exists in inventory
    public void addItemToInventory(Item item)
    {
        inventory.put(item, inventory.getOrDefault(item, 0) + 1);
    }

    // removes item from inventory (i.e. after it was used to clear an obstacle)
    public void removeItemFromInventory(Item item)
    {
        if (inventory.containsKey(item))
        {
            int count = inventory.get(item);
            if (count > 1)
            {
                inventory.put(item, count - 1);
            }
            else
            {
                inventory.remove(item);
            }
        }
    }

}
