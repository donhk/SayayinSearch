drop table if exists files;

create table files
(
  path  varchar(1500) primary key,
  name  varchar(100),
  hints bigint default 1
);

create index idx_path on files(path);