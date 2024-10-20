size (10,10)
start (1,5)
goal (9,8)

item "Wooden Sword" {
    at (0,0), (10,5), (7,1)
    message "Use wisely."
}


item "Meow" {
    at (0,1), (1,5), (3,1)
    message "meow meow meow meow."
}


item "Woof" {
    at (0,1), (1,5), (3,1)
    message "woof woof woof woof."
}

obstacle {
    at (1,1)
    requires "Wooden Sword"
}

plugin org.example.myplugins.PluginX

script !{
class MyCallback(edu.curtin.game.Callback):
    def handleEvent(e):
        print(api.getSomeInfo())
}