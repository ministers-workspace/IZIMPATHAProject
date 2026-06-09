# Project 4: Configuration File Parser & Validator

## 1. Scenario / Background
Virtually all software applications use configuration files (like `.ini`, `.json`, or `.csv` files) to load user settings on startup. A major problem in software engineering occurs when users manually edit these text files and introduce syntax errors, typos, or incorrect data types (e.g., typing "O" instead of "0"). If the program blindly accepts bad data, it crashes.

Your task is to build a robust Configuration Parser. It will read a simulated raw configuration grid, manipulate the raw `char` data to validate it, use 1D and 2D arrays to map out the settings, and utilize custom exception handling to gracefully reject corrupted settings while keeping the application running.

## 2. Objectives
*   **Design & Problem Solving:** Construct parameterized methods that pass arrays by reference to manipulate configuration grids in memory.
*   **Implementation:** Master the `char` data type, understanding its underlying 16-bit Unicode numeric value, to perform validation and type-checking manually.
*   **Implementation:** Use 2D arrays to map out the configuration grid and nested `for` loops to process the data row by row.
*   **Explanation/Reflection:** Explain how Java handles pass-by-value vs. pass-by-reference for arrays, and how `char` mathematical manipulation works.

## 3. Rules and Constraints
1.  **Personalization Condition:** The configuration grid must be initialized using an `N x M` two-dimensional array, where `N` is the first digit of your student ID and `M` is the last digit (if either is 0, 1, or 2, use 3).
2.  **No External Libraries:** Rely strictly on arrays and native data types. Do not use external parsing libraries (like Jackson or GSON) or `java.util.regex`.
3.  **The Character Constraint:** You must perform mathematical/relational operations directly on the `char` variables (e.g., `if(c >= '0' && c <= '9')`) to validate whether a setting is a number or a letter.
4.  **No Generic AI Answers:** During the oral defense, you must trace exactly how your nested loops traverse your specific 2D array dimensions.

## 4. Required Deliverables
You must submit a single compressed folder containing:
1.  **Planning Notes:** A hand-drawn or text-based grid showing how your 2D array maps the configuration rows and columns. Include one draft of a failed nested loop.
2.  **Source Code:** All `.java` files, fully commented.
3.  **Parsed Output:** A text file containing the output of your program running against a provided test grid with intentional syntax errors.
4.  **Reasoning Document:** A document answering the reflection questions and detailing your array processing choices.

## 5. Milestones & Checkpoints
*   **Milestone 1: Character Mathematics.** Create a method `isNumeric(char c)` that returns `true` if the character is a number. Do NOT use `Character.isDigit()`. You must mathematically compare the char against the Unicode values of '0' and '9'.
*   **Milestone 2: The 2D Config Grid.** Initialize your `N x M` two-dimensional `char[][]` array with simulated configuration values (e.g., `{{'p','o','r','t','=','8'}, {'i','p','=','A'}}`). Write a nested `for` loop that prints this grid in a formatted matrix layout.
*   **Milestone 3: The Validation Engine.** Write a method that accepts the 2D array by reference. Traverse the rows. If the setting requires a number (e.g., the "port" setting), use your `isNumeric()` method to validate it.
*   **Milestone 4: Handling Corruption.** Users will make mistakes. Create a custom `SyntaxParsingException`. If your validation engine encounters an invalid character for a specific setting, `throw` this exception with a descriptive message.
*   **Milestone 5: The Safe Startup.** In your main parsing loop, implement a `try-catch` block. Catch the `SyntaxParsingException`, print a warning (e.g., "Warning: Defaulting port to 8080 due to syntax error"), and use a `continue` statement to skip to the next configuration line without crashing.

## 6. Side Exercises for Hints
*   *Stuck on character math?* **Exercise:** Write a program that assigns `char c = 88;` and prints it. Then assign `char d = 'Y';` and print `(int) d`.
    *   *Hint Unlocked:* Reminds the student that chars are fundamentally 16-bit numeric types in Java.
*   *Stuck on passing arrays?* **Exercise:** Pass an `int[]` to a method, change index 0 inside the method, and print index 0 in `main()` after the method returns.
    *   *Hint Unlocked:* Demonstrates that array references are passed by value, so modifying the array contents inside a method alters the original array.

## 7. Grading Rubric
| Criteria | Excellent (80-100%) | Satisfactory (50-79%) | Needs Work (<50%) |
| :--- | :--- | :--- | :--- |
| **Array Manipulation** | 1D and 2D arrays traversed flawlessly with nested loops. No off-by-one errors. | Arrays work, but loops are hardcoded to specific sizes rather than using `.length`. | `ArrayIndexOutOfBoundsException` occurs. |
| **Data Types** | Perfect manipulation of `char` types using relational operators. | Casts heavily between `int` and `char` unnecessarily or relies on forbidden wrappers. | Treats characters strictly as Strings. |
| **Exception Handling** | Custom exception used perfectly with a jump statement to skip bad config lines. | Exception caught, but program flow is clunky afterward. | Bad data crashes the parser. |
| **Reflection & Process** | Drafts submitted. Deep understanding of pass-by-reference logic shown. | Drafts present. Explanations are generic. | No drafts. Explanations are shallow. |

## 8. Oral Defense Questions
During your viva presentation, be prepared to answer:
1.  *"Explain how Java treats the `char` data type differently from C/C++. Why does Java use 16 bits for a `char`?"*
2.  *"When you passed the `char[][]` array to your parser method, did Java copy the entire array into the method, or did it copy something else? Explain."*
3.  *"If your 2D array is `grid[][]`, how did you programmatically determine the number of rows and the number of columns using the `.length` property?"*

## 9. Extension Challenges (Bonus)
*   Modify the program to handle actual Strings instead of `char[][]` arrays, using `String` slicing methods (`substring`, `indexOf`) to isolate the key and value of the configuration.
*   Implement a `java.io.FileOutputStream` to save the "corrected" and validated configuration out to a new file.

## 10. Instructor-Only Notes
*   **Expected Misconceptions:** Students might try to use `String` manipulation entirely, missing the point of Chapter 1's native data types section. They also frequently confuse `array.length` (a property) with `String.length()` (a method).
*   **The Trap Assumption:** The prompt asks students to manipulate `char` mathematically. A student who relies on generic knowledge might not realize that a `char` is effectively an integer under the hood, and they can directly write `char > 'A'`.
*   **Concepts Tested:** Data Types (`char`, `int`, Unicode), Arrays (1D, 2D, `.length`), Methods (parameter passing, return types), Control Flow (nested loops, `continue`), Exception Handling (custom exceptions).
*   **Anti-Copying Measure:** The personalized 2D array size forces distinct nested loop limits.
