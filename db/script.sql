INSERT INTO `tenant` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `type`, `abbr_name`, `display_name`, `email`, `status`)
VALUES(1, now(), NULL, now(), NULL, 'DEFAULT', 'default', null, null, 'NORMAL'), (2, now(), NULL, now(), NULL, 'ORG', 'platform', null, null, 'NORMAL');

INSERT INTO `tenant_user` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `login_name`, `login_pwd`, `max_retry_times`, `retry_times`, `tenant_id`, `nick_name`, `status`, `email`, `role_type`)
VALUES(1, now(), NULL, now(), NULL, 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', 5, 0, 2, '平台管理员', 'NORMAL', 'admin@imocker.cn', 'PLATFORM_ADMIN');
