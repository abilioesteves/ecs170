Objectives
=======
The assignment is to create an implementation of the AIModule interface that computes a path from the start location to the end location while minimizing the total search space.

Details
=======
All java code, except for AStarDiv_993083613_999550263.java and AStarExp_993083613_999550263.java, were given by the professor.

Both files present an implementation of the *A star* algorithm, but they differ on the Heuristics choice. This is rather redundant, but is for the sake of assignment constraints.

Speaking of assignment constraints, we had to implement all the auxiliary classes as *nested* classes in both files.

How to build and run
=======
To compile:
```
 javac <src_path>/*.java
```

To try out our AI implementation, with the division heuristics, go to the src folder and:
 1. Make sure that in the getCost() method in TerrainMap.java the line 285 is commented, and the line 284 is uncommented;
 2. Compile;
 3. Then go to the terminal and run:
  ```
   java Main AStarDiv_993083613_999550263.java
  ```
 
To try out our AI implementation, with the exponential heuristics, go to the src folder and:
 1. Make sure that in the getCost() method in TerrainMap.java the line 284 is commented, and the line 285 is uncommented;
 2. Compile;
 3. Then go to the terminal and run:
  ```
   java Main AStarExp_993083613_999550263.java
  ```

