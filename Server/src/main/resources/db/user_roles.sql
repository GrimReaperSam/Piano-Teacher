create table authorities (
  username varchar(50) not null,
  authority varchar(50) not null,
  foreign key (username) references users (username)
);
create unique index authorities_idx_1 on authorities (username, authority)