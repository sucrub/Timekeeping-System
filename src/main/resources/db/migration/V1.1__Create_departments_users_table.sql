CREATE TABLE departments
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    roleId       INT          NOT NULL,
    birthday     DATE,
    email        VARCHAR(255),
    address      VARCHAR(255),
    departmentId INT,
    FOREIGN KEY (departmentId) REFERENCES departments (id)
);
