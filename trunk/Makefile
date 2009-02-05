all: ConcSecureSystem.java UnitTests.java
	@javac -classpath /lusr/share/lib/java/junit/junit-4.5.jar ConcSecureSystem.java UnitTests.java

run: ConcSecureSystem.class
	@java ConcSecureSystem

test: UnitTests.class
	@java -classpath /lusr/share/lib/java/junit/junit-4.5.jar:. org.junit.runner.JUnitCore UnitTests
