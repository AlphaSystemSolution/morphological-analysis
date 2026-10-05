SBT = sbt

compile:
	$(SBT) compile

clean:
	$(SBT) clean

test:
	$(SBT) test

all: clean build test

tools-assembly:
	$(SBT) tools-assembly

morphologicalengine-ui-run:
	$(SBT) morphologicalengine-ui-run

fontawesome-app-run:
	$(SBT) fontawesome-app-run
	