package hamza.patient.net.service;

import hamza.patient.net.domain.Student;
import hamza.patient.net.exceptions.NotFoundException;
import hamza.patient.net.repository.StudentRepository;
import hamza.patient.net.service.strategy.AverageStrategy;
import hamza.patient.net.util.Validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Aggregates various student statistics.
 */
public class CalculationService {
    private static final double FAILING_THRESHOLD = 10.0;

    private final StudentRepository repository;
    private final AverageStrategy averageStrategy;

    public CalculationService(StudentRepository repository, AverageStrategy averageStrategy) {
        Validation.requireNotNull(repository, "StudentRepository");
        Validation.requireNotNull(averageStrategy, "AverageStrategy");
        this.repository = repository;
        this.averageStrategy = averageStrategy;
    }

    public Double computeAverage(String studentId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Étudiant introuvable : " + studentId));
        Double average = averageStrategy.compute(student);
        return average == null ? null : roundTwoDecimals(average);
    }

    public Optional<Student> getBestStudent() {
        return repository.findAll().stream()
                .filter(Student::hasCourses)
                .max(Comparator
                        .comparing(this::averageOfOrZero)
                        .thenComparing(Student::getName, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Student::getId));
    }

    public List<Student> getFailingStudents() {
        return repository.findAll().stream()
                .filter(Student::hasCourses)
                .filter(student -> averageOfOrZero(student) < FAILING_THRESHOLD)
                .sorted(Comparator
                        .comparing(this::averageOfOrZero)
                        .thenComparing(Student::getName, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Student::getId))
                .collect(Collectors.toList());
    }

    public OptionalDouble computeGlobalAverage() {
        List<Student> students = repository.findAll().stream()
                .filter(Student::hasCourses)
                .toList();
        if (students.isEmpty()) {
            return OptionalDouble.empty();
        }
        double sum = students.stream()
                .map(this::averageOfOrZero)
                .reduce(0.0, Double::sum);
        return OptionalDouble.of(sum / students.size());
    }

    private double averageOfOrZero(Student student) {
        Double avg = averageStrategy.compute(student);
        return avg == null ? 0.0 : avg;
    }

    private double roundTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
