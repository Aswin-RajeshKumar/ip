/**
 * Represents a generic task within the Nova application.
 * This class serves as a base for specific task types, tracking a description
 * and a completion status to allow the user to monitor their progress.
 */
public class Task {

    private final String description;
    private boolean isDone;

    /**
     * Initializes a new task with the provided description.
     * New tasks are set to an incomplete status by default.
     *
     * @param description The textual summary of the task to be performed.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Updates the task's state to indicate that it has been completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Updates the task's state to indicate that it is still pending.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns a visual representation of the task's completion status.
     *
     * @return "X" if the task is finished, otherwise a blank space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Provides the raw text description of the task.
     * This is primarily used for data persistence during saving operations.
     *
     * @return The description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether the task has been marked as finished.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a formatted string containing the status icon and description for display.
     *
     * @return A string formatted as "[Status] Description".
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}