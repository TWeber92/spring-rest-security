CREATE DATABASE IF NOT EXISTS customer_db;
USE customer_db;

CREATE TABLE customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    email_id VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE
);

INSERT INTO customer (email_id, name, date_of_birth) VALUES
('charles@example.com', 'Charles Smith', '1985-04-12'),
('jane@example.com', 'Jane Doe', '1990-07-23'),
('robert@example.com', 'Robert Brown', '1978-11-05');

SELECT * FROM customer;