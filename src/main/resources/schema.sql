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
  result INTEGER NOT NULL,
  CONSTRAINT pk_tb_user_quiz PRIMARY KEY (id_user, id_quiz)
);

CREATE TABLE tb_question (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  answer INTEGER NOT NULL,
  ord INTEGER NOT NULL,
  points INTEGER NOT NULL,
  id_quiz INTEGER NOT NULL,
  CONSTRAINT fk_tb_quiz FOREIGN KEY (id_quiz) REFERENCES tb_quiz(id)
);

CREATE TABLE tb_choice (
  id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  display VARCHAR(100) NOT NULL,
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

CREATE TABLE tb_image (
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  zenvia_url VARCHAR(200),
  mimetype VARCHAR(30),
  name VARCHAR(100),
  content BLOB
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
  id_image BIGINT,
  mail VARCHAR(100),
  storage VARCHAR(200),
  CONSTRAINT fk_tb_project FOREIGN KEY (id_user) REFERENCES tb_user(id),
  CONSTRAINT fk_tb_project2 FOREIGN KEY (id_image) REFERENCES tb_image(id)
);

INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Shoiti', '5511971803191','andre.shoiti@gmail.com','t123456', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Paulo', '5511960814485','','t111111', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Feijoada da Cris', '5511930703459','andre.shoiti@gmail.com','t222222', 'USER');

INSERT INTO tb_quiz (name) values ('Questionário 1');

INSERT INTO tb_user_quiz (id_user, id_quiz, answered, result) values (1, 1, 0, 0);
INSERT INTO tb_user_quiz (id_user, id_quiz, answered, result) values (2, 1, 0, 0);
INSERT INTO tb_user_quiz (id_user, id_quiz, answered, result) values (3, 1, 0, 0);

INSERT INTO tb_question (name, answer, ord, id_quiz, points) values ('A nova linha Zero tem como principal benefício?', 3, 1, 1, 10);

INSERT INTO tb_choice (name, display, id_question) values ('Ser uma linha com 0% ingredientes artificiais (aromas, corantes, conservantes, adoçantes), vegano e sem glúten', 'Resposta A', 1);
INSERT INTO tb_choice (name, display, id_question) values ('Linha com embalagem reciclável', 'Resposta B', 1);
INSERT INTO tb_choice (name, display, id_question) values ('Produto vegano', 'Resposta C', 1);
INSERT INTO tb_choice (name, display, id_question) values ('Produto sem conservantes', 'Resposta D', 1);

INSERT INTO tb_question (name, answer, ord, id_quiz, points) values ('Quando foi a última vez que o Brasil ganhou uma copa do mundo?', 7, 2, 1, 10);

INSERT INTO tb_choice (name, display, id_question) values ('2010', 'Resposta A', 2);
INSERT INTO tb_choice (name, display, id_question) values ('2014', 'Resposta B', 2);
INSERT INTO tb_choice (name, display, id_question) values ('2002', 'Resposta C', 2);
INSERT INTO tb_choice (name, display, id_question) values ('1994', 'Resposta D', 2);