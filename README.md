# Artemis JMS Connection Pooling Example 
Sample POJO implementation using Artemis JMS Client library and JMS Connection Pooling.

## Build the application
Replace the trustore under the `resources` folder with your broker cluster truststore, then run this command:
````shell
mvn clean package
````

## Run the Producer
````shell
mvn exec:java -Dexec.mainClass="com.example.ArtemisJmsPoolProducer" -Dexec.args="<brokerHost1:brokerPort1>,<brokerHostN:brokerPortN> <maxConnections> <queueName> <messageCount>"
````

## Run the Consumer
````shell
mvn exec:java -Dexec.mainClass="com.example.ArtemisJmsPoolConsumer" -Dexec.args="<brokerHost1:brokerPort1>,<brokerHostN:brokerPortN> <maxConnections> <queueName>"
````

### TODO:
- Parameterize broker URI string
- Move application parameters to application.properties.