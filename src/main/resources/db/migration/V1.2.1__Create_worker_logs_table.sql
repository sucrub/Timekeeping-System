CREATE TABLE worker_logs
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    day    DATE NOT NULL,
    shift1 DOUBLE,
    shift2 DOUBLE,
    shift3 DOUBLE,
    userId INT  NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (id)
);
