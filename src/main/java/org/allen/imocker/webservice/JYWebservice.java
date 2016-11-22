package org.allen.imocker.webservice;

public interface JYWebservice {
    // 三通接口
    public String compareAll(String strName, String strId, String strIDPhoto,
                             String strData, String strTime, String strLicenseCode1,
                             String strEncryValue1, String strLicenseCode2,
                             String strEncryValue2, String strLicenseCode3, String strEncryValue3)
            throws Exception;

    // 两照比对
    public String compare(String strName, String strId, String strPhoto1,
                          String strPhoto2, String strTime, String strLicenseCode,
                          String strEncryValue) throws Exception;

    /////////////////////////////////////////////////////////////////
    // 核查
    public String check(String strName, String strId, String strTime,
                        String strLicenseCode, String strEncryValue);

    // 比对
    public String compareLibrary(String strName, String strId, String strJsonPhotos,
                                 String strTime, String strLicenseCode, String strEncryValue)
            throws Exception;

    // 内部比对
    public String compareInner(String strName, String strId, String strIDPhoto,
                               String strJsonPhotos, String strTime, String strLicenseCode,
                               String strEncryValue) throws Exception;

    //通用接口
    public String method(String strHeadIn, String strParamIn, String strEncryValue) throws Exception;
}
