import java.util.Scanner;

public class Nova {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[100]; // max 100 tasks
        int taskCount = 0;

        System.out.println("    ____________________________________________________________");
        System.out.println("     Hello! I'm Nova");
        System.out.println("     What can I do for you?");
        System.out.println("    ____________________________________________________________");

        while (true) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                break;
            }

            System.out.println("    ____________________________________________________________");

            if (input.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsDone();
                    System.out.println("     Nice! I've marked this task as done:");
                    System.out.println("       " + tasks[index]);
                }
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsNotDone();
                    System.out.println("     OK, I've marked this task as not done yet:");
                    System.out.println("       " + tasks[index]);
                }
            } else { // add a new task
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("     added: " + input);
            }

            System.out.println("    ____________________________________________________________");
        }

        System.out.println("    ____________________________________________________________");
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");

        sc.close();
    }
}

class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " "; // X for done, blank for not done
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
