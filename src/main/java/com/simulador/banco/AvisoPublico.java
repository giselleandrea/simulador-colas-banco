package com.simulador.banco;

import com.rabbitmq.client.Channel;

public class AvisoPublico {
    private static final String FANOUT_EXCHANGE = "avisos_exchange";

    public static void main(String[] args) throws Exception {
        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout");
        String mensaje = "¡Nuevo turno disponible!";
        channel.basicPublish(FANOUT_EXCHANGE, "", null, mensaje.getBytes());

        System.out.println("Aviso enviado: " + mensaje);
        channel.close();
        connection.close();
    }
}