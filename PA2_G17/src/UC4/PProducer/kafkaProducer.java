package UC4.PProducer;


import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;



public class kafkaProducer extends Thread {
    
    /* producer object used to send data to topic */
    private final Producer<String, String> producer;
        
    String id;
    
    private ReentrantLock rl;
    
    private Condition condition;
    
    private String topic;
    
    private int partition;
    
    private String key;
    
    private String msg;
    /**
     * Constructor 
     * @param servers
     * @param ack 
     * @param retries 
     * @param deliveryTimeoutMs  
     */
    public kafkaProducer(String servers, 
            String ack, int retries,  int deliveryTimeoutMs){
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        props.put("acks", ack); 
        props.put("retries", retries); 
        props.put("delivery.timeout.ms",  deliveryTimeoutMs);
        
        this.producer = new KafkaProducer<>(props);
        
        this.rl = new ReentrantLock(true);
        
        this.condition = this.rl.newCondition();
        
    }
    
    @Override
    public void run(){
        while(true){
            this.rl.lock();
            try {
                this.condition.await();

                ProducerRecord<String, String> toSend = 
                        new ProducerRecord<>(this.topic, this.partition, 
                                this.key, this.msg);
        
                this.producer.send(toSend);
            } catch (InterruptedException ex) {
            } finally {
                this.rl.unlock();
            }
        }
    }
    
    /**
     * Send data to topic Sensor and partitions
     * Sensor X, topic Sensor, partition X-1
     * @param topic
     * @param partition
     * @param key
     * @param msg 
     */
    public void sendData(String topic, int partition, String key, String msg){
        this.rl.lock();
        try {
            this.topic = topic;
            this.partition = partition;
            this.key = key;
            this.msg = msg;
            this.condition.signal();
        } finally {
            this.rl.unlock();
        }
        
        
    }

    /**
     * Close producer
     */
    public void close(){
        this.producer.close();
    }
}
