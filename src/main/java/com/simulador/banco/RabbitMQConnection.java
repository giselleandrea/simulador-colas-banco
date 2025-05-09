package com.simulador.banco;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

public class RabbitMQConnection {
    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection();
    }
}