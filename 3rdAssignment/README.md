Objectives
=======
The assignment was to implement in Java a Artificial Neural Network using *Sigmoid Units* that recognizes, with some uncertainty, if an encoded face picture is from a male or female character. The chapter 4 of *Machine Learning, Tom Mitchell, McGraw Hill, 1997* was used as a reference.

Details
=======
The images provided were encoded already, with no indication of the exact format. So, we had to implement a small parser to collect such information. 

Given assignment constraints, we had to nest all classes in one java file.

The *training* data was randomly divided into five folds, where we perform a 5-fold cross-validation to adjust the sigmoid units. 

The *execution* phase performs 10 experiments and evaluates all images of the *Test* folder.

How to build and run
=======
To compile:
```
 javac <src_path>/DirtyAI.java
```

To properly run:
 1. Train the sigmoid units: `<source path>$: java -train DirtyAI `
 2. Test the network: `<source path>$: java -test DirtyAI`

To view the network sigmoid unit values:
 1. Train the sigmoid units: `<source path>$: java -train DirtyAI `
 2. View the network: `<source path>$: java -test DirtyAI`

*Obs: For viewing and testing you can define a input file with the `-f` option.*

