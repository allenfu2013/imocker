create database imocker default character set=utf8;

CREATE TABLE `tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `abbr_name` varchar(255) NOT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_abbrname` (`abbr_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tenant` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `type`, `abbr_name`, `display_name`, `email`, `status`)
VALUES(1, now(), NULL, now(), NULL, 'DEFAULT', 'default', null, null, 'NORMAL'), (2, now(), NULL, now(), NULL, 'ORG', 'platform', null, null, 'NORMAL');

CREATE TABLE `tenant_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) NOT NULL,
  `login_pwd` varchar(255) NOT NULL,
  `max_retry_times` int(11) NOT NULL,
  `retry_times` int(11) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `nick_name` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_login_name` (`tenant_id`,`login_name`),
  CONSTRAINT `fk_tenant_user_tenant_id` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tenant_user` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `login_name`, `login_pwd`, `max_retry_times`, `retry_times`, `tenant_id`, `nick_name`, `status`, `email`, `role_type`)
VALUES(1, now(), NULL, now(), NULL, 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', 5, 0, 2, '平台管理员', 'NORMAL', 'admin@imocker.cn', 'PLATFORM_ADMIN');

CREATE TABLE `access_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `access_key` varchar(255) NOT NULL,
  `locked` bit(1) NOT NULL,
  `ref_id` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_access_key` (`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `activation_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `activate_code` varchar(255) NOT NULL,
  `expired_date` datetime DEFAULT NULL,
  `ref_id` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activate_code` (`activate_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `api_name` varchar(255) NOT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `has_condition` bit(1) NOT NULL,
  `method` varchar(255) NOT NULL,
  `qa_url` varchar(255) DEFAULT NULL,
  `ret_result` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `uri_variable` varchar(255) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `project` varchar(255) DEFAULT NULL,
  `short_api_name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpqp6tfntgt3shqk7nru8jvm5p` (`tenant_id`),
  CONSTRAINT `fk_api_info_tenant_id` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cond_expression` varchar(255) NOT NULL,
  `cond_key` varchar(255) NOT NULL,
  `cond_type` varchar(255) NOT NULL,
  `cond_value` varchar(255) NOT NULL,
  `mock_ret_value` varchar(255) DEFAULT NULL,
  `api_info_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs9iyxcu15q1wuup3o8221gpbk` (`api_info_id`),
  CONSTRAINT `fk_api_condition_api_info_id` FOREIGN KEY (`api_info_id`) REFERENCES `api_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_cert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `cert_name` varchar(255) NOT NULL,
  `client_key_pwd` varchar(255) NOT NULL,
  `client_store` longtext NOT NULL,
  `trust_key_pwd` varchar(255) NOT NULL,
  `trust_store` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_doc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `api_desc` varchar(255) NOT NULL,
  `api_method` varchar(255) NOT NULL,
  `api_name` varchar(255) NOT NULL,
  `api_param_example` varchar(255) DEFAULT NULL,
  `api_response_example` varchar(255) DEFAULT NULL,
  `api_response_status` varchar(255) DEFAULT NULL,
  `project` varchar(255) DEFAULT NULL,
  `test_url` varchar(255) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKot0pylf54l0rgpwlgb4d3ha0g` (`tenant_id`),
  CONSTRAINT `FKot0pylf54l0rgpwlgb4d3ha0g` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `header_key` varchar(255) NOT NULL,
  `header_value` varchar(255) NOT NULL,
  `api_doc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrqyli1thlrtleiq1q23lv58nl` (`api_doc_id`),
  CONSTRAINT `FKrqyli1thlrtleiq1q23lv58nl` FOREIGN KEY (`api_doc_id`) REFERENCES `api_doc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_desc` varchar(255) NOT NULL,
  `param_key` varchar(255) NOT NULL,
  `param_parent` varchar(255) DEFAULT NULL,
  `param_required` varchar(255) NOT NULL,
  `param_type` varchar(255) NOT NULL,
  `api_doc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl1rexr7vu9lgqse60ku8pmo9h` (`api_doc_id`),
  CONSTRAINT `FKl1rexr7vu9lgqse60ku8pmo9h` FOREIGN KEY (`api_doc_id`) REFERENCES `api_doc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_response_body` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `response_desc` varchar(255) NOT NULL,
  `response_key` varchar(255) NOT NULL,
  `response_parent` varchar(255) DEFAULT NULL ,
  `response_required` varchar(255) NOT NULL,
  `response_type` varchar(255) NOT NULL,
  `api_doc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdnfapwn2crj6bw74eqmvl8qse` (`api_doc_id`),
  CONSTRAINT `FKdnfapwn2crj6bw74eqmvl8qse` FOREIGN KEY (`api_doc_id`) REFERENCES `api_doc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `api_error` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_code` varchar(255) DEFAULT NULL,
  `error_msg` varchar(255) DEFAULT NULL,
  `http_status` varchar(255) NOT NULL,
  `api_doc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7woxhymqyaluytyk7dorsk5yd` (`api_doc_id`),
  CONSTRAINT `FK7woxhymqyaluytyk7dorsk5yd` FOREIGN KEY (`api_doc_id`) REFERENCES `api_doc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
