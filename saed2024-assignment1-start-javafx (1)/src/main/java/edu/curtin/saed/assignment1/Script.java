package edu.curtin.saed.assignment1;

public class Script
{
    private String className;
    private String superclassName;
    private String methodName;
    private String statement;

    // setters
    public void setClassName(String className)
    {
        this.className = className;
    }

    public void setSuperclassName(String superclassName)
    {
        this.superclassName = superclassName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public void setStatement(String statement)
    {
        this.statement = statement;
    }

    // TO DO: getters

    @Override
    public String toString()
    {
        return "Script [className=" + className + ", superclassName=" + superclassName +
               ", methodName=" + methodName + ", statement=" + statement + "]";
    }
}
