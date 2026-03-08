/**
 * Handles the interpretation of raw user input into meaningful commands.
 * This class acts as a translator between the user's text and the application's
 * internal logic, ensuring the program understands which action to perform.
 */
public class Parser {

    /**
     * Defines the set of valid operations that the application can execute.
     */
    public enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE, FIND, UNKNOWN
    }

    /**
     * Analyzes the provided input string to determine the intended command type.
     * It uses keyword matching and prefix checks to categorize the user's request.
     *
     * @param input The raw line of text entered by the user.
     * @return The identified CommandType enum corresponding to the user's intent.
     */
    public static CommandType parse(String input) {
        String trimmed = input.trim();
        if (trimmed.equals("list")) return CommandType.LIST;
        if (trimmed.equals("bye")) return CommandType.BYE;
        if (trimmed.startsWith("mark ")) return CommandType.MARK;
        if (trimmed.startsWith("unmark ")) return CommandType.UNMARK;
        if (trimmed.startsWith("todo")) return CommandType.TODO;
        if (trimmed.startsWith("deadline")) return CommandType.DEADLINE;
        if (trimmed.startsWith("event")) return CommandType.EVENT;
        if (trimmed.startsWith("delete ")) return CommandType.DELETE;
        if (trimmed.startsWith("find ")) return CommandType.FIND;
        return CommandType.UNKNOWN;
    }
}