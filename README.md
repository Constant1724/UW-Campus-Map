# UW-Campus-Map
An app for UW Campus Map. Built backend in java and UI in React

## Note that this project is still under project cleaning && More Info is on the way!!

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- You will need npm and Maven to start. 

- You must use the Chrome or Safari web browser. Do not use Firefox or other browsers.

- Java 8 is required.

- Using Intellij is recommended

- Try running 
   ```
   ./gradlew build
   ```
   from the source root to run & build the project.

#### To check if npm is installed, run
 
```
npm -v
```
It should should print a version number. If not, then [install npm](https://www.npmjs.com/get-npm). [npm](https://www.npmjs.com/) is a JavaScript package manager that helps manage the dependencies for the UI (that is, the libraries that the UI code uses).

#### To check if mvn is installed, run
 
```
mvn -v
```
It should print a version number and some other information. If not, then [install Maven](https://maven.apache.org/install.html). Maven is a build system that is the predecessor of Gradle.

### Installing Dependencies 

Run
```
npm install
```

from src/main/resources. It will take a while to install all dependencies needed for UI.

### Run the program!

#### Run the java backend server.

- From the command line: 
Run
```
./gradlew bootrun
```
at the source root of this project.

Running the server prints some information to the console, then will appear to stop (because it is interacting via a webpage). If needed, you can halt the server by typing Ctrl-c.
  
- From IntelliJ: run ConvolutionApplication.java

Note that this step will take a while.

#### Run the React server.

Since the UI is written in React, we need to launch it too

To run the React server, run 

```
npm start
````

from src/main/resources. Once your server is running, visit http://localhost:3000 to view your UI.

You are all done!


## Running the tests

To run the test, simply run

```
./gradlew test
```

## Built With

* [React](https://reactjs.org/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management for UI
* [Gradlew](https://gradle.org/install/) - Dependency Management for the entire project (Actually we use gradlew)

## Authors

* **Haopeng Zhou**
