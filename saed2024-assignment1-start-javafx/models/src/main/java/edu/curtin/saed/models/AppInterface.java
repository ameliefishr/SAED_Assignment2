package edu.curtin.saed.models;

// class to allow Game class to adjust UI elements without having a direct dependency on app
// (to avoid circular dependency)

public interface AppInterface
{
    void addMenuButton(String buttonName);
}
