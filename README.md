# cellsociety
Duke CompSci 308 Cell Society Project
****

CellSociety Design
-------------
Group 12: Colette Torres (cft6), Michelle Chen (mmc56), Saumya Jain (sj166)


### Introduction
	This program aims to solve the problem of consistently simulating the behavior of various cellular automata in a way that is compatible with any simulation. From a design perspective this means that commonalities between simulations (such as the use of cells, periodic updating of these cells, and visual display of cells) will be the backbone of the program. The details of specific simulations will be encapsulated and passed to the “backbone” components, which will display those results to the user. The states of cells will be shared between the specific simulations and the central components that interact with the user, but the rules that control the states of cells will be encapsulated. 

###Overview
#####Classes
Simulation class (abstract)
* Fields
  * myGrid: a grid (2-D array of Cell objects)
  * myTimeline: a TimeLine object that runs the update loop
  * myScene: a Scene containing the Grid displayed to the user, and the GUI which has buttons used to control the TimeLine 
* Methods: 
  * init → initializes a Scene object, populates it with a gri
  * step → updates state of all cells in grid and updates display based on cell states
  * updateCells → sets states of all cells in the grid, and modifies the Scene to display the results visually
  * updateColors → sets rectangle node for each cell to be myColor
  * start → starts the timeline playing the simulation
  * stop → stops the timeline playing the simulation
  * pause → pauses the timeline playing the simulation
  * resume → resumes the timeline playing the simulation
  * stepForward → calls the step function once
  * speedUp → decreases the duration of the timeline’s frame
  * slowDown → increases the duration of the timeline’s frame
  * switchSimulation → changes the scene of the stage displaying the simulation to display a SplashScreen to allow the user to select   a different simulation
  
Subclasses: Each simulation will be a subclass containing rules of the simulation
* Segregation
  * Fields
      * percentSatisfied: % agents satisfied
      * dissatisfiedAgents: collection of dissatisfied agents on grid 
      * emptyCells: collection of empty cells on grid 
  * Methods
    * init → randomly populate the grid with equal quantities of two groups; add empty cells to emptyCells
    * step
    * updateCells 
    * updateStates → for all cells: 1) update myState to myNextState; 2) set myNextState = myState
    * updateColors
    * updateSatisfaction → calculates satisfaction of all agents on grid (based on state of neighbors and pre-determined satisfaction       threshold), adds dissatisfied ones to dissatisfiedAgents, updates satisfaction of cells accordingly, and updates                      percentSatisfied
    * setNextState → for all cells on grid: if dissatisfied, sets next state to empty and setNextLocation; if satisfied or empty,           leaves alone
    * setNextLocation → assign nearest available empty cell for dissatisfied agents by setting next state for that empty cell to be         occupied to state of dissatisfied agent, updating emptyCells
    * getNeighbors → retrieves 8 cells (N, NE, E, SE, S, SW, W, NW) surrounding a particular cell
* PredatorPrey
  * Fields
    * emptyCells: A collection of empty Cell objects
  * Methods
    * init → populate the grid with some fish and sharks; add empty cells to emptyCells
    * step 
    * updateCells
    * updateStates → for all cells: 1) update myState to myNextState; 2) set myNextState = myState
    * updateAges → for all cells: 1) update myAge to myNextAge; 2) set myNextAge = myAge
    * updateSharks → for all cells with shark state: 1) sees if shark have fish in adjacent cells and if so, feeds shark, else, sets        next location for shark; 2) reproduces
    * feedSharks → 1) eats fish in adjacent cell by clearing state/age of that adjacent cells, and updating emptyCells; 2) increments       myNextAge of shark cell by 1
    * updateFish → for all cells with fish state: 1) set next location for fish; 2) reproduce
    * setNextLocation → randomly selects empty adjacent cell for animal to move into by 1) updating next state/age of that adjacent         cell; 2) clearing next state/age of current fish cell; and 3) updating emptyCells
      reproduce → if shark/fish & passes shark’s/fish’s time to breed (pre-determined), creates new shark/fish by 1) setting next s         state/age of random empty adjacent cell (if any) and 2) updating emptyCells 
    * getNeighbors → retrieves 4 cells (N,E,S,W)  surrounding a particular cell
* Fire
  * Fields
    * emptyCells: collection of empty cells
  * Methods
    * init→populate grid with only one burning tree in the middle, surrounding cells with trees, cells that comprise grid boundary set     * to empty
    * step
    * updateCells 
    * updateStates → for all cells: 1) update myState to myNextState; 2) set myNextState = myState
    * spread → generate random number between 0 and 1 and if number <probCatch and the cell contains a tree with a burning neighbor       set it to burning
    * getNeighbors → retrieves 4 cells (N,E,S,W)  surrounding a particular cell to check if neighbor contains burning tree
* GameOfLife
  * Initialize
  * Update: method: change whether cell is alive/dead,  check surroundings

* Cell class (abstract)
  * Fields 
    * myState: State of the cell
    * myRect: rectangle node to represent the cell on simulation display 
    * myColor: color of rectangle node
    * myLocation: coordinates of cell in grid 
    * myNextState: The state that the cell will have after it’s updated

  * Methods
    * getState → gets myState
    * setState → sets myState
    * getNextState → gets myNextState
    * setNextState → sets myNextState
    * Subclasses: Contain parameters specific to the simulation (probabilities and other information that is needed in the simulation     rules)
    * SegregationCell
  * Fields
    * satisfied: true or false depending on satisfaction
    * Methods
    * setSatisfaction → sets satisfied 

PredatorCell
 Fields
  * myAge: duration alive
  *  myNextAge: age of animal to occupy cell next
Methods
 * getAge → gets myAge
  * setAge → sets myAge
  * getNextAge → gets myNextAge
  * setNextAge → sets myNextAge 

* FireCell
  * Fields
    * probCatch: probability of tree in cell catching fire
  * Methods
    * setProbability → sets user determined probability of catching fire if tree in neighboring cell is on fire
* LifeCell
  * Methods: 
    * Update states based on state of neighbors and self
* Main 
  * Fields
   * myStage: the stage containing the screen to be displayed before the user
   * myTimeline: the timeline running the simulation loop
  *  mySplash: a SplashScreen object 
  * Methods
    * start → displays a SplashScreen that takes user input to determine what Simulation scene to run and sets the stage’s scene          according to the Simulation chosen by user from SplashScreen; creates the Timeline that the simulations will be run on and            establishes the timeframe for the simulations
* SplashScreen class
  * Fields: 
    * A Scene object containing a menu to select simulations
        * This Scene has buttons that are used to select an XML file to load
    * A Simulation object initialized based on the user’s selection
Methods: 
  * 4 functions (one for each simulation) to parse an XML file, initialize a simulation based on XML input, and initialize the           Simulation field 


###User Interface
The user can start, stop, pause, resume, step forward, speed up and slow down rates, and switch the simulation (this involves loading a new xml file). User interaction will be handled entirely using buttons and input in the format of an XML file. XML file specifications include name of simulation represented, settings for global configuration parameters, grid dimensions, initial configuration of the states for the cells in the grid. Erroneous situations reported to the user include incorrectly formatted XML file input data and empty data.

###Design Details
  The key components in our design are the Simulation class and the Cell class, which are both abstract. These classes house the logic for creating a grid of Cells, updating the state of the grid periodically, and displaying the state of the grid visually. These components were created because their behaviors are common to ANY grid simulation. They were made abstract because simulations could conceivably have lots of complex behaviors and specific rules that don’t apply to other simulations. 
	For that reason, we created four subclasses of Simulation and Cell. The Simulation subclasses contain rules for updating their Cells, and the Cell subclasses contain fields and methods that are specific to those simulations. For example, SegregationCells have an isSatisfied criteria that does not apply to other simulations. 
	One key use case is the application of rules to Cells in a grid. In the simulation Game Of Life, we will handle this in the GameOfLife subclass of Simulation. This subclass will inherit a 2-D array representing the grid of Cells, and will contain its own method to retrieve the relevant neighbors of any Cell. In its update function, the GameOfLife class will change the state of each Cell based on the state of that Cell and its neighbors. This basic process applies to all Cells in the grid. 
	The visual display of updated Cells will be handled in the Simulation class’s update method, because this is a behavior that is common to all simulations. The Simulation class will draw a grid on a Scene object, and modify the appearance of square in the grid, based on the states of Cells contained in a 2-D array. 
	Setting simulation parameters will be done in the SplashScreen class, whose purpose is to allow the user to select a simulation. Based on the user’s selection, the SplashScreen class initializes a Simulation object using the values specified in an XML file. That Simulation object is run in the Main class. 
	Switching simulations will be done using the GUI, which has buttons allowing the user to interact with the simulation. This GUI exists in the Simulation class - when the “switch” button is selected, the Simulation class will set the Scene to the SplashScreen. The SplashScreen has the ability to modify the Stage and insert a Scene containing a new Simulation. 

###Design Considerations 
	The team discussed at length whether or not to make the cell class abstract. The justification behind making Cell abstract was that Cells in different simulations can have different properties, especially in highly complex simulations. Making Cell abstract allowed us to package together behaviors that are common between cells, while leaving the option of adapting Cells for specific uses. One drawback is that for simple simulations, cells don’t have many specific properties, so some subclasses of Cell will have little or no additional information.  

  We also discussed whether or not to make the simulation class abstract.  We ultimately decided on making it abstract for similar reasons as above for the cell class-- this will allow us to package together common behaviors between simulations (such as basic functions that are run with each step through a simulation, the kind of data structure used to represent the simulation grid, etc.), while allowing for specific simulations subclasses to extend their own particular implementations.  Although the Simulation superclass may be a seemingly small class with not too many methods or properties due to its extension by multiple subclasses that hold the bulk of the implementation details, we ultimately decided that having a superclass would condense code in other classes depending on  simulations, allowing, for example, the Main class to simply call the abstract step method of the superclass, instead of specific step methods for each of the different simulations. 

  Another major consideration was how to handle switching simulations. In the end we decided that selecting simulations would be encapsulated in the SplashScreen class, which will handle the whole process of handling user choice and reading an XML document. The alternative we considered was having a series of gameStates in the Simulation class - based on the gameState, Main would initialize and display a different simulation with different parameters. We decided against this because it involves lots of communication between the Main and Simulation classes, and we would have to add logic every time a new simulation was added. With our current design, all of this is encapsulated in one class (SplashScreen), which would simplify the process of adding a new simulation. 

###Team Responsibilities
The team has met to discuss the design overview and details for this assignment, creating and agreeing upon a roadmap for classes and methods that need to be written. The team plans on writing the simulation class and one of the four simulations together. Then, each member will take primary responsibility on one of the three other simulations. Team members will have secondary responsibility on checking and helping debug the other simulations. 

