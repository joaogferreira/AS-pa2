package UC2.PConsumer;


public class PConsumer {
    
    private kafkaConsumer consumer1; 
    private kafkaConsumer consumer2;
    private kafkaConsumer consumer3;
    private kafkaConsumer consumer4;
    private kafkaConsumer consumer5;
    private kafkaConsumer consumer6;
    private PConsumerGUI gui;
    
    private final long time;
    
    public PConsumer(long time){
        this.time = time;
    }
    
    
    public void main(){
        
        /* graphical interface initialize */
        this.gui = new PConsumerGUI("", 1000, 250);
        gui.setVisible(true); 
        
        
        /**
         * Properties to avoid duplicates and data loss when commiting offsets
         * + consumer.commitAsync() -> low latency
         * Source: https://strimzi.io/blog/2021/01/07/consumer-tuning/
         */
        boolean enableAutoCommit = false;
        boolean allowAutoCreateTopics = false;
        String autoOffsetReset = "earliest";
        int fetchMinBytes = 10000;        
        
        /**
         * Consumers Threads 
         * One per each Sensor 
         */
        this.consumer1 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", "consumer", this, fetchMinBytes, 
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 1, this.time); // number of sensor, time of messages
        this.consumer1.subscribeTopic("Sensor", 0); // topic, partition
        this.consumer1.start();

        this.consumer2 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", "consumer", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 2, this.time); // number of sensor, time of messages
        this.consumer2.subscribeTopic("Sensor", 1); // topic, partition
        this.consumer2.start();
        
        this.consumer3 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", "consumer", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 3, this.time); // number of sensor, time of messages
        this.consumer3.subscribeTopic("Sensor", 2); // topic, partition
        this.consumer3.start();
        
        this.consumer4 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", "consumer", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 4, this.time); // number of sensor, time of messages
        this.consumer4.subscribeTopic("Sensor", 3); // topic, partition
        this.consumer4.start();

        this.consumer5 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", "consumer", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 5, this.time); // number of sensor, time of messages
        this.consumer5.subscribeTopic("Sensor", 4); // topic, partition
        this.consumer5.start();
        
        this.consumer6 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, "
                + "localhost:9097", 
                "consumer", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, 6, this.time); // number of sensor, time of messages
        this.consumer6.subscribeTopic("Sensor", 5); // topic, partition
        this.consumer6.start();

    }
    
    
    String id = "";
    
    /**
     * Update number of records received for each sensor
     * @param received
     * @param numSensor 
     */
    public void updateSensor(String received, int numSensor){

        id = received.split(",")[0];
        
        this.gui.addToTextField(received.split(":")[0], numSensor);
        this.gui.incSensor(numSensor);
        this.gui.incTotal();
      
    }

       
}