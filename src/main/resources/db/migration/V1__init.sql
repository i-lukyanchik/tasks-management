create table if not exists team_members
(
    id   bigserial primary key,
    name text not null
);

insert into team_members(id, name) values (1, 'testName') on conflict do nothing;

create table if not exists tasks
(
    id                   bigserial primary key,
    title                text                                not null,
    description          text                                not null,
    due_date             timestamp,
    priority             int,
    assigned_member_id   bigint,
    status               text                                not null,
    created_at           timestamp default CURRENT_TIMESTAMP not null,
    created_by_member_id bigint                              not null,
    updated_at           timestamp,
    updated_by_member_id bigint,
    foreign key (assigned_member_id) references team_members (id),
    foreign key (created_by_member_id) references team_members (id),
    foreign key (updated_by_member_id) references team_members (id)
);

create unique index u_idx_title on tasks (title);
create index idx_task_status on tasks (status);
create index idx_task_assigned_member_id on tasks (assigned_member_id);