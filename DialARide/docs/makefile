JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
	CabLocationModel.java \
	RequestModel.java \
	CabShareModel.java \
        floyd.java \
        InputHandelController.java \
        ProcessController.java \
	Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class