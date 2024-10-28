package edu.curtin.saed.api;

import java.lang.reflect.Constructor;

public class PluginInterface
{
    public void loadPlugin(String pluginClassName, GameAPI gameAPI)
    {
        try
        {
            Class<?> pluginClass = Class.forName(pluginClassName);
            Constructor<?> constructor = pluginClass.getConstructor(GameAPI.class);

            constructor.newInstance(gameAPI);

            System.out.println("Plugin loaded: " + pluginClassName);
        }
        catch (Exception e)
        {
            System.out.println("Failed to load plugin: " + pluginClassName);
        }
    }
}
