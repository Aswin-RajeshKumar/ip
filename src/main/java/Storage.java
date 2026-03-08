import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;
    private final String dirPath;

    public Storage(String filePath) {
        this.filePath = filePath;
        this.dirPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
    }

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