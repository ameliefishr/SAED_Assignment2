size (10,10)
start (1,5)
goal (9,8)

item "Wooden Sword" {
    at (0,0), (10,5), (7,1)
    message "Use wisely."
}

item "Rock" {
    at (1,3), (2,7), (9,8)
    message "A cool rock."
}

item "map" {
    at (2,4)
    message "Suddenly you can see."
}

obstacle {
    at (1,1)
    requires "Wooden Sword"
}

plugin org.example.myplugins.PluginX

script !{
print("starting Reveal python script...")

class Reveal:
    def __init__(self, api):
        self.api = api
        print("Reveal script running")

    def onItemAcquired(self, item):
        print("Item acquired: "+ item.getName())
        if "map" in item.getName().lower():
            print("Revealing goal and hidden items.")
            self.api.toggleReveal(True)
            print("Goal and hidden items revealed.")

reveal = Reveal(api)
}

