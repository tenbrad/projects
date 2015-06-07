JCC = javac
JFLAGS = -g

.SUFFIXES: .java .class

.java.class:
	$(JCC) $(JFLAGS) $*.java

CLASSES = \
	Board.java \
	Cell.java \
	Circle.java \
	Ball.java \
	Barrier.java \
	Game.java \
	Grid.java \
  Search.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm *.class 
