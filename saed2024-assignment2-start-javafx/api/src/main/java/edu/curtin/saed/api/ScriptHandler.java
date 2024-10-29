package edu.curtin.saed.api;

import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;

import java.util.List;

import edu.curtin.saed.models.Script; 

public class ScriptHandler
{
    private GameAPI api;

    public ScriptHandler(GameAPI api)
    {
        this.api = api; // initialize api so scripthandler can access it's methods
    }

    // triggers script execution for the specific item name (runs each time item picked up)
    public void loadScript(String itemName)
    {
        List<Script> scripts = api.getScripts(); // get scripts from api (there's only 1 atm)
        for(Script script : scripts)
        {
            // execute each script with the itemName as a parameter
            String pythonScript = script.getScriptCode();
            executeScript(pythonScript, itemName);
        }
    }

    // executes the specified python script with the provided item name
    // this would be a bit different for each script, right now it's set up for the reveal script
    public void executeScript(String script, String itemName)
    {
        // use try-with-resource to open python enterpreter to ensure it can close properly
        try (PythonInterpreter interpreter = new PythonInterpreter()) 
        {
            // interpreter setup
            interpreter.set("api", api); // set api instance for interpreter
            interpreter.exec(script); // execute the python script
            PyObject reveal = interpreter.get("reveal"); // locate and get the reveal object in script
            interpreter.set("item", itemName); // set the itemName for the interpreter

            //call the onItemAcquired method on the located reveal object and pass the item name
            reveal.invoke("onItemPickedUp", interpreter.get("item")); 
        }
        catch (PySyntaxError | IllegalArgumentException e) // catch any of the errors that could be thrown (including python errors)
        {
            System.out.println("Error while executing script: " + script);
            System.out.println("Error details: " + e.toString()); // py errors weren't showing up in e.getMessage so using toString instead
        }
        
    }

}

