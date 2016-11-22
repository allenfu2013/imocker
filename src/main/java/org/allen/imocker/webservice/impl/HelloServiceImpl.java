package org.allen.imocker.webservice.impl;

import org.allen.imocker.webservice.HelloService;
import org.springframework.stereotype.Service;

@Service("helloServiceImpl")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String username) {
        return "hello " + username;
    }

}
