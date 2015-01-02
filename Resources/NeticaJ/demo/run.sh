#!/bin/sh
javac -deprecation -classpath ../bin/NeticaJ.jar  *.java
java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  Demo
rm *.class
