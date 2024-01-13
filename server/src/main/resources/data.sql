-- id: 1
insert into
  tenants(
    created_date,
    last_modified_date,
    created_by,
    last_modified_by,
    name
  )
values
(
    '2024-01-13 08:11:17.497',
    '2024-01-13 08:11:17.497',
    'jsmith',
    'jsmith',
    'jsmith'
  );

-- id: 2
insert into
  tenants(
    created_date,
    last_modified_date,
    created_by,
    last_modified_by,
    name
  )
values
(
    '2024-01-13 08:11:17.497',
    '2024-01-13 08:11:17.497',
    'rsandor',
    'rsandor',
    'rsandor'
  );

-- jsmith's bookmarks
-- id: 1
insert into
  bookmark (title, url, tenant_id)
values
(
    'Best Cheesecake Recipe',
    'https://sugarspunrun.com/best-cheesecake-recipe/',
    1
  );

-- id: 2
insert into
  bookmark (title, url, tenant_id)
values
(
    'Dark mode guide',
    'https://blog.logrocket.com/dark-mode-react-in-depth-guide/',
    1
  );

-- id: 3
insert into
  bookmark (title, url, tenant_id)
values
(
    'Chicken Parm',
    'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359',
    1
  );

-- rsandor's bookmark 
-- id: 4
insert into
  bookmark (title, url, tenant_id)
values
(
    'Favorite Chicken Parm',
    'https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359',
    2
  );

-- jsmith tags
-- id:   1
insert into
  tag(tag_title, tenant_id)
values
('Cooking', 1);

-- id:   2
insert into
  tag(tag_title, tenant_id)
values
('web_dev', 1);

-- id:   3
insert into
  tag(tag_title, tenant_id)
values
('deserts', 1);

-- id:   4
insert into
  tag(tag_title, tenant_id)
values
('camping', 1);

-- id:   5
insert into
  tag(tag_title, tenant_id)
values
('spring docs', 1);

-- id:   6
insert into
  tag(tag_title, tenant_id)
values
('web docs', 1);

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
-- rsandor 
------------------------------------
-- id: 7
insert into
  tag(tag_title, tenant_id)
values
('cooking', 2);

-- id: 8
insert into
  tag(tag_title, tenant_id)
values
('wishlist', 2);

insert into
  bookmark_tag
values
(4, 7);

insert into
  roles(role_id, name)
values
(0, 'ROLE_USER');

-- login is jsmith/test
insert into
  users(
    enabled,
    username,
    email,
    password,
    role_role_id,
    tenant_id
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
    tenant_id
  )
values
(
    TRUE,
    'rsandor',
    'rsandor@google.com',
    '$2a$10$uhmwgpRI0vxF51s8pxt94Ojs8Cwrg7uLwhf3sK7EYv1i7QLML7aJ6', -- pwd: test
    0,
    2
  );
