--!Ups

create table department(
    id bigint primary key,
    name varchar(100)
)

alter table employee add column department_id bigint
alter table employee add constraint DEPT_FK foreign key (department_id) references department(id)


--!Downs

alter table employee drop constraint DEPT_FK
alter table employee drop column department_id

drop table department