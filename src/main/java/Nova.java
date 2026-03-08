import java.util.Scanner;
import java.util.ArrayList;

public class Nova {
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
        printWelcome();

        while (true) {
            String input = sc.nextLine().trim();
            Command command = Command.from(input);
            if (command == Command.BYE) break;

            printDivider();
            try {
                handleCommand(input, command, tasks);
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

    private void deleteTasks(ArrayList<Task> tasks, String input) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) throw new NovaException("OOPS!!! Invalid task number.");
            Task removed = tasks.remove(index);
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + removed);
            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Please provide a valid task number (e.g., delete 1).");
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
            throw new NovaException("OOPS!!! Use format: deadline <desc> /by <time>");
        }
    }

    private void addEvent(ArrayList<Task> tasks, String input) throws NovaException {
        try {
            String[] first = input.substring(6).split(" /from ");
            String[] second = first[1].split(" /to ");
            tasks.add(new Event(first[0].trim(), second[0].trim(), second[1].trim()));
            printAddMessage(tasks.get(tasks.size() - 1), tasks.size());
        } catch (Exception e) {
            throw new NovaException("OOPS!!! Use format: event <desc> /from <time> /to <time>");
        }
    }

    private void markTask(String input, ArrayList<Task> tasks, boolean markDone) throws NovaException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task t = tasks.get(index);
            if (markDone) t.markAsDone(); else t.markAsNotDone();
            System.out.println("     " + (markDone ? "Nice! I've marked this done:" : "OK, I've marked this not done:"));
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

    private void printAddMessage(Task task, int count) {
        System.out.println("     Got it. I've added this task:\n       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
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