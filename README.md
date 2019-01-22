#####  master-mind-Game
###### Subject: Projektowanie Aplikacji Internetowych (Web Application Development)
###### Semester: 5
## Mastermind 
**Tools**
- Java SDK 1.8.0_191
- JavaFX
- Java sockets (java.net)
- JUnit 5

## Tasks
- [x] FXML
- [x] Server configuration via XML settings file
- [X] Logic of the game
- [X] Add chat 
- [X] java.net Sockets
- [x] Multi rooms simultaneously
- [x] JUnit 5 partly done

## Rules of Game
There are two players:
  - The code-breaker
  - The code-maker
  
  The code is a sequence of 4 colored pegs chosen from 6 available colors (light blue, pink, blue, yellow, green, red).
The code-breaker has 10 attemps to guess the sequence. After each guess the code-maker (or automatically computer) gives feedback in the form of 2 numbers, the number of pegs that are in the right position and of the right color, and the number of pages that are of the correct color but not in the right position - these numbers are represented by small black and white pegs - indicators

### Useful links
- [Java Architecture for XML Binding](https://javastart.pl/baza-wiedzy/baza-wiedzy/frameworki/jaxb)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/) 

### Problems encounter
- [Serializable - InvalidClassException](https://stackoverflow.com/questions/27489780/getting-a-java-io-invalidclassexception-when-trying-to-save-and-read-a-file)
- [JDK bug - crash on focus loss from dialog on macOS 10.14 Mojave](https://bugs.openjdk.java.net/browse/JDK-8211304)
