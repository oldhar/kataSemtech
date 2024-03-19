# coding-interview

## The exercise
You've been given CSV files (e.g. _population_2019_500.csv_). The files contain the population of French cities.  
We would like you to write a program that takes a CSV file as input and print to stdout:
* a line for each department with:
  * the department name
  * the total population for the department
  * the city with the largest population
* a line with the department name with the smallest population

## More instructions
* write your code in Java or Scala
* tend to minimize the use of external dependencies (not forbidden)
* provide instructions to run your program
* provide tests and instructions to run your tests

## Even more instructions about industrialization
We rely a lot in automation for build/release and deploy, 

* can you propose a test/pull-request workflow and a build workflow using GHA or any solution you have access/knowledge ?
* can you propose a solution to host a simple service on which we would feed this csv and get the output result ?

## How to submit your solution
Send us an archive of the project.

## Good luck!
Please don’t spend more than 2 hours on this exercise, and let us know if you have any questions.


## Solution proposed

The application uses maven to manage dependencies, build lifecycle including tests and packaging. 
Java version required: 21 as declared in the pom.

No need to install maven on your local machine, a maven wrapper is included and can be used that way:

`./mvnw clean install` for instance.

### Dependencies
The only dependencies added are tests libraries `junit5` and `assertj` for tests assertions. 

Library `common-lang` for string utils.

Nothing more.


### Running the tests

The tests can be run using maven:

`./mnvw test`

or using your IDE, on Intellij it renders that way:
![Tests runned on IJ](doc/images/testOnIJ.png "Running test on IDE")

Only one unit test class `StatsComputationTest` has been created to test the business of computation. 
If this computation class was used in a lambda, an integration test could have been created to test the output rendered. See section how it could be deployed.

This test class is divided into 2 major parts and tests can be read easily and used as documentation.

### Running the application

The application can be packaged using:
`./mvnw clean install`

Then running the jar:
`java -jar target/semtech-1.0-SNAPSHOT-jar-with-dependencies.jar /home/hcapitaine/projects/semtech/target/test-classes/population_2019_500.csv`

You can change the path to the csv file as you need.

The application should output in your terminal an output like this:
![Output part 1](doc/images/appOutputPart1.png "Output part 1")
![Output part 2](doc/images/appOutputPart2.png "Output part 2")

### Github