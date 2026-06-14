# Multi-threaded Local Search & Indexing Engine (CLI)

A small command-line tool written in **pure Java** (no external libraries) that
searches a folder for a word or phrase. It scans many files at once using
multiple threads, prints every matching line, and saves the results to a CSV
file.

Give it a **folder** and a **search term**, and it will find every line in every
`.txt` and `.java` file (including sub-folders) that contains that term.

---

## What's in this folder

| File | What it is |
| :--- | :--- |
| `SearchEngine.java` | Main program. Finds the files, splits them across threads, prints results, writes the CSV. |
| `SearchWorker.java` | A single worker thread. Scans its assigned files and safely adds matches to the shared list. |
| `SearchResult.java` | A small data object for one match (file name, line number, matched text). |
| `*.class` | Already-compiled versions of the three files above (so you can run without compiling). |
| `testdir/` | 22 sample text files you can use to try the tool right away. |
| `results.csv` | Output from the most recent run (`FileName,LineNumber,Snippet`). Overwritten each run. |
| `output_log.txt` | A saved copy of a previous console run. |
| `project_1_search_engine.md` | The original assignment brief this code was written for (reference only). |

---

## Requirements

- **Java 17 or newer** installed. Check with:
  ```bash
  java -version
  ```
- If you also want to recompile, you need the full **JDK** (which includes
  `javac`), not just the runtime.

---

## How to run it

First, open a terminal **inside this folder** (the one containing
`SearchEngine.java`):

```bash
cd path\to\project_1_search_engine
```

### The basic command

```bash
java -cp . SearchEngine <folderToSearch> <searchTerm>
```

- `<folderToSearch>` — the folder to search (sub-folders are included automatically)
- `<searchTerm>` — the exact text to look for (**case-sensitive**)

### Try it right now with the sample data

```bash
java -cp . SearchEngine testdir TODO
```

This searches the included `testdir` folder for the word `TODO`.

### Search this whole project folder

```bash
java -cp . SearchEngine . main
```

(`.` means "the current folder")

---

## Recompiling (optional)

The `.class` files are already built, so you usually don't need this. But if you
change any `.java` file, rebuild with:

```bash
javac SearchEngine.java SearchWorker.java SearchResult.java
```

Then run it again with the command above.

> **No `javac`?** If you only have the Java runtime, you can still run straight
> from source on Java 11+ (it compiles in memory):
> ```bash
> java SearchEngine.java testdir TODO
> ```

---

## What you'll see

```text
Found 22 files under 'testdir'.
Searching for: "TODO" using 4 worker thread(s)...

--- Search Complete ---
Total matches found: 22
------------------------

file1.txt:1: line 1 has a TODO marker
file2.txt:1: line 2 has a TODO marker
...

Results written to results.csv
```

Each result line is `fileName:lineNumber: the matching line`. The same matches
are also saved to `results.csv`.

> The order of results changes from run to run because several threads report
> their findings at the same time. This is normal.

---

## How it works (quick overview)

1. **Find files** — walks the folder (and sub-folders) and keeps every `.txt`
   and `.java` file.
2. **Split the work** — divides those files into groups, one group per worker
   thread.
3. **Search in parallel** — each `SearchWorker` reads its files line-by-line and
   collects matches.
4. **Combine safely** — workers add their matches to one shared list inside a
   `synchronized` block, so nothing gets lost when threads run at the same time.
5. **Report** — the main program waits for all threads to finish, prints the
   total, and writes `results.csv`.

If a file can't be read (e.g. no permission), the tool prints a warning and
skips it instead of crashing.

---

## Configuration

These are simple edits in `SearchEngine.java` (recompile afterwards):

- **Number of threads:** change `private static final int THREAD_COUNT = 4;`
- **File types searched:** change the `.txt` / `.java` check inside
  `collectFiles()`
- **Output file name:** change the `"results.csv"` value in `main`
