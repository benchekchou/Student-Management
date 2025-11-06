package hamza.patient.net.repository.file;

import hamza.patient.net.domain.CourseNote;
import hamza.patient.net.domain.Student;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Simple text serializer: one student per line.
 * Format: id|name|email|course=note;course2=note2
 */
public class TxtSerializer {

    private static final String FIELD_SEPARATOR = "\\|";
    private static final String RAW_FIELD_SEPARATOR = "|";
    private static final String COURSES_SEPARATOR = ";";

    public List<Student> parse(List<String> lines) {
        List<Student> students = new ArrayList<>();
        if (lines == null) {
            return students;
        }

        for (String rawLine : lines) {
            if (rawLine == null) {
                continue;
            }

            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] fields = line.split(FIELD_SEPARATOR, -1);
            if (fields.length < 3) {
                // skip malformed lines silently
                continue;
            }

            String id = fields[0].trim();
            String name = emptyToNull(fields[1]);
            String email = emptyToNull(fields[2]);
            List<CourseNote> notes = new ArrayList<>();

            if (fields.length > 3 && !fields[3].isBlank()) {
                notes.addAll(parseNotes(fields[3]));
            }

            students.add(new Student(id, name, email, notes));
        }
        return students;
    }

    public List<String> format(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return List.of();
        }

        return students.stream()
                .map(this::formatStudent)
                .collect(Collectors.toList());
    }

    private List<CourseNote> parseNotes(String raw) {
        return Arrays.stream(raw.split(COURSES_SEPARATOR))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::parseCourseToken)
                .collect(Collectors.toList());
    }

    private CourseNote parseCourseToken(String token) {
        int equalsIdx = token.indexOf('=');
        if (equalsIdx <= 0 || equalsIdx == token.length() - 1) {
            return new CourseNote(token, 0.0);
        }
        String courseName = token.substring(0, equalsIdx).trim();
        String noteRaw = token.substring(equalsIdx + 1).trim();
        double note;
        try {
            note = Double.parseDouble(noteRaw);
        } catch (NumberFormatException e) {
            note = 0.0;
        }
        double sanitizedNote = Math.max(0.0, Math.min(20.0, note));
        return new CourseNote(courseName, sanitizedNote);
    }

    private String formatStudent(Student s) {
        String courses = s.getNotes().stream()
                .map(n -> n.getCourseName() + "=" + formatDouble(n.getNote()))
                .collect(Collectors.joining(COURSES_SEPARATOR));
        return String.join(RAW_FIELD_SEPARATOR,
                nullToEmpty(s.getId()),
                nullToEmpty(s.getName()),
                nullToEmpty(s.getEmail()),
                courses
        );
    }

    private String formatDouble(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String emptyToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
