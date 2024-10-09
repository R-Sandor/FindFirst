create table if not exists screenshot (
  id bigserial not null primary key, 
  version smallint, 
  disabled boolean,
  manually_captured boolean,
  last_updated timestamp(6)
);
