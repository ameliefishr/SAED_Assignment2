package edu.curtin.saed.models;

public class Location
{
    // class variables
    private int xPos;
    private int yPos;

    // constructor
    public Location(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
    }

    // getters
    public int getX()
    {
        return xPos;
    }
    
    public int getY()
    {
        return yPos;
    }
    
    // setters
    public void setX(int x)
    {
        this.xPos = x;
    }

    public void setY(int y)
    {
        this.xPos = y;
    }
}
