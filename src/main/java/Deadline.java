/**
 * Represents a task that carries a specific deadline for completion.
 * This class extends the basic Task by adding a time constraint, helping users
 * prioritize tasks based on their due dates.
 */
public class Deadline extends Task {

    private final String by;

    /**
     * Initializes a new Deadline task with a description and a due date.
     *
     * @param description A textual summary of the task.
     * @param by The date or time by which the task must be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Provides the raw deadline string.
     * This is primarily used for saving task data to a file in a readable format.
     *
     * @return The deadline string.
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns a string representation of the Deadline task, prefixed with "[D]"
     * and including its due date.
     * This format allows users to quickly distinguish deadlines from other task types.
     *
     * @return A formatted string representation of the task and its deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}