package edu.curtin.gameplugins;

import edu.curtin.saed.api.GameAPI;

import java.util.Random;

// plugin to add a penalty object in players adjacent direction if they take longer than 5sec to move
public class Penalty implements GameAPI.MovementListener {
    private final GameAPI gameAPI;
    private long lastMoveTime;
    private String playerDirection;
    private static final long COUNTDOWN = 5000; // 5sec countdown for player move
    
    public Penalty(GameAPI gameAPI) {
        this.gameAPI = gameAPI; // supply access to gameAPI
        this.lastMoveTime = System.currentTimeMillis(); // get time of last move 
        gameAPI.registerPlayerMoveListener(this); // register as a listener for player movements
    }

    // define what happens when movement listener is triggered
    @Override
    public void onMovement(String direction) {
        playerDirection = direction; // get players direction
        checkCountdown(); // check the countdown
    }

    // checks if player has exceeded the countdown
    public void checkCountdown()
    {
        long currentTime = System.currentTimeMillis(); 
        long timeTaken = currentTime - lastMoveTime; // find how long it took player to make their move

        if (timeTaken > COUNTDOWN) // if it was greater than 5 seconds
        {
            createPenaltyObstacle(); // create a penalty object in their adjacent direction
        }
        
        lastMoveTime = currentTime; // reset the timer
    }

    // creates a penalty object in player's adjacent direction
    private void createPenaltyObstacle() {
        int[] playerLocation = gameAPI.getPlayerLocation();

        // location for penalty object 
        int penaltyX = playerLocation[0];
        int penaltyY = playerLocation[1];

        switch (playerDirection) {
            case "up": // if player is moving up
                penaltyY = penaltyY - 1; // place obstacle above them
                break;
            case "down": // if player is moving down
                penaltyY = penaltyY + 1; // place obstacle below them
                break;
            case "left": // if player is moving left
                penaltyX = penaltyX - 1; // place obstacle to their left
                break;
            case "right": // if player is moving right
                penaltyX = penaltyX + 1; // place obstacle to their right
                break;
            default:
                throw new IllegalStateException("unexpected direction: " + playerDirection); // if they somehow move in an unexpected direction
        }

        // add the new penalty obstacle
        gameAPI.addObstacle("Obstacle", penaltyX, penaltyY);

        // alert player that a penalty object was added
        gameAPI.printMessage("\nYou took too long!\nPenalty object added.");

    }
}
