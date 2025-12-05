// Contact.java
import java.util.Objects;

public class Contact {
    private String name;
    private String phone;
    private String email;

    public Contact() {}

    public Contact(String name, String phone, String email) {
        this.name = name == null ? "" : name.trim();
        this.phone = phone == null ? "" : phone.trim();
        this.email = email == null ? "" : email.trim();
    }

    // Getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? "" : name.trim(); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone == null ? "" : phone.trim(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? "" : email.trim(); }

    // CSV helpers (very small, safe escaping)
    public String toCSV() {
        return escape(name) + "," + escape(phone) + "," + escape(email);
    }

    public static Contact fromCSV(String csvLine) {
        String[] parts = splitCSV(csvLine);
        String n = parts.length > 0 ? unescape(parts[0]) : "";
        String p = parts.length > 1 ? unescape(parts[1]) : "";
        String e = parts.length > 2 ? unescape(parts[2]) : "";
        return new Contact(n, p, e);
    }

    private static String escape(String s) {
        if (s == null) return "";
        // Replace " with "" and wrap with quotes if contains comma or quote or newline
        String escaped = s.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    private static String unescape(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            String inner = s.substring(1, s.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return s;
    }

    // Rudimentary CSV split that respects quoted fields
    private static String[] splitCSV(String line) {
        if (line == null) return new String[0];
        java.util.List<String> out = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // handle double quotes inside quoted field
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++; // skip one
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "Contact{" + name + ", " + phone + ", " + email + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contact)) return false;
        Contact c = (Contact) o;
        return Objects.equals(name, c.name)
            && Objects.equals(phone, c.phone)
            && Objects.equals(email, c.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email);
    }
}
