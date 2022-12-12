INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Shoiti', '5511971803191','andre.shoiti@gmail.com','t123456', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Paulo', '5511960814485','','t111111', 'ADMIN');
INSERT INTO tb_user (name, telephone, email, registry , profile) values ('Feijoada da Cris', '5511930703459','andre.shoiti@gmail.com','t222222', 'USER');

INSERT INTO tb_quiz (name) values ('Questionário 1');

INSERT INTO tb_user_quiz (id_user, id_quiz, answered) values (1, 1, 0);

INSERT INTO tb_question (name, answer, ord, id_quiz) values ('Quem fez o primeiro gol do Brasil na copa de 2022?', 2, 1, 1);

INSERT INTO tb_choice (name, id_question) values ('Neymar', 1);
INSERT INTO tb_choice (name, id_question) values ('Richarlison', 1);
INSERT INTO tb_choice (name, id_question) values ('Casemiro', 1);
INSERT INTO tb_choice (name, id_question) values ('Brasil não fez gol', 1);