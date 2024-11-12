CREATE TABLE officer_logs
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    day       DATE NOT NULL,
    morning   BOOLEAN,
    afternoon BOOLEAN,
    timeLate DOUBLE,
    timeEarly DOUBLE,
    userId    INT  NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (id)
);
