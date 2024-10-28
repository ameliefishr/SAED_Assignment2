package edu.curtin.saed.api;

import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import java.util.List;

import edu.curtin.saed.models.Item; 
import edu.curtin.saed.models.Script; 

public class ScriptHandler
{
    private GameAPI api;

    public ScriptHandler(GameAPI api)
    {
        this.api = api;
    }

    public void loadScript(Item item)
    {
        List<Script> scripts = api.getScripts();
        for(Script script : scripts)
        {
            String pythonScript = script.getScriptCode();
            executeScript(pythonScript, item);
        }
    }

    public void executeScript(String script, Item item)
    {
        try (PythonInterpreter interpreter = new PythonInterpreter()) 
        {
            interpreter.set("api", api);
            interpreter.exec(script);
            PyObject reveal = interpreter.get("reveal"); 
            interpreter.set("item", item); 
            reveal.invoke("onItemAcquired", interpreter.get("item")); 
        }
        catch (Exception e)
        {
            System.out.println("error");
            //TO DO: explicit exception handling
        }
    }
}

