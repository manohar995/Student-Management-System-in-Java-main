-- Create database if needed
CREATE DATABASE IF NOT EXISTS studata;
USE studata;

DROP TABLE IF EXISTS sdata;

CREATE TABLE sdata (
    student_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    major VARCHAR(50) NOT NULL,
    phone VARCHAR(15),
    gpa DECIMAL(4,2) NOT NULL,
    dob DATE NOT NULL
);

INSERT INTO sdata (student_id, first_name, last_name, major, phone, gpa, dob) VALUES
('001', 'Sankar', 'Nagarapu', 'EEE', '9398068299', 7.00, '2002-06-24'),
('002', 'Dharani', 'Nagarapu', 'CSE', '9133501412', 8.00, '2004-06-05'),
('003', 'Sindhu', 'Makarapu', 'CSE', '9133501411', 9.25, '2003-11-12'),
('004', 'Ravi', 'Kumar', 'ECE', '9876543210', 8.75, '2001-02-15'),
('005', 'Priya', 'Sharma', 'IT', '9123456780', 9.10, '2002-08-19');
