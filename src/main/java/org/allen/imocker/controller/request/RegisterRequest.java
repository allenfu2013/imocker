package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.entity.type.TenantType;

@Data
public class RegisterRequest {

    /**
     * tenant type
     */
    private TenantType tenantType;

    /**
     * organization abbreviation name
     */
    private String tenantAbbrName;

    /**
     * organization display name
     */
    private String tenantDisplayName;

    /**
     * email for organization or personal user
     */
    private String email;

    /**
     * login name for personal user
     */
    private String loginName;

    /**
     * login password for personal user
     */
    private String loginPwd;

    /**
     * nick name for personal user
     */
    private String nickName;
}
