package com.simulador.banco.ui;

import com.rabbitmq.client.Channel;
import com.simulador.banco.RabbitMQConnection;

import javax.swing.*;
import java.awt.*;

public class ClienteProductorUI {
    private static final String DIRECT_EXCHANGE = "direct_turnos";
    private static final String FANOUT_EXCHANGE = "avisos_exchange";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente - Solicitar Turno");
        JButton boton = new JButton("Solicitar Turno");
        JTextArea area = new JTextArea(5, 30);
        area.setEditable(false);

        boton.addActionListener(e -> {
            try {
                var connection = RabbitMQConnection.getConnection();
                Channel channel = connection.createChannel();

                channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");
                channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout");

                String mensaje = "Turno: Cliente #" + System.currentTimeMillis();
                channel.basicPublish(DIRECT_EXCHANGE, "turno", null, mensaje.getBytes());
                channel.basicPublish(FANOUT_EXCHANGE, "", null, mensaje.getBytes());

                area.append("Turno enviado: " + mensaje + "\n");
                channel.close();
                connection.close();
            } catch (Exception ex) {
                area.append("Error al enviar turno: " + ex.getMessage() + "\n");
            }
        });

        frame.add(boton, BorderLayout.NORTH);
        frame.add(new JScrollPane(area), BorderLayout.CENTER);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}