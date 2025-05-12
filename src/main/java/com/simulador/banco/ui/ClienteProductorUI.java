package com.simulador.banco.ui;

import com.rabbitmq.client.Channel;
import com.simulador.banco.RabbitMQConnection;

import javax.swing.*;
import java.awt.*;

public class ClienteProductorUI {
    private static final String DIRECT_EXCHANGE = "direct_turnos";
    private static final String FANOUT_EXCHANGE = "avisos_exchange";
    private static final String COLA_TURNOS = "turnos";

    public static void main(String[] args) {
        inicializarColas();

        JFrame frame = new JFrame("Cliente - Solicitar Turno");
        frame.setLayout(new BorderLayout());

        JButton boton = new JButton("Solicitar Turno");
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(new Color(100, 149, 237));
        boton.setForeground(Color.WHITE);

        JTextArea area = new JTextArea(5, 30);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setBackground(new Color(245, 245, 245));
        area.setForeground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(211, 211, 211)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(boton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        boton.addActionListener(e -> {
            try {
                var connection = RabbitMQConnection.getConnection();
                Channel channel = connection.createChannel();

                // Publicar mensaje
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

        frame.add(panel);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void inicializarColas() {
        try {
            var connection = RabbitMQConnection.getConnection();
            Channel channel = connection.createChannel();

            // Declarar exchange directo y su cola
            channel.exchangeDeclare(DIRECT_EXCHANGE, "direct", true);
            channel.queueDeclare(COLA_TURNOS, true, false, false, null);
            channel.queueBind(COLA_TURNOS, DIRECT_EXCHANGE, "turno");

            // Declarar exchange fanout (para avisos públicos)
            channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout", true);

            channel.close();
            connection.close();
            System.out.println("RabbitMQ: colas y exchanges inicializados correctamente.");
        } catch (Exception ex) {
            System.err.println("Error al inicializar colas: " + ex.getMessage());
        }
    }
}
