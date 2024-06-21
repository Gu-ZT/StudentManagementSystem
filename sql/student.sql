create table if not exists `student`
(
    `id`          bigint auto_increment comment '学生ID',
    `version`     int              not null default 0 comment '乐观锁',
    `del_flag`    tinyint unsigned not null default 0 comment '逻辑删除',
    `create_time` datetime         not null comment '创建时间',
    `update_time` datetime         not null comment '更新时间',
    `student_id`  varchar(255)     not null comment '学号',
    `name`        varchar(255)     not null comment '姓名',
    `sex`         varchar(255)     not null comment '性别',
    `birth`       date             not null comment '出生日期',
    `qq`          varchar(255)     not null comment 'QQ号',
    `phone`       varchar(255)     not null comment '手机号',
    `address`     varchar(255)     not null comment '住址',
    `image`       bigint           not null default 0 comment '照片',
    constraint student_pk primary key (id)
);