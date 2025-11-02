package hamza.patient.net.repository;

import hamza.patient.net.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    List<Student> loadAll();                    // au démarrage
    void saveAll(List<Student> students);       // après mutation

    List<Student> findAll();
    Optional<Student> findById(String id);
    Student create(Student s);
    Student update(Student s);
    void delete(String id);
}
