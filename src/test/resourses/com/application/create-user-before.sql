delete
from role;
delete
from user;

insert into user(id, enabled, password, username, wallet)
values (1, true, 123456, 'stalker', 0),
       (2, true, 654321, 'phantom', 1000),
       (5, true, 111111, 'technology', 10000);

insert into role(id, role)
values (1, 'USER'),
       (2, 'USER'),
       (5, 'USER');

