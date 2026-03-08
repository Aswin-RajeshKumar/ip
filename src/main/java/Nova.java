import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

public class Nova {

    private static final String FILE_PATH = "./data/nova.txt";
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

        // Load data from hard disk at startup
        taskCount = loadTasks(tasks);
        printWelcome();

        while (true) {
            String input = sc.nextLine().trim();
            Command command = Command.from(input);

            if (command == Command.BYE) break;

            printDivider();
            try {
                taskCount = handleCommand(input, command, tasks, taskCount);
                // Save to hard disk after the task list changes
                saveTasks(tasks, taskCount);
            } catch (NovaException e) {
                System.out.println("     " + e.getMessage());
            }
            printDivider();
        }

        printGoodbye();
        sc.close();
    }

    private void saveTasks(Task[] tasks, int count) {
        try {
            // OS-independent path handling and folder creation
            Files.createDirectories(Paths.get("./data/"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));

            for (int i = 0; i < count; i++) {
                Task t = tasks[i];
                String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
                int done = t.isDone() ? 1 : 0;
                String line = type + " | " + done + " | " + t.getDescription();

                if (t instanceof Deadline) {
                    line += " | " + ((Deadline) t).getBy();
                } else if (t instanceof Event) {
                    line += " | " + ((Event) t).getFrom() + " | " + ((Event) t).getTo();
                }

                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("     OOPS!!! Error saving to file: " + e.getMessage());
        }
    }

    private int loadTasks(Task[] tasks) {
        File f = new File(FILE_PATH);
        if (!f.exists()) return 0;

        int count = 0;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext() && count < MAX_TASKS) {
                String line = s.nextLine();
                try {
                    String[] p = line.split(" \\| ");
                    Task t = null;

                    // Basic parsing and corruption handling (Stretch Goal)
                    switch (p[0]) {
                    case "T" -> t = new Todo(p[2]);
                    case "D" -> t = new Deadline(p[2], p[3]);
                    case "E" -> t = new Event(p[2], p[3], p[4]);
                    }

                    if (t != null) {
                        if (p[1].equals("1")) t.markAsDone();
                        tasks[count++] = t;
                    }
                } catch (Exception e) {
                    System.out.println("     [Warning] Skipping corrupted data line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("     OOPS!!! Error loading file.");
        }
        return count;
    }

    private int handleCommand(String input, Command command, Task[] tasks, int taskCount) throws NovaException {
        switch (command) {
        case LIST -> listTasks(tasks, taskCount);
        case MARK -> markTask(input, tasks, taskCount, true);
        case UNMARK -> markTask(input, tasks, taskCount, false);
        case TODO -> taskCount = addTodo(tasks, taskCount, input);
        case DEADLINE -> taskCount = addDeadline(tasks, taskCount, input);
        case EVENT -> taskCount = addEvent(tasks, taskCount, input);
        default -> throw new NovaException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        return taskCount;
    }

    private int addTodo(Task[] tasks, int taskCount, String input) throws NovaException {
        if (taskCount >= MAX_TASKS) throw new NovaException("Task list full!");
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new NovaException("OOPS!!! The description of a todo cannot be empty.");
        tasks[taskCount] = new Todo(desc);
        taskCount++;
        printAddMessage(tasks[taskCount - 1], taskCount);
        return taskCount;
    }

    private int addDeadline(Task[] tasks, int taskCount, String input) throws NovaException {
        if (taskCount >= MAX_TASKS) throw new NovaException("Task list full!");
        try {
            String[] parts = input.substring(9).split(" /by ");
            tasks[taskCount] = new Deadline(parts[0].trim(), parts[1].trim());
            taskCount++;
            printAddMessage(tasks[taskCount - 1], taskCount);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid format. Use: deadline <desc> /by <time>");
        }
        return taskCount;
    }

    private int addEvent(Task[] tasks, int taskCount, String input) throws NovaException {
        if (taskCount >= MAX_TASKS) throw new NovaException("Task list full!");
        try {
            String[] first = input.substring(6).split(" /from ");
            String[] second = first[1].split(" /to ");
            tasks[taskCount] = new Event(first[0].trim(), second[0].trim(), second[1].trim());
            taskCount++;
            printAddMessage(tasks[taskCount - 1], taskCount);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid format. Use: event <desc> /from <time> /to <time>");
        }
        return taskCount;
    }

    private void markTask(String input, Task[] tasks, int taskCount, boolean markDone) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= taskCount) throw new NovaException("OOPS!!! Invalid task number.");
            if (markDone) tasks[index].markAsDone(); else tasks[index].markAsNotDone();
            System.out.println("     " + (markDone ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:"));
            System.out.println("       " + tasks[index]);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid command format.");
        }
    }

    private void listTasks(Task[] tasks, int taskCount) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i]);
        }
    }

    private void printAddMessage(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:\n       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    private void printWelcome() {
        printDivider();
        System.out.println("     Hello! I'm Nova\n     What can I do for you?");
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