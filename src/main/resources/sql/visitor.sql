create table blog.visitor
(
  id      char(36)       not null
    primary key,
  ip      varchar(50)    not null,
  agent   varchar(100)   not null,
  date    datetime       not null
);