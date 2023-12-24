insert into bookmark (title, url, tenant_id) values('Best Cheesecake Recipe', 'https://sugarspunrun.com/best-cheesecake-recipe/',1 );
insert into bookmark (title, url, tenant_id) values('Dark mode guide', 'https://blog.logrocket.com/dark-mode-react-in-depth-guide/',1 );
insert into bookmark (title, url, tenant_id) values('Chicken Parm', 'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359', 1);

-- rsandor's bookmark 
insert into bookmark (title, url, tenant_id) values('Favorite Chicken Parm', 'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359', 2);

-- jsmith tags
insert into tag(tag_title, tenant_id) values('Cooking', 1);
insert into tag(tag_title, tenant_id) values('web_dev', 1);
insert into tag(tag_title, tenant_id) values('deserts', 1);
insert into tag(tag_title, tenant_id) values('camping', 1);

-- Cooking related bookmark_tags
insert into bookmark_tag values(1,1);
insert into bookmark_tag values(1,3);
insert into bookmark_tag values(3,1);

-- tech related bookmark_tags
insert into bookmark_tag values(2,2);

-- rsandor's tags
insert into tag(tag_title, tenant_id) values('Cooking', 2);
insert into tag(tag_title, tenant_id) values('wishlist', 2);

insert into bookmark_tag values(4,4);

insert into roles(role_id, name) values(0, 'ROLE_USER');
-- login is jsmith/test
insert into users(enabled, username, name, email, password, role_role_id, tenant_id) values(TRUE, 'jsmith', 'John Smith', 'jsmith@google.com', '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6', 0, 1);
insert into users(enabled, username, name, email, password, role_role_id, tenant_id) values(TRUE, 'rsandor', 'Raphael Sandor', 'rsandor@google.com', '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6', 0, 2);
