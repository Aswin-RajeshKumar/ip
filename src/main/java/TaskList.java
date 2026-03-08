import java.util.ArrayList;

/**
 * Manages the collection of tasks in the Nova application.
 * This class provides an abstraction over the underlying data structure to handle
 * task manipulation such as adding, deleting, and searching.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Initializes an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Initializes a task list with an existing collection of tasks.
     * This is typically used when loading saved data from storage.
     *
     * @param tasks An ArrayList of Task objects to be managed.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Appends a new task to the end of the list.
     *
     * @param task The Task object to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     * Index validation is performed to ensure the application remains stable.
     *
     * @param index The zero-based position of the task to be removed.
     * @return The Task object that was removed.
     * @throws NovaException If the index provided does not exist in the current list.
     */
    public Task remove(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
        return tasks.remove(index);
    }

    /**
     * Retrieves a specific task from the list without removing it.
     *
     * @param index The zero-based position of the task.
     * @return The requested Task object.
     * @throws NovaException If the index is out of the valid range.
     */
    public Task get(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("OOPS!!! Invalid task number.");
        }
        return tasks.get(index);
    }

    /**
     * Returns the total number of tasks currently managed in the list.
     *
     * @return The count of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Provides access to the underlying list of all tasks.
     * This is required by the storage component to iterate and save data.
     *
     * @return The internal ArrayList of tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Filters the task list for items that contain a specific search keyword.
     * This provides the core logic for the "find" feature.
     *
     * @param keyword The string to look for within task descriptions.
     * @return A new ArrayList containing only the tasks that match the criteria.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().contains(keyword)) {
                matchingTasks.add(t);
            }
        }
        return matchingTasks;
    }
}