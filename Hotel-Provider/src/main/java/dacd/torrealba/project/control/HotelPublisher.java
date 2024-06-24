package dacd.torrealba.project.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dacd.torrealba.project.model.Hotel;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.time.Instant;

public class HotelPublisher implements HotelStore {
    private final String brokerUrl;
    private final String topicName;
    private final Gson gson;

    public HotelPublisher(String brokerUrl, String topicName) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new adaptInstant())
                .create();
    }

    @Override
    public void save(Hotel hotel) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);

            MessageProducer producer = session.createProducer(topic);
            String jsonMessage = gson.toJson(hotel);
            TextMessage message = session.createTextMessage(jsonMessage);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static class adaptInstant extends TypeAdapter<Instant> {
        @Override
        public void write(JsonWriter out, Instant value) throws IOException {
            out.value(value.toString());
        }
        @Override
        public Instant read(JsonReader in) throws IOException {
            return Instant.parse(in.nextString());
        }
    }

}
