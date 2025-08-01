package com.example;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;

public class ConnectionFactoryProvider {
    private static ActiveMQConnectionFactory baseFactory;
    private static JmsPoolConnectionFactory pooledFactory;
    private static final int maxSessionsPerConnection = 1;

    public static synchronized JmsPoolConnectionFactory getPooledFactory(String brokerUrl, int maxConnections) {
        if (pooledFactory == null) {
            baseFactory = new ActiveMQConnectionFactory(
                "(" + brokerUrl +
                ")?useTopologyForLoadBalancing=true&sslEnabled=true&verifyHost=false&trustStoreType=PKCS12&trustStorePath=truststore.p12&trustStorePassword=changeit");
                        
            // Uncomment the following to use a specific load balancing policy    
            // baseFactory.setConnectionLoadBalancingPolicyClassName(
            //     "org.apache.activemq.artemis.api.core.client.loadbalance.RandomConnectionLoadBalancingPolicy");
            
            pooledFactory = new JmsPoolConnectionFactory();
            pooledFactory.setConnectionFactory(baseFactory);
            pooledFactory.setMaxConnections(maxConnections);
            pooledFactory.setMaxSessionsPerConnection(maxSessionsPerConnection);
        }
        return pooledFactory;
    }

    public static void printTopology() {
        System.out.println("Use topology for load balancing: " + baseFactory.getServerLocator().getUseTopologyForLoadBalancing());
        System.out.println("Cluster topology: " + baseFactory.getServerLocator().getTopology().describe());
    }
}