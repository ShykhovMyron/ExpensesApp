delete from user_types;
delete from types;
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

insert into types(id, type)
values (1,'FOOD'),
       (2,'ENTERTAINMENT'),
       (3,'RESTAURANTS'),
       (4,'PRODUCTS'),
       (5,'CINEMA'),
       (6,'BOOKS'),
       (7,'FLOWERS');

insert into user_types(user_id, type_id)
values (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5),
       (1,6),
       (1,7),
       (2,1),
       (2,2),
       (2,3),
       (2,4),
       (2,5),
       (2,6),
       (2,7),
       (5,1),
       (5,2),
       (5,3),
       (5,4),
       (5,5),
       (5,6),
       (5,7);


