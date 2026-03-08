import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the persistence of task data to a local text file.
 * This class manages the reading and writing of task information, allowing the application
 * to preserve user data across different sessions.
 */
public class Storage {
    private final String filePath;
    private final String dirPath;

    /**
     * Initializes a Storage component with a specified file path.
     * The directory path is automatically derived to ensure the storage folder exists.
     *
     * @param filePath The relative or absolute path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        this.dirPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
    }

    /**
     * Serializes the current list of tasks into a formatted text file.
     * It ensures the target directory exists before attempting to write.
     *
     * @param taskList The collection of tasks to be saved.
     * @throws IOException If an error occurs during the file writing process.
     */
    public void save(TaskList taskList) throws IOException {
        Files.createDirectories(Paths.get(dirPath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        for (Task t : taskList.getAllTasks()) {
            String type = (t instanceof Todo) ? "T" : (t instanceof Deadline) ? "D" : "E";
            int done = t.isDone() ? 1 : 0;
            String line = type + " | " + done + " | " + t.getDescription();
            if (t instanceof Deadline) line += " | " + ((Deadline) t).getBy();
            if (t instanceof Event) line += " | " + ((Event) t).getFrom() + " | " + ((Event) t).getTo();
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Reads and reconstructs task objects from the data file.
     * If the file does not exist, it returns an empty list to indicate a fresh start.
     *
     * @return An ArrayList containing the tasks loaded from the file.
     * @throws IOException If there is a problem reading the file content.
     */
    public ArrayList<Task> load() throws IOException {
        File f = new File(filePath);
        ArrayList<Task> loadedTasks = new ArrayList<>();
        if (!f.exists()) return loadedTasks;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] p = line.split(" \\| ");
                Task t = switch (p[0]) {
                    case "T" -> new Todo(p[2]);
                    case "D" -> new Deadline(p[2], p[3]);
                    case "E" -> new Event(p[2], p[3], p[4]);
                    default -> null;
                };
                if (t != null) {
                    if (p[1].equals("1")) t.markAsDone();
                    loadedTasks.add(t);
                }
            }
        }
        return loadedTasks;
    }
}