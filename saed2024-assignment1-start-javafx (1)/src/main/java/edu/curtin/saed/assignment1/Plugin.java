package edu.curtin.saed.assignment1;

public class Plugin
{
    private String name;

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

    @Override
    public String toString()
    {
        return "Plugin [name=" + name + "]";
    }
}
