package UC4.PConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;


public class kafkaConsumer extends Thread {
    
    private final Consumer<String, String> consumer;

    private final PConsumer pconsumer;
    
    private final int numSensor;
    

    /**
     * Constructor
     * @param servers
     * @param groupId
     * @param pconsumer
     * @param enableAutoCommit
     * @param autoOffsetReset
     * @param numSensor 
     */
    public kafkaConsumer(String servers, String groupId, PConsumer pconsumer, boolean enableAutoCommit, String autoOffsetReset, int numSensor){
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", enableAutoCommit);
        props.put("auto.offset.reset", autoOffsetReset);
        
        this.consumer = new KafkaConsumer<>(props);
        
        this.pconsumer = pconsumer;
        
        this.numSensor = numSensor;

    }
    
    
    @Override
    public void run(){
        while(true){
            ConsumerRecords<String, String> records = consumer.poll( Duration.ofMillis(100) );
            
            for (ConsumerRecord<String, String> record : records) {
                this.pconsumer.updateSensor( record.value(), this.numSensor );
                
            }   
        }
    }
    
    

    /**
     * Subscribe topic and specific partition
     * sensor X, topic Sensor, partition X-1
     * @param topic
     * @param partition
     */
    public void subscribeTopic(String topic, int partition){
        this.consumer.assign( Arrays.asList(  new TopicPartition(topic, partition) ));
        
    }
}
