package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.AccessKeyRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.AccessKey;
import org.allen.imocker.entity.type.TenantType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.allen.imocker.dto.Constants.*;

@RestController
@Slf4j
@RequestMapping("/accesskeys")
public class AccessKeyController {

    @Autowired
    private AccessKeyRepository accessKeyRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse query(@RequestAttribute(ATTR_TENANT_TYPE) TenantType tenantType,
                             @RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                             @RequestAttribute(ATTR_USER_ID) Long userId) {

        log.info("Query accessKey , tenantType:{}, tenantId:{}, userId:{}",
                tenantType, tenantId, userId);

        List<AccessKey> accessKeyList = accessKeyRepository.findByTypeAndRefId(tenantType,
                TenantType.ORG == tenantType ? tenantId : userId);

        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(accessKeyList);
        return apiResponse;
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.POST)
    public ApiResponse reset(@RequestAttribute(ATTR_TENANT_TYPE) TenantType tenantType,
                             @RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                             @RequestAttribute(ATTR_USER_ID) Long userId,
                             @RequestAttribute(ATTR_NICK_NAME) String nickName,
                             @PathVariable("id") Long id) {
        log.info("Reset accesskey, id:{}, userId:{}, tenantId:{}, tenantType:{}",
                id, userId, tenantId, tenantType);

        AccessKey accessKey = accessKeyRepository.findOne(id);
        if (accessKey != null) {
            if (accessKey.getType() != tenantType) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            Long refId = TenantType.ORG == tenantType ? tenantId : userId;
            if (!userId.equals(refId)) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            accessKey.setAccessKey(DigestUtils.md5Hex(UUID.randomUUID().toString()));
            accessKey.setUpdatedBy(nickName);
            accessKeyRepository.save(accessKey);
            return new ApiResponse(ApiResponseCode.SUCCESS);
        }

        return new ApiResponse(ApiResponseCode.INVALID_ACCESS_KEY);
    }

    @RequestMapping(value = "/lock/{id}", method = RequestMethod.POST)
    public ApiResponse lock(@RequestAttribute(ATTR_TENANT_TYPE) TenantType tenantType,
                            @RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                            @RequestAttribute(ATTR_USER_ID) Long userId,
                            @RequestAttribute(ATTR_NICK_NAME) String nickName,
                            @PathVariable("id") Long id) {
        log.info("Lock accesskey, id:{}, userId:{}, tenantId:{}, tenantType:{}",
                id, userId, tenantId, tenantType);

        AccessKey accessKey = accessKeyRepository.findOne(id);
        if (accessKey != null) {
            if (accessKey.getType() != tenantType) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            Long refId = TenantType.ORG == tenantType ? tenantId : userId;
            if (!userId.equals(refId)) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            accessKey.setLocked(true);
            accessKey.setUpdatedBy(nickName);
            accessKeyRepository.save(accessKey);
            return new ApiResponse(ApiResponseCode.SUCCESS);
        }

        return new ApiResponse(ApiResponseCode.INVALID_ACCESS_KEY);
    }

    @RequestMapping(value = "/unlock/{id}", method = RequestMethod.POST)
    public ApiResponse unlock(@RequestAttribute(ATTR_TENANT_TYPE) TenantType tenantType,
                            @RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                            @RequestAttribute(ATTR_USER_ID) Long userId,
                            @RequestAttribute(ATTR_NICK_NAME) String nickName,
                            @PathVariable("id") Long id) {
        log.info("Lock accesskey, id:{}, userId:{}, tenantId:{}, tenantType:{}",
                id, userId, tenantId, tenantType);

        AccessKey accessKey = accessKeyRepository.findOne(id);
        if (accessKey != null) {
            if (accessKey.getType() != tenantType) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            Long refId = TenantType.ORG == tenantType ? tenantId : userId;
            if (!userId.equals(refId)) {
                return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
            }

            accessKey.setLocked(false);
            accessKey.setUpdatedBy(nickName);
            accessKeyRepository.save(accessKey);
            return new ApiResponse(ApiResponseCode.SUCCESS);
        }

        return new ApiResponse(ApiResponseCode.INVALID_ACCESS_KEY);
    }
}
