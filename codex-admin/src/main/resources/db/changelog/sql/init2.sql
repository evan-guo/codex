create table user
(
    id           varchar(50) null,
    created_by   varchar(50) null,
    created_time datetime    null,
    updated_by   varchar(50) null,
    updated_time datetime    null,
    name         varchar(50) null,
    status       int         null
)
    comment '用戶';

