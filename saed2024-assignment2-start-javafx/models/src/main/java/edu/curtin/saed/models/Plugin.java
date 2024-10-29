package edu.curtin.saed.models;

public class Plugin
{
    // class variables
    private String name;

    // constructor
    public Plugin(String name)
    {
        this.name = name;
    }

    // getters
    public String getName()
    {
        return name;
    }

    // setters
    public void setName(String pluginName)
    {
        this.name = pluginName;
    }
}
