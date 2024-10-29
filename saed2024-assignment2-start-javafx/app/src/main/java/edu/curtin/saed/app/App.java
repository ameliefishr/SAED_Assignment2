package edu.curtin.saed.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import edu.curtin.saed.api.*;
import edu.curtin.saed.models.AppInterface;
import edu.curtin.saed.models.Item;
import edu.curtin.saed.models.Obstacle;
import edu.curtin.saed.models.Player;
import edu.curtin.saed.models.Location;

import java.io.InputStreamReader;
import java.io.FileInputStream;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * JavaFX GUI application that integrates with the parser.
 */
public class App extends Application implements AppInterface
{
    // class global variables was the easiest way I could think to handle my translation texts, probably not the most efficient
    // I will explore different ways if i have some extra time at the end -> (I did not have time)
    private TextArea textArea;
    private TextArea inventoryDisplay;
    private Label statusText;
    private ResourceBundle bundle;
    private ToolBar toolbar;
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
    private String messageText;
    private Date date;
    private Locale locale;
    private int dayCount;
    private boolean cheatMode;
    private GridArea area;
    private GridAreaIcon playerIcon;
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
        
        // game instance is created in my parser and comes already filled with object data (items, obstacles, etc.)
        Game gameInstance = MyParser.getGameInstance();

        if (gameInstance == null)
        {
            // game was not initialized, can be caused by error parsing file
            return; // return to avoid any null pointer exceptions
        }

        // passing app interface to game
        gameInstance.passAppInstance(this);
        cheatMode = gameInstance.getCheatMode();

        // intializing grid
        int gridWidth = gameInstance.getGridWidth();
        int gridHeight = gameInstance.getGridHeight();
        player = gameInstance.getPlayer();
        System.out.println("Grid dimensions: " + gridHeight + " x " + gridWidth);

        area = new GridArea(gridWidth, gridHeight);
        area.setGridLines(false); 
        area.setStyle("-fx-background-color:#905c34;");


        // adding player icon to grid at start coords
        playerIcon = new GridAreaIcon(
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

        // initializing obstacles on the grid
        List<Obstacle> obstaclesToRemove = new ArrayList<>();
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

        // hiden icon logic
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
            revealArea(player.getLocation(), area, cheatMode);
        }

        // # BASIC GUI SETUP # //
        // default values for UI text
        startupText = "[ Diglett is stuck underground ]\n[ Help him find his way out ]\n\n";
        locale = Locale.getDefault();
        date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        dateText = dateFormat.format(date);
        obstacleRequired1 = "\nYou need ";
        obstacleRequired2 = " to clear this obstacle.";
        obstaclePassed1 = "\nUsed ";
        obstaclePassed2 = " to clear obstacle.";
        inventoryText = "Inventory:\n";
        pickedUpString = "Player picked up:";
        congratsText = "Congratulations!";
        summaryText = "You have completed the game.\nTotal days taken:";
        messageText = "\nMessage: ";
        dayCount = 0;

        // initializing api and loading plugins/scripts
        gameInstance.initializeAPI();
        gameInstance.initializeScriptHandler();
        GameAPI api = gameInstance.getAPI();
        PluginLoader pluginInterface = new PluginLoader();
        toolbar = new ToolBar(); // initialize before plugins incase plugins need to access to add buttons
        pluginInterface.loadPlugin("edu.curtin.gameplugins.Teleport", api);
        pluginInterface.loadPlugin("edu.curtin.gameplugins.Penalty", api);
        pluginInterface.loadPlugin("edu.curtin.gameplugins.Prize", api);

        // button and text field for changing locale
        TextField localeInput = new TextField();
        localeInput.setPromptText("Enter IETF Language Tag (e.g., en-AU)");
        var changeLocaleBtn = new Button("Change Locale");

        changeLocaleBtn.setOnAction(event -> {
            String inputLocaleTag = localeInput.getText();
            updateUILocale(inputLocaleTag);
            DateFormat newFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
            dateText = newFormat.format(date);
            statusText.setText(dateText);

        });

        // action fo when close button is pressed
        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });

        // status text used to display date
        statusText = new Label(dateText); 
        textArea = new TextArea(); 
        textArea.setEditable(false); 
        textArea.appendText(startupText);

        // set up inventory display
        inventoryDisplay = new TextArea(); 
        inventoryDisplay.setEditable(false); 
        updateInventoryDisplay(inventoryText);

        // box to sort differnt text areas
        VBox textAreaContainer = new VBox();
        textAreaContainer.getChildren().addAll(textArea, inventoryDisplay);

        // box to organise locale textfield/button
        HBox localeBox = new HBox(10); // 10px spacing between text box and button
        localeBox.getChildren().addAll(localeInput, changeLocaleBtn);

        // setting up toolbar structure
        toolbar.getItems().addAll(localeBox, new Separator(), statusText);

        // setting up main pane's GUI
        var splitPane = new SplitPane();
        splitPane.getItems().addAll(area, textAreaContainer); 
        splitPane.setDividerPositions(0.75);

        var contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        area.requestFocus();

        // bring focus back to grid if player clicks on it (incase they somehow lose focus)
        area.setOnMouseClicked(event -> {
            area.requestFocus(); 
        });

        // movement via W A S D keys
        // scene event to detect when a keyboard key is entered
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
                        gameInstance.setMovementDirection("up"); 
                        api.playerMoved("up"); // alert api listeners of player movement & direction
                    }
                    break;
                case S:  // S = move down
                    if (y < gridHeight - 1) // -1 so they dont go out of grid bounds
                    {
                        tempY++;
                        gameInstance.setMovementDirection("down");
                        api.playerMoved("down"); // alert api listeners of player movement & direction
                    }
                    break;
                case A:  // A: move left
                    if (x > 0)
                    {
                        tempX--;
                        gameInstance.setMovementDirection("left");  
                        api.playerMoved("left"); // alert api listeners of player movement & direction
                    }
                    break;
                case D:  // D: move right
                    if (x < gridWidth - 1) // -1 so they dont go out of grid bounds
                    {
                        tempX++;
                        gameInstance.setMovementDirection("right");
                        api.playerMoved("right"); // alert api listeners of player movement & direction
                    }
                    break;
                default:
                    break;
            }

            // after all player movement is complete, check if they have reached the goal
            if (player.getLocation().getX() == gameInstance.getGoalLocation().getX() && player.getLocation().getY() == gameInstance.getGoalLocation().getY())
            {
                playerIcon.setShown(false); //make it look like diglett went through the hole
                textArea.appendText(congratsText + "\n" + summaryText + " " + dayCount);
                area.setDisable(true); // disable further actions on the grid

                // display a pop up aler on the UI
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(congratsText + " ദ്ദി ˉ͈̀꒳ˉ͈́ )✧");
                    alert.setContentText(summaryText + " " + dayCount);
                    alert.showAndWait();
                });
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
                            // normalizing required item name for comparison
                            String normalizedRequiredItemName = Normalizer.normalize(requiredItem.getName(), Normalizer.Form.NFC);

                            // check if the player's inventory contains the item required to clear the obstcle
                            for (Item item : player.getInventory().keySet())
                            {
                                //normalizing item name
                                String itemName = item.getName();
                                String normalizedItemName = Normalizer.normalize(itemName, Normalizer.Form.NFC);

                                // if the user has the required item
                                if (normalizedItemName.equals(normalizedRequiredItemName))
                                {
                                    hasRequiredItem = true;
                                    obstacleToRemove = obstacle;  // get the obstacle ready to be removed (have to do outside of loop to avoid ConcurrentException)
                                    api.obstacleTraversed();

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

                                    // remove used item from inventory
                                    player.removeItemFromInventory(item);
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
                cheatMode = gameInstance.getCheatMode();
                revealArea(player.getLocation(), area, cheatMode); 

            }

            // check if player is in same grid as an item
            Item itemToRemove = null;
            Location locationToRemove = null;
            for (Item item : gameInstance.getItems())
            {
                // iterate through each item in the game
                for (Location itemLocation : item.getLocations())
                {
                    // if item is found at players location
                    if (itemLocation.getX() == tempX && itemLocation.getY() == tempY)
                    {
                        // add to inventory and remove from game
                        player.addItemToInventory(item);
                        api.itemPickup(item.getName()); // trigger onItemPickup to alert api listeners
                        cheatMode = gameInstance.getCheatMode();
                        revealArea(player.getLocation(), area, cheatMode);
                        textArea.appendText("\n" + pickedUpString + " " +  item.getName());
                        textArea.appendText(messageText+ " " + item.getMessage());
                        updateInventoryDisplay(inventoryText);
                        
                        // since items can have multiple locations you are only removing the item at that specific location (unless only 1 of item remains then remove whole item)
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
            for (Map.Entry<Item, Integer> entry : player.getInventory().entrySet())
            {
                inventory.append("-> ").append(entry.getKey().getName()).append(": ").append(entry.getValue()).append("\n");
            }
            inventoryDisplay.setText(inventory.toString());
        }
    }
    

    // parse configuration file from cmd line to parser to process 
    private void parseConfigurationFile(String filename)
    {
        // setting up charset variable
        Charset charset;
    
        // checks what encoding to use based off file extension
        // note: since it's not possible to accurately guess the encoding type I am just going based off file extension
        // I am assuming that the contents of the provided files are actually encoded in the encoding form they claim to be
        if (filename.endsWith(".utf8.map")) // for UTF8 encoding
        {
            charset = StandardCharsets.UTF_8; // using StandardCharset for UTF8
        }

        else if (filename.endsWith(".utf16.map")) // for UTF16 encoding
        {
            charset = StandardCharsets.UTF_16; // using StandardCharset for UTF16
        }

        else if (filename.endsWith(".utf32.map")) // for UTF32 encoding
        { 
            charset = Charset.forName("UTF-32"); // have to set up a new charset as StandCharsets does not include UTF32
        }
        
        else // if it's any other encoding type
        {
            System.out.println("Unsupported file encoding type, accepted encodings: UTF8, UTF16, UTF32");
            return;
        }
    
        // try to create an inputStreamReader of the file using the defined charset
        try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(filename), charset))
        {
            MyParser parser = new MyParser(fileReader);
            parser.parseFile(); // send it to MyParser class to be parse
            System.out.println("File parsed successfully! :D"); // alert user of successful parse
        }
        catch (IOException | ParseException e) // catch potential exceptions
        {
            System.out.println("Error parsing file: " + e.getMessage());
        }
    }

    // method to update the UI's locale
    private void updateUILocale(String newLocale) 
    {
        Locale tempLocale = Locale.forLanguageTag(newLocale);
        Locale[] availableLocales = Locale.getAvailableLocales(); // since invalid locales dont throw an exception im checking against java's list of available ones
        boolean isValidLocale = false;
        
        // for all the available locales
        for (Locale loc : availableLocales)
        {
            if (loc.equals(tempLocale))
            {
                isValidLocale = true; // if the locale provided is valid (is in the list)
                break;
            }
        }

        // if it's valid, update locale. if not, alert user it is invalid
        if (isValidLocale)
        {
            locale = tempLocale;
            textArea.appendText("Locale changed to: " + locale + "\n");

            // load in resource bundle for that locale for translations (if it exists)
            bundle = ResourceBundle.getBundle("bundle", locale);
            loadBundle();
            updateUILocale(); // updates UI based on locale
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
        messageText = bundle.getString("messageText");
    }

    // method to update ui componenets after locale changes
    private void updateUILocale()
    {
        textArea.setText(startupText); 
        updateInventoryDisplay(inventoryText);
    }

    // to be used by plugins through the GameAPI so they can add menu buttons
    @Override
    public void addMenuButton(String buttonName, EventHandler<ActionEvent> onClickEvent)
    {
        Button addedButton = new Button(buttonName);
        addedButton.setOnAction(onClickEvent); 
        toolbar.getItems().addAll(new Separator(), addedButton); 
    }

    // so plugins can refresh the UI after UI changes
    @Override
    public void updatePlayerIcon()
    {
        playerIcon.setPosition(player.getLocation().getX(),player.getLocation().getY());
        area.requestLayout(); 
        revealArea(player.getLocation(), area, cheatMode); 
        playerIcon.setShown(true);
    }

    // so plugins can add new obstacles
    @Override
    public void addObstacle(String name, int locX, int locY)
    {
        GridAreaIcon obstacleIcon = new GridAreaIcon(
            locX,
            locY,
            0.0, 1.0,
            App.class.getClassLoader().getResourceAsStream("obstacle.png"),
            name
        );
        obstacleIcon.setShownCaption(SHOW_CAPTIONS);
        if(!cheatMode)
        {
            obstacleIcon.setShown(false);
        }

        area.getIcons().add(obstacleIcon);
        revealArea(player.getLocation(), area, cheatMode);
        area.requestLayout();

        // actual logic to add a new obstacle object occurrs in game class
    }

    // so plugins can print messages to the UI
    @Override
    public void printToUI(String message)
    {
        textArea.appendText(message);
    }

    // method to get current date and increment by 1 day
    private String incrementDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1); // add 1 day
        date = calendar.getTime(); 
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale); // make sure it's in the date format of the currently set locale
        dateText = dateFormat.format(date);
        dayCount++; // increase count

        return dateText;
    } 
    
    // manages the player's visibility
    public void revealArea(Location playerLocation, GridArea area, boolean cheatMode)
    {
        boolean cheat = cheatMode; // check if cheatmode has changed (i.e. player picked up map)
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
        proccessAreaArray(squaresToReveal, area, cheat);
    }
    
    private void proccessAreaArray(double[][] squaresToReveal, GridArea area, boolean cheat)
    {
        // iterate over each item in the grid
        area.getIcons().forEach(icon ->
        {
            // get their coordinates
            double getX = icon.getX();
            double getY = icon.getY();
            
            // bool to check whether icon should be revealed
            boolean reveal = false;

            if(cheat) // if cheat mode activated set everything to reveal
            {
                reveal = true;
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
}
