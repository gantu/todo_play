
-- !Ups

create table employee (
    id bigint primary key,
    first_name varchar(100),
    last_name varchar(100),
    email text
)


-- !Downs

drop table employee