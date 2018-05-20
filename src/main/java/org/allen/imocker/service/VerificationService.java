package org.allen.imocker.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.VerificationRequest;
import org.allen.imocker.dao.AccessKeyRepository;
import org.allen.imocker.dao.ActivationCodeRepository;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.RoleType;
import org.allen.imocker.entity.AccessKey;
import org.allen.imocker.entity.ActivationCode;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ActivateStatus;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class VerificationService {

    private static final String SUBJECT = "账号激活";
    private static final String TEMPLATE_NAME = "activate.html";

    @Value("${app.activation.url}")
    private String activationUrl;

    @Value("${app.activation.sender}")
    private String emailFrom;

    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @Autowired
    private AccessKeyRepository accessKeyRepository;

    @Async
    public void pass(VerificationRequest request) {

        TenantType tenantType = request.getTenantType();
        String email = null;
        Tenant tenant = null;
        TenantUser tenantUser = null;
        if (TenantType.ORG == tenantType) {
            tenant = tenantRepository.findOne(request.getRefId());
            if (tenant != null) {
                email = tenant.getEmail();
            }
        } else if (TenantType.DEFAULT == tenantType) {
            tenantUser = tenantUserRepository.findOne(request.getRefId());
            if (tenantUser != null) {
                email = tenantUser.getEmail();
            }
        }

        String uuid = UUID.randomUUID().toString();
        ActivationCode activationCode = new ActivationCode();
        activationCode.setType(request.getTenantType());
        activationCode.setRefId(request.getRefId());
        activationCode.setActivateCode(uuid);
        activationCode.setStatus(ActivateStatus.CREATED);
//        activationCode.setExpiredDate();
        activationCodeRepository.save(activationCode);

        Map<String, Object> data = Maps.newHashMap();
        data.put("activationUrl", String.format(activationUrl, uuid));
        boolean sendStatus = emailSender.send(SUBJECT, emailFrom, new String[]{email}, TEMPLATE_NAME, data);
        log.info("Send activation email to {}, status {}", email, sendStatus);

        if (sendStatus) {
            if (TenantType.ORG == tenantType) {
                tenant.setStatus(ApplyStatus.CONFIRMATION);
                tenantRepository.save(tenant);
            } else if (TenantType.DEFAULT == tenantType) {
                tenantUser.setStatus(ApplyStatus.CONFIRMATION);
                tenantUserRepository.save(tenantUser);
            }
        }
    }


    @Transactional
    public void activate(String token) {
        ActivationCode activationCode = activationCodeRepository.findOneByActivateCode(token);
        if (activationCode == null) {

        }

        activationCode.setStatus(ActivateStatus.SUCCESS);

        TenantType tenantType = activationCode.getType();
        if (TenantType.DEFAULT == tenantType) {
            TenantUser tenantUser = tenantUserRepository.findOne(activationCode.getRefId());
            tenantUser.setStatus(ApplyStatus.NORMAL);
            tenantUserRepository.save(tenantUser);
        } else if (TenantType.ORG == tenantType) {
            Tenant tenant = tenantRepository.findOne(activationCode.getRefId());
            tenant.setStatus(ApplyStatus.NORMAL);
            tenantRepository.save(tenant);

            TenantUser admin = new TenantUser();
            admin.setLoginName("admin");
            admin.setNickName("admin");
            admin.setLoginPwd(DigestUtils.md5Hex("password"));
            admin.setEmail(tenant.getEmail());
            admin.setStatus(ApplyStatus.NORMAL);
            admin.setTenant(tenant);
            admin.setRoleType(RoleType.TENANT_ADMIN);
            tenantUserRepository.save(admin);
        }

        generateAccessKey(tenantType, activationCode.getRefId());
    }

    private void generateAccessKey(TenantType tenantType, Long refId) {
        AccessKey accessKey = new AccessKey();
        accessKey.setType(tenantType);
        accessKey.setRefId(refId);
        accessKey.setAccessKey(DigestUtils.md5Hex(UUID.randomUUID().toString()));
        accessKeyRepository.save(accessKey);
    }
}
