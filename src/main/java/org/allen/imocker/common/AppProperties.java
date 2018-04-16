package org.allen.imocker.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties {

    @Value("${app.admin.user}")
    private String appAdminUser;

    @Value("${app.admin.password}")
    private String appAdminPassword;

    @Value("${app.uri.prefix}")
    private String appUriPrefix;

}
