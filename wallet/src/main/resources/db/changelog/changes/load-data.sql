--liquibase formatted sql

--changeset oleg:changeset-data-1
INSERT INTO account (id, email, amount)
VALUES (1, 'aaa@gmail.com', 100);

INSERT INTO account (id, email, amount)
VALUES (2, 'bbb@gmail.com', 50);

INSERT INTO account (id, email, amount)
VALUES (3, 'ccc@gmail.com', 10);

--changeset oleg:changeset-data-2
INSERT INTO account_operation (id, account_id, type, amount)
VALUES (1, 1, 'DEPOSIT', 60);

INSERT INTO account_operation (id, account_id, type, amount)
VALUES (2, 1, 'DEPOSIT', 40);
