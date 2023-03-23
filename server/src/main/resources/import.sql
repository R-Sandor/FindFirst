insert into bookmark (id, title, url) values(1, 'test', 'test.com');
insert into bookmark values(2, 'test', 'test.com');
--insert into bookmark values(3, "test", "test.com");
--insert into bookmark values(4, "test", "test.com");

insert into tag(id, tag_title) values(1, 'TESTING');

insert into bookmark_tag values(1,1);
insert into roles(id, name) values(1, 'ROLE_USER');
-- insert into users(id, username, name, email, password) values(1, 'JSmith', 'John Smith', 'jsmith@google.com', 'test');
-- insert into user_roles values(1,1);
