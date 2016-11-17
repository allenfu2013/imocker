package org.allen.imocker.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Value("${app.admin.user}")
    private String appAdminUser;

    @Value("${app.admin.password}")
    private String appAdminPassword;

    @Value("${app.uri.prefix}")
    private String appUriPrefix;

    public String getAppAdminUser() {
        return appAdminUser;
    }

    public String getAppAdminPassword() {
        return appAdminPassword;
    }

    public String getAppUriPrefix() {
        return appUriPrefix;
    }
}
