insert into bookmark (id, title, url) values(1, 'test', 'test.com');
insert into bookmark values(2, 'test', 'test.com');
--insert into bookmark values(3, "test", "test.com");
--insert into bookmark values(4, "test", "test.com");

insert into tag(id, tag_title) values(1, 'TESTING');

insert into bookmark_tag values(1,1);

insert into users("JSmith", "John Smith", "jsmith@google.com", "test")