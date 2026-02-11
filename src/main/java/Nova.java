import java.util.Scanner;

public class Nova {

    private static final int MAX_TASKS = 100;

    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, BYE, UNKNOWN;

        static Command from(String input) {
            if (input.equals("list")) return LIST;
            if (input.equals("bye")) return BYE;
            if (input.startsWith("mark ")) return MARK;
            if (input.startsWith("unmark ")) return UNMARK;
            if (input.startsWith("todo ")) return TODO;
            if (input.startsWith("deadline ")) return DEADLINE;
            if (input.startsWith("event ")) return EVENT;
            return UNKNOWN;
        }
    }

    public static void main(String[] args) {
        new Nova().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printWelcome();

        while (true) {
            String input = sc.nextLine();
            Command command = Command.from(input);

            if (command == Command.BYE) {
                break;
            }

            printDivider();
            taskCount = handleCommand(input, command, tasks, taskCount);
            printDivider();
        }

        printGoodbye();
        sc.close();
    }

    // =========================
    // MAIN COMMAND HANDLER
    // =========================
    private int handleCommand(String input, Command command, Task[] tasks, int taskCount) {

        switch (command) {
        case LIST:
            listTasks(tasks, taskCount);
            return taskCount;

        case MARK:
            markTask(input, tasks, taskCount, true);
            return taskCount;

        case UNMARK:
            markTask(input, tasks, taskCount, false);
            return taskCount;

        case TODO:
            return addTodo(tasks, taskCount, input);

        case DEADLINE:
            return addDeadline(tasks, taskCount, input);

        case EVENT:
            return addEvent(tasks, taskCount, input);

        default:
            System.out.println("     Unknown command.");
            return taskCount;
        }
    }

    // =========================
    // TASK OPERATIONS
    // =========================
    private int addTodo(Task[] tasks, int taskCount, String input) {
        if (isTaskListFull(taskCount)) return taskListFull(taskCount);

        String description = input.substring(5).trim();
        return storeTask(tasks, taskCount, new Todo(description));
    }

    private int addDeadline(Task[] tasks, int taskCount, String input) {
        if (isTaskListFull(taskCount)) return taskListFull(taskCount);

        try {
            String[] parts = input.substring(9).split(" /by ");
            String description = parts[0].trim();
            String by = parts[1].trim();
            return storeTask(tasks, taskCount, new Deadline(description, by));
        } catch (Exception e) {
            System.out.println("     Invalid format. Use: deadline <desc> /by <time>");
            return taskCount;
        }
    }

    private int addEvent(Task[] tasks, int taskCount, String input) {
        if (isTaskListFull(taskCount)) return taskListFull(taskCount);

        try {
            String[] firstSplit = input.substring(6).split(" /from ");
            String description = firstSplit[0].trim();
            String[] secondSplit = firstSplit[1].split(" /to ");
            String from = secondSplit[0].trim();
            String to = secondSplit[1].trim();
            return storeTask(tasks, taskCount, new Event(description, from, to));
        } catch (Exception e) {
            System.out.println("     Invalid format. Use: event <desc> /from <time> /to <time>");
            return taskCount;
        }
    }

    private void markTask(String input, Task[] tasks, int taskCount, boolean markDone) {
        try {
            int index = parseIndex(input);

            if (!isValidIndex(index, taskCount)) {
                System.out.println("     Invalid task number.");
                return;
            }

            if (markDone) {
                tasks[index].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
            } else {
                tasks[index].markAsNotDone();
                System.out.println("     OK, I've marked this task as not done yet:");
            }

            System.out.println("       " + tasks[index]);

        } catch (Exception e) {
            System.out.println("     Invalid command format.");
        }
    }

    private int storeTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        taskCount++;
        printAddMessage(task, taskCount);
        return taskCount;
    }

    private boolean isTaskListFull(int taskCount) {
        return taskCount >= MAX_TASKS;
    }

    private int taskListFull(int taskCount) {
        System.out.println("     Task list full! Cannot add more tasks.");
        return taskCount;
    }

    private boolean isValidIndex(int index, int taskCount) {
        return index >= 0 && index < taskCount;
    }

    private int parseIndex(String input) {
        String[] parts = input.split(" ");
        return Integer.parseInt(parts[1]) - 1;
    }

    private void listTasks(Task[] tasks, int taskCount) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i]);
        }
    }

    // =========================
    // PRINT HELPERS
    // =========================
    private void printAddMessage(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    private void printWelcome() {
        printDivider();
        System.out.println("     Hello! I'm Nova");
        System.out.println("     What can I do for you?");
        printDivider();
    }

    private void printGoodbye() {
        printDivider();
        System.out.println("     Bye. Hope to see you again soon!");
        printDivider();
    }

    private void printDivider() {
        System.out.println("    ____________________________________________________________");
    }
}
