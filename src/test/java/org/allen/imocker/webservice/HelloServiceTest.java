package org.allen.imocker.webservice;

import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.junit.Test;

public class HelloServiceTest {

    @Test
    public void test() {
        try {
            Service serviceModel = new ObjectServiceFactory().create(HelloService.class);
            HelloService service = (HelloService) new XFireProxyFactory().create(serviceModel,
                    "http://127.0.0.1:8081/imocker/webservice/HelloService");
            String str = service.sayHello("world");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
