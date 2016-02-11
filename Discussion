###Lab Discussion: Refactoring 

####Duplicated Code

* PredatorPrey class originally had two update methods, one for sharks and one for fish
* These update functions did some of the same things, like listing neighbors and empty cells
* Solution: Consolidated them into one update() function. The common behaviors are no longer duplicated. I picked this option because both functions 
  were performing the same function, so it makes sense to put them in the same method. 
* I considered creating 2 helper methods to limit the length of the new update() method, but the curreng lenght is not unreasonable

* There was one other issue causing code duplication: All four subclasses of Simulation have a function to update the colors of cells based on their states
* Originally these functions had exactly the same logical structure, with a nested for loops and a set of identical if statements
* I modified the code by creating a hashmap of colors and states in the superclass Simulation, and adding a function in the superclass 
  that modifies colors based on this hashmap. 
* I chose this option because the color modification behavior is common to every simulation. Therefore it made sense to to handle this behavior in the 
  abstract class. The construction of a Map allows each class to use its own specific values for states and colors. 
  
  
####Use of ArrayList
* In all of the spots where we used ArrayList, it was unnecessary, so all uses of ArrayList have been relaced with List

####Long functions

Main.splashScene: 
  * This function turned out to be performing multiple purposes. It added comboboxes to the splash screen, and it added a start button 
    that would initialize the Simulation
  * To shorten the function I created several helper methods, each of which had one distinct purpose
  * In the future this redesign will make it easier to add new menus to the splash screen or to change the content of a given menu


