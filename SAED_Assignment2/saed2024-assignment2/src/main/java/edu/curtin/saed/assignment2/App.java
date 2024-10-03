package edu.curtin.saed.assignment2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * This is demonstration code intended for you to modify. Currently, it sets up a rudimentary
 * JavaFX GUI with the basic elements required for the assignment.
 *
 * (There is an equivalent Swing version of this, which you can use if you have trouble getting
 * JavaFX as a whole to work.)
 *
 * You will need to use the GridArea object, and create various GridAreaIcon objects, to represent
 * the on-screen map.
 *
 * Use the startBtn, endBtn, statusText and textArea objects for the other input/output required by
 * the assignment specification.
 *
 * Break this up into multiple methods and/or classes if it seems appropriate. Promote some of the
 * local variables to fields if needed.
 */
public class App extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

   @Override
    public void start(Stage stage)
    {
        GridArea area = new GridArea(10, 10);
        area.setStyle("-fx-background-color: #006000;");
        
        // load game configuration from input file
        loadGameConfiguration("input_generator/bin/generate.bat", area); // input generator
        
        // player controls
        var upBtn = new Button("Up");
        var downBtn = new Button("Down");
        var leftBtn = new Button("Left");
        var rightBtn = new Button("Right");

        var statusText = new Label("Welcome to the Puzzle Game!");
        
        // text area for player moves/inventory, will probably switch to popups later
        var textArea = new TextArea();
        textArea.setEditable(false);

        // - player movement, coded in buttons for now but will switch to keyboard input later
        upBtn.setOnAction((event) -> movePlayer("up", area, statusText, textArea));
        downBtn.setOnAction((event) -> movePlayer("down", area, statusText, textArea));
        leftBtn.setOnAction((event) -> movePlayer("left", area, statusText, textArea));
        rightBtn.setOnAction((event) -> movePlayer("right", area, statusText, textArea));
        
        var toolbar = new ToolBar();
        toolbar.getItems().addAll(upBtn, downBtn, leftBtn, rightBtn, new Separator(), statusText);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(area, textArea);
        splitPane.setDividerPositions(0.75);

        stage.setTitle("Puzzle Game");
        var contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
    }

    private void loadGameConfiguration(String filePath, GridArea area) {
        // TODO: logic to read from input generator and initialize grid area based off it's data
    }

    private void movePlayer(String direction, GridArea area, Label statusText, TextArea textArea) {
        // TODO: code to handle player movement, update grid visibility, and check for items or obstacles
    }

}
