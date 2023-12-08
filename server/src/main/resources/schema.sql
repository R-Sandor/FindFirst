DROP TABLE IF EXISTS public.bookmark_tag;
DROP TABLE IF EXISTS public.bookmark;
DROP TABLE IF EXISTS public.tag;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.user_roles CASCADE;
DROP TYPE  IF EXISTS urole CASCADE;
DROP TABLE IF EXISTS roles;

CREATE TYPE urole as ENUM ('ROLE_USER','ROLE_MODERATOR','ROLE_ADMIN');

CREATE TABLE public.bookmark (
    id serial not null PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    tenant_id int NOT Null
);

-- tag --
CREATE TABLE public.tag (
  id serial not null  PRIMARY KEY,
  tag_title VARCHAR(50) NOT NULL,
  tenant_id int NOT NULL
);
-- ALTER TABLE public.bookmark_tag
--   ADD CONSTRAINT fk_tag_bookmark_id FOREIGN KEY (id) REFERENCES public.bookmark(id);
-- CREATE UNIQUE INDEX ix_bookmark_tag_tag_title ON public.tag(tag_title);

CREATE TABLE public.bookmark_tag (
    bookmark_id INT,
    tag_id INT
);

CREATE TABLE IF NOT EXISTS public.roles (
  role_id INT NOT NULL PRIMARY key, 
  name urole
);

-- Not need in a one to one relationship
-- CREATE TABLE IF NOT EXISTS public.user_roles (
--   user_id INT,
--   role_id INT
-- );

CREATE TABLE public.users (
  user_id serial NOT NULL PRIMARY key,
  enabled Boolean, 
  username VARCHAR(20) NOT NULL,
  name VARCHAR(50) NOT NULL, 
  email VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role_role_id INT NOT NULL,
  tenant_id INT NOT NULL
);