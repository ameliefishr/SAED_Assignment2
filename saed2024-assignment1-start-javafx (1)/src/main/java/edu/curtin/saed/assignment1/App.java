package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * JavaFX GUI application that integrates with the parser.
 */
public class App extends Application
{
    private TextArea textArea; 
    private static String fileName;
    private static Player player;

    public static void main(String[] args)
    {
        if (args.length > 0) {
            // TO DO: get file from cmd line args
            fileName = args[0]; 
        }
        launch(args);
    }
    
    @Override
public void start(Stage stage)
{
    // main display area
    parseConfigurationFile();
    Game gameInstance = MyParser.getGameInstance();

    int gridWidth = gameInstance.getGridWidth();
    int gridHeight = gameInstance.getGridHeight();
    player = gameInstance.getPlayer();
    System.out.println("Grid dimensions: " + gridHeight + " x " + gridWidth);

    GridArea area = new GridArea(gridWidth, gridHeight);
    area.setGridLines(false); 
    area.setStyle("-fx-background-color: #7db5db;");

    // adding player icon to grid at start coords
    GridAreaIcon playerIcon = new GridAreaIcon(
        player.getLocation().getX(),
        player.getLocation().getY(),
        0.0, 1.0,
        App.class.getClassLoader().getResourceAsStream("character.png"),
        "Player"
    );
    area.getIcons().add(playerIcon);

    // adding goal to grid
    GridAreaIcon goalIcon = new GridAreaIcon(
        gameInstance.getGoalLocation().getX(),
        gameInstance.getGoalLocation().getY(),
        0.0, 1.0,
        App.class.getClassLoader().getResourceAsStream("goal.png"),
        "Goal"
    );
    area.getIcons().add(goalIcon);


    // initializing items on the grid
    for (Item item : gameInstance.getItems())
    {
        for (Location itemLocation : item.getLocations())
        {
            String iconPath = "item.png";  // icon for default
            if (item.getName().equals("Wooden Sword"))
            {
                iconPath = "sword.png";  // if its a wooden sword use this icon
            }

            GridAreaIcon itemIcon = new GridAreaIcon(
                itemLocation.getX(),
                itemLocation.getY(),
                0.0, 1.0,
                App.class.getClassLoader().getResourceAsStream(iconPath), 
                item.getName()
            );
            area.getIcons().add(itemIcon); 
        }
    }

    // initializing obstacles on the grid
    for (Obstacle obstacle : gameInstance.getObstacles())
    {
        for (Location obstacleLocation : obstacle.getLocation())
        {
            GridAreaIcon obstacleIcon = new GridAreaIcon(
                obstacleLocation.getX(),
                obstacleLocation.getY(),
                0.0, 1.0,
                App.class.getClassLoader().getResourceAsStream("obstacle.png"), // icon for obstacles (rocks)
                "Obstacle"
            );
            area.getIcons().add(obstacleIcon); 
        }
    }

    // # BASIC GUI SETUP # //
    var inventoryBtn = new Button("Get Inventory"); // button to view inventory contents

    inventoryBtn.setOnAction((event) -> {
        System.out.println("Get inventory button pressed");
        viewInventory();
    });

    stage.setOnCloseRequest((event) -> {
        System.out.println("Close button pressed");
    });

    var statusText = new Label("Label Text");
    textArea = new TextArea(); 
    textArea.setEditable(false); 

    var toolbar = new ToolBar();
    toolbar.getItems().addAll(inventoryBtn, new Separator(), statusText);

    var splitPane = new SplitPane();
    splitPane.getItems().addAll(area, textArea);
    splitPane.setDividerPositions(0.75);

    var contentPane = new BorderPane();
    contentPane.setTop(toolbar);
    contentPane.setCenter(splitPane);

    var scene = new Scene(contentPane, 1200, 1000);
    area.requestFocus();

    // movement via W A S D keys
    // note: i originally tried using arrow keys but kept running into issues with losing focus so had to switch to WASD
    scene.setOnKeyPressed((KeyEvent event) ->
    {
        Location currentLocation = player.getLocation();
        int x = currentLocation.getX();
        int y = currentLocation.getY();
    
        // store original coords as temp variables
        int tempX = x;
        int tempY = y;
    
        // movement logic: adapted from a similar maze game I made in UCP
        switch (event.getCode())
        {
            case W:  // W = move up
                if (y > 0)
                {
                    tempY--;  
                    System.out.println("moving up: y is now: " + tempY);
                }
                break;
            case S:  // S = move down
                if (y < gridHeight - 1)
                {
                    tempY++;  
                    System.out.println("moving down: y is now: " + tempY);
                }
                break;
            case A:  // A: move left
                if (x > 0)
                {
                    tempX--;  
                    System.out.println("moving left: x is now: " + tempX);
                }
                break;
            case D:  // D: move right
                if (x < gridWidth - 1)
                {
                    tempX++;  
                    System.out.println("moving right: x is now: " + tempX);
                }
                break;
            default:
                break;
        }
    
        // checking if an obstacle occupies the player's desired location
        boolean canMove = true;
        Obstacle obstacleToRemove = null;
    
        // iterate all of the obstacles in the game & check their location against player's
        for (Obstacle obstacle : gameInstance.getObstacles())
        {
            for (Location obstacleLocation : obstacle.getLocation())
            {
                if (obstacleLocation.getX() == tempX && obstacleLocation.getY() == tempY)
                {
                    // if an obstacle is there, player cannot move there
                    canMove = false;
    
                    // get the item/s required for that obstacle
                    List<Item> requiredItems = obstacle.getRequiredItems();
                    boolean hasRequiredItem = false;
    
                    // iterate over the item/s required
                    for (Item requiredItem : requiredItems)
                    {
                        // check if the player's inventory contains the item required to clear the obstcle
                        for (String itemName : player.getInventory().keySet())
                        {
                            // if the user has the required item
                            if (itemName.equals(requiredItem.getName()))
                            {
                                hasRequiredItem = true;
                                obstacleToRemove = obstacle;  // get the obstacle ready to be removed (have to do outside of loop to avoid ConcurrentException)
    
                                // remove obstacle icon
                                area.getIcons().removeIf(icon -> 
                                    icon.getX() == obstacleLocation.getX() && 
                                    icon.getY() == obstacleLocation.getY() && 
                                    icon.getCaption().equals("Obstacle")
                                );

                                // update UI
                                Platform.runLater(() ->
                                {
                                    area.getIcons().removeIf(icon -> 
                                        icon.getX() == obstacleLocation.getX() && 
                                        icon.getY() == obstacleLocation.getY() && 
                                        icon.getCaption().equals("Obstacle")
                                    );
                                    area.requestLayout(); 
                                });

                                player.removeItemFromInventory(itemName);
                                textArea.appendText("Used " + requiredItem.getName() + " to clear obstacle.\n");
                                System.out.println("Used " + requiredItem.getName() + " to clear obstacle.");
                                
                                break;
                            }
                        }
                        if (hasRequiredItem)
                        {
                            break; // exit loop when item found
                        }
                    }
                    
                    // if player's inventpry does not include require item
                    if (!hasRequiredItem)
                    {
                        // tell user the path is blocked and what they need to clear it
                        textArea.appendText("You need " + requiredItems.stream().map(Item::getName).findFirst().get() + " to clear the obstacle.\n");
                        System.out.println("You need " + requiredItems.stream().map(Item::getName).findFirst().get() + " to clear the obstacle.");
                    }
                    
                    break; // exit loop
                }
            }
        }
    
        // if there is an obstacle to remove
        if (obstacleToRemove != null)
        {
            gameInstance.removeObstacle(obstacleToRemove); // remove it
            obstacleToRemove = null; // set back to null
        }
        
        // if obstacle is cleared and player can move
        if (canMove)
        {
            // move them to the desired location
            player.setLocation(new Location(tempX, tempY));
            playerIcon.setPosition(tempX, tempY);
            area.requestLayout(); // update UI
        }
    
        Item itemToRemove = null;
        Location locationToRemove = null;
        

        // check if player is in same grid as an item
        for (Item item : gameInstance.getItems())
        {
            // iterate through each item in the game
            for (Location itemLocation : item.getLocations()) {
                // if item is found at players location
                if (itemLocation.getX() == tempX && itemLocation.getY() == tempY)
                {
                    // add to inventory and rmeove from game
                    player.addItemToInventory(item);
                    textArea.appendText("Player picked up: " + item.getName() + "\n");
                    System.out.println("Player picked up: " + item.getName());
                    
                    // since items can have multiple locations you are only removing the item at that specific location
                    // stored to remove later to avoid ConcurrentEditException
                    itemToRemove = item;
                    locationToRemove = itemLocation;

                    GridAreaIcon itemIconToRemove = null;
                    for (GridAreaIcon icon : area.getIcons())
                    {
                        if (icon.getX() == tempX && icon.getY() == tempY && icon.getCaption().equals(item.getName()))
                        {
                            itemIconToRemove = icon;
                            break;
                        }
                    }
        
                    // remove item from grid
                    if (itemIconToRemove != null)
                    {
                        area.getIcons().remove(itemIconToRemove);
                    }
                    break; // exit loop
                }
            }
        }
        
        // remove item's location at that position, if there is only one location left then remove item object
        if (itemToRemove != null && locationToRemove != null)
        {
            itemToRemove.removeLocation(locationToRemove);
            if (itemToRemove.getLocations().isEmpty())
            {
                gameInstance.removeItem(itemToRemove);
            }
            
            itemToRemove = null;
            locationToRemove = null;
        }
        area.requestFocus(); // keep focus on grid for key input
    });
    

    stage.setScene(scene);
    stage.setTitle("Puzzle Game");
    stage.show();
    area.requestFocus();  
}

    // prints out inventories contents & item counts
    private void viewInventory()
    {
        textArea.appendText("\nInventory\n");
        Map<String, Integer> inventory = player.getInventory();

        for (Map.Entry<String, Integer> entry : inventory.entrySet())
        {
            textArea.appendText(" --> " + entry.getKey() + ": " + entry.getValue() + "\n"); // key = item name, value = item count
        }
    }

    // parse configuration file to parser to process 
    // TO DO: this will need to be called in main when receiving file from cmd line
    private void parseConfigurationFile()
    {
        String pathToFile = "input.dsl"; // hardcoded input file for testing 
        try (FileReader fileReader = new FileReader(pathToFile))
        {
            MyParser.parseFile(fileReader); 
            System.out.println("File parsed successfully! :D");
        } 
        catch (Exception e) // TO DO: more defined error handling this is just a placeholder
        {
            e.printStackTrace();
            System.out.println("Error parsing file: " + e.getMessage());
        }
    }
}
