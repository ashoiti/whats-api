CREATE TABLE tb_user (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  telephone VARCHAR(20)  NOT NULL,
  email VARCHAR(50) NULL,
  registry VARCHAR(15) NOT NULL,
  profile VARCHAR(20) NOT NULL
);

CREATE TABLE tb_quiz (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL
);

CREATE TABLE tb_user_quiz (
  id_user INTEGER NOT NULL,
  id_quiz INTEGER NOT NULL,
  answered INTEGER NOT NULL,
  CONSTRAINT pk_tb_user_quiz PRIMARY KEY (id_user, id_quiz)
);

CREATE TABLE tb_question (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  answer INTEGER NOT NULL,
  ord INTEGER NOT NULL,
  id_quiz INTEGER NOT NULL, 
  CONSTRAINT fk_tb_quiz FOREIGN KEY (id_quiz) REFERENCES tb_quiz(id)
);

CREATE TABLE tb_choice (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  id_question INTEGER NOT NULL, 
  CONSTRAINT fk_tb_choice FOREIGN KEY (id_question) REFERENCES tb_question(id)
);

CREATE TABLE tb_user_question (
  id_user INTEGER NOT NULL,
  id_question INTEGER NOT NULL,
  id_choice INTEGER NOT NULL,
  date TIMESTAMP NOT NULL,
  CONSTRAINT fk_tb_user_question FOREIGN KEY (id_choice) REFERENCES tb_choice(id),
  CONSTRAINT pk_tb_user_question PRIMARY KEY (id_user, id_question)
);

CREATE TABLE tb_project (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  store VARCHAR(200) NOT NULL,
  activation VARCHAR(200) NULL,
  alignment VARCHAR(100) NOT NULL,
  responsible VARCHAR(100) NOT NULL,
  assistant VARCHAR(100) NOT NULL,
  description VARCHAR(200) NOT NULL,
  id_user INTEGER NOT NULL,
  CONSTRAINT fk_tb_project FOREIGN KEY (id_user) REFERENCES tb_user(id)
);

INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Shoiti', '5511971803191','andre.shoiti@gmail.com','t123456', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Paulo', '5511960814485','','t111111', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Feijoada da Cris', '5511930703459','andre.shoiti@gmail.com','t222222', 'USER');

INSERT INTO tb_quiz (name) values ('Questionário 1');

INSERT INTO tb_user_quiz (id_user, id_quiz, answered) values (1, 1, 0);
INSERT INTO tb_user_quiz (id_user, id_quiz, answered) values (2, 1, 0);
INSERT INTO tb_user_quiz (id_user, id_quiz, answered) values (3, 1, 0);

INSERT INTO tb_question (name, answer, ord, id_quiz) values ('Quem fez o primeiro gol do Brasil na copa de 2022?', 2, 1, 1);

INSERT INTO tb_choice (name, id_question) values ('Neymar', 1);
INSERT INTO tb_choice (name, id_question) values ('Richarlison', 1);
INSERT INTO tb_choice (name, id_question) values ('Casemiro', 1);
INSERT INTO tb_choice (name, id_question) values ('Brasil não fez gol', 1);

INSERT INTO tb_question (name, answer, ord, id_quiz) values ('Quando foi a última vez que o Brasil ganhou uma copa do mundo?', 7, 2, 1);

INSERT INTO tb_choice (name, id_question) values ('2010', 2);
INSERT INTO tb_choice (name, id_question) values ('2014', 2);
INSERT INTO tb_choice (name, id_question) values ('2002', 2);
INSERT INTO tb_choice (name, id_question) values ('1994', 2);