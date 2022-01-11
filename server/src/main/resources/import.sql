insert into bookmark (id, title, url) values(1, 'test', 'test.com');
insert into bookmark values(2, 'test', 'test.com');
--insert into bookmark values(3, "test", "test.com");
--insert into bookmark values(4, "test", "test.com");

insert into bookmark_tag(id, tag_title, bookmark_id) values(1, 'TESTING', 1);