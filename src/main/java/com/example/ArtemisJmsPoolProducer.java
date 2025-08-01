package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.*;

public class ArtemisJmsPoolProducer {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            // No arguments provided
            System.out.println("Usage: java ArtemisJmsPoolProducer <broker-host:port> <maxConnections> <queueName> <messageCount>");
            System.exit(1);
        }

        ConnectionFactory factory = ConnectionFactoryProvider.getPooledFactory(args[0], Integer.parseInt(args[1]));
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        for (int i = 0; i < Integer.parseInt(args[3]); i++) {
            final int messageId = i; // Capture the current value of i for use in the lambda
            executor.submit(() -> {
                try (Connection connection = factory.createConnection();
                     Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                    ConnectionFactoryProvider.printTopology();
                    Queue queue = session.createQueue(args[2]);
                    MessageProducer producer = session.createProducer(queue);
                    TextMessage message = session.createTextMessage("Message #" + messageId);
                    System.out.println("Sending message: " + message.getText());
                    producer.send(message);
                    executor.shutdown();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}