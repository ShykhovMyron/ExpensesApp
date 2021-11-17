delete
from role;
delete
from user;

insert into user(id, enabled, password, username, budget)
values (1, true, 123456, 'stalker', 0),
       (2, true, 654321, 'phantom', 1000);

insert into role(id, role)
values (1, 'USER'),
       (2, 'USER');

