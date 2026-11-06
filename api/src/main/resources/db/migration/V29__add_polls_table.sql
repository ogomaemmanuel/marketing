create table if not exists polls
(
    id                   uuid primary key         not null,
    version              bigint                   not null default 0,
    name                 varchar(255)             not null,
    question             text                     not null,
    question_description text                     not null,
    created_at           timestamp with time zone not null,
    created_by           varchar(255)             not null,
    orientation          varchar(50)              not null,
    list_style           varchar(50)              not null,
    redirect_url         text                     not null
)