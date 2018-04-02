<p align="center">
  <img src="https://user-images.githubusercontent.com/6493590/38195471-7ff6083c-3675-11e8-9c05-c07d5bc094fb.png" alt="CHAIn: Combining Heterogeneous Agencies' Information" />
</p>

## Getting Started
Begin by cloning the repository by running the following in your terminal:

```
$ git clone https://github.com/lewis785/CHAINJava
$ cd CHAINJava
```

Note: [Maven](https://maven.apache.org/install.html) must be installed for the project to build. If you're a Mac user with [Homebrew](https://brew.sh) installed, you can use the `brew install maven` command.

## Gradle
The project uses Gradle as its build tool and for dependency management.  

### Dependencies

While most dependencies are available on a 
public repository, SPSM is not.  A gradle task will execute a script to retrieve them and build them from github sources.

The following task will do this (though it will be automatically done if the project is built and it is not yet 
installed):

```
$ ./gradlew getSPSM
```

### Testing

The tests can be executed using gradle.  There are two test sets: test and fastTest.  The fastTest set only runs a few tests
and is completed much faster than the full test set.

The tests can be run using:

```
$ ./gradlew test
$ ./gradlew fastTest
```

A test report will be available at /build/reports/(test/fastTest)/index/html

### Building

The project can be built from gradle.  This is done using:


```
$ ./gradlew build
```

This will build everything and run the tests.  The tests can be ignored by appending `-x test` and/or `-x fastTest`.
All of the built items will available in the build directory (the jar file will be in build/libs).

### Running

Gradle can be used to run the main program.  This can be done by running

```
$ ./gradlew run
```


## Importing into Eclipse

To import into eclipse:
 
- From the `file`  menu, choose`import`.
- Select `Gradle` then `Gradle Project`.
- Press `Next`, choose the location of the project, then press `Finish`.

The project will now be imported, but will show errors if the `getSPSM` task has not been run.  To fix this in eclipse:

- From the `Gradle Tasks` pane, run `build->getSPSM` and wait of the task to finish.
- In the folder pane, right click on the `build.gradle` file and select `gradle` then `refresh gradle project` - 
this will cause eclipse to refresh the project, this time finding the SPSM dependencies.

## Importing into Intellij

To import into intellij

- Close the current project and select `import`
- Find the location of the project and select the `build.gradle` file

The project should now be imported.  Hover over the icon in the bottom left of the screen and select `Gradle` to view the 
Gradle pane.  If there are errors in the project it will be because SPSM has not been installed yet.

To fix this, run the `getSPSM` task by clicking `Tasks` -> `build` -> `getSPSM` in the Gradle pane.
