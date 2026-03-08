/**
 * Represents a basic task type without any date or time constraints.
 * This class is used for tasks that only require a description to be tracked.
 */
public class Todo extends Task {

    /**
     * Initializes a new Todo task with the specified description.
     *
     * @param description A textual summary of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the Todo task, prefixed with "[T]".
     * This unique identifier helps distinguish Todo tasks from other types like Deadlines or Events.
     *
     * @return A formatted string representation of the task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}