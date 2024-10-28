package edu.curtin.saed.api;

import java.util.ArrayList;
import java.util.List;
import edu.curtin.saed.models.*;

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
    public Game(Location playerLocation, Location goalLocation, List<Item> itemList, List<Obstacle> obstacleList)
    {
        this.gridWidth = 10; // default size if none is given
        this.gridHeight = 10;
        this.playerStartLocation = playerLocation;
        this.goalLocation = goalLocation;
        this.items = (itemList != null) ? itemList : new ArrayList<>();
        this.obstacles = (obstacleList != null) ? obstacleList : new ArrayList<>();
        this.scripts = new ArrayList<>();
        this.plugins = new ArrayList<>();
        this.player = new Player(playerLocation);
        this.cheatMode = false;
        this.movementDirection = "Idle";
    }

    public void initializeAPI()
    {
        this.api = new GameAPI(this); 
        this.api.registerItemListener(new GameEventHandler());
    }

    public void passAppInstance(AppInterface appInstance)
    {
        this.app = appInstance;
    }

    // getters
    public int getGridWidth()
    { 
        return this.gridWidth; 
    }

    public boolean getCheatMode()
    {
        return this.cheatMode;
    }

    public List<Obstacle> getObstacles()
    {
        return this.obstacles;
    }

    public int getGridHeight()
    {
        return this.gridHeight;
    }

    public Location getPlayerStartLocation()
    {
        return this.playerStartLocation;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Location getGoalLocation()
    {
        return this.goalLocation;
    }

    public List<Item> getItems()
    {
        return this.items;
    }

    public String getMovementDirection()
    {
        return this.movementDirection;
    }

    public List<Script> getScripts()
    {
        return this.scripts;
    }

    public int[] getGridSize()
    {
        int[] gridSize = new int[] {this.gridWidth, this.gridHeight}; //to access -> int[0] = grid width & int[1] = grid height
        return gridSize;
    }

    public ScriptHandler initializeScriptHandler()
    {
        this.scriptHandler = new ScriptHandler(api); 
        return scriptHandler;
    }

    public GameAPI getAPI()
    {
        return this.api;
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

    public void setPlayerLocation(Location playerLocation)
    {
        this.player.setLocation(playerLocation); 
    }

    public void setGoalLocation(Location goalLocation)
    {
        this.goalLocation = goalLocation;
    }

    public void setCheatMode(boolean mode)
    {
        this.cheatMode = mode;
    }

    public void setGridWidth(int gridWidth)
    {
        this.gridWidth = gridWidth;
    }

    public void setGridHeight(int gridHeight)
    {
        this.gridHeight = gridHeight;
    }

    public void setGridSize(int width, int height)
    {
        setGridWidth(width);
        setGridHeight(height);
    }

    public void addItem(Item item)
    {
        this.items.add(item); 
    }

    public void setMovementDirection(String direction)
    {
        this.movementDirection = direction;
    }
    
    public void removeItem(Item item)
    {
        this.items.remove(item);
    }

    // so plugins interact with the application without directly requiring application access
    public void addAppMenuButton(String buttonName)
    {
        app.addMenuButton(buttonName);
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

    public void removeObstacle(Obstacle obstacle)
    {
        this.obstacles.remove(obstacle);
    }

    public void addObstacle(Obstacle obstacle)
    {
        this.obstacles.add(obstacle); 
    }

    public void addScript(Script script)
    {
        this.scripts.add(script); 
    }

    public void addPlugin(Plugin plugin)
    {
        this.plugins.add(plugin); 
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Game State:\n");
        sb.append("Grid Size: ").append(gridWidth).append(" x ").append(gridHeight).append("\n");
        sb.append("Player Start Location: ").append(playerStartLocation).append("\n");
        sb.append("Goal Location: ").append(goalLocation).append("\n");
        sb.append("Items:\n");
        for (Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Obstacles:\n");
        for (Obstacle obstacle : obstacles) {
            sb.append(obstacle.toString()).append("\n");
        }
        sb.append("Plugins:\n");
        for (Plugin plugin : plugins) {
            sb.append(plugin.toString()).append("\n");
        }
        sb.append("Scripts:\n");
        for (Script script : scripts) {
            sb.append(script.toString()).append("\n");
        }
        return sb.toString();
    }

    public class GameEventHandler implements GameAPI.ItemListener {
        @Override
        public void onItemPickup(Item item) {
            scriptHandler.loadScript(item);
        }
    }
    
}
