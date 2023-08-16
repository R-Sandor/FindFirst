insert into bookmark (id, title, url) values(1, 'Best Cheesecake Recipe', 'https://sugarspunrun.com/best-cheesecake-recipe/');
insert into bookmark values(2, 'Dark mode guide', 'https://blog.logrocket.com/dark-mode-react-in-depth-guide/');
--insert into bookmark values(3, "test", "test.com");
--insert into bookmark values(4, "test", "test.com");

insert into tag(id, tag_title) values(1, 'Cooking');
insert into tag(id, tag_title) values(2, 'web_dev');

insert into bookmark_tag values(1,1);

insert into bookmark_tag values(2,2);

insert into roles(id, name) values(1, 'ROLE_USER');
-- Password is test
insert into users(id, username, name, email, password) values(1, 'jsmith', 'John Smith', 'jsmith@google.com', '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6');
-- insert into user_roles values(1,1);
