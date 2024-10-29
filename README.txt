STUDENT NAME: Amelie Fisher
STUDENT ID: 20637064

GAME USAGE:
- To run ->    gradle run --args ../input.utf8.map
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
- When a player uses an item to clear an obstacle that item is consumed
  ( This means that input files will have to take into consideration having enough of a 
   particular item to clear the number of obstacles requiring that item )
- Players can have multiple of one item
- Item names are unique and can be used to identify items
- Item names/messages do not need to be translated as they could be anything
- "obstacles traversed" = obstacles cleared

KNOWN ISSUES:
- when the python script is triggered for the first time (whenever the player first
  picks up an item) there is some freezing in the UI, this goes away after the first
  item has been picked up

- I supressed warnings in the gradle build file relating to accessing static methods in
  a non-static way, I kept getting these from some of my auto-generated jj files despite
  them not being in my src, I tried fixing the errors but could not figure it out + weren't
  many online resources
      -> If cheat mode is on for a non-square grid it can be hard to tell where the grid ends
         so I recommend enabling the grid lines in this scenario

- The window for displaying the grid area has to be manually resized if the grid size is not
  square, I did attempt to dynamically resize it but it caused a lot of issues with my player
  movement logic so I had to disregard it, I am hoping I will not be penalized for this since
  it was not a requirement

- I know my coupling is pretty terrible, I made the mistake of making everything in one
  gradle project then trying to split it into sub-projects at the very end only to find 
  out I had many circular dependencies which I had to find workarounds for (lesson learned)

  adding onto this, since the gameAPI only communicates directly with game (a design choice),
  all UI updates need to be piped from app through game and then to GameAPI which does make 
  for some overly complicated code but I thought it would be better than my API communicating
  with two different components. In future I will need to reconsider these design choices
