# README

This is a toy implementation of the famous Tetris Game as a Java desktop app using Processing 3 graphic library.

The code repo is: https://github.com/alfre2v/tetris-toy

## Author

Alfredo Valles Valdes
My Github: https://github.com/alfre2v
email: {my first name} {dot} valles {at} gmail.com

## License

This work is distributed under the GNU GENERAL PUBLIC LICENSE Version 3
Refer to file LICENSE

## Why coding a Tetris Game in Java?

Tetris is a very interesting game, it has a few simple rules and almost everybody has played Tetris 
and is familiar with its game rules.

Designing a Tetris game is a problem that come up from time to time in job interviews,
the interviewer may use it to assess the candidate's understanding of object oriented principles.
The logic of the game is simple enough so that a high level design of the game can be done relatively fast,
but the actual implementation is a little more involved that it may seem.

I decided to implement a full version of Tetris in a strong object oriented language (Java) 
to better appreciate the design choices I made, its advantages and shortcomings.

## Links

Quick guide to run processing in Java: http://happycoding.io/tutorials/java/processing-in-java


## Using Processing3 as a Java library

Copy Processing libraries to our local project libs folder (Gradle adds this to our classpath).

1. Locate the installation directory of Processing (for MacOSX: /Applications/Processing.app/Contents/Java/).
 Also locate your local project libs folder (Gradle will add this to Java classpath).

```
$ PROCESSING_HOME="/Applications/Processing.app/Contents/Java"
$ PROCESSING_LIBS="${PROCESSING_HOME}/core/library"
$ GRADLE_LIBS="/Users/alfredo/workspace/processing_tetris/tetris-toy/libs"
```

2. Copy relevant libs from $PROCESSING_LIBS to $GRADLE_LIBS

```
$ cd $PROCESSING_LIBS
$ cp \
      core.jar \
      gluegen-rt-natives-macosx-universal.jar \
      gluegen-rt.jar \
      jogl-all-natives-macosx-universal.jar \
      jogl-all.jar \
  ${GRADLE_LIBS}

```

