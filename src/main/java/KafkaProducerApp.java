import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProducerApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093,localhost:9094,localhost:9095");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(props);
        try {
            for (int i = 0; i < 150; i++) {
                Future<RecordMetadata> future = myProducer.send(
                        new ProducerRecord<String, String>(
                                "my-topic",
                                Integer.toString(i),
                                "MyMessage : " + i
                        )
                );
                System.out.println(future.get().partition());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myProducer.close();
        }
    }

}