# Project 2: Server Log Anomaly Detector

## 1. Scenario / Background
System administrators and DevOps engineers are often tasked with parsing massive, gigabyte-sized server logs to identify security anomalies, such as brute-force login attempts or sudden spikes in `500 Internal Server Error` responses. 

Your task is to build an automated log parsing engine. It will read simulated log entries, classify them into an object-oriented hierarchy (Info logs, Error logs, Security logs), and use robust control flow to detect patterns that indicate a cyberattack or server meltdown.

## 2. Objectives
*   **Design & Problem Solving:** Create a class hierarchy using Inheritance to represent `LogEntry`, `ErrorLog`, and `SecurityLog`.
*   **Implementation:** Use advanced Control Statements (`switch`, nested `if`s, loops) to parse raw strings into your objects based on error codes and keywords.
*   **Implementation:** Store the parsed logs in a Java Collection (`ArrayList`) and use Iterators to scan for specific anomaly patterns.
*   **Explanation/Reflection:** Explain how Polymorphism allows your anomaly detection loop to treat all logs identically while still extracting specific behaviors.

## 3. Rules and Constraints
1.  **Personalization Condition:** Your anomaly detection threshold must be `A` errors within a 1-minute window, where `A` is the first digit of your student ID plus 4.
2.  **No External Libraries:** Rely strictly on the `java.lang` and `java.util` packages as covered in the Programming 731 study guide. No external JSON parsers or regex libraries (`java.util.regex`) allowed.
3.  **The Parsing Constraint:** You must manually parse the simulated log strings using `String` methods (like `.substring()`, `.indexOf()`) and `char` extraction, combined with a `switch` statement to route the data to the correct constructor.
4.  **No Generic AI Answers:** During the reflection, you must provide specific examples of variables from your code to explain Dynamic Method Dispatch.

## 4. Required Deliverables
You must submit a single compressed folder containing:
1.  **Planning Notes:** A class diagram showing your Inheritance hierarchy. Include a rough flowchart of your `switch`/`if` parsing logic.
2.  **Source Code:** All `.java` files, fully commented.
3.  **Output Logs:** A text file showing the console output of your engine successfully detecting a brute-force attack and a server crash from the test data.
4.  **Reasoning Document:** A PDF or text file answering the reflection questions and detailing your polymorphic choices.

## 5. Milestones & Checkpoints
*   **Milestone 1: The Hierarchy.** Create an abstract `LogEntry` class with a `getSeverity()` method. Create subclasses `ErrorLog` and `SecurityLog` that override `getSeverity()`.
*   **Milestone 2: The Raw Data.** Create an array of raw `String` logs mimicking an Apache or NGINX server (e.g., `"[2023-10-24 10:15:32] ERROR 500 Database Connection Timeout"`, `"[2023-10-24 10:16:01] WARN 401 Failed Login Attempt"`).
*   **Milestone 3: The Parser Engine.** Loop through the raw strings. Use `indexOf()` to extract the severity code (ERROR, WARN, INFO). Use a `switch` statement on this code to instantiate the correct polymorphic object (`ErrorLog`, `SecurityLog`, etc.) and add it to an `ArrayList<LogEntry>`.
*   **Milestone 4: The Anomaly Detector.** Iterate through your `ArrayList`. Write a logic block that tracks the time differences between consecutive `SecurityLog` entries. If it detects `A` failed logins within 60 seconds, print an "ANOMALY DETECTED: BRUTE FORCE" alert.
*   **Milestone 5: Method Overloading.** Add an overloaded method `detectAnomaly(String ipAddress)` to filter your search to a specific attacker, alongside your general `detectAnomaly()` method.

## 6. Side Exercises for Hints
*   *Stuck on String parsing?* **Exercise:** Write a program that takes `String s = "apple-banana-cherry"` and extracts "banana" using only `.indexOf()` and `.substring()`.
    *   *Hint Unlocked:* Reminds the student how to slice strings manually without relying on `.split()`.
*   *Stuck on Polymorphism?* **Exercise:** Create an array of type `Animal` containing a `Dog` and a `Cat`. Loop through the array and call `.makeSound()` on each.
    *   *Hint Unlocked:* Demonstrates that Java automatically calls the overridden subclass method (Dynamic Method Dispatch).

## 7. Grading Rubric
| Criteria | Excellent (80-100%) | Satisfactory (50-79%) | Needs Work (<50%) |
| :--- | :--- | :--- | :--- |
| **Control Flow & Parsing** | Complex string slicing handled elegantly. `switch` statement used cleanly. | Parsing works but relies on massive chains of `if-else` or messy index math. | Parsing fails or crashes on malformed strings. |
| **Polymorphism** | Overridden methods function perfectly. ArrayList holds the superclass but executes subclass behaviors. | Hierarchy exists, but polymorphism is bypassed (e.g., heavily using `instanceof` instead of overriding). | No overriding used. |
| **Anomaly Logic** | Detection logic correctly tracks state across multiple loop iterations to find time-based spikes. | Logic works partially but misses edge cases (e.g., resets counter incorrectly). | Logic is broken or triggers false positives constantly. |
| **Reflection & Process** | Drafts submitted. Deep understanding of Dynamic Method Dispatch shown. | Drafts present. Explanations are generic. | No drafts. Explanations are shallow. |

## 8. Oral Defense Questions
During your viva presentation, be prepared to answer:
1.  *"In your parsing loop, explain exactly how you extracted the timestamp from the string using `.indexOf()`. What happens if the bracket `]` is missing from the log string?"*
2.  *"Explain how Dynamic Method Dispatch allowed your anomaly detector loop to process the `ArrayList<LogEntry>` without needing to check if each item was an `ErrorLog` or a `SecurityLog` first."*
3.  *"Explain the difference between the overloaded `detectAnomaly()` methods you wrote. How does the compiler know which one to execute?"*

## 9. Extension Challenges (Bonus)
*   Implement a `java.io.FileInputStream` to read the raw log data from an actual 100MB text file instead of a hardcoded array. Profile the performance.
*   Implement a custom `MalformedLogException`. If the parser hits a string it cannot parse, throw the exception, catch it, log it to an "error.log" file, and continue parsing the next line.

## 10. Instructor-Only Notes
*   **Expected Misconceptions:** Students often struggle to maintain state across loop iterations (e.g., keeping track of the timestamp of the *previous* log entry to compare with the *current* log entry). They may also attempt to cast objects heavily instead of relying on overridden methods.
*   **The Trap Assumption:** The prompt asks them to use polymorphism and avoid `instanceof` checks if possible. A student who shallowly memorizes code might try to write `if (log instanceof SecurityLog) { ((SecurityLog)log).checkSecurity(); }`, defeating the purpose of dynamic method dispatch taught in Chapter 5.
*   **Concepts Tested:** Control Statements (`switch`, loops), String manipulation, Inheritance, Polymorphism (Overriding, Overloading, Dynamic Method Dispatch), Collection Framework (`ArrayList`).
*   **Anti-Copying Measure:** The personalized variable sets the anomaly threshold. The oral defense questions specifically target the flow of execution, requiring the student to trace *their specific* parsing math to answer correctly.
