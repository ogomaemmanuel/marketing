alter table if exists polls
    add column if not exists poll_type varchar(50);