package com.simulador.banco;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitMQInitializer {
    private static final String NOMBRE_COLA_TURNOS = "turnos";
    private static final String NOMBRE_EXCHANGE_AVISOS = "avisos";

    public static void inicializar() {
        try {
            Connection conexion = RabbitMQConnection.getConnection();
            Channel canal = conexion.createChannel();

            //Crear cola de turnos
            canal.queueDeclare(NOMBRE_COLA_TURNOS, true, false, false, null);

            //Crear exchange fanout para avisos
            canal.exchangeDeclare(NOMBRE_EXCHANGE_AVISOS, "fanout", true);

            //Solo para evidencia: crear una cola temporal y vincularla al exchange
            String queueName = "avisos_inicial_temp";
            canal.queueDeclare(queueName, true, false, false, null);
            canal.queueBind(queueName, NOMBRE_EXCHANGE_AVISOS, "");

            System.out.println("RabbitMQ inicializado correctamente.");
        } catch (Exception e) {
            System.err.println("Error inicializando RabbitMQ: " + e.getMessage());
        }
    }
}
