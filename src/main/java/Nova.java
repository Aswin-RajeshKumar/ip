import java.util.Scanner;

public class Nova {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("    ____________________________________________________________");
        System.out.println("     Hello! I'm Nova");
        System.out.println("     What can I do for you?");
        System.out.println("    ____________________________________________________________");

        String[] tasks = new String[100]; // store tasks
        int taskCount = 0; // number of tasks stored

        while (true) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                break; // exit loop without echoing
            }

            System.out.println("    ____________________________________________________________");

            if (input.equals("list")) {
                // print all tasks numbered
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                // store task and echo "added: task"
                if (taskCount < tasks.length) {
                    tasks[taskCount] = input;
                    taskCount++;
                    System.out.println("     added: " + input);
                } else {
                    System.out.println("     Task list full! Cannot add more tasks.");
                }
            }

            System.out.println("    ____________________________________________________________");
        }

        System.out.println("    ____________________________________________________________");
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");

        sc.close();
    }
}
