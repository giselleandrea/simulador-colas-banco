package com.simulador.banco;

import com.rabbitmq.client.*;

public class VentanillaConsumidor {
    private static final String QUEUE = "cola_general";
    private static final String DIRECT_EXCHANGE = "direct_turnos";

    public static void main(String[] args) throws Exception {
        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.queueBind(QUEUE, DIRECT_EXCHANGE, "turno");

        System.out.println("Esperando turnos en Ventanilla...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String turno = new String(delivery.getBody());
            System.out.println("Atendiendo: " + turno);
        };
        channel.basicConsume(QUEUE, true, callback, consumerTag -> {});
    }
}