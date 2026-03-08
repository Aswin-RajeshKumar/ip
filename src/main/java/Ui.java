import java.util.Scanner;

/**
 * Handles all interactions between the user and the Nova application.
 * This class is responsible for reading user input and displaying messages to the
 * standard output, ensuring a consistent visual format throughout the program.
 */
public class Ui {
    private final Scanner sc;

    /**
     * Initializes a new Ui component with a standard input scanner.
     */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Reads a full line of text entered by the user.
     *
     * @return The raw input string provided by the user.
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Prints a horizontal divider line to separate different sections of the output.
     * This helps improve readability by clearly distinguishing between different commands.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Displays the initial greeting message when the application starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println("     Hello! I'm Nova\n     What can I do for you?");
        showLine();
    }

    /**
     * Displays a farewell message when the user exits the application.
     */
    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    /**
     * Displays a specific error message to the user.
     *
     * @param message The detailed error message to be printed.
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /**
     * Informs the user that the saved task data could not be retrieved.
     * This warns the user that the application will start with an empty list for this session.
     */
    public void showLoadingError() {
        System.out.println("     [Error] Could not load tasks from file. Starting with an empty list.");
    }

    /**
     * Prints a standard message to the user interface.
     *
     * @param message The text to be displayed.
     */
    public void showMessage(String message) {
        System.out.println("     " + message);
    }
}