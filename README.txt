STUDENT NAME: Amelie Fisher
STUDENT ID: 20637064

GAME USAGE:
- To run ->    gradle run --args 'input.dsl' 
- To move ->   WASD
               W -> Move up
               A -> Move left
               S -> Move down
               D -> Move Right 

GAME GOAL:
- Navigate the puzzle and clear obstacles to locate and reach the goal

ICON BREAKDOWN:
- Since I disabled my icon captions for aesthetic purposes here is a quick breakdown:
- Player -> represented by Diglett
- Item -> mainly presented by Chests
- Item (Wooden Sword) -> represented by a Minecraft wooden Sword
- Obstacles -> represented by Boulders
- Goal -> represented by a hole in the ground

ASSUMPTIONS I MADE:
- When a players uses an item to clear an obstacle that item is consumed
  ( This means that input files will have to take into consideration having enough of a 
   particular item to clear the number of obstacles requiring that item )
- Players can have multiple of one item
- Item names are unique and can be used to identify items
- Item names/messages do not need to be translated as they could be anything

KNOWN ISSUES:
- when the python script is triggered for the first time (whenever the player first
  picks up an item) there is some freezing in the UI, this goes away after the first
  item has been picked up

- I supressed warnings in the gradle build file relating to accessing static methods in
  a non-static way, I kept getting these from some of my auto-generated jj files despite
  them not being in my src, I tried fixing the errors but could not figure it out + weren't
  many online resources
