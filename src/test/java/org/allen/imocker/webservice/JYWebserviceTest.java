package org.allen.imocker.webservice;

import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.junit.Test;

public class JYWebserviceTest {

    @Test
    public void test() {
        try {
            Service serviceModel = new ObjectServiceFactory().create(JYWebservice.class);
            JYWebservice service = (JYWebservice) new XFireProxyFactory().create(serviceModel,
                    "http://127.0.0.1:8081/imocker/webservice/JYWebservice");
            String str = service.check(null, null, null, null, null);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
