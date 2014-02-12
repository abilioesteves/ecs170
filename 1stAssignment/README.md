<p1>Project 1: A* On Terrain Maps<p1>

 

<b>Introduction</b>

You are lost but fortunately you have Leonid Shamis's A* framework which you can use to find your way home. 

Important: No two adjacent nodes are at height zero. This is important for the division cost function.

In this assignment, you will devise an algorithm for plotting a course home, minimizing the total cost of the path and the amount of time spent searching. 

<b>The World</b>

In this assignment, you'll first work with the TerrainMap class, a class encapsulating a two-dimensional world layered on top of a rectangular grid. Each point in the world has a height, represented by an integer value between 0 and 255. Depending on the selected movement type, you can either move to any of the eight squares adjacent to your own location (e.g. the four cardinal directions and the diagonals) or just the cardinal directions. As you would expect, the cost to traverse between tiles is dependent on the differences in height between the tiles. Below are different const functions for you to experiment with.

The TerrainMap class also keeps track of which tiles your algorithm visits as it looks for an optimal path home. Part of your grade on this assignment will be determined by how many tiles your algorithm visited. For example, if two different algorithms each yield the optimal path, but one accomplishes this task and only considers 10% of the number of squares as the other, the algorithm that visited fewer squares would be considered superior to the other. Interestingly, many of the better search algorithms that visit fewer squares also run in considerably less time, so you'll have a dual incentive to keep your search space small. Please note that this is still secondary to finding the shortest path.

<b>Coding Instructions</b>

Your assignment is to create an implementation of the AIModule interface that computes a path from the start location to the end location while minimizing the total search space. Once you've written this function, you can plug it in to the existing starter code by compiling your module and specifying it as a command-line parameter to the main program. For example, if you've written an AI module called PerfectAI, you can see the result by running "java Main PerfectAI". This will run your AI, print its score and number of visited squares to stdout, and will create a display window showing the terrain, the path you've taken, and the squares your AI module visited.

To help test your implementation, we've provided a working Dijkstra's algorithm AI class called DijkstraAI. As you've learned in class, Dijkstra's algorithm always yields the optimal path, so you can compare your own AI against the DijkstraAI module to see if your path is indeed optimal. The starter code is designed such that you can have several different AI modules each plot a path over the terrain. You can do this by specifying multiple parameters to the main program, as in "java Main PerfectAI DijkstraAI". Because you can interface with the starter code directly from the command line, you do not need to make any changes to the provided starter code.

Your submission for this assignment should consist of a single .java file containing your AIModule implementation, and it will be run using a clean copy of the starter code. If you have any extensions to the project that require changes to the starter code, please let us know before you begin making changes.

<b>Questions</b>

Cost Functions    
<code>
 // Exponential of the height difference
 public double getCost(final Point p1, final Point p2)
 {
      return Math.exp(getTile(p2) - getTile(p1));
 }
 // New height divided by old height
 public double getCost(final Point p1, final Point p2)
 {
      return (getTile(p2) / (getTile(p1) + 1));
 }
</code>
You may cut and paste them into TerrainMap.java over the current getCost function. Note that the second cost function can return 0. However, the Tim Carver “no worm hole assumption” holds, that is there will never be 2 points of height 0 next to each other.

<b>Part 1: Creating Heuristics</b>

For each cost function above and for the movement type all eight neighbors, do the following: 

a) Create an admissible heuristic, document the exact form of the heuristic and prove or show examples that it is admissible. (6 points per cost function) 

Submission Requirements: For questions a)  submit a concise but complete PDF writeup labeled Part1.PDF

<b>Part 2: Implementing the Heuristics and A* Algorithm</b>

For implementation purposes only consider chess (eight option) movement with the two cost functions.

Note, compare your results against the Dijkstra's runtime module to make sure you get the correct results.

You will now implement your own version of A*. Look at the DirectAI and StupidAI classes to get an idea of how to search the state space.

To implement the heuristic, write valid java code in the form of:
<code>
 private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2)
 {
  ...
 }
</code>
Submission Requirements: For questions a) and b) submit a java module labeled as AStarExp_<your-student-ids>.java for the first cost function and
AStarDiv_<your-student-ids>.java for the second cost function. (5 points each)

<b>Part 3: How Efficient is Your Heuristic: Trying out your Code on a Small Problem</b>

You will test your heuristic functions from the previous question with the appropriate cost function on 500x500 maps with different random seeds. Try out your heuristic functions with the appropriate cost function on 500x500 maps with random seeds 1, 2, 3, 4 and 5.


5 points per cost function for getting the shortest path. Non-optimal paths get zero points.

For all students who get the shortest path: we will rank your performance and you shall receive bonus marks of:

5*(Number of Qualified Students + 1 - Your Rank) / (Number of Qualified Students) for each cost function.

The ranking is based on the measurement “Uncovered” in the output of the program.

Submission Requirements (one for each cost function): For each execution record the cost of the shortest path and the number of nodes expanded as per the output of the program. Submit two text files: Exp_Efficient.txt and Div_Efficient.txt

<b>Part 4: How Efficient is Your Implementation: Climbing Mount Saint Helens During The Eruption</b>

Answer this question ONLY for the “Exponential of the height difference” cost function.
If you wish you can go to http://tahoe.usgs.gov/viewers.html and download the Dem3D viewer. This will allow you to view the file in MtAftDem.zip which is in the old USGS DEM format of the Mount Saint Helens after the eruption. 

There are better DEM viewers you could use that allows fly throughs etc, but this one works with a standards graphics card and will help them understand the terrain you are navigating. 

This is a much larger grid and hence you will have cleverly implement your algorithm and/or use a clever variation. Your aim is to modify both your A* algorithm and admissible heuristic so as to find the optimal path in this new environment in the least possible time.  You can use any valid technique to improve the performance of the algorithm. By this, I mean any clever modification of the base algorithm (such as waypoints) but NOT say writing the A* algorithm in assembler or memorizing paths etc. If you have any doubts about the validity of your approach then first consult me. The algorithm must still be guaranteed to find the global optima.

Use the following command to run this part of the assignment: "java Main YourAIModule -load MTAFT.XYZ".

<b>Submission Requirements: </b>

a) A clear and concise description of your modified A* and admissible heuristic.  Submit File Part4.PDF (5 points)

b) The implementation of your modified A* and admissible heuristic in the file MtStHelensExp_<your-student-ids>.java.  (15 points)