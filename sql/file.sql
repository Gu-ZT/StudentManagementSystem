create table if not exists `file`
(
    `id`          bigint auto_increment comment '文件ID',
    `version`     int              not null default 0 comment '乐观锁',
    `del_flag`    tinyint unsigned not null default 0 comment '逻辑删除',
    `create_time` datetime         not null comment '创建时间',
    `update_time` datetime         not null comment '更新时间',
    `name`        varchar(255)     not null comment '文件名称',
    `addr`        varchar(255)     not null comment '文件位置',
    constraint user_pk primary key (id)
);