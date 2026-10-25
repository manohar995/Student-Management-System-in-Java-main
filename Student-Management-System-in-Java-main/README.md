# Student Management System in Java
This project is a Java Swing application for managing student records using MySQL.

## Features
- Add new students
- Display all students
- Search by student ID, last name, or major
- Update student information
- Delete student records
- Input validation and safer SQL usage

## Requirements
- Java 17 or newer
- MySQL Server
- MySQL Connector/J JDBC driver

## Database setup
1. Start MySQL.
2. Import the schema and sample data:

```bash
mysql -u root -p < student_data.sql
```

3. Make sure your MySQL credentials are available as environment variables or update the defaults in `dbConnect.java`.

Example:

```bash
export DB_URL="jdbc:mysql://localhost:3306/studata"
export DB_USER="root"
export DB_PASSWORD="1236"
```

## Run the application
Compile and run:

```bash
javac src/*.java
java -cp src Main
```

## Notes
The original project used hardcoded database values and unsafe SQL string concatenation. This version modernizes the code by using:
- parameterized SQL
- validation for inputs
- better database handling
- cleaner Swing UI with CRUD actions
