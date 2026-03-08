public class Parser {
    public enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE, UNKNOWN
    }

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
        return CommandType.UNKNOWN;
    }
}