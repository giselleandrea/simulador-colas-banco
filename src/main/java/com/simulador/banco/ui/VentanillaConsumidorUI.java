package com.simulador.banco.ui;

import com.rabbitmq.client.*;
import com.simulador.banco.RabbitMQConnection;

import javax.swing.*;

public class VentanillaConsumidorUI {
    private static final String QUEUE = "cola_general";
    private static final String DIRECT_EXCHANGE = "direct_turnos";

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Ventanilla - Atención al Cliente");
        JTextArea area = new JTextArea(10, 40);
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.queueBind(QUEUE, DIRECT_EXCHANGE, "turno");

        area.append("Esperando turnos en Ventanilla...\n");

        channel.basicConsume(QUEUE, true, (consumerTag, delivery) -> {
            String turno = new String(delivery.getBody());
            SwingUtilities.invokeLater(() -> area.append("Atendiendo: " + turno + "\n"));
        }, consumerTag -> {});
    }
}