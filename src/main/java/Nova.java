import java.util.Scanner;

public class Nova {

    private static final int MAX_TASKS = 100;

    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, BYE, UNKNOWN;

        static Command from(String input) {
            input = input.trim();
            if (input.equals("list")) return LIST;
            if (input.equals("bye")) return BYE;
            if (input.startsWith("mark ")) return MARK;
            if (input.startsWith("unmark ")) return UNMARK;
            if (input.startsWith("todo")) return TODO;
            if (input.startsWith("deadline")) return DEADLINE;
            if (input.startsWith("event")) return EVENT;
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
            String input = sc.nextLine().trim();
            Command command = Command.from(input);

            if (command == Command.BYE) break;

            printDivider();
            taskCount = handleCommand(input, command, tasks, taskCount);
            printDivider();
        }

        printGoodbye();
        sc.close();
    }

    private int handleCommand(String input, Command command, Task[] tasks, int taskCount) {
        switch (command) {
        case LIST -> listTasks(tasks, taskCount);
        case MARK -> markTask(input, tasks, taskCount, true);
        case UNMARK -> markTask(input, tasks, taskCount, false);
        case TODO -> taskCount = addTodo(tasks, taskCount, input);
        case DEADLINE -> taskCount = addDeadline(tasks, taskCount, input);
        case EVENT -> taskCount = addEvent(tasks, taskCount, input);
        default -> System.out.println("     OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        return taskCount;
    }

    private int addTodo(Task[] tasks, int taskCount, String input) {
        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) {
            System.out.println("     OOPS!!! The description of a todo cannot be empty.");
            return taskCount;
        }

        tasks[taskCount] = new Todo(desc);
        taskCount++;
        printAddMessage(tasks[taskCount - 1], taskCount);
        return taskCount;
    }

    private int addDeadline(Task[] tasks, int taskCount, String input) {
        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        try {
            String[] parts = input.substring(9).split(" /by ");
            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty() || by.isEmpty()) {
                System.out.println("     OOPS!!! The description and date/time of a deadline cannot be empty.");
                return taskCount;
            }

            tasks[taskCount] = new Deadline(desc, by);
            taskCount++;
            printAddMessage(tasks[taskCount - 1], taskCount);
        } catch (Exception e) {
            System.out.println("     OOPS!!! Invalid format. Use: deadline <desc> /by <time>");
        }

        return taskCount;
    }

    private int addEvent(Task[] tasks, int taskCount, String input) {
        if (taskCount >= MAX_TASKS) {
            System.out.println("     Task list full! Cannot add more tasks.");
            return taskCount;
        }

        try {
            String[] first = input.substring(6).split(" /from ");
            String desc = first[0].trim();
            String[] second = first[1].split(" /to ");
            String from = second[0].trim();
            String to = second[1].trim();

            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                System.out.println("     OOPS!!! The description, start and end time of an event cannot be empty.");
                return taskCount;
            }

            tasks[taskCount] = new Event(desc, from, to);
            taskCount++;
            printAddMessage(tasks[taskCount - 1], taskCount);
        } catch (Exception e) {
            System.out.println("     OOPS!!! Invalid format. Use: event <desc> /from <time> /to <time>");
        }

        return taskCount;
    }

    private void markTask(String input, Task[] tasks, int taskCount, boolean markDone) {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= taskCount) {
                System.out.println("     OOPS!!! Invalid task number.");
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
            System.out.println("     OOPS!!! Invalid command format.");
        }
    }

    private void listTasks(Task[] tasks, int taskCount) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i]);
        }
    }

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

