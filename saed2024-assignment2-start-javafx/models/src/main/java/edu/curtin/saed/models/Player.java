package edu.curtin.saed.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Player
{
    // class variables
    private Location location;
    private Map<Item, Integer> inventory; 
    private List<Item> itemOrder; // was using linked hashmap before to keep track of item order 
                                  // but got pmd violations so switch to using a separte list to track

    // constructor
    public Player(Location location)
    {
        this.location = location;
        this.inventory = new HashMap<>();
        this.itemOrder = new ArrayList<>();
    }

    // getters
    // gets players current location
    public Location getLocation()
    {
        return location;
    }

    // returns players inventory
    public Map<Item, Integer> getInventory()
    {
        return inventory;
    }

    // returns most recently added item to inventory
    public Item getMostRecentlyAddedItem() 
    {
        // if player currenty has no items
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
        return recentlyAdded; // return most recently obtained item
    }

    // see if player has specific item in inventory
    public boolean hasItem(String itemName)
    {
        for (Item item : inventory.keySet()) // check each item
        {
            if (item.getName().equals(itemName) && inventory.get(item) > 0) // check the name of each item against the spcified item
            {
                return true; // if item is found in players inventory
            }
        }
        return false; // if player does not have item
    }
    
    // setters
    // sets players current location
    public void setLocation(Location location)
    {
        this.location = location;
    }

    // add an item to the player's inventory OR increment that items count if it already exists in inventory
    public void addItemToInventory(Item item)
    {
        inventory.put(item, inventory.getOrDefault(item, 0) + 1);
        itemOrder.add(item); // list to keep track the order of which items are added
    }

    // removes item from inventory (i.e. after it was used to clear an obstacle)
    public void removeItemFromInventory(Item item)
    {
        if (inventory.containsKey(item)) // if item to remove exists in inventory
        {
            int count = inventory.get(item);
            if (count > 1) // if player has more than 1 of these items
            {
                inventory.put(item, count - 1); // simply decrease the amount of this item by 1
            }
            else // if player only has 1 of this item
            {
                inventory.remove(item); // remove the item entirely
                itemOrder.remove(item); // update item order
            }
        }
    }

}
