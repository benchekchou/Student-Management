# Student Management Console

Console application to manage students, their course grades, and statistics with text-file persistence.

## Features
- CRUD operations for students with validation (names, emails).
- Course grade management per student (add/update/remove with bounds checking).
- Statistics: per-student average, best student, failing students, global average.
- Pluggable strategy for average calculation (simple mean provided).
- File-based repository using human-readable `students.txt`.

## Running
```bash
mvn exec:java -Dexec.mainClass=hamza.patient.net.Main
```
The application prompts through a menu in the terminal. Data is stored in `students.txt` in the working directory by default (configurable via `-Dstudents.file` or `STUDENTS_FILE`).

## Testing
```bash
mvn test
```
If Maven is not available locally, install it or use the Maven Wrapper as needed.
