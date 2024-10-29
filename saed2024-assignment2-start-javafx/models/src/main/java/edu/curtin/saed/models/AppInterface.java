package edu.curtin.saed.models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// class to allow Game class to adjust UI elements without having a direct dependency on app
// (to avoid circular dependency)

public interface AppInterface
{
     void addMenuButton(String buttonName, EventHandler<ActionEvent> onClickEvent);
     void updatePlayerIcon();
     void addObstacle(String name, int locX, int locY);
     void printToUI(String message);
}
