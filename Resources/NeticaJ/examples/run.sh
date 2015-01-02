#!/bin/sh
javac -deprecation -classpath ../bin/NeticaJ.jar  *.java

#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  BuildNet
java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  ClassifyData
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  DoInference
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  SimulateCases
java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  LearnCPTs
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  LearnLatent
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  MakeDecision
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  TestNet
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  DrawNet "Data Files/ChestClinic_WithVisuals.dne"
#java  -classpath .:../bin/NeticaJ.jar -Djava.library.path=../bin  NetViewer "Data Files"

rm *.class
