package edu.curtin.saed.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import edu.curtin.saed.models.*;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Game
{
    private int gridWidth;
    private int gridHeight;
    private Location playerStartLocation;
    private Location goalLocation;
    private List<Item> items; 
    private List<Obstacle> obstacles; 
    private List<Script> scripts; 
    private List<Plugin> plugins;
    private Player player;
    private GameAPI api;
    private ScriptHandler scriptHandler;
    private boolean cheatMode;
    private String movementDirection;
    private AppInterface app;

    // constructor
    public Game(Location playerLocation, Location goalLocation)
    {
        this.gridWidth = 10; // default size if none is given
        this.gridHeight = 10;
        this.playerStartLocation = playerLocation;
        this.goalLocation = goalLocation;
        this.scripts = new ArrayList<>();
        this.plugins = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.items = new ArrayList<>();
        this.player = new Player(playerLocation);
        this.cheatMode = false;
        this.movementDirection = "Idle"; // default direction to start

    }

    // initializes gameAPI with this game instance
    public void initializeAPI()
    {
        this.api = new GameAPI(this); 
        this.api.registerItemListener(new GameEventHandler());
    }

    // to allow communication with app without a circular dependency
    public void passAppInstance(AppInterface appInstance)
    {
        this.app = appInstance;
    }

    // getters
    // returns grid width
    public int getGridWidth()
    { 
        return this.gridWidth; 
    }

    // returns whether cheat mode is active or not
    public boolean getCheatMode()
    {
        return this.cheatMode;
    }

    // returns all obstacles in game
    public List<Obstacle> getObstacles()
    {
        return this.obstacles;
    }

    // returns grid height
    public int getGridHeight()
    {
        return this.gridHeight;
    }

    // returns player's start location
    public Location getPlayerStartLocation()
    {
        return this.playerStartLocation;
    }

    // returns player object
    public Player getPlayer()
    {
        return this.player;
    }

    // returns player location
    public Location getPlayerLocation()
    {
        return this.player.getLocation();
    }

    // returns goal location
    public Location getGoalLocation()
    {
        return this.goalLocation;
    }

    // returns all items in game
    public List<Item> getItems()
    {
        return this.items;
    }

    // method to return a random item 
    public Item getRandomItem() {
        if (this.items == null || this.items.isEmpty())
        {
            // no items exist to be returned
            return null; 
        }
    
        // so I can exclude maps since map's aren't intended to be used to clear obstacles
        List<Item> validItems = new ArrayList<>();
    
        for (Item item : this.items) {
            if (!item.getName().equalsIgnoreCase("Map"))
            {
                validItems.add(item); // add any item that's not a map
            }
        }
    
        // if no items other than map exist in game
        if (validItems.isEmpty())
        {
            return null; 
        }
    
        Random random = new Random();
        int randomIndex = random.nextInt(validItems.size()); // get random item from the validItems list
        return validItems.get(randomIndex); // return the random balid item
    }

    // returns movement direction
    public String getMovementDirection()
    {
        return this.movementDirection;
    }

    // returns all scripts
    public List<Script> getScripts()
    {
        return this.scripts;
    }

    // gets grid size in an int array so can be used by plugins 
    public int[] getGridSize()
    {
        int[] gridSize = new int[] {this.gridWidth, this.gridHeight}; //to access -> int[0] = grid width & int[1] = grid height
        return gridSize;
    }

    // initializes script handle with api
    public ScriptHandler initializeScriptHandler()
    {
        this.scriptHandler = new ScriptHandler(api); 
        return scriptHandler;
    }

    // gets api instance
    public GameAPI getAPI()
    {
        return this.api;
    }

    // updates player icon on the app
    public void appUpdatePlayerUI()
    {
        app.updatePlayerIcon();
    }

    // getContents method for API
    // returns whatever object is found, i.e. could be player, goal, item or obstacle
    // when accessing will need to use instanceof to determine type
    public Object getContentsAtLocation(Location location)
    {
        // check if player object is at specified location
        if (player.getLocation().equals(location))
        {
            return player; 
        }
    
        // check if player object is at specified location
        for (Item item : items)
        {
            if (item.getLocations().contains(location))
            {
                return item; 
            }
        }
    
        // check if player object is at specified location
        for (Obstacle obstacle : obstacles)
        {
            if (obstacle.getLocation().equals(location))
            { 
                return obstacle; 
            }
        }
    
        // check if player object is at specified location
        if (goalLocation.equals(location))
        {
            return goalLocation; 
        }
    
        return null; // if no object is found at location return null
    }
    

    // setters
    public void setPlayerStartLocation(Location playerLocation)
    {
        this.playerStartLocation = playerLocation;
    }

    // set's players current location
    public void setPlayerLocation(Location playerLocation)
    {
        this.player.setLocation(playerLocation); 
    }

    // sets goal location
    public void setGoalLocation(Location goalLocation)
    {
        this.goalLocation = goalLocation;
    }

    // sets whether cheatmode is active or not
    public void setCheatMode(boolean mode)
    {
        this.cheatMode = mode;
    }

    // sets grid width
    public void setGridWidth(int gridWidth)
    {
        this.gridWidth = gridWidth;
    }

    // sets grid height
    public void setGridHeight(int gridHeight)
    {
        this.gridHeight = gridHeight;
    }

    // sets grid size
    public void setGridSize(int width, int height)
    {
        setGridWidth(width);
        setGridHeight(height);
    }

    // adds item to game
    public void addItem(Item item)
    {
        this.items.add(item); 
    }

    // sets movement direction of player
    public void setMovementDirection(String direction)
    {
        this.movementDirection = direction;
    }
    
    // removes items from game
    public void removeItem(Item item)
    {
        this.items.remove(item);
    }

    // so plugins interact with the application without directly requiring application access
    // adds a new menu item to the app's UI
    public void addAppMenuButton(String buttonName, EventHandler<ActionEvent> onClickEvent)
    {
        app.addMenuButton(buttonName, onClickEvent); 
    }

    // adds a new obstacle to the app's UI as well and adding it to the game's obstacle list with a random required item
    public void addPluginObstacle(String obstacleName, int obstacleX, int obstacleY)
    {
        app.addObstacle(obstacleName, obstacleX, obstacleY); 

        List<Location> tempLocList = new ArrayList<>(); // sets obstacles location to the specified position
        tempLocList.add(new Location(obstacleX, obstacleY));

        List<Item> tempRequiredItems = new ArrayList<>();
        tempRequiredItems.add(getRandomItem()); // gets random required item from list of available items to ensure the obstacle can still be beaten

        Obstacle newObstacle = new Obstacle(tempLocList, tempRequiredItems);
        addObstacle(newObstacle); // add to game
    }

    // adds a new item to the game, does not add to UI, just to player's inventory
    public void addPluginItem(String itemName, String message)
    {
        List<Location> tempLocList = new ArrayList<>();
        tempLocList.add(new Location(-1, -1));

        Item pluginItem = new Item(itemName, tempLocList, message);
        getPlayer().addItemToInventory(pluginItem);
    }

    // remove item's location at that position, if there is only one location left then remove item object
    // kept re-using this code so made it a method
    public void removeItemLocation(Item item, Location location)
    {
        if(item != null && location != null)
        {
            item.removeLocation(location);
            if(item.getLocations().isEmpty())
            {
                removeItem(item);
            }
        }
    }

    // adds a way for plugins to print messages to the UI without direct interaction with app
    public void pluginPrintToUI(String message)
    {
        app.printToUI(message);
    }

    // removes an obstacle from the game
    public void removeObstacle(Obstacle obstacle)
    {
        this.obstacles.remove(obstacle);
    }

    // adds an obstacle to the game
    public void addObstacle(Obstacle obstacle)
    {
        this.obstacles.add(obstacle); 
    }

    // adds a script to the game
    public void addScript(Script script)
    {
        this.scripts.add(script); 
    }

    // adds a plugin to the game
    public void addPlugin(Plugin plugin)
    {
        this.plugins.add(plugin); 
    }

    // innerclass to handle itemPickup event and load the reveal script(since game handle the initialization of script handler)
    public class GameEventHandler implements GameAPI.ItemListener {
        @Override
        public void onItemPickup(String itemName) {
            scriptHandler.loadScript(itemName);
        }
    }
    
}
