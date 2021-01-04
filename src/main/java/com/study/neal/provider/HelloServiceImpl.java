package com.study.neal.provider;

import com.study.neal.api.HelloService;

public class HelloServiceImpl implements HelloService {

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public String sayBye(String name) {
        return "Bye " + name;
    }
}
