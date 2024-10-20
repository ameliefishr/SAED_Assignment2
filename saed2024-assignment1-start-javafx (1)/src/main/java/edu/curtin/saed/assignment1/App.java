package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;

/**
 * JavaFX GUI application that integrates with the parser.
 */
public class App extends Application {
    private TextArea textArea; 
    private static String fileName; 

    public static void main(String[] args) {
        if (args.length > 0) {
            // TO DO: get file from cmd line args
            fileName = args[0]; 
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // main display area
        GridArea area = new GridArea(10, 10);
        area.setStyle("-fx-background-color: #006000;");

        // buttons
        var startBtn = new Button("Start");
        var endBtn = new Button("End");
        var parseBtn = new Button("Parse Configuration");

        // button actions
        startBtn.setOnAction((event) -> {
            System.out.println("Start button pressed");
        });
        endBtn.setOnAction((event) -> {
            System.out.println("End button pressed");
        });
        parseBtn.setOnAction((event) -> {
            parseConfigurationFile(); 
        });

        // close event
        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });

        // status text and text area
        var statusText = new Label("Label Text");
        textArea = new TextArea(); 

        // GUI layout
        var toolbar = new ToolBar();
        toolbar.getItems().addAll(startBtn, endBtn, parseBtn, new Separator(), statusText);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(area, textArea);
        splitPane.setDividerPositions(0.75);

        var contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.setTitle("Air Traffic Simulator");
        stage.show();
    }

    private void parseConfigurationFile() {
        String pathToFile = "input.dsl"; 
        try (FileReader fileReader = new FileReader(pathToFile)) {
            MyParser.parseFile(fileReader); 
            textArea.setText("File parsed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error parsing file: " + e.getMessage());
        }
    }
}
