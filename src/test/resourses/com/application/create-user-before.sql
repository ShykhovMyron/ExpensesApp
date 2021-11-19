delete
from role;
delete
from wallet;
delete
from user;

insert into user(id, enabled, password, username)
values (1, true, 123456, 'stalker'),
       (2, true, 654321, 'phantom'),
       (5, true, 111111, 'technology');

insert into role(id, role)
values (1, 'USER'),
       (2, 'USER'),
       (5, 'USER');

insert into wallet(user_id, budget, balance)
values (1, 0, 0),
       (2, 1000,335),
       (5, 10000,3720);

