package com.echo.chapter1.beanoverview;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }

    @Override
    public String toString() {
        return "Client Service has been created";
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("pojo.xml");
        ClientService clientService = context.getBean("clientService", ClientService.class);
        System.out.println(clientService);
    }
}