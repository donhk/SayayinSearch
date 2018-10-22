drop table if exists files;

create table files
(
  id    bigint primary key auto_increment,
  path  varchar(2000) unique,
  name  varchar(2000),
  hints bigint default 0
);

create index idx_path on files(path);