/**
 * Represents a task that occurs within a specific time frame.
 * This class extends the basic Task by adding "from" and "to" timestamps,
 * allowing users to track activities that span a duration of time.
 */
public class Event extends Task {

    private final String from;
    private final String to;

    /**
     * Initializes a new Event task with a description and a defined time range.
     *
     * @param description A textual summary of the activity.
     * @param from The start time or date of the event.
     * @param to The end time or date of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Provides the raw start time string of the event.
     * This is utilized when saving the task list to ensure the time data is persisted.
     *
     * @return The start time string.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Provides the raw end time string of the event.
     * This is utilized when saving the task list to ensure the time data is persisted.
     *
     * @return The end time string.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns a string representation of the Event, prefixed with "[E]" and
     * including the specified time range.
     * This unique format distinguishes scheduled events from simple tasks or deadlines.
     *
     * @return A formatted string representation of the event and its duration.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}