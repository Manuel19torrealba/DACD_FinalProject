package dacd.torrealba.project.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class SubscriberBusinessUnit implements SubscriberSupplier {
    private final String brokerUrl;
    private final String topicName1;
    private final String topicName2;
    private final String clientID;
    private final BusinessUnitStore SQLBusinessUnitStore;

    public SubscriberBusinessUnit(String brokerUrl, String topicName1, String topicName2,
                                  String clientID, BusinessUnitStore SQLBusinessUnitStore) {
        this.brokerUrl = brokerUrl;
        this.topicName1 = topicName1;
        this.topicName2 = topicName2;
        this.clientID = clientID;
        this.SQLBusinessUnitStore = SQLBusinessUnitStore;
    }

    @Override
    public void start() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(clientID);
            connection.start();
            System.out.println("Conexion establecida ActiveMQ");

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic1 = session.createTopic(topicName1);
            MessageConsumer messageConsumer1 = session.createDurableSubscriber(topic1, clientID+"_"+topicName1);
            messageConsumer1.setMessageListener(this::onMessage);

            Topic topic2 = session.createTopic(topicName2);
            MessageConsumer messageConsumer2 = session.createDurableSubscriber(topic2, clientID+"_"+topicName2);
            messageConsumer2.setMessageListener(this::onMessage);

            System.out.println("Subscriber esta esperando eventos: ....");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void onMessage(Message message) {
        if (message instanceof  TextMessage textMessage) {
            try {
                String jsonEvent = textMessage.getText();
                SQLBusinessUnitStore.save(jsonEvent);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Mensaje no es de tipo TextMensagge");
        }
    }

}
