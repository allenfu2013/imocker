CREATE TABLE `api_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `api_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'api名称',
  `ret_result` text COMMENT 'api返回值',
  `method` varchar(10) NOT NULL DEFAULT '' COMMENT 'http方法',
  `regex` varchar(50) DEFAULT NULL COMMENT '有url变量的正则表达式',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `qa_url` varchar(100) DEFAULT NULL COMMENT '测试环境地址',
  `prd_url` varchar(100) DEFAULT NULL COMMENT '生产环境地址',
  `status` int(11) NOT NULL COMMENT 'api是否有效',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` varchar(20) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='api信息';


CREATE TABLE `para_info` (
  `api_id` int(11) unsigned NOT NULL COMMENT 'api_info表的id',
  `para_name` varchar(20) DEFAULT NULL COMMENT '参数名称',
  `para_type` int(11) DEFAULT NULL COMMENT '参数值类型',
  `required` tinyint(1) DEFAULT NULL COMMENT '是否必须',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `created_by` varchar(10) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(10) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='api的参数信息';