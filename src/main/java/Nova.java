import java.io.IOException;

public class Nova {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public Nova(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

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
                    storage.save(tasks);
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

    private void handleCommand(String input, Parser.CommandType type) throws NovaException {
        switch (type) {
        case LIST -> listTasks();
        case MARK -> markTask(input, true);
        case UNMARK -> markTask(input, false);
        case TODO -> addTodo(input);
        case DEADLINE -> addDeadline(input);
        case EVENT -> addEvent(input);
        case DELETE -> deleteTask(input);
        default -> throw new NovaException("OOPS!!! I don't know what that means.");
        }
    }

    private void listTasks() {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            try {
                ui.showMessage((i + 1) + "." + tasks.get(i));
            } catch (NovaException ignored) {}
        }
    }

    private void markTask(String input, boolean isDone) throws NovaException {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        Task t = tasks.get(index);
        if (isDone) t.markAsDone(); else t.markAsNotDone();
        ui.showMessage(isDone ? "Nice! I've marked this task as done:" : "OK, I've marked this as not done:");
        ui.showMessage("  " + t);
    }

    private void addTodo(String input) throws NovaException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new NovaException("OOPS!!! Todo description is empty.");
        Todo t = new Todo(desc);
        tasks.add(t);
        printAddMessage(t);
    }

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

    private void deleteTask(String input) throws NovaException {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        Task removed = tasks.remove(index);
        ui.showMessage("Noted. I've removed this task:\n    " + removed);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    private void printAddMessage(Task t) {
        ui.showMessage("Got it. I've added this task:\n    " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    public static void main(String[] args) {
        new Nova("./data/nova.txt").run();
    }
}