package UC3.PConsumer;


public class PConsumer{
        
    private kafkaConsumer consumer1;
    
    private kafkaConsumer consumer2;
    
    private kafkaConsumer consumer3;
    
    private PConsumerGUI gui;

    
    public void main(){
        
        
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

        /**
         * Maximum throughput
         * Source : https://docs.confluent.io/cloud/current/client-apps/optimizing/throughput.html
         * https://granulate.io/optimizing-kafka-performance/
         * https://strimzi.io/blog/2021/01/07/consumer-tuning/
         */
        int fetchMinBytes = 100000;
        int fetchMaxWaitMs = 500;
        
        
        
        /**
         * Consumer threads
         */
        
        /* consumer for sensor 1 and sensor 4 */
        this.consumer1 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "consumer-group", this, fetchMinBytes, 
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 1); // numSensor
        this.consumer1.subscribeTopic("Sensor", 0);
        this.consumer1.start();

        /* consumer for sensor 2 and sensor 5 */
        this.consumer2 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "consumer-group", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 2); // numSensor
        this.consumer2.subscribeTopic("Sensor", 1);
        this.consumer2.start();
        
        /* consumer for sensor 3 and sensor 6 */
        this.consumer3 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "consumer-group", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 3); // numSensor
        this.consumer3.subscribeTopic("Sensor", 2);
        this.consumer3.start();
    }
    
    
    String id = "";
    
    /**
     * Update GUI with record, count by sensor and count total
     * @param received
     * @param numSensor 
     */
    public void updateSensor( String received, int numSensor ){

        id = received.split(",")[0];
        
        this.gui.addToTextField(received, numSensor);
        this.gui.incSensor(numSensor);
        this.gui.incTotal();
                
    }

       
}
