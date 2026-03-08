# Nova Task Manager - User Guide

Nova is a **desktop app for managing tasks, optimized for use via a Command Line Interface** (CLI). If you can type fast, Nova can get your task management functions done faster than traditional GUI apps.

## Features

> [!NOTE]
> **Notes about the command format:**
> * Words in `UPPER_CASE` are the parameters to be supplied by the user.
    >   * e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter which can be used as `todo read book`.
> * Items in square brackets are optional.
> * Extraneous parameters for commands that do not take in parameters (such as `list` and `bye`) will be ignored.

---

### Adding a todo: `todo`
Adds a task without any date/time constraint to the list.
* **Format:** `todo DESCRIPTION`
* **Example:** `todo read book`

### Adding a deadline: `deadline`
Adds a task that needs to be done by a specific time.
* **Format:** `deadline DESCRIPTION /by TIME`
* **Example:** `deadline return book /by Sunday`

### Adding an event: `event`
Adds a task that starts and ends at specific times.
* **Format:** `event DESCRIPTION /from START_TIME /to END_TIME`
* **Example:** `event project meeting /from Mon 2pm /to 4pm`

### Listing all tasks: `list`
Shows a list of all tasks currently stored in Nova.
* **Format:** `list`

### Marking a task as done: `mark`
Marks the task at the specified index as completed.
* **Format:** `mark INDEX`
* **Example:** `mark 2`

### Unmarking a task: `unmark`
Reverts the task at the specified index back to "not done" status.
* **Format:** `unmark INDEX`
* **Example:** `unmark 2`

### Locating tasks by keyword: `find`
Finds tasks whose descriptions contain the given keyword.
* **Format:** `find KEYWORD`
* **Example:** `find book`

### Deleting a task: `delete`
Removes the specified task from the list using its index number.
* **Format:** `delete INDEX`
* **Example:** `delete 3`

### Exiting the program: `bye`
Exits the application.
* **Format:** `bye`

---

## FAQ

**Q**: How do I save my data?
**A**: Nova saves your data automatically after every command. There is no need to save manually!