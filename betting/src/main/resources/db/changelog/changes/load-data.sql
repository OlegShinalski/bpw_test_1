--liquibase formatted sql

--changeset oleg:betting-data-1
insert into bet (id, account_id, stake, state)
values (1, 1, 10.00, 'PENDING');

insert into bet (id, account_id, stake, state)
values (2, 1, 5.00, 'PENDING');

insert into bet (id, account_id, stake, state)
values (3, 1, 2.00, 'PENDING');


--changeset oleg:betting-data-2
insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (1, 1, 10, 'PENDING', 1.2);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (2, 1, 12, 'PENDING', 1.5);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (3, 1, 15, 'PENDING', 1.3);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (4, 2, 10, 'PENDING', 1.2);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (5, 2, 11, 'PENDING', 1.5);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (6, 3, 12, 'PENDING', 1.2);

insert into bet_item (id, bet_id, bet_item_id, state, odds)
values (7, 3, 15, 'PENDING', 1.5);
