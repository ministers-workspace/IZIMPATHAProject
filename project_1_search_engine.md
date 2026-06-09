# Project 1: Multi-threaded Local Search & Indexing Engine (CLI Tool)

## 1. Scenario / Background
In software engineering, quickly finding a specific string (like a function name or error code) across thousands of text and source code files is critical. Existing tools like `grep` are fast, but your team wants a custom Java-based CLI (Command Line Interface) tool that not only searches but builds an in-memory index of hits.

Your task is to build a high-performance, multi-threaded search engine. It will accept a directory path and a search term, spawn multiple threads to read and scan files concurrently, and safely aggregate the results into a central index without race conditions causing missing data.

## 2. Objectives
*   **Design & Problem Solving:** Architecture a `SearchWorker` class that implements `Runnable` to handle the I/O bottleneck of reading files.
*   **Implementation:** Utilize `java.io.FileInputStream` or `java.io.FileReader` alongside `BufferedReader` to process text files line-by-line efficiently.
*   **Implementation:** Master thread safety by using the `synchronized` keyword to allow multiple worker threads to safely append their findings to a shared `ArrayList` of results.
*   **Explanation/Reflection:** Explain the concept of thread context-switching and why multithreading drastically improves performance for I/O bound tasks like file reading.

## 3. Rules and Constraints
1.  **Personalization Condition:** Your search engine must spawn exactly `T` worker threads, where `T` is the last digit of your student ID (if 0 or 1, use 2).
2.  **No External Libraries:** You must build the threading and file I/O logic using only `java.lang`, `java.util`, and `java.io`. No external indexing libraries (like Apache Lucene) are permitted.
3.  **The Resiliency Constraint:** The engine must NOT crash if it encounters a file it doesn't have permission to read or a corrupted binary file. You must use `try-catch` blocks to catch `IOException`s, log a warning to the console, and gracefully continue to the next file.
4.  **No Generic AI Answers:** Code comments explaining your synchronization strategy must reference the exact variables in your code that are acting as the monitor/lock.

## 4. Required Deliverables
You must submit a single compressed folder containing:
1.  **Planning Notes:** A diagram showing the main thread dispatching tasks to worker threads, and how they synchronize back to the main list. Include one failed code snippet where synchronization was broken.
2.  **Source Code:** All `.java` files, fully commented.
3.  **Output Logs:** A screenshot or text file showing the tool's console output after successfully searching a directory containing at least 20 text files.
4.  **Reasoning Document:** A document answering the reflection questions and detailing your concurrency choices.

## 5. Milestones & Checkpoints
*   **Milestone 1: The File Scanner.** Write a standard (single-threaded) method that takes a `File` object and a `String` target. Open the file using a Character Stream, read it line-by-line, and return the line numbers where the target is found.
*   **Milestone 2: The Data Structure.** Create a `SearchResult` class to hold the filename, line number, and the text snippet of the match. Create a `sharedResults` `ArrayList` in your main engine class.
*   **Milestone 3: Enter Multithreading.** Create the `SearchWorker` class (`implements Runnable`). Pass it a subset of files to scan. 
*   **Milestone 4: The Synchronization Trap.** Start your `T` threads simultaneously. Have them all write to `sharedResults`. You will likely encounter lost data or a `ConcurrentModificationException`. Implement a `synchronized` block or method to lock the `sharedResults` list during the add operation.
*   **Milestone 5: Graceful Failure.** Create a "poison pill" file (e.g., remove read permissions from a text file). Ensure your `catch(IOException e)` block intercepts the error, prints "Skipped unreadable file: [name]", and prevents the thread from terminating.

## 6. Side Exercises for Hints
*   *Stuck on File I/O?* **Exercise:** Write a 5-line program that reads `test.txt` and prints it to the console using a `try-with-resources` block (or `try-catch-finally` closing the stream).
    *   *Hint Unlocked:* Reminds the student how to construct a `BufferedReader` and handle the required `IOException`.
*   *Stuck on Threads crashing the list?* **Exercise:** Create an `ArrayList`. Start two threads that both run a loop adding 1000 items to it. Print the final size (it won't be 2000). 
    *   *Hint Unlocked:* Demonstrates race conditions and unlocks the syntax for the `synchronized(list)` block.

## 7. Grading Rubric
| Criteria | Excellent (80-100%) | Satisfactory (50-79%) | Needs Work (<50%) |
| :--- | :--- | :--- | :--- |
| **Concurrency** | Threads implemented perfectly. `synchronized` blocks are extremely tight (only locking the add operation, not the file reading). | Threads work, but the entire `run` method is synchronized, destroying the benefit of multithreading. | Threads crash or fail to synchronize, losing search results. |
| **File I/O & Exceptions** | Character streams used efficiently. Unreadable files are skipped gracefully without crashing. | Files are read, but streams aren't closed properly (memory leak) or exceptions cause the thread to die. | Cannot read files or program crashes on bad files. |
| **Data Structures** | `ArrayList` and custom objects used effectively to store results. | Uses primitive arrays with hardcoded limits that break easily. | Fails to aggregate results. |
| **Reflection & Process** | Drafts submitted. Deep understanding of I/O bounds vs CPU bounds shown. | Drafts present. Explanations are generic. | No drafts. Explanations are shallow. |

## 8. Oral Defense Questions
During your viva presentation, be prepared to answer:
1.  *"Show me your `synchronized` block. Why did you choose to lock that specific object, and what would happen to performance if you synchronized the entire file-reading process instead?"*
2.  *"What is the difference between a Byte Stream and a Character Stream, and why is a Character Stream better suited for a text search engine?"*
3.  *"Explain how your program ensures that the main thread waits for all worker threads to finish before printing the final results. Did you use `Thread.join()`?"*

## 9. Extension Challenges (Bonus)
*   Implement a recursive directory crawler that finds all `.txt` or `.java` files in a folder and its subfolders before dispatching them to the worker threads.
*   Add a feature to write the final aggregated search index out to a `.csv` file using `FileOutputStream`.

## 10. Instructor-Only Notes
*   **Expected Misconceptions:** Students often over-synchronize. They might put the `synchronized` keyword on the entire `run()` method of the thread. This makes the threads run sequentially instead of concurrently, defeating the purpose.
*   **The Trap Assumption:** Students might assume `ArrayList.add()` is inherently thread-safe. They must experience the data loss/corruption first-hand to understand the necessity of the monitor concept taught in Chapter 7.
*   **Concepts Tested:** Multithreading (`Runnable`, `Thread`, `join`), Synchronization (Monitors, `synchronized`), File I/O (`Reader`, `BufferedReader`, `IOException`), Collections (`ArrayList`).
*   **Anti-Copying Measure:** The thread count `T` is unique per student. The requirement to defend the exact placement of the `synchronized` block prevents them from copying a StackOverflow answer without understanding it.
