# Project 3: Thread-Safe In-Memory Key-Value Store

## 1. Scenario / Background
High-performance backend applications (like web servers or game servers) often need a place to cache data temporarily without constantly hitting a slow hard drive. Real-world systems use tools like Redis or Memcached for this.

Your task is to build a custom, thread-safe Key-Value store in Java. It must allow multiple threads (simulating different users) to concurrently write, read, and delete data without corrupting the memory. Additionally, it must have a background "daemon" thread that periodically saves snapshots of the data to disk, and it must gracefully handle object destruction via Garbage Collection concepts.

## 2. Objectives
*   **Design & Problem Solving:** Construct a robust `CacheStore` class demonstrating mastery of the Collection Framework and encapsulation.
*   **Implementation:** Use the `synchronized` keyword correctly to protect the cache from race conditions during simultaneous reads/writes.
*   **Implementation:** Implement a background `Thread` that runs continuously, writing the cache state to a file using Byte/Character Streams.
*   **Explanation/Reflection:** Explain Java's Garbage Collection process, variable shadowing, and the usage of the `this` keyword within your cache nodes.

## 3. Rules and Constraints
1.  **Personalization Condition:** Your cache must simulate exactly `C` concurrent user threads writing to the database simultaneously, where `C` is the sum of the digits of your student ID (minimum 5).
2.  **No External Libraries:** Use only standard Java libraries (`java.util`, `java.io`, `java.lang`) as covered in the Programming 731 study guide.
3.  **The Static/Instance Constraint:** You must use at least one `static` variable to track the total memory operations performed globally across the entire system, while keeping the actual cache data as instance variables.
4.  **No Generic AI Answers:** Code comments explaining the `synchronized` keyword must specifically reference why the monitor prevents data loss in your specific `put()` method.

## 4. Required Deliverables
You must submit a single compressed folder containing:
1.  **Planning Notes:** A text file detailing why you made certain fields `static` and others instance variables. Show one failed draft of your synchronization strategy.
2.  **Source Code:** All `.java` files, fully commented.
3.  **Execution Logs:** Output showing the `C` concurrent threads successfully writing data, and proof that the background thread saved the `snapshot.txt` file.
4.  **Reasoning Document:** A document answering the reflection questions and explaining your use of Garbage Collection concepts.

## 5. Milestones & Checkpoints
*   **Milestone 1: The Cache Node.** Create a `CacheEntry` class. It must have instance variables for `key`, `value`, and `timestamp`. Create a constructor that takes these parameters and uses the `this` keyword to resolve naming conflicts.
*   **Milestone 2: The Data Store.** Create the `CacheStore` class. Give it an `ArrayList<CacheEntry>` (or another collection) to store the data. Write `put(key, value)` and `get(key)` methods. Add a `private static int totalOperations` counter.
*   **Milestone 3: The Thread Conflict.** Create a `UserThread` class (`implements Runnable`) that loops 100 times, adding random keys to the `CacheStore`. Start `C` user threads at once. Notice the `ConcurrentModificationException` or lost data.
*   **Milestone 4: Synchronization.** Implement a `synchronized` block or method around your `put()` and `get()` logic to ensure the cache size is perfectly accurate and no data is lost during the concurrency storm.
*   **Milestone 5: Background Snapshot.** Create a `SnapshotThread` that wakes up every 2 seconds (`Thread.sleep()`). When awake, it securely reads the cache and writes all entries to `snapshot.txt` using a `FileOutputStream`. 

## 6. Side Exercises for Hints
*   *Stuck on static variables?* **Exercise:** Create a class with a `static int count` and an `int instanceCount`. Increment both in the constructor. Create 5 objects and print both variables for each.
    *   *Hint Unlocked:* Visually demonstrates the difference between class variables (shared) and instance variables (independent).
*   *Stuck on Threads crashing?* **Exercise:** Write a `for` loop that iterates over an ArrayList. Inside the loop, try to `.remove()` an item from that same ArrayList.
    *   *Hint Unlocked:* Explains why modifying a collection while another process is reading it causes a crash, highlighting the need for synchronization monitors.

## 7. Grading Rubric
| Criteria | Excellent (80-100%) | Satisfactory (50-79%) | Needs Work (<50%) |
| :--- | :--- | :--- | :--- |
| **Concurrency & Sync** | Threads run perfectly. `synchronized` is used minimally but effectively. Snapshot thread doesn't lock out users forever. | Threads run, but synchronization is clunky, causing massive bottlenecks or occasional crashes. | Threads crash or fail to synchronize the writes. |
| **Class Architecture** | Perfect use of `static`, `this`, and constructors. Encapsulation is strictly followed. | Classes function, but `static` or `this` is used improperly or redundantly. | Poor class design, public fields used instead of encapsulation. |
| **File I/O** | Snapshot thread successfully dumps state to disk securely. | File writing works but occasionally throws IOExceptions due to thread clashes. | File writing fails entirely. |
| **Reflection & Process** | Drafts submitted. Deep understanding of instance vs class variables shown. | Drafts present. Explanations are generic but correct. | No drafts. Explanations are shallow or missing. |

## 8. Oral Defense Questions
During your viva presentation, be prepared to answer:
1.  *"Why did you use the `this` keyword in your `CacheEntry` constructor? What would happen if you removed it, assuming the parameter names stayed the same?"*
2.  *"Explain the difference between a `static` variable and an instance variable. Why was `totalOperations` made static, but the cache array was not?"*
3.  *"When your `SnapshotThread` is writing to the disk, what stops a `UserThread` from modifying the cache array at the exact same millisecond?"*

## 9. Extension Challenges (Bonus)
*   **Garbage Collection Trap:** Override the `finalize()` method in the `CacheEntry` class to print a message when an entry is evicted and destroyed. Create a loop that generates thousands of temporary cache objects to force the Garbage Collector to run.
*   Use `wait()` and `notify()` to create a scenario where the Snapshot thread only runs if a certain number of new `put()` operations have occurred.

## 10. Instructor-Only Notes
*   **Expected Misconceptions:** Students often treat `static` variables as "constants" rather than shared class variables. They may struggle with synchronizing the `SnapshotThread` safely without locking up the entire cache for a long period while disk I/O happens.
*   **The Trap Assumption:** Students might assume that `Collections.synchronizedList()` (if they find it online) solves all their problems, completely bypassing the manual monitor creation taught in Chapter 7. The defense questions force them to explain the monitor mechanics.
*   **Concepts Tested:** Classes, Constructors, `this`, `static` (variables), Multithreading (`Runnable`, `Thread.sleep`, `synchronized`), File I/O, variable shadowing.
*   **Anti-Copying Measure:** The personalized variable sets the thread storm size. The concurrency architecture is difficult to fake without understanding the underlying monitor mechanics.
