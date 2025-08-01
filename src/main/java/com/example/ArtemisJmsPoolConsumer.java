package com.example;

import javax.jms.*;

public class ArtemisJmsPoolConsumer {
    private static int timeout = 10000; // Default timeout for message receive in ms
    
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            // No arguments provided
            System.out.println("Usage: java ArtemisJmsPoolConsumer <broker-host:port> <maxConnections> <queueName>");
            System.exit(1);
        }

        ConnectionFactory factory = ConnectionFactoryProvider.getPooledFactory(args[0], Integer.parseInt(args[1]));

        try (Connection connection = factory.createConnection()) {
            connection.start();

            try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                Queue queue = session.createQueue(args[2]);
                MessageConsumer consumer = session.createConsumer(queue);

                while (true) {
                    Thread.sleep(1000); // Simulate some processing delay
                    Message message = consumer.receive(timeout);
                    if (message == null) {
                        // No more messages after timeout
                        break;
                    }
                    if (message instanceof TextMessage) {
                        System.out.println("Received: " + ((TextMessage) message).getText());
                    } else {
                        System.out.println("No message received.");
                    }
                }
            }
            catch(JMSException e) {
                e.printStackTrace();
            }
        }
    }
}