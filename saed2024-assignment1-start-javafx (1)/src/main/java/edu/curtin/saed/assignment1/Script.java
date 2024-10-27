package edu.curtin.saed.assignment1;

public class Script
{
    private String content;

    public Script(String content)
    {
        this.content = content;
    }

    public void setScriptCode(String newContent)
    {
        this.content = newContent;
    }
    
    public String getScriptCode()
    {
        return this.content;
    }

    public void handleItemPickup(Item item)
    {
        System.out.println("script triggered for item pickup: " + item.getName());
    }


    // TO DO: getters

    @Override
    public String toString()
    {
        return "Script [content=\n" + content + "]";
    }
}