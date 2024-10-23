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
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;

/**
 * JavaFX GUI application that integrates with the parser.
 */
public class App extends Application
{
    private TextArea textArea;
    private TextArea inventoryDisplay;
    private Button inventoryBtn;
    private Button changeLocaleBtn;
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
        area.setStyle("-fx-background-color:#905c34;"); // #7db5db


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
        goalIcon.setShown(false);
        area.getIcons().add(goalIcon);

        // lists to store item/locaiton to delete later to avoid ConcurrentException
        List<Item> itemsToRemove = new ArrayList<>();
        List<Location> locationsToRemove = new ArrayList<>();

        // initializing items on the grid
        for (Item item : gameInstance.getItems())
        {
            for (Location itemLocation : item.getLocations())
            {
                // check to ensure only one item can occupy square, if an item alraady exists there skip adding this item (print error)
                if (area.isOccupied(itemLocation.getX(), itemLocation.getY()))
                {
                    System.out.println("Skipped adding item " + item.getName() + " at (" + itemLocation.getX() + ", " + itemLocation.getY() + ") due to square being already occupied.");
                    itemsToRemove.add(item);
                    locationsToRemove.add(itemLocation);
                    continue; 
                }
        
                String iconPath = "item.png";  // default icon
                if (item.getName().equals("Wooden Sword"))
                {
                    iconPath = "sword.png";  // specific icon for wooden sword
                }
        
                GridAreaIcon itemIcon = new GridAreaIcon(
                    itemLocation.getX(),
                    itemLocation.getY(),
                    0.0, 1.0,
                    App.class.getClassLoader().getResourceAsStream(iconPath),
                    item.getName()
                );
                itemIcon.setShown(false);
                area.getIcons().add(itemIcon);
            }
        }

        for (Item itemToRemove : itemsToRemove)
        {
            for (Location locationToRemove : locationsToRemove) 
            {
                gameInstance.removeItemLocation(itemToRemove, locationToRemove);
            }
        }

        itemsToRemove = null;
        locationsToRemove = null;

        List<Obstacle> obstaclesToRemove = new ArrayList<>();
        // initializing obstacles on the grid
        for (Obstacle obstacle : gameInstance.getObstacles())
        {
            for (Location obstacleLocation : obstacle.getLocation())
            {
                // if something already exists at this position, skip adding the obstacle 
                if (area.isOccupied(obstacleLocation.getX(), obstacleLocation.getY()))
                {
                    System.out.println("Skipped adding obstacle at (" + obstacleLocation.getX() + ", " + obstacleLocation.getY() + ") due to square being already occupied.");
                    obstaclesToRemove.add(obstacle);
                    continue; 
                }
        
                GridAreaIcon obstacleIcon = new GridAreaIcon(
                    obstacleLocation.getX(),
                    obstacleLocation.getY(),
                    0.0, 1.0,
                    App.class.getClassLoader().getResourceAsStream("obstacle.png"),
                    "Obstacle"
                );
                obstacleIcon.setShown(false);
                area.getIcons().add(obstacleIcon);
            }
        }

        for(Obstacle obstacle : obstaclesToRemove)
        {
            gameInstance.removeObstacle(obstacle);
        }

        // add our "hidden" icon to every square of the grid, this is how i control what my player cam see
        for (int i = 0; i < gridWidth+1; i++)
        {
            for (int j = 0; j < gridHeight+1; j++)
            {
                GridAreaIcon hiddenIcon = new GridAreaIcon(
                    i, j, 0.0, 1.0,
                    App.class.getClassLoader().getResourceAsStream("dark.png"),
                    "" // no caption so it blends seamlessly, also so its easy to identify 
                );
                hiddenIcon.setShown(true);  // set to hidden initially
                area.getIcons().add(hiddenIcon);
            }
        }

        // after everything is initialized, clear the area around the player
        gameInstance.revealArea(player.getLocation(), area);

        // # BASIC GUI SETUP # //
        inventoryBtn = new Button("Get Inventory"); // button to view inventory contents
        changeLocaleBtn = new Button("Change Locale");

        inventoryBtn.setOnAction((event) -> {
            System.out.println("Get inventory button pressed");
            updateInventoryDisplay();
        });

        changeLocaleBtn.setOnAction(event -> {
            // TO DO: logic here
        });

        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });

        var statusText = new Label(""); // leaving this here as a placeholder incase I decide to use it for something later
        textArea = new TextArea(); 
        textArea.setEditable(false); 
        textArea.appendText("[ Diglett has found himself stuck underground ]\n[ Help him find his way out ]\n\n");

        inventoryDisplay = new TextArea(); 
        inventoryDisplay.setEditable(false); 
        updateInventoryDisplay();

        // box to sort differnt text areas
        VBox textAreaContainer = new VBox();
        textAreaContainer.getChildren().addAll(textArea, inventoryDisplay);

        var toolbar = new ToolBar();
        toolbar.getItems().addAll(inventoryBtn, new Separator(), statusText);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(area, textAreaContainer); 
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
                                    updateInventoryDisplay();
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
            if (canMove) {
                player.setLocation(new Location(tempX, tempY));
                playerIcon.setPosition(tempX, tempY);
                area.requestLayout(); 
                gameInstance.revealArea(player.getLocation(), area); 
                System.out.println("player = " + player.getLocation().toString() + "goal = " + gameInstance.getGoalLocation().toString() );

            }
            
            // after all player movement is complete, check if they have reached the goal
            if (player.getLocation().getX() == gameInstance.getGoalLocation().getX() && player.getLocation().getY() == gameInstance.getGoalLocation().getY()) {
                textArea.appendText("Congratulations! You've reached the goal.\n");
                System.out.println("Player has reached the goal.");
                area.setDisable(true); // disable further actions on the grid
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("Congratulations!");
                    alert.setContentText("You have completed the game.");
                    alert.showAndWait();
                });

            // TO DO: display total amount of days taken to complete
            }

        
            Item itemToRemove = null;
            Location locationToRemove = null;
            // check if player is in same grid as an item
            for (Item item : gameInstance.getItems())
            {
                // iterate through each item in the game
                for (Location itemLocation : item.getLocations())
                {
                    // if item is found at players location
                    if (itemLocation.getX() == tempX && itemLocation.getY() == tempY)
                    {
                        // add to inventory and rmeove from game
                        player.addItemToInventory(item);
                        textArea.appendText("Player picked up: " + item.getName() + "\n");
                        System.out.println("Player picked up: " + item.getName());
                        updateInventoryDisplay();
                        
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
            gameInstance.removeItemLocation(itemToRemove, locationToRemove);
            area.requestFocus(); // keep focus on grid for key input
        });
        

        stage.setScene(scene);
        stage.setTitle("Puzzle Game");
        stage.show();
        area.requestFocus();  
    }

    // prints out inventories contents & item counts
    private void updateInventoryDisplay()
    {
        StringBuilder inventoryText = new StringBuilder("Inventory:\n");
        for (Map.Entry<String, Integer> entry : player.getInventory().entrySet())
        {
            inventoryText.append("-> ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        inventoryDisplay.setText(inventoryText.toString());
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

    private void updateUILocale(Locale locale)
    {
        // TO DO: implement
    }
    
}
