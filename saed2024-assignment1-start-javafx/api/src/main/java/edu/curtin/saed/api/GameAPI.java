package edu.curtin.saed.api;

import edu.curtin.saed.models.Item; 
import edu.curtin.saed.models.Location; 
import edu.curtin.saed.models.Script; 

import java.util.List;
import java.util.Map;

public class GameAPI
{
    private final Game game;
    private ItemListener itemHandler;
    private MovementListener movementHandler;
    private MenuListener menuHandler;

    public GameAPI(Game game)
    {
        this.game = game;
    }

    // listener for item pickups
    public interface ItemListener
    {
        void onItemPickup(Item item);
    }

    // listener for player movements
    public interface MovementListener
    {
        void onMovement(String direction);
    }

    // listener for menu 
    public interface MenuListener
    {
        void onMenuAction(String action);
    }

    // register method for item listener
    public void registerItemListener(ItemListener handler)
    {
        System.out.print("API: registerItemListener" + handler);
        this.itemHandler = handler;
    }

    // register method for player moves listener
    public void registerPlayerMoveListener(MovementListener handler) 
    {
        System.out.print("API: registerPlayerMoveListener " + handler);
        this.movementHandler = handler;
    }

    // register method for menu actions listener
    public void registerMenuActionListener(MenuListener handler)
    {
        System.out.print("API: registerMenuActionListener " + handler);
        this.menuHandler = handler;
    }

    public List<Script> getScripts()
    {
        return game.getScripts();
    }

    // method to trigger on item pickup and pass item to the handler
    public void itemPickup(Item item)
    {
        if (itemHandler != null)
        {
            System.out.print("API: itemPickup" + item);
            itemHandler.onItemPickup(item);
        }
    }

    // method to trigger whenevr player moves and pass the direction to the handler
    public void playerMoved(String direction)
    {
        if (movementHandler != null) 
        {

            System.out.print("API: playerMove " + direction);
            movementHandler.onMovement(direction);
        }
    }

    // method to trigger whenevr player moves and pass the direction to the handler
    public void menuAction(String action)
    {
        if (menuHandler != null) {
            System.out.print("API: menuAction " + action);
            menuHandler.onMenuAction(action);
        }
    }

    // togggles visibility of the grid map
    public void toggleReveal(boolean reveal)
    {
        System.out.print("API: toggleReveal" + reveal);
        game.setCheatMode(reveal);
    }

    // gets teh players current location
    public Location getPlayerLocation()
    {
        return game.getPlayer().getLocation();
    }

    // gets players inventory
    public Map<Item, Integer> getPlayerInventory()
    {
        return game.getPlayer().getInventory(); 
    }

    // gets the most recently added inventory item
    public Item getMostRecentItem()
    {
        return game.getPlayer().getMostRecentlyAddedItem();
    }

    // gets size of grid in (int width, int height) format
    public int[] getGridSize()
    {
        return game.getGridSize();
    }

    
    // gets size of grid in (int width, int height) format
    public void addMenuButton(String buttonName)
    {
        game.addAppMenuButton(buttonName);
    }

}
