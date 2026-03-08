import java.io.IOException;
import java.util.ArrayList;

/**
 * Acts as the main entry point and coordinator for the Nova task management application.
 * It integrates the UI, storage, and task logic components to provide a unified user experience.
 */
public class Nova {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Initializes the application by setting up the UI and storage, and attempting to load
     * existing task data from the specified file path.
     *
     * @param filePath The relative path to the text file where tasks are persisted.
     */
    public Nova(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            // Start with an empty list to allow the user to use the app even if loading fails.
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Starts the main application loop, handling the flow of user input, command execution,
     * and automatic data saving until the user exits.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            ui.showLine();
            try {
                Parser.CommandType commandType = Parser.parse(fullCommand);
                if (commandType == Parser.CommandType.BYE) {
                    isExit = true;
                } else {
                    handleCommand(fullCommand, commandType);
                    storage.save(tasks); // Save after every successful command to prevent data loss.
                }
            } catch (NovaException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("Error saving tasks: " + e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.showGoodbye();
    }

    /**
     * Routes the parsed command type to the appropriate internal logic handler.
     *
     * @param input The raw input string containing command arguments.
     * @param type The identified CommandType from the Parser.
     * @throws NovaException If the command execution fails or the type is unknown.
     */
    private void handleCommand(String input, Parser.CommandType type) throws NovaException {
        switch (type) {
        case LIST -> listTasks();
        case MARK -> markTask(input, true);
        case UNMARK -> markTask(input, false);
        case TODO -> addTodo(input);
        case DEADLINE -> addDeadline(input);
        case EVENT -> addEvent(input);
        case DELETE -> deleteTask(input);
        case FIND -> findTask(input);
        default -> throw new NovaException("OOPS!!! I don't know what that means.");
        }
    }

    /**
     * Searches for and displays tasks that contain the user's provided keyword.
     *
     * @param input The command input containing the search keyword.
     * @throws NovaException If the keyword is missing or empty.
     */
    private void findTask(String input) throws NovaException {
        String keyword = input.length() > 5 ? input.substring(5).trim() : "";
        if (keyword.isEmpty()) {
            throw new NovaException("OOPS!!! The search keyword cannot be empty.");
        }

        ArrayList<Task> found = tasks.findTasks(keyword);
        ui.showMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < found.size(); i++) {
            ui.showMessage((i + 1) + "." + found.get(i));
        }
    }

    /**
     * Displays all tasks currently held in the task list.
     */
    private void listTasks() {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            try {
                ui.showMessage((i + 1) + "." + tasks.get(i));
            } catch (NovaException ignored) {}
        }
    }

    /**
     * Updates the completion status of a specific task based on the user's index input.
     *
     * @param input The command input containing the task index.
     * @param isDone True to mark as done, false to unmark.
     * @throws NovaException If the index is invalid or non-numeric.
     */
    private void markTask(String input, boolean isDone) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task t = tasks.get(index);
            if (isDone) t.markAsDone(); else t.markAsNotDone();
            ui.showMessage(isDone ? "Nice! I've marked this task as done:" : "OK, I've marked this as not done:");
            ui.showMessage("  " + t);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
    }

    /**
     * Adds a simple task with no specific date or time to the list.
     *
     * @param input The raw input string containing the description.
     * @throws NovaException If the description is missing.
     */
    private void addTodo(String input) throws NovaException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new NovaException("OOPS!!! Todo description is empty.");
        Todo t = new Todo(desc);
        tasks.add(t);
        printAddMessage(t);
    }

    /**
     * Adds a task that must be completed by a certain deadline.
     *
     * @param input The input string containing description and "/by" delimiter.
     * @throws NovaException If the format is invalid.
     */
    private void addDeadline(String input) throws NovaException {
        try {
            String[] parts = input.substring(9).split(" /by ");
            Deadline d = new Deadline(parts[0].trim(), parts[1].trim());
            tasks.add(d);
            printAddMessage(d);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Use: deadline <desc> /by <time>");
        }
    }

    /**
     * Adds a task that occurs during a specific time frame.
     *
     * @param input The input string containing description, "/from", and "/to" delimiters.
     * @throws NovaException If the format is invalid.
     */
    private void addEvent(String input) throws NovaException {
        try {
            String[] first = input.substring(6).split(" /from ");
            String[] second = first[1].split(" /to ");
            Event e = new Event(first[0].trim(), second[0].trim(), second[1].trim());
            tasks.add(e);
            printAddMessage(e);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Use: event <desc> /from <time> /to <time>");
        }
    }

    /**
     * Removes a task from the list using its 1-based index.
     *
     * @param input The command string containing the index.
     * @throws NovaException If the index is out of bounds or invalid.
     */
    private void deleteTask(String input) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task removed = tasks.remove(index);
            ui.showMessage("Noted. I've removed this task:\n    " + removed);
            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
    }

    /**
     * Standardizes the output message shown whenever a new task is successfully created.
     *
     * @param t The task that was just added.
     */
    private void printAddMessage(Task t) {
        ui.showMessage("Got it. I've added this task:\n    " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    public static void main(String[] args) {
        new Nova("./data/nova.txt").run();
    }
}