-- jsmith's bookmarks
-- id: 1
insert into
  bookmark (id, title, url, user_id, scrapable)
values
( 
    1,
    'Best Cheesecake Recipe',
    'https://sugarspunrun.com/best-cheesecake-recipe/',
    1, 
    true
  );

-- id: 2
insert into
  bookmark (id, title, url, user_id, scrapable)
values
(
    2,
    'Dark mode guide',
    'https://blog.logrocket.com/dark-mode-react-in-depth-guide/',
    1, 
    true
  );

-- id: 3
insert into
  bookmark (id, title, url, user_id, scrapable)
values
(
    3,
    'Chicken Parm',
    'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359',
    1, 
    true
  );

-- king's bookmark 
-- id: 4
insert into
  bookmark (id, title, url, user_id, scrapable)
values
(
    4,
    'Favorite Chicken Parm',
    'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359',
    2, 
    true
  );

-- jsmith tags
-- id:   1
insert into
  tag(id, tag_title, user_id)
values
(1, 'Cooking', 1);

-- id:   2
insert into
  tag(id, tag_title, user_id)
values
(2, 'web_dev', 1);

-- id:   3
insert into
  tag(id, tag_title, user_id)
values
(3, 'deserts', 1);

-- id:   4
insert into
  tag(id, tag_title, user_id)
values
(4, 'camping', 1);

-- id:   5
insert into
  tag(id, tag_title, user_id)
values
(5, 'spring docs', 1);

-- id:   6
insert into
  tag(id, tag_title, user_id)
values
(6,'web docs', 1);

-- Cooking related bookmark_tags
insert into
  bookmark_tag(bookmark_id, tag_id)
values
(1, 1);

insert into
  bookmark_tag(bookmark_id, tag_id)
values
(1, 3);

insert into
  bookmark_tag(bookmark_id, tag_id)
values
(3, 1);

-- tech related bookmark_tags
insert into
  bookmark_tag(bookmark_id, tag_id)
values
(2, 2);
------------------------------------

------------------------------------
-- king 
------------------------------------
-- id: 7
insert into
  tag(id, tag_title, user_id)
values
(7, 'cooking', 2);

-- id: 8
insert into
  tag(id, tag_title, user_id)
values
(8, 'wishlist', 2);

insert into
  bookmark_tag
values
(4, 7);

-- login is jsmith/test
insert into
  users(
    enabled,
    username,
    email,
    password,
    role_role_id,
    user_id
  )
values
(
    TRUE,
    'jsmith',
    'jsmith@google.com',
    '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6', -- pwd: test
    0,
    1
  );

insert into
  users(
    enabled,
    username,
    email,
    password,
    role_role_id,
    user_id 
  )
values
(
    TRUE,
    'king',
    'king@google.com',
    '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6', -- pwd: test
    0,
    2
  );
