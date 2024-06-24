package dacd.torrealba.project.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Subscriber implements SubscriberSupplier, MessageListener {
    private final String brokerUrl;
    private final String topicName;
    private final String clientID;
    private final DataLake dataLake;

    public Subscriber(String brokerUrl, String topicName, String clientID, DataLake dataLake) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        this.clientID = clientID;
        this.dataLake = dataLake;
    }

    public void start() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(clientID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);


            MessageConsumer messageConsumer = session.createDurableSubscriber(topic, clientID+"_"+topic);

            messageConsumer.setMessageListener(this);

            System.out.println("Subscriber " + topicName + " esta esperando eventos: ....");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof  TextMessage textMessage) {
            try {
                String jsonEvent = textMessage.getText();
                dataLake.save(jsonEvent);;
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Mensaje no es de tipo TextMensagge");
        }
    }
}
