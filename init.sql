USE app;
DROP TABLE IF EXISTS users
CREATE TABLE IF NOT EXISTS users
(
    email          VARCHAR(30) NOT NULL,
    password        VARCHAR (30) NOT NULL,
    PRIMARY KEY (email)
)