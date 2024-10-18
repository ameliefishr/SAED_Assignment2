package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
public class App extends Application {
    private TextArea textArea; 
    private static String fileName; 

    public static void main(String[] args) {
        if (args.length > 0) {
            //fileName = args[0]; 
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set up the main "top-down" display area.
        GridArea area = new GridArea(10, 10);
        area.setStyle("-fx-background-color: #006000;");

        var startBtn = new Button("Start");
        var endBtn = new Button("End");
        var parseBtn = new Button("Parse Configuration");

        startBtn.setOnAction((event) -> {
            System.out.println("Start button pressed");
        });
        endBtn.setOnAction((event) -> {
            System.out.println("End button pressed");
        });
        parseBtn.setOnAction((event) -> {
            parseConfigurationFile(); 
        });

        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });
        var statusText = new Label("Label Text");
        textArea = new TextArea(); 

        // Below is basically just the GUI "plumbing" (connecting things together).
        var toolbar = new ToolBar();
        toolbar.getItems().addAll(startBtn, endBtn, parseBtn, new Separator(), statusText);

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

    private void parseConfigurationFile() {
        try (InputStream inputStream = new FileInputStream("input.dsl")) {
            //System.out.println("Using file: " + file.getAbsolutePath());
            MyParser.parseFile(inputStream); 
            Game game = MyParser.game; 
            System.out.println(game.toString());
            textArea.appendText("Parsed Configuration:\n" + game.toString() + "\n");
        } catch (IOException e) {
            textArea.appendText("Error reading file: " + e.getMessage() + "\n");
        } catch (Exception e) {
            textArea.appendText("Parsing error: " + e.getMessage() + "\n");
        }
    }
}