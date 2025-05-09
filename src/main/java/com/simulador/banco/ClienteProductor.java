package com.simulador.banco;

import com.rabbitmq.client.Channel;

public class ClienteProductor {
    private static final String DIRECT_EXCHANGE = "direct_turnos";

    public static void main(String[] args) throws Exception {
        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");
        String mensaje = "Turno: Cliente #" + System.currentTimeMillis();
        channel.basicPublish(DIRECT_EXCHANGE, "turno", null, mensaje.getBytes());

        System.out.println("Turno enviado: " + mensaje);
        channel.close();
        connection.close();
    }
}