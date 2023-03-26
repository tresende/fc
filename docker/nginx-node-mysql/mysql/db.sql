use nodedb;

CREATE TABLE people (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);


INSERT INTO people(name) values('Thiago');
INSERT INTO people(name) values('Camila');