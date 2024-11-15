DROP TABLE refreshtoken;
DROP TABLE token;


create table if not exists refresh_token (
  id serial not null,
  user_id integer,
  expiry_date timestamp(6) with time zone not null,
  token varchar(255) not null unique,
  primary key (id)
);

create table if not exists token (
  id serial not null,
  user_id integer not null unique,
  expiry_date date,
  token varchar(255),
  primary key (id)
);
