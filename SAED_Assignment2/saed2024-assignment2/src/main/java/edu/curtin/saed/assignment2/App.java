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
        // Set up the main "top-down" display area. This is an example only, and you should
        // change this to set it up as you require.

        GridArea area = new GridArea(10, 10);
        // area.setGridLines(false); // If desired
        area.setStyle("-fx-background-color: #006000;");

        area.getIcons().add(new GridAreaIcon(
            1,   // x
            1,   // y
            0.0, // rotation (degrees)
            1.0, // scale
            App.class.getClassLoader().getResourceAsStream("airport.png"),  // Image (InputStream)
            "Airport 1"));  // caption

        area.getIcons().add(new GridAreaIcon(
            5, 3, 45.0, 1.0,
            App.class.getClassLoader().getResourceAsStream("plane.png"),
            "Plane 1"));


        // Set up other key parts of the user interface. You'll also want to adjust this.

        var startBtn = new Button("Start");
        var endBtn = new Button("End");

        startBtn.setOnAction((event) ->
        {
            System.out.println("Start button pressed");
        });
        endBtn.setOnAction((event) ->
        {
            System.out.println("End button pressed");
        });
        stage.setOnCloseRequest((event) ->
        {
            System.out.println("Close button pressed");
        });
        var statusText = new Label("Label Text");
        var textArea = new TextArea();
        textArea.appendText("Sidebar\n");
        textArea.appendText("Text\n");


        // Below is basically just the GUI "plumbing" (connecting things together).

        var toolbar = new ToolBar();
        toolbar.getItems().addAll(startBtn, endBtn, new Separator(), statusText);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(area, textArea);
        splitPane.setDividerPositions(0.75);

        stage.setTitle("Air Traffic Simulator");
        var contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
    }
}
