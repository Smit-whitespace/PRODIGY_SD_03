// ContactManager.java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private final List<Contact> contacts = new ArrayList<>();
    private final File persistenceFile;

    public ContactManager(String filePath) {
        this.persistenceFile = new File(filePath);
    }

    public List<Contact> getAll() {
        return new ArrayList<>(contacts);
    }

    public void add(Contact c) {
        contacts.add(c);
    }

    public void update(int index, Contact c) {
        if (index >= 0 && index < contacts.size()) {
            contacts.set(index, c);
        }
    }

    public void remove(int index) {
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
        }
    }

    public void load() throws IOException {
        contacts.clear();
        if (!persistenceFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(persistenceFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    contacts.add(Contact.fromCSV(line));
                }
            }
        }
    }

    public void save() throws IOException {
        // Ensure parent dirs exist
        File parent = persistenceFile.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(persistenceFile), StandardCharsets.UTF_8))) {
            for (Contact c : contacts) {
                bw.write(c.toCSV());
                bw.newLine();
            }
            bw.flush();
        }
    }

    public int size() {
        return contacts.size();
    }
}
