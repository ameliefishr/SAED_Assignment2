size (10, 10)
start (1, 5)
goal (9, 8)

item "Wooden Sword" {
    at (0, 0), (10, 5), (7, 1)
    message "Use wisely."
}

item "Toffee Apple" {
    at (2, 3)
    message "An apple a day keeps the doctor away."
}

obstacle {
    at (1,1), (2,3), (3,4), (4,5)
    requires "Wooden Sword", "Toffee Apple", "Neil deGrasse Tyson"
}

plugin org.example.myplugins.PluginX

script !{
class MyCallback(edu.curtin.game.Callback):
    def handleEvent(e):
        print(api.getSomeInfo())
}

