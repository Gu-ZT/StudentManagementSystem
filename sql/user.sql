create table if not exists `user`
(
    `id`          bigint auto_increment comment '用户ID',
    `version`     int              not null default 0 comment '乐观锁',
    `del_flag`    tinyint unsigned not null default 0 comment '逻辑删除',
    `create_time` datetime         not null comment '创建时间',
    `update_time` datetime         not null comment '更新时间',
    `nickname`    varchar(255)     not null comment '昵称',
    `username`    varchar(255)     not null comment '用户名',
    `password`    varchar(255)     not null comment '密码',
    constraint user_pk primary key (id)
);