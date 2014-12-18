create table users (
  username varchar(50) not null primary key,
  password varchar(50) not null,
  enabled boolean not null
)