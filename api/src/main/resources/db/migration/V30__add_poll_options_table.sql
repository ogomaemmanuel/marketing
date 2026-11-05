create table if not exists poll_options
(
    poll_id uuid,
    value   text not null
)