insert into bookmark (id, title, url) values(1, 'Best Cheesecake Recipe', 'https://sugarspunrun.com/best-cheesecake-recipe/');
insert into bookmark (id, title, url) values(2, 'Dark mode guide', 'https://blog.logrocket.com/dark-mode-react-in-depth-guide/');
insert into bookmark (id, title, url) values(3, 'Chicken Parm', 'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359');
--insert into bookmark values(3, "test", "test.com");
--insert into bookmark values(4, "test", "test.com");

insert into tag(id, tag_title) values(1, 'Cooking');
insert into tag(id, tag_title) values(2, 'web_dev');
insert into tag(id, tag_title) values(3, 'deserts');

-- Cooking related bookmark_tags
insert into bookmark_tag values(1,1);
insert into bookmark_tag values(1,3);
insert into bookmark_tag values(3,1);
-- tech related bookmark_tags
insert into bookmark_tag values(2,2);

insert into roles(role_id, name) values(1, 'ROLE_USER');
-- login is jsmith/test
insert into users(user_id, username, name, email, password) values(1, 'jsmith', 'John Smith', 'jsmith@google.com', '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6');
insert into user_roles values(1,1);
