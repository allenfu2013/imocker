package org.allen.imocker.webservice.impl;

import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.webservice.JYWebservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("jyWebserviceImpl")
public class JYWebServiceImpl implements JYWebservice {

    @Autowired
    private ApiInfoDao apiInfoDao;

    public String compareAll(String strName, String strId, String strIDPhoto,
                             String strData, String strTime, String strLicenseCode1,
                             String strEncryValue1, String strLicenseCode2,
                             String strEncryValue2, String strLicenseCode3, String strEncryValue3)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public String compare(String strName, String strId, String strPhoto1,
                          String strPhoto2, String strTime, String strLicenseCode,
                          String strEncryValue) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public String check(String strName, String strId, String strTime,
                        String strLicenseCode, String strEncryValue) {
        List<ApiInfo> apiInfoList = apiInfoDao.findApiInfoByName("jyVerifyIdentity");

        if (!CollectionUtils.isEmpty(apiInfoList)) {
            return apiInfoList.get(0).getRetResult();
        } else {
            return "api not found";
        }
    }

    public String compareLibrary(String strName, String strId,
                                 String strJsonPhotos, String strTime, String strLicenseCode,
                                 String strEncryValue) throws Exception {
        return null;
    }

    public String compareInner(String strName, String strId, String strIDPhoto,
                               String strJsonPhotos, String strTime, String strLicenseCode,
                               String strEncryValue) throws Exception {
        return null;
    }

    public String method(String strHeadIn, String strParamIn,
                         String strEncryValue) throws Exception {
        List<ApiInfo> apiInfoList = apiInfoDao.findApiInfoByName("jyVerifyBankCard");

        if (!CollectionUtils.isEmpty(apiInfoList)) {
            return apiInfoList.get(0).getRetResult();
        } else {
            return "api not found";
        }
    }

    public ApiInfoDao getApiInfoDao() {
        return apiInfoDao;
    }

    public void setApiInfoDao(ApiInfoDao apiInfoDao) {
        this.apiInfoDao = apiInfoDao;
    }
}
