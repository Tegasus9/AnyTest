-- test.test_user definition

CREATE TABLE `test_user` (
                             `user_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                             `user_name` varchar(100) NOT NULL COMMENT '用户名称',
                             `user_phone` varchar(100) NOT NULL COMMENT '用户手机',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`user_id`),
                             KEY `test_user_user_id_IDX` (`user_id`,`user_name`,`user_phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户测试表';