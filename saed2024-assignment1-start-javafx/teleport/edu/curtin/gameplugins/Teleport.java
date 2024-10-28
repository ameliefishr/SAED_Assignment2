package edu.curtin.gameplugins;

import edu.curtin.saed.api.GameAPI;
import java.util.Random;

public class Teleport implements GameAPI.MenuListener
{
    private final GameAPI gameAPI;
    private boolean hasTeleported;

    public Teleport(GameAPI gameAPI)
    {
        this.gameAPI = gameAPI;
        this.hasTeleported = false; 
        gameAPI.registerMenuActionListener(this); 
        gameAPI.addMenuButton("Teleport");
    }

    @Override
    public void onMenuAction(String action)
    {
        if(action.equals("teleport") && !hasTeleported)
        {
            teleportPlayer();
            hasTeleported = true; 
        }
    }

    private void teleportPlayer()
    {
        int[] gridSize = gameAPI.getGridSize();
        int gridWidth = gridSize[0];
        int gridHeight = gridSize[1];

        Random rand = new Random();
        int newX = rand.nextInt(gridWidth);
        int newY = rand.nextInt(gridHeight);

        gameAPI.getPlayerLocation().setX(newX);
        gameAPI.getPlayerLocation().setY(newY);

        System.out.println("Teleport: Player has been teleported to (" + newX + ", " + newY + ")");
    }
}
