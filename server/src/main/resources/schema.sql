-- Development Uncomment
drop table if exists bookmark_tag cascade;

drop table if exists bookmark cascade;

drop table if exists tag cascade;

drop table if exists refreshtoken cascade;

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS user_roles CASCADE;

drop table if exists roles cascade;

drop table if exists tenants cascade;

drop table if exists token cascade;

drop sequence if exists refreshtoken_seq;

drop sequence if exists tenants_seq;

drop sequence if exists token_seq;

create table if not exists bookmark (
  tenant_id integer not null,
  created_date timestamp(6),
  id bigserial not null,
  last_modified_date timestamp(6),
  title varchar(255),
  created_by varchar(255),
  last_modified_by varchar(255),
  url varchar(255),
  primary key (id)
);

create table if not exists tag (
  tenant_id integer not null,
  created_date timestamp(6),
  id bigserial not null,
  last_modified_date timestamp(6),
  tag_title varchar(50),
  created_by varchar(255),
  last_modified_by varchar(255),
  primary key (id)
);

create table if not exists tenants (
  id serial not null,
  created_date timestamp(6),
  last_modified_date timestamp(6),
  created_by varchar(255),
  last_modified_by varchar(255),
  name varchar(255) not null,
  primary key (id)
);

create table if not exists token (
  expiry_date date,
  user_id integer not null unique,
  id bigint not null,
  token varchar(255),
  primary key (id)
);

create table if not exists users (
  enabled boolean,
  role_role_id integer not null,
  tenant_id integer not null,
  user_id serial not null,
  username varchar(20),
  email varchar(50),
  password varchar(255),
  primary key (user_id),
  unique (username),
  unique (email)
);

create table if not exists bookmark_tag (
  bookmark_id bigint not null,
  tag_id bigint not null,
  primary key (bookmark_id, tag_id)
);

create table if not exists refreshtoken (
  user_id integer,
  expiry_date timestamp(6) with time zone not null,
  id bigint not null,
  token varchar(255) not null unique,
  primary key (id)
);

create table if not exists roles (
  role_id serial not null,
  name varchar(20) check (
    name in ('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN')
  ),
  primary key (role_id)
);

alter table
  if exists bookmark_tag drop constraint if exists FKhq7j2vott6kem0g51hhgq5nfl;

alter table
  if exists bookmark_tag drop constraint if exists FKpfa5mq9fkkjmv9jmu4hk9igpw;

alter table
  if exists refreshtoken drop constraint if exists FKa652xrdji49m4isx38pp4p80p;

alter table
  if exists token drop constraint if exists FKj8rfw4x0wjjyibfqq566j4qng;

alter table
  if exists users drop constraint if exists FKruo12mi6hchjfi06jhln9tdkt;

create sequence if not exists refreshtoken_seq start with 1 increment by 50;

create sequence if not exists tenants_seq start with 1 increment by 50;

create sequence if not exists token_seq start with 1 increment by 50;

alter table
  if exists bookmark_tag
add
  constraint FKhq7j2vott6kem0g51hhgq5nfl foreign key (tag_id) references tag;

alter table
  if exists bookmark_tag
add
  constraint FKpfa5mq9fkkjmv9jmu4hk9igpw foreign key (bookmark_id) references bookmark;

alter table
  if exists refreshtoken
add
  constraint FKa652xrdji49m4isx38pp4p80p foreign key (user_id) references users;

alter table
  if exists token
add
  constraint FKj8rfw4x0wjjyibfqq566j4qng foreign key (user_id) references users;

alter table
  if exists users
add
  constraint FKruo12mi6hchjfi06jhln9tdkt foreign key (role_role_id) references roles;