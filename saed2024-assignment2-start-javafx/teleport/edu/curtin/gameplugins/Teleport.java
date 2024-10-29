package edu.curtin.gameplugins;

import edu.curtin.saed.api.GameAPI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Random;

// plugin to randomly teleport a player if they click on the added 'Teleport' UI button
public class Teleport implements GameAPI.MenuListener
{
    private final GameAPI gameAPI;
    private boolean teleported;

    public Teleport(GameAPI gameAPI)
    {
        this.gameAPI = gameAPI; // provide api access
        this.teleported = false; // to ensure players can only teleport once
        gameAPI.registerMenuActionListener(this); // register as a listener for menu actions

        // adds new teleport button to ui
        gameAPI.addMenuButton("Teleport", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                // triggers menu action listeners with "teleport"
                gameAPI.menuAction("teleport"); 
            }
        });
    }

    // define what to do when menu action listeners are triggered
    @Override
    public void onMenuAction(String action)
    {
        // if the menu action triggered was a "teleport" action
        if(action.equals("teleport"))
        {
            if(teleported) // if player has already teleported
            {
                // alert them that they cant teleport again
                gameAPI.printMessage("\nTeleport can only be used once!");
                return;
            }

            // if player has not already teleported
            teleportPlayer(); // teleport the player
            teleported = true; // make sure they cant teleport again
        }
    }

    // method to randomly teleport player within grid bounds
    private void teleportPlayer()
    {
        // gets grid bounds
        int[] gridSize = gameAPI.getGridSize();
        int gridWidth = gridSize[0];
        int gridHeight = gridSize[1];

        // gets random coordinates (within grid bounds)
        Random rand = new Random();
        int newX = rand.nextInt(gridWidth-1);
        int newY = rand.nextInt(gridHeight-1);

        // sets players location to the random coordinates
        gameAPI.setPlayerLocation(newX, newY);

        // make's sure the players icon also get's updated
        gameAPI.refreshPlayerUI(newX, newY);

        // alert player of successful teleport
        gameAPI.printMessage("Player has been teleported.");
    }
}
