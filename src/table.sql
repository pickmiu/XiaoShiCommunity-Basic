use xiaoshi_community;
drop table if exists User;

create table User
(
    id                 int auto_increment comment 'id',
    nickname           varchar(20) comment '昵称',
    email              varchar(255) not null default '' comment '邮箱',
    password           char(88)     not null comment '密码',
    phone              char(11)     not null default '' comment '手机号',
    schoolEmail        varchar(255) not null default '' comment '校园邮箱',
    passwordChangeTime timestamp             default current_timestamp comment '最近一次密码修改时间',
    createTime         timestamp             default current_timestamp comment '字段创建时间',
    updateTime         timestamp on update current_timestamp comment '字段修改时间',
    constraint pk_User_id primary key (id),
    constraint uk_User_email unique key (email),
    constraint uk_User_phone unique key (phone)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;