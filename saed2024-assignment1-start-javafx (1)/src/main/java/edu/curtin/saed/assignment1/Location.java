package edu.curtin.saed.assignment1;

public class Location
{
    private int xPos;
    private int yPos;

    // constructor
    public Location(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
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

    // getters
    public int getX()
    {
        return xPos;
    }
    
    public int getY()
    {
        return yPos;
    }

    @Override
    public String toString() {
        return "(" + xPos + ", " + yPos + ")";
    }
}
