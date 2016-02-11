####Duplicate Code Refactoring
#####Michelle Chen and Carine Torres

We decided to get rid of the duplicate code that updated colors for each extension of the simulation class. We chose this section because the repetition affected 4 classes and because it was repetition in iteration which takes up a lot of space and time. Thus, it is a big source of inefficiency to have too much iteration.

The way we refactored was by creating a general method in the cell class to update a single cell's color. To accomplish this, we also had to add a next state color. In our abstract simulation class, the update color method that is now applicable to all subclasses is now the only time it iterates through every cell. Thus, we were able to get rid of the inefficient iterations that plagued all four of our simulation subclasses.
