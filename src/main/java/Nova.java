import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;

public class Nova {
    private static final String FILE_PATH = "./data/nova.txt";
    private static final String DIR_PATH = "./data/";

    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE, UNKNOWN;

        static Command from(String input) {
            input = input.trim();
            if (input.equals("list")) return LIST;
            if (input.equals("bye")) return BYE;
            if (input.startsWith("mark ")) return MARK;
            if (input.startsWith("unmark ")) return UNMARK;
            if (input.startsWith("todo")) return TODO;
            if (input.startsWith("deadline")) return DEADLINE;
            if (input.startsWith("event")) return EVENT;
            if (input.startsWith("delete ")) return DELETE;
            return UNKNOWN;
        }
    }

    public static void main(String[] args) {
        new Nova().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        loadTasks(tasks);
        printWelcome();

        while (true) {
            String input = sc.nextLine().trim();
            Command command = Command.from(input);

            if (command == Command.BYE) break;

            printDivider();
            try {
                handleCommand(input, command, tasks);
                saveTasks(tasks);
            } catch (NovaException e) {
                System.out.println("     " + e.getMessage());
            }
            printDivider();
        }

        printGoodbye();
        sc.close();
    }

    private void handleCommand(String input, Command command, ArrayList<Task> tasks) throws NovaException {
        switch (command) {
        case LIST -> listTasks(tasks);
        case MARK -> markTask(input, tasks, true);
        case UNMARK -> markTask(input, tasks, false);
        case TODO -> addTodo(tasks, input);
        case DEADLINE -> addDeadline(tasks, input);
        case EVENT -> addEvent(tasks, input);
        case DELETE -> deleteTasks(tasks, input);
        default -> throw new NovaException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private void saveTasks(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(Paths.get(DIR_PATH));
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
            for (Task t : tasks) {
                String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
                int done = t.isDone() ? 1 : 0;
                String line = type + " | " + done + " | " + t.getDescription();
                if (t instanceof Deadline) line += " | " + ((Deadline) t).getBy();
                if (t instanceof Event) line += " | " + ((Event) t).getFrom() + " | " + ((Event) t).getTo();
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("     Error saving file: " + e.getMessage());
        }
    }

    private void loadTasks(ArrayList<Task> tasks) {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                try {
                    String[] p = line.split(" \\| ");
                    Task t = switch (p[0]) {
                        case "T" -> new Todo(p[2]);
                        case "D" -> new Deadline(p[2], p[3]);
                        case "E" -> new Event(p[2], p[3], p[4]);
                        default -> null;
                    };
                    if (t != null) {
                        if (p[1].equals("1")) t.markAsDone();
                        tasks.add(t);
                    }
                } catch (Exception e) {
                    System.out.println("     [Warning] Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("     Error loading file.");
        }
    }

    private void deleteTasks(ArrayList<Task> tasks, String input) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task removed = tasks.remove(index);
            System.out.println("     Noted. I've removed this task:\n       " + removed);
            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
    }

    private void addTodo(ArrayList<Task> tasks, String input) throws NovaException {
        String desc = input.length() > 4 ? input.substring(5).trim() : "";
        if (desc.isEmpty()) throw new NovaException("OOPS!!! The description of a todo cannot be empty.");
        tasks.add(new Todo(desc));
        printAddMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private void addDeadline(ArrayList<Task> tasks, String input) throws NovaException {
        try {
            String[] parts = input.substring(9).split(" /by ");
            tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
            printAddMessage(tasks.get(tasks.size() - 1), tasks.size());
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid format. Use: deadline <desc> /by <time>");
        }
    }

    private void addEvent(ArrayList<Task> tasks, String input) throws NovaException {
        try {
            String[] first = input.substring(6).split(" /from ");
            String[] second = first[1].split(" /to ");
            tasks.add(new Event(first[0].trim(), second[0].trim(), second[1].trim()));
            printAddMessage(tasks.get(tasks.size() - 1), tasks.size());
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid format. Use: event <desc> /from <time> /to <time>");
        }
    }

    private void markTask(String input, ArrayList<Task> tasks, boolean markDone) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task t = tasks.get(index);
            if (markDone) t.markAsDone(); else t.markAsNotDone();
            System.out.println("     " + (markDone ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:"));
            System.out.println("       " + t);
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
    }

    private void listTasks(ArrayList<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
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