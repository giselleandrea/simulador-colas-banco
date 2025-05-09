package com.simulador.banco;

import com.rabbitmq.client.*;

public class ReceptorAvisos {
    private static final String FANOUT_EXCHANGE = "avisos_exchange";

    public static void main(String[] args) throws Exception {
        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, FANOUT_EXCHANGE, "");

        System.out.println("Escuchando avisos...");

        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String aviso = new String(delivery.getBody());
            System.out.println("Aviso recibido: " + aviso);
        }, consumerTag -> {});
    }
}