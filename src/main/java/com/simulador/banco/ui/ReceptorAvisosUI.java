package com.simulador.banco.ui;

import com.rabbitmq.client.*;
import com.simulador.banco.RabbitMQConnection;

import javax.swing.*;

public class ReceptorAvisosUI {
    private static final String FANOUT_EXCHANGE = "avisos_exchange";

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Panel de Avisos Públicos");
        JTextArea area = new JTextArea(10, 40);
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        var connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, FANOUT_EXCHANGE, "");

        area.append("Esperando avisos públicos...\n");

        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String aviso = new String(delivery.getBody());
            SwingUtilities.invokeLater(() -> area.append("Aviso recibido: " + aviso + "\n"));
        }, consumerTag -> {});
    }
}