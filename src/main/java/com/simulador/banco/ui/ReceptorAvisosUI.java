package com.simulador.banco.ui;

import com.rabbitmq.client.*;
import com.simulador.banco.RabbitMQConnection;

import javax.swing.*;
import java.awt.*;

public class ReceptorAvisosUI {
    private static final String FANOUT_EXCHANGE = "avisos_exchange";

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Panel de Avisos Públicos");
        JTextArea area = new JTextArea(10, 40);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setBackground(new Color(245, 245, 245));
        area.setForeground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(211, 211, 211)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton button = new JButton("Cerrar");
        button.addActionListener(e -> frame.dispose());
        panel.add(button, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
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
