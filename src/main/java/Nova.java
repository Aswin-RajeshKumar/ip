import java.util.Scanner;

public class Nova {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("    ____________________________________________________________");
        System.out.println("     Hello! I'm Nova");
        System.out.println("     What can I do for you?");
        System.out.println("    ____________________________________________________________");

        String input = "";
        while (true) {
            input = sc.nextLine(); // read user input
            if (input.equals("bye")) {
                break; // exit loop without echoing
            }
            System.out.println("    ____________________________________________________________");
            System.out.println("     " + input); // echo the command with proper indentation
            System.out.println("    ____________________________________________________________");
        }

        System.out.println("    ____________________________________________________________");
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");

        sc.close();
    }
}
