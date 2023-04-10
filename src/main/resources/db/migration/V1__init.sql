create table role (
    id   varchar(255) not null primary key,
    name varchar(255) not null,
    constraint ok_role_name unique (name)
);

create table membership (
    id varchar(255) not null primary key,
    team_id binary(255)  not null,
    user_id binary(255)  not null,
    role_id varchar(255) not null,
    constraint uk_membership_rid_tid_uid unique (role_id, team_id, user_id),
    constraint fk_membership_role_id foreign key (role_id) references role (id)
);
