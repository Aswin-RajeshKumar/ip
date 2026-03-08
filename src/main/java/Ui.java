import java.util.Scanner;

public class Ui {
    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    public void showWelcome() {
        showLine();
        System.out.println("     Hello! I'm Nova\n     What can I do for you?");
        showLine();
    }

    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    public void showError(String message) {
        System.out.println("     " + message);
    }

    public void showLoadingError() {
        System.out.println("     [Error] Could not load tasks from file. Starting with an empty list.");
    }

    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}
