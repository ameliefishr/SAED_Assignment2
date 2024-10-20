package edu.curtin.saed.assignment1;

public class Plugin {
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

    @Override
    public String toString()
    {
        return "Plugin [name=" + name + "]";
    }
}
