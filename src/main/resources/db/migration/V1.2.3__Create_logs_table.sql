CREATE TABLE logs
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    log    TIMESTAMP,
    userId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (id)
);
