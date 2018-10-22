drop table if exists files;

create table files
(
  path  varchar(2000) primary key,
  name  varchar(2000),
  hints bigint default 1
);

create index idx_path on files(path);