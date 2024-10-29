package edu.curtin.saed.models;

public class Script
{
    // class variables
    private String content;

    // constructor
    public Script(String content)
    {
        this.content = content;
    }

    // setters
    public void setScriptCode(String newContent)
    {
        this.content = newContent;
    }
    
    // getters
    public String getScriptCode()
    {
        return this.content;
    }
}