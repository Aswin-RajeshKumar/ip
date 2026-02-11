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

        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printLine();
        System.out.println("     Hello! I'm Nova");
        System.out.println("     What can I do for you?");
        printLine();

        while (true) {
            String input = sc.nextLine();
            Command command = Command.from(input);

            if (command == Command.BYE) {
                break;
            }

            printLine();

            switch (command) {
            case LIST:
                listTasks(tasks, taskCount);
                break;

            case MARK:
                markTask(tasks, taskCount, input, true);
                break;

            case UNMARK:
                markTask(tasks, taskCount, input, false);
                break;

            case TODO:
                taskCount = addTodo(tasks, taskCount, input);
                break;

            case DEADLINE:
                taskCount = addDeadline(tasks, taskCount, input);
                break;

            case EVENT:
                taskCount = addEvent(tasks, taskCount, input);
                break;

            default:
                System.out.println("     Unknown command.");
            }

            printLine();
        }

        printLine();
        System.out.println("     Bye. Hope to see you again soon!");
        printLine();

        sc.close();
    }

    // =========================
    // LIST
    // =========================
    private static void listTasks(Task[] tasks, int taskCount) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i]);
        }
    }

    // =========================
    // ADD TODO
    // =========================
    private static int addTodo(Task[] tasks, int taskCount, String input) {

        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        String description = input.substring(5);
        tasks[taskCount] = new Todo(description);
        taskCount++;

        printAddMessage(tasks[taskCount - 1], taskCount);
        return taskCount;
    }

    // =========================
    // ADD DEADLINE
    // =========================
    private static int addDeadline(Task[] tasks, int taskCount, String input) {

        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        try {
            String[] parts = input.substring(9).split(" /by ");
            String description = parts[0];
            String by = parts[1];

            tasks[taskCount] = new Deadline(description, by);
            taskCount++;

            printAddMessage(tasks[taskCount - 1], taskCount);

        } catch (Exception e) {
            System.out.println("     Invalid format. Use: deadline <desc> /by <time>");
        }

        return taskCount;
    }

    // =========================
    // ADD EVENT
    // =========================
    private static int addEvent(Task[] tasks, int taskCount, String input) {

        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        try {
            String[] firstSplit = input.substring(6).split(" /from ");
            String description = firstSplit[0];

            String[] secondSplit = firstSplit[1].split(" /to ");
            String from = secondSplit[0];
            String to = secondSplit[1];

            tasks[taskCount] = new Event(description, from, to);
            taskCount++;

            printAddMessage(tasks[taskCount - 1], taskCount);

        } catch (Exception e) {
            System.out.println("     Invalid format. Use: event <desc> /from <time> /to <time>");
        }

        return taskCount;
    }

    // =========================
    // MARK / UNMARK
    // =========================
    private static void markTask(Task[] tasks, int taskCount, String input, boolean markDone) {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;

            if (index < 0 || index >= taskCount) {
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

    // =========================
    // COMMON PRINT METHODS
    // =========================
    private static void printAddMessage(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    private static void printLine() {
        System.out.println("    ____________________________________________________________");
    }
}

