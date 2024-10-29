package edu.curtin.saed.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import edu.curtin.saed.api.GameAPI;

public class PluginLoader
{
    // uses reflection to dynamically find/load pluygins based off their class name
    public void loadPlugin(String pluginClassName, GameAPI gameAPI)
    {
        try
        {
            // load plugin using reflection
            Class<?> pluginClass = Class.forName(pluginClassName); // find based off class name
            Constructor<?> constructor = pluginClass.getConstructor(GameAPI.class);
            constructor.newInstance(gameAPI);

            System.out.println("Plugin loaded: " + pluginClassName); // alert user plugin load was successful
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) // catching the bucket load of exceptions that can be thrown from this
        {
            System.out.println("Failed to load plugin: " + pluginClassName);
            System.out.println("Error details: " + e.getMessage());
        }
    }
}
