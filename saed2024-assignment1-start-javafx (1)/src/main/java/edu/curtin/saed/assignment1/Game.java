package edu.curtin.saed.assignment1;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int gridWidth;
    private int gridHeight;
    private Location playerStartLocation;
    private Location goalLocation;
    private List<Item> items; 
    private List<Obstacle> obstacles; 
    private List<Script> scripts; 

    // Constructor
    public Game(Location playerLocation, Location goalLocation, List<Item> itemList, List<Obstacle> obstacleList) {
        this.gridWidth = 10; 
        this.gridHeight = 10; 
        this.playerStartLocation = playerLocation;
        this.goalLocation = goalLocation;
        this.items = (itemList != null) ? itemList : new ArrayList<>(); 
        this.obstacles = (obstacleList != null) ? obstacleList : new ArrayList<>(); 
        this.scripts = new ArrayList<>(); 
    }

    // getters
    public int getGridWidth() { 
        return this.gridWidth; 
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public int getGridHeight() {
        return this.gridHeight;
    }

    public Location getPlayerStartLocation() {
        return this.playerStartLocation;
    }

    public Location getGoalLocation() {
        return this.goalLocation;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public List<Script> getScripts() {
        return this.scripts;
    }

    // setters
    public void setPlayerStartLocation(Location playerLocation) {
        this.playerStartLocation = playerLocation;
    }

    public void setGoalLocation(Location goalLocation) {
        this.goalLocation = goalLocation;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public void setGridSize(int width, int height) {
        setGridWidth(width);
        setGridHeight(height);
    }

    public void addItem(Item item) {
        this.items.add(item); 
    }
    
    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle); 
    }

    public void addScript(Script script) {
        this.scripts.add(script); 
    }

    public void addPlugin(Plugin plugin) {
        // TODO: implement  
    }
}
