AUTOMATED LOG PARSING ENGINE

How to open in VS Code:

1. Open VS Code.
2. Click File > Open Folder.
3. Select /home/ayanda/LogParsingEngineProject.
4. Open the src folder.

How to run in the VS Code terminal:

1. Open Terminal > New Terminal.
2. Compile:

   javac src/*.java

3. Run with the first digit of your student ID:

   java -cp src LogEngine 3

   Replace 3 with the real first digit of your student ID.

4. Save output for submission:

   java -cp src LogEngine 3 > output/OutputLogs.txt

Deliverables in this folder:

- PlanningNotes.txt
- ReasoningDocument.txt
- src/*.java
- output/OutputLogs.txt after running the command above

Important:

The Java source uses only java.lang and java.util. It does not use external
libraries, JSON parsers, or java.util.regex.
