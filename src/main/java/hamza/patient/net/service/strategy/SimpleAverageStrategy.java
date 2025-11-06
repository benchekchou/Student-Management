package hamza.patient.net.service.strategy;

import hamza.patient.net.domain.Student;

/**
 * Straightforward arithmetic mean on the student's notes.
 */
public class SimpleAverageStrategy implements AverageStrategy {
    @Override
    public Double compute(Student student) {
        if (student == null || !student.hasCourses()) {
            return null;
        }
        double avg = student.getNotes().stream()
                .mapToDouble(note -> note.getNote())
                .average()
                .orElse(Double.NaN);
        return Double.isNaN(avg) ? null : avg;
    }
}
