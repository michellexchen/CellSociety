##DUPLICATE CODE

We chose to refactor some of the update methods in the Predator class because these update functions deal with
updating animals which could be useful for future simulations, such as the ones in the next Sprint dealing
with ants.  Thus their design is very relevant to future extensions of this project.

We talked about dealing with the fact that the way this function updated two different types
of animals started off really similarly because it iterates through them in a similar manner. 
Yet, there was one slight difference in the way that the sharks updated vs. the fish because 
the shark looked for a fish cell to move into, while the fishes do not require
that as they are iterated through.  In addition, all of the code below those duplicated
lines was quite different, so we had to deal with that too.  Thus we created a new general
method for updating an animal that took in a parameter to distinguish which animal was being
updated and simply had a case to update the sharks and one to update the fish to capture all of
those nuances.  An alternative would be to make their update methods more similar so that they could
be better abstracted, but for now, because those algorithmic details were very specific and we didn't 
want to mess anything up in terms of functionality, we made use of an if else statement for the two cases.

##CHECKLIST 

We chose to refactor the longest method because it was alarming to realize how long this method was
(it was pointed out before class) when it could easily be refactored.  To refactor, we discussed just 
taking chunks of the code under this long method and creating helper methods for different components
of the splash screen set up, like adding the combo boxes to the screen, dealing with the START button, etc.
