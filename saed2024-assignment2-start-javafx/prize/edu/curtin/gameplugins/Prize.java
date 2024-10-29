package edu.curtin.gameplugins;

import edu.curtin.saed.api.GameAPI;

public class Prize implements GameAPI.ItemListener, GameAPI.ObstacleTraversalListener {
    private final GameAPI gameAPI;
    private int itemCount = 0;
    private int obstacleCount = 0;
    private String itemName;
    private String itemMessage;
    private boolean alreadyAchieved;

    // plugin to add a prize to the players inventory once they reach 5 items picked up/obstacles traversed
    public Prize(GameAPI gameAPI) {
        this.gameAPI = gameAPI; // providing API access

        // prize item details
        itemName = "Trophy";
        itemMessage = "I've been digging for 5 days and all I got was this lame trophy";

        alreadyAchieved = false; // to ensure they can only receive it once

        // register obstactle traversal and item pickup listeners
        gameAPI.registerItemListener(this);
        gameAPI.registerObstacleTraversalListener(this);
    }

    // define what to do when item pickup listener is triggered
    @Override
    public void onItemPickup(String itemName)
    {
        itemCount++; // increase item count
        checkPrize(); // check if prize condition reached
    }

    // define what to do when the obstacle traversal listener is triggered
    @Override
    public void onObstacleTraversed()
    {
        obstacleCount++; // increase obstacle count
        checkPrize(); // chheck if prize condition has been reached
    }

    // method to check if prize condition has been reached
    private void checkPrize() {
        if (itemCount + obstacleCount >= 5 && !alreadyAchieved) // if the counts add up to 5 and prize is not already acheived
        {
            // add item to players inventory
            gameAPI.addItem(itemName, itemMessage); 

            // alert player of their acheivement
            gameAPI.printMessage("\nAcheivement: traverse/pickup a total of 5 items/obstacles");
            gameAPI.printMessage("\nItem Received: " + itemName);
            gameAPI.printMessage("\nMessage: " + itemMessage);

            // update alreadyAcheived to true so player can no longer receive 
            alreadyAchieved = true;
        }
    }
}
