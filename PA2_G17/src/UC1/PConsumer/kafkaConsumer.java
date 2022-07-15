package UC1.PConsumer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class kafkaConsumer extends Thread {
    
    private final Consumer<String, String> consumer;

    private final PConsumer pconsumer;
    
    /**
     * Constructor
     * @param servers
     * @param groupId
     * @param pconsumer 
     */
    public kafkaConsumer(String servers, String groupId, PConsumer pconsumer){
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        this.consumer = new KafkaConsumer<>(props);
        
        this.pconsumer = pconsumer;

    }
    
    
    @Override
    public void run(){
        
        while(true){
            ConsumerRecords<String, String> records = consumer.poll( Duration.ofMillis(100) ); // read data from kafka
            
            for (ConsumerRecord<String, String> record : records) {
                this.pconsumer.updateSensor( record.value() ); // update gui from pconsumer
                
            }   
        }
    }
    
    
    /**
     * Subscribe topic
     * @param topic 
     */
    public void subscribeTopic(String topic){
        this.consumer.subscribe( Pattern.compile(topic) );
    }
}
