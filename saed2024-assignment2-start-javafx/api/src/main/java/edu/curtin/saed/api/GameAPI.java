package edu.curtin.saed.api;

import edu.curtin.saed.models.Item; 
import edu.curtin.saed.models.Script; 

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;


// API for scripts/plugins to communicate with
public class GameAPI
{
    private final Game game;
    private final List<ItemListener> itemHandlers; // list for itemPickup because multiple listeners are used (reveal script & prize plugin)
    private MovementListener movementHandler;
    private MenuListener menuHandler;
    private ObstacleTraversalListener obstacleTraversalHandler;

    public GameAPI(Game game)
    {
        this.game = game;
        this.itemHandlers = new ArrayList<>();
    }

    // listener for item pickups
    public interface ItemListener
    {
        void onItemPickup(String itemName);
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

    // listener for obstacle traversal
    public interface ObstacleTraversalListener
    {
        void onObstacleTraversed();
    }

    // register method for item listener
    public void registerItemListener(ItemListener handler)
    {
        this.itemHandlers.add(handler); 
    }

    // register method for player moves listener
    public void registerPlayerMoveListener(MovementListener handler) 
    {
        this.movementHandler = handler;
    }

    // register method for menu actions listener
    public void registerMenuActionListener(MenuListener handler)
    {
        this.menuHandler = handler;
    }

    // register method for obstacle traversal listener
    public void registerObstacleTraversalListener(ObstacleTraversalListener handler)
    {
        this.obstacleTraversalHandler = handler; 
    }

    // gets all of the scripts in game
    public List<Script> getScripts()
    {
        return game.getScripts();
    }

    // method to trigger on item pickup and pass item to the handler
    public void itemPickup(String itemName)
    {
        for (ItemListener handler : itemHandlers)
        {
            handler.onItemPickup(itemName);
        }
    }

    // method to trigger whenevr player moves and pass the direction to the handler
    public void playerMoved(String direction)
    {
        if (movementHandler != null) 
        {

            movementHandler.onMovement(direction);
        }
    }

    // method to trigger whenevr player moves and pass the direction to the handler
    public void menuAction(String action)
    {
        if (menuHandler != null)
        {
            menuHandler.onMenuAction(action);
        }
    }

    // method to trigger whenever player traverses an obstacle
    public void obstacleTraversed()
    {
        if (obstacleTraversalHandler != null)
        {
            obstacleTraversalHandler.onObstacleTraversed(); 
        }
    }

    // togggles visibility of the grid map
    public void toggleReveal(boolean reveal)
    {
        game.setCheatMode(reveal);
    }

    // gets teh players current location
    // using ints so plugins dont have to depend on location class
    public int[] getPlayerLocation()
    {
        int playerLocationX = game.getPlayerLocation().getX();
        int playerLocationY = game.getPlayerLocation().getY();
        
        return new int[]{playerLocationX, playerLocationY};
    }

    // setting player location
    public void setPlayerLocation(int x, int y)
    {
        game.getPlayerLocation().setX(x);
        game.getPlayerLocation().setY(y);
    }

    // gets players inventory
    public Map<Item, Integer> getPlayerInventory()
    {
        return game.getPlayer().getInventory(); 
    }

    // gets the most recently added inventory item
    // using string so plugins dont have to depend on item class
    public String getMostRecentItem()
    {
        return game.getPlayer().getMostRecentlyAddedItem().toString();
    }

    // gets size of grid in (int width, int height) format
    public int[] getGridSize()
    {
        return game.getGridSize();
    }
    
    // gets size of grid in (int width, int height) format
    public void addMenuButton(String buttonName, EventHandler<ActionEvent> onClickEvent)
    {
        game.addAppMenuButton(buttonName, onClickEvent);  
    }

    // refreshes players UI icon and updated it to the new position
    public void refreshPlayerUI(int newX, int newY)
    {
        game.appUpdatePlayerUI();
    }

    // gets the players current movement direction
    public String getPlayerDirection()
    {
        return game.getMovementDirection();
    }

    // allows plugins to add obstacles to the UI
    public void addObstacle(String obstacleName, int obstacleX, int obstacleY)
    {
        game.addPluginObstacle(obstacleName, obstacleX, obstacleY);
    }

    // allows plugisn to add items to the UI
    public void addItem(String itemName, String itemMessage)
    {
        game.addPluginItem(itemName, itemMessage);
    }

    // allows plugisn to add items to the UI
    public void printMessage(String message)
    {
        game.pluginPrintToUI(message);
    }
}
