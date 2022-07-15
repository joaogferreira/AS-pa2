package UC6.PConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;


public class kafkaConsumer extends Thread {
    
    private final Consumer<String, String> consumer;

    private final PConsumer pconsumer;
    
    private final int numSensor;
    
    private long time;
    
    /**
     * Constructor
     * @param servers
     * @param groupId
     * @param pconsumer
     * @param fetchMinBytes
     * @param enableAutoCommit
     * @param allowAutoCreateTopics
     * @param autoOffsetReset
     * @param fetchMaxWaitMs
     * @param numSensor 
     * @param time 
     */
    public kafkaConsumer(String servers, String groupId, PConsumer pconsumer,
            int fetchMinBytes, boolean enableAutoCommit , boolean allowAutoCreateTopics, 
            String autoOffsetReset, int fetchMaxWaitMs, int numSensor, long time){
        
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        
        /* minimize latency */
        props.put("fetch.min.bytes", fetchMinBytes);
        props.put("fetch.max.wait.ms", fetchMaxWaitMs);
        
        /* 
        Properties to avoid duplicates and data loss when commiting offsets
        Source: https://strimzi.io/blog/2021/01/07/consumer-tuning/
        */
        props.put("enable.auto.commit", enableAutoCommit);
        props.put("allow.auto.create.topics", allowAutoCreateTopics);
        props.put("auto.offset.reset", autoOffsetReset);
        
        this.consumer = new KafkaConsumer<>(props);
        
        this.pconsumer = pconsumer;
        
        this.numSensor = numSensor;
        
        this.time = time;

    }
    
    
    @Override
    public void run(){

        while(true){
            ConsumerRecords<String, String> records = consumer.poll( Duration.ofMillis(30) );
            
            for (ConsumerRecord<String, String> record : records) {
                //System.out.println(record.value());
                if( (record.value().split(",")[0].equals("00000"+this.numSensor) || record.value().split(",")[0].equals("00000"+ (this.numSensor+3))) &&
                        record.value().split(",").length == 3) {
                    if ( record.value().split(":")[1].equals( Long.toString(this.time) ) ){
                        this.pconsumer.updateSensor( record.value(), numSensor); 
                    }
                }
                //this.pconsumer.updateSensor( record.value(), this.numSensor);
            }
            
            /* 
            Avoid duplicates
            Does not wait for the broker to respond to a commit request
            -> low latency 
            source : https://strimzi.io/blog/2021/01/07/consumer-tuning/
            */
            consumer.commitAsync();
            
            /*
            commits the offsets of all messages returned from polling. 
            -> high latency
            -> low throughput
            */
            //consumer.commitSync();
        }
    }
    
    /**
     * Subscribe topic and specific partition
     * sensor X, topic Sensor, partition X-1
     * @param topic
     * @param partition1
     * @param partition2
     */
    public void subscribeTopic(String topic, int partition1, int partition2){
        this.consumer.assign( Arrays.asList(  new TopicPartition(topic, partition1), new TopicPartition(topic, partition2) ) );
        
    }
}