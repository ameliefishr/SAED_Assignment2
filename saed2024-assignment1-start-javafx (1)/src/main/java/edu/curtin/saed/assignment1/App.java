package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import edu.curtin.saed.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.text.Normalizer;
//import java.nio.charset.Charset;

/**
 * JavaFX GUI application that integrates with the parser.
 */
public class App extends Application
{
    // class variables was the easiest way I could think to handle my translation texts, probably not the most efficient
    // I will explore different ways if i have some extra time at the end
    private TextArea textArea;
    private TextArea inventoryDisplay;
    private Label statusText;
    private ResourceBundle bundle;
    private String startupText;
    private String inventoryText;
    private String obstacleRequired1;
    private String obstacleRequired2;
    private String obstaclePassed1;
    private String obstaclePassed2;
    private String pickedUpString;
    private String dateText;
    private String congratsText;
    private String summaryText;
    private Date date;
    private Locale locale;
    private int dayCount;
    private boolean cheatMode;
    private static String filename;
    private static Player player;
    private static final boolean SHOW_CAPTIONS = false; // manually adjust this value to toggle icon captions on/off 

    public static void main(String[] args)
    {
        if (args.length == 0 || args.length > 1) // if invalid number of command line args provided
        {
            System.out.println("Incorrect arguments provided. \nCorrect Usage: ./gradlew run --args filename");
        }
        else
        {
            filename = args[0];
        }
        launch(args);
    }
    
    @Override
    public void start(Stage stage)
    {
        // main display area
        if(filename != null) // double check a file was actually provided before parsing
        {
            parseConfigurationFile(filename);
        }
        else
        {
            return;
        }
        Game gameInstance = MyParser.getGameInstance();
        cheatMode = gameInstance.getCheatMode();

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

        playerIcon.setShownCaption(SHOW_CAPTIONS);
        area.getIcons().add(playerIcon);

        // adding goal to grid
        GridAreaIcon goalIcon = new GridAreaIcon(
            gameInstance.getGoalLocation().getX(),
            gameInstance.getGoalLocation().getY(),
            0.0, 1.0,
            App.class.getClassLoader().getResourceAsStream("goal.png"),
            "Goal"
        );

        goalIcon.setShownCaption(SHOW_CAPTIONS);

        if(!cheatMode)
        {
            goalIcon.setShown(false);
        }
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
                itemIcon.setShownCaption(SHOW_CAPTIONS);
                if(!cheatMode)
                {
                    itemIcon.setShown(false);
                }
                area.getIcons().add(itemIcon);
            }
        }

        if (itemsToRemove != null)
        {
            for (Item itemToRemove : itemsToRemove)
            {
                for (Location locationToRemove : locationsToRemove) 
                {
                    gameInstance.removeItemLocation(itemToRemove, locationToRemove);
                }
            }
        }

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
                obstacleIcon.setShownCaption(SHOW_CAPTIONS);
                if(!cheatMode)
                {
                    obstacleIcon.setShown(false);
                }
                area.getIcons().add(obstacleIcon);
            }
        }

        for(Obstacle obstacle : obstaclesToRemove)
        {
            gameInstance.removeObstacle(obstacle);
        }

        if (!cheatMode)
        {
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
        }

        // # BASIC GUI SETUP # //
        startupText = "[ Diglett is stuck underground ]\n[ Help him find his way out ]\n\n";
        locale = Locale.getDefault();
        date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        dateText = dateFormat.format(date);
        obstacleRequired1 = "You need ";
        obstacleRequired2 = " to clear this obstacle. \n";
        obstaclePassed1 = "Used ";
        obstaclePassed2 = " to clear obstacle. \n";
        inventoryText = "Inventory:\n";
        pickedUpString = "Player picked up:";
        congratsText = "Congratulations!";
        summaryText = "You have completed the game.\nTotal days taken:";
        dayCount = 0;

        gameInstance.initializeAPI();
        gameInstance.initializeScriptHandler();
        GameAPI api = gameInstance.getAPI();
        //criptHandler.loadScript(null);

        TextField localeInput = new TextField();
        localeInput.setPromptText("Enter IETF Language Tag (e.g., en-AU)");
    
        // Create a button for changing the locale
        var changeLocaleBtn = new Button("Change Locale");

        changeLocaleBtn.setOnAction(event -> {
            String inputLocaleTag = localeInput.getText();
            updateUILocale(inputLocaleTag);
            DateFormat newFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
            dateText = newFormat.format(date);
            statusText.setText(dateText);

        });

        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });

        

        statusText = new Label(dateText); // leaving this here as a placeholder incase I decide to use it for something later
        textArea = new TextArea(); 
        textArea.setEditable(false); 
        textArea.appendText(startupText);

        inventoryDisplay = new TextArea(); 
        inventoryDisplay.setEditable(false); 
        updateInventoryDisplay(inventoryText);

        // box to sort differnt text areas
        VBox textAreaContainer = new VBox();
        textAreaContainer.getChildren().addAll(textArea, inventoryDisplay);

        HBox localeBox = new HBox(10); // 10px spacing between text box and button
        localeBox.getChildren().addAll(localeInput, changeLocaleBtn);

        var toolbar = new ToolBar();
        toolbar.getItems().addAll(localeBox, new Separator(), statusText);

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
                    }
                    break;
                case S:  // S = move down
                    if (y < gridHeight - 1)
                    {
                        tempY++;  
                    }
                    break;
                case A:  // A: move left
                    if (x > 0)
                    {
                        tempX--;  
                    }
                    break;
                case D:  // D: move right
                    if (x < gridWidth - 1)
                    {
                        tempX++;  
                    }
                    break;
                default:
                    break;
            }
        
            // checking if an obstacle occupies the player's desired location
            statusText.setText(incrementDate());
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
                            String normalizedRequiredItemName = Normalizer.normalize(requiredItem.getName(), Normalizer.Form.NFC);
                            // check if the player's inventory contains the item required to clear the obstcle
                            for (String itemName : player.getInventory().keySet())
                            {
                                String normalizedItemName = Normalizer.normalize(itemName, Normalizer.Form.NFC);
                                // if the user has the required item
                                if (normalizedItemName.equals(normalizedRequiredItemName))
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
                                    textArea.appendText(obstaclePassed1 + " " + requiredItem.getName() + " " + obstaclePassed2);
                                    updateInventoryDisplay(inventoryText);
                                    
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
                            textArea.appendText(obstacleRequired1 + " " + requiredItems.stream().map(Item::getName).findFirst().get() + " " + obstacleRequired2);
                        }
                        
                        break; // exit loop
                    }
                }
            }
        
            // if there is an obstacle to remove
            if (obstacleToRemove != null)
            {
                gameInstance.removeObstacle(obstacleToRemove); // remove it
            }
            
            // if obstacle is cleared and player can move
            if (canMove) {
                player.setLocation(new Location(tempX, tempY));
                playerIcon.setPosition(tempX, tempY);
                area.requestLayout(); 
                gameInstance.revealArea(player.getLocation(), area); 

            }
            
            // after all player movement is complete, check if they have reached the goal
            if (player.getLocation().getX() == gameInstance.getGoalLocation().getX() && player.getLocation().getY() == gameInstance.getGoalLocation().getY()) {
                playerIcon.setShown(false); //make it look like diglett went through the hole
                textArea.appendText(congratsText + "\n" + summaryText + " " + dayCount);
                area.setDisable(true); // disable further actions on the grid
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(congratsText + " ദ്ദി ˉ͈̀꒳ˉ͈́ )✧");
                    alert.setContentText(summaryText + " " + dayCount);
                    alert.showAndWait();
                });
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
                        api.itemPickup(item); // trigger onItemPickup event listener
                        gameInstance.revealArea(player.getLocation(), area);
                        textArea.appendText("\n" + pickedUpString + " " +  item.getName() + "\n");
                        textArea.appendText((item.getMessage()) + "\n");
                        updateInventoryDisplay(inventoryText);
                        
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
        stage.setTitle("Amelie's Puzzle Game");
        stage.show();
        area.requestFocus();  
    }

    // prints out inventories contents & item counts
    private void updateInventoryDisplay(String inventoryText)
    {
        if (inventoryText != null)
        {
            StringBuilder inventory = new StringBuilder(inventoryText);
            for (Map.Entry<String, Integer> entry : player.getInventory().entrySet())
            {
                inventory.append("-> ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            inventoryDisplay.setText(inventory.toString());
        }
    }
    

    // parse configuration file from cmd line to parser to process 
    private void parseConfigurationFile(String filename)
    {
        //String pathToFile = "input.dsl"; // hardcoded input file for testing 
        //Charset charset;

        // TO DO: encoding

        //if (filename.endsWith(".utf8.map"))
        //{
        //    charset = Charset.forName("UTF-8");
        //}
        try (FileReader fileReader = new FileReader(filename))
        {
            MyParser.parseFile(fileReader); 
            System.out.println("File parsed successfully! :D");
        } 
        catch (IOException | ParseException e) 
        {
            System.out.println("Error parsing file: " + e.getMessage());
        }
    }

    private void updateUILocale(String newLocale) 
    {
        Locale tempLocale = Locale.forLanguageTag(newLocale);
        Locale[] availableLocales = Locale.getAvailableLocales(); // since invalid locales dont throw an exception im checking against java's list of available ones
        
        boolean isValidLocale = false;
        
        for (Locale loc : availableLocales)
        {
            if (loc.equals(tempLocale))
            {
                isValidLocale = true;
                break;
            }
        }

        // if it's valid, update locale. if not, alert user it is invalid
        if (isValidLocale)
        {
            locale = tempLocale;
            textArea.appendText("Locale changed to: " + locale + "\n");
            bundle = ResourceBundle.getBundle("bundle", locale);
            loadBundle();
            updateUIComponents();
        }
        else
        {
            textArea.appendText("Invalid locale: " + newLocale + "\n");
        }
    }

    // loads translated text from resource bundle based off selected locale
    private void loadBundle()
    {
        startupText = bundle.getString("startupText");
        obstacleRequired1 = bundle.getString("obstacleRequired1");
        obstacleRequired2 = bundle.getString("obstacleRequired2");
        obstaclePassed1 = bundle.getString("obstaclePassed1");
        obstaclePassed2 = bundle.getString("obstaclePassed2");
        inventoryText = bundle.getString("inventoryText");
        pickedUpString = bundle.getString("pickedUpString");
        congratsText = bundle.getString("congratsText");
        summaryText = bundle.getString("summaryText");
    }

    // method to update ui componenets after locale changes
    private void updateUIComponents()
    {
        textArea.setText(startupText); 
        updateInventoryDisplay(inventoryText);
    }

    // method to increment date by 1 day
    private String incrementDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1); 
        date = calendar.getTime(); 
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        dateText = dateFormat.format(date);
        dayCount++;

        return dateText;
    }    
}
