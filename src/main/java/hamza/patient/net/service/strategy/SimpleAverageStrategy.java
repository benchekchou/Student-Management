package hamza.patient.net.service.strategy;

import hamza.patient.net.domain.Student;

public class SimpleAverageStrategy implements AverageStrategy{
    @Override
    public Double compute(Student s) {
        return 0.0;
    }
}
