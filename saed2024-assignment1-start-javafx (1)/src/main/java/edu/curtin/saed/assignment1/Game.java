package edu.curtin.saed.assignment1;

import java.util.ArrayList;
import java.util.List;
import edu.curtin.saed.api.*;

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
    }

    public void initializeAPI()
    {
        this.api = new GameAPI(this); 
        this.api.registerItemListener(new GameEventHandler());
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

    public List<Script> getScripts()
    {
        return this.scripts;
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
        //System.out.println("setCheatMode called: " + mode);
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
    
    public void removeItem(Item item)
    {
        this.items.remove(item);
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

    // manages the player's visibility
    public void revealArea(Location playerLocation, GridArea area)
    {
        int x = playerLocation.getX();
        int y = playerLocation.getY();
        
        // 2d array to store the area of visibility surrounding plater
        double[][] squaresToReveal = new double[][]
        {
            {x, y},         //player icon
            {x - 1, y},     // left square
            {x + 1, y},     // right right
            {x, y - 1},     // up square
            {x, y + 1},     // down square
            {x - 1, y - 1}, // top-left square
            {x + 1, y - 1}, // top-right square
            {x - 1, y + 1}, // bottom-left square 
            {x + 1, y + 1}  // bottom-right squ8are
        };

        // pass on array for processing 
        proccessAreaArray(squaresToReveal, area);
    }
    
    private void proccessAreaArray(double[][] squaresToReveal, GridArea area)
    {
        // iterate over each item in the grid
        area.getIcons().forEach(icon ->
        {
            // get their coordinates
            double getX = icon.getX();
            double getY = icon.getY();
            
            // bool to check whether icon should be revealed
            boolean reveal = false;

            //System.out.println("cheat mode not activated, reveal is still false");

            if(cheatMode)
            {
                reveal = true;
                //System.out.println("cheat mode activated, reveal set to true");
            }
            
            // iterate over reveal array and check whether the current icon's coords match any coords in reveal array
            for (double[] coords : squaresToReveal)
            {
                // if a match is found
                if (coords[0] == getX && coords[1] == getY)
                {
                    reveal = true; // reveal = true for this icon
                    break;
                }
            }
            
            // reveal the icon
            if (reveal)
            {
                if (icon.getCaption().isEmpty())
                {
                    icon.setShown(false);  // if it's one of the "hidden" icons, you want to do the opposite and make it invis
                } 
                else
                {
                    icon.setShown(true);   // if it's a normal icon then make it visible
                }
            }
            else // if it's not within players visibility
            {
                if (icon.getCaption().isEmpty())
                {
                    icon.setShown(true);  // if it's a "hidden" icon then switch it back to being shown
                }
                else
                {
                    icon.setShown(false);   // hide all other icons
                }
            }
        });
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
            //System.out.println("EVENT HANDLER: Item picked up: " + item);
            scriptHandler.loadScript(item);
        }
    }
    
}
