package edu.curtin.saed.api;

import edu.curtin.saed.assignment1.*;

import java.util.List;
import java.util.Map;

public class GameAPI
{
    private final Game game;
    private ItemListener itemHandler;

    public GameAPI(Game game)
    {
        this.game = game;
    }

    public interface ItemListener
    {
        void onItemPickup(Item item);
    }

    public void registerItemListener(ItemListener handler)
    {
        System.out.print("API: registerItemListener" + handler);
        this.itemHandler = handler;
    }

    public List<Script> getScripts()
    {
        return game.getScripts();
    }

    public void itemPickup(Item item) {
        if (itemHandler != null)
        {
            System.out.print("API: itemPickup" + item);
            itemHandler.onItemPickup(item);
        }
    }

    public void toggleReveal(boolean reveal)
    {
        System.out.print("API: toggleReveal" + reveal);
        game.setCheatMode(reveal);
    }

    // Query the player's current location
    public Location getPlayerLocation() {
        return game.getPlayer().getLocation();
    }

    // Query the player's inventory
    public Map<String, Integer> getPlayerInventory() {
        return game.getPlayer().getInventory(); 
    }

    // Query the grid width
    public int getGridWidth() {
        return game.getGridWidth();
    }

    // Query the grid height
    public int getGridHeight() {
        return game.getGridHeight();
    }

}
