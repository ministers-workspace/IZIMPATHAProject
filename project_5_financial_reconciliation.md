# Project 5: Financial Transaction Reconciliation Engine

## 1. Scenario / Background
Small businesses and FinTech startups frequently deal with reconciling financial data. They receive massive, disorganized lists of transactions (credits, debits, refunds) from various payment gateways. The system must process these transactions, apply the correct mathematical rules depending on the transaction type, and flag or remove duplicate entries safely.

Your task is to build a modern, object-oriented Reconciliation Engine. It must dynamically handle a fluctuating number of transactions using the Java Collection Framework, differentiate between transaction behaviors using polymorphism, safely remove duplicate data using Iterators, and archive the final clean ledger to disk.

## 2. Objectives
*   **Design & Problem Solving:** Construct a deeply integrated class hierarchy utilizing Inheritance to represent a general `Transaction`, `CreditTransaction`, and `DebitTransaction`.
*   **Implementation:** Prove mastery of Polymorphism by demonstrating both Method Overriding (modifying reconciliation behavior based on subclass) and Method Overloading (accepting different parameter types for data ingestion).
*   **Implementation:** Utilize `java.util.ArrayList` and `java.util.Iterator` to dynamically manage the ledger and safely remove duplicate objects while traversing.
*   **Explanation/Reflection:** Clearly explain the difference between method overriding and method overloading, referencing your specific FinTech code.

## 3. Rules and Constraints
1.  **Personalization Condition:** Your engine must process an initial batch of exactly `W` transactions, where `W` is your birth day multiplied by 3 (e.g., if born on the 10th, W = 30).
2.  **No External Libraries:** Use only standard Java libraries (`java.util`, `java.io`, `java.lang`) as covered in the Programming 731 study guide.
3.  **The Overload/Override Constraint:** Your `Transaction` superclass must have a method that is both overloaded within the superclass AND overridden in at least one subclass.
4.  **No Generic AI Answers:** During the oral defense, you must justify your use of `Iterator` versus a standard `for` loop for duplicate removal.

## 4. Required Deliverables
You must submit a single compressed folder containing:
1.  **Planning Notes:** A text file detailing your class hierarchy and explicitly pointing out line numbers where overloading and overriding occur.
2.  **Source Code:** All `.java` files, fully commented.
3.  **Output Logs:** A text file showing the console output of the reconciliation process, including the final account balance and notifications of removed duplicates.
4.  **Reasoning Document:** A document answering the reflection questions and detailing your Collection Framework choices.

## 5. Milestones & Checkpoints
*   **Milestone 1: The Transaction Hierarchy.** Create a `Transaction` class with an `applyToBalance(double currentBalance)` method. Create subclasses `CreditTransaction` and `DebitTransaction`. Overload `applyToBalance(double currentBalance, double conversionRate)` in the superclass. Override `applyToBalance()` in the subclasses (Credits add, Debits subtract).
*   **Milestone 2: The Dynamic Ledger.** Create an `Account` class that maintains an `ArrayList<Transaction>`. Add a mix of credit and debit transactions to this list using polymorphic references (e.g., `Transaction t = new CreditTransaction();`).
*   **Milestone 3: The Reconciliation Loop.** Write a method to iterate through the `ArrayList` using an enhanced `for-each` loop to calculate the final account balance.
*   **Milestone 4: Safe Deduplication.** Duplicates occur during data imports. Attempt to remove a duplicate transaction *inside* the `for-each` loop. Observe the crash. Rewrite the logic using a `java.util.Iterator` and its `.remove()` method to safely strip out duplicates.
*   **Milestone 5: Archiving the Ledger.** When reconciliation is complete, use a `FileOutputStream` to append the clean, deduplicated ledger to a `final_ledger.txt` file. Use the `throws` keyword to push the `IOException` handling to the main menu.

## 6. Side Exercises for Hints
*   *Stuck on Overloading vs Overriding?* **Exercise:** Create a class `MathOps` with `double process(double a)`. Add `int process(int a)`. Create a subclass `AdvancedMath` that overrides `double process(double a)`.
    *   *Hint Unlocked:* Visually demonstrates overloading (same class, different parameters) vs overriding (subclass, identical signature).
*   *Stuck on Iterator removal crashes?* **Exercise:** Create an `ArrayList` of strings. Try `for(String s : list) { list.remove(s); }`. Note the `ConcurrentModificationException`.
    *   *Hint Unlocked:* Explains why the enhanced for-loop breaks when the underlying collection's size changes dynamically, and why `Iterator.remove()` is the solution.

## 7. Grading Rubric
| Criteria | Excellent (80-100%) | Satisfactory (50-79%) | Needs Work (<50%) |
| :--- | :--- | :--- | :--- |
| **Polymorphism** | Both overloading and overriding implemented perfectly and explained clearly. | One is implemented well, but the other is missing or confused. | Neither is implemented correctly. |
| **Collections** | `ArrayList` and `Iterator` used flawlessly. No crashes during deduplication. | `ArrayList` used, but removal is handled clunkily (e.g., looping backward) instead of Iterator. | `ConcurrentModificationException` occurs. |
| **File I/O & Exceptions** | `FileOutputStream` works perfectly; `throws` delegates to `main`. | File writes work, but exceptions are swallowed silently. | File writing fails or crashes. |
| **Reflection & Process** | Drafts submitted. Deep understanding of polymorphic references shown. | Drafts present. Explanations are generic. | No drafts. Explanations are shallow. |

## 8. Oral Defense Questions
During your viva presentation, be prepared to answer:
1.  *"Explain the difference between Method Overloading and Method Overriding using your `applyToBalance` method as an example."*
2.  *"Why did you have to use an `Iterator` to remove duplicate transactions from the `ArrayList` in Milestone 4? What error were you trying to avoid?"*
3.  *"When you wrote `Transaction t = new DebitTransaction();`, why did calling `t.applyToBalance()` execute the code in the `DebitTransaction` class rather than the `Transaction` class?"*

## 9. Extension Challenges (Bonus)
*   Implement a `java.util.LinkedList` instead of an `ArrayList` and explain in your comments why a `LinkedList` might be better or worse for a ledger where you frequently insert records in the middle based on timestamps.
*   Implement custom exception `OverdraftException` that is thrown if a `DebitTransaction` causes the running balance to drop below zero.

## 10. Instructor-Only Notes
*   **Expected Misconceptions:** The most common misconception tested here is the difference between Overloading (compile-time polymorphism) and Overriding (run-time polymorphism). Students also frequently struggle with `ConcurrentModificationException` when modifying an `ArrayList` they are currently iterating over.
*   **The Trap Assumption:** The prompt asks students to use polymorphism: `Transaction t = new DebitTransaction()`. Students who memorize code might assume that because the reference variable `t` is of type `Transaction`, any method calls will execute the `Transaction` superclass version of the code. They must understand dynamic method dispatch.
*   **Concepts Tested:** Inheritance, Polymorphism (Overloading & Overriding), Dynamic Method Dispatch, Collection Framework (`ArrayList`, `Iterator`), Exception Handling (`throws`), File I/O (`FileOutputStream`).
*   **Anti-Copying Measure:** The personalized variable sets the exact batch size, making iteration counts unique. The requirement to force a `ConcurrentModificationException` draft ensures they actually experienced the problem rather than copying a finished Iterator solution.
