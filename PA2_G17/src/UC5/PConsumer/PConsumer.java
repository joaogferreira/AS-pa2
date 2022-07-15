package UC5.PConsumer;


public class PConsumer{
        
    private kafkaConsumer consumer1;
    
    private kafkaConsumer consumer2;
    
    private kafkaConsumer consumer3;
    
    private kafkaConsumer consumer4;
    
    private kafkaConsumer consumer5;
    
    private kafkaConsumer consumer6;
    
    private kafkaConsumer consumer7;
    
    private kafkaConsumer consumer8;
    
    private kafkaConsumer consumer9;
    
    private PConsumerGUI gui1;
    private PConsumerGUI gui2;
    private PConsumerGUI gui3;

    private minMaxGUI results;

    private float min;
    
    private float max;
    
    private final long time;
    
    public PConsumer(long time){
        this.time = time;
    }
    
    
    public void main(){
        
        
        this.min = 1000f;
        this.max = 0f;
        
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
         * results gui 
         */
        this.results = new minMaxGUI();
        results.setVisible(true);
        
        /**
         * Consumer threads - group 1
         */
        
        this.gui1 = new PConsumerGUI("", 1000, 0, this.time, 1 );
        gui1.setVisible(true); 
        
        /* GROUP 1 - consumer for sensor 1 and sensor 4 */
        this.consumer1 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group1", this, fetchMinBytes, 
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 1, this.time); // numSensor
        this.consumer1.subscribeTopic("Sensor", 0, 3);
        this.consumer1.start();

        /* GROUP 1 - consumer for sensor 2 and sensor 5 */
        this.consumer2 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group1", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 2, this.time); // numSensor
        this.consumer2.subscribeTopic("Sensor", 1, 4);
        this.consumer2.start();
        
        /* GROUP 1 - consumer for sensor 3 and sensor 6 */
        this.consumer3 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group1", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 3, this.time); // numSensor
        this.consumer3.subscribeTopic("Sensor", 2, 5);
        this.consumer3.start();
        
        
        
        /**
         * Consumer threads - group 2
         */
        
        this.gui2 = new PConsumerGUI("", 0, 1000, this.time, 2 );
        gui2.setVisible(true); 
        
        /* GROUP 2 - consumer for sensor 1 and sensor 4 */
        this.consumer4 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group2", this, fetchMinBytes, 
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 1, this.time); // numSensor
        this.consumer4.subscribeTopic("Sensor", 0, 3);
        this.consumer4.start();

        /* GROUP 2 - consumer for sensor 2 and sensor 5 */
        this.consumer5 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group2", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 2, this.time); // numSensor
        this.consumer5.subscribeTopic("Sensor", 1, 4);
        this.consumer5.start();
        
        /* GROUP 2 - consumer for sensor 3 and sensor 6 */
        this.consumer6 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group2", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 3, this.time); // numSensor
        this.consumer6.subscribeTopic("Sensor", 2, 5);
        this.consumer6.start();
        
        /**
         * Consumer threads - group 3
         */
        
        this.gui3 = new PConsumerGUI("", 1000, 1000, this.time, 3 );
        gui3.setVisible(true); 
        
        /* GROUP 3 - consumer for sensor 1 and sensor 4 */
        this.consumer7 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group3", this, fetchMinBytes, 
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 1, this.time); // numSensor
        this.consumer7.subscribeTopic("Sensor", 0, 3);
        this.consumer7.start();

        /* GROUP 3 - consumer for sensor 2 and sensor 5 */
        this.consumer8 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group3", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 2, this.time); // numSensor
        this.consumer8.subscribeTopic("Sensor", 1, 4);
        this.consumer8.start();
        
        /* GROUP 3 - consumer for sensor 3 and sensor 6 */
        this.consumer9 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097", 
                "group3", this, fetchMinBytes,
                enableAutoCommit, allowAutoCreateTopics, 
                autoOffsetReset, fetchMaxWaitMs, 3, this.time); // numSensor
        this.consumer9.subscribeTopic("Sensor", 2, 5);
        this.consumer9.start();
    }
    
    
    String id = "";
    
    /**
     * Update GUI with record, count by sensor and count total
     * @param received
     * @param numSensor 
     * @param group 
     */
    public void updateSensor( String received, int numSensor, String group ){

        id = received.split(",")[0];
        
        switch(group){
            case "group1":
                this.gui1.addToTextField(received, numSensor);
                this.gui1.incSensor(numSensor);
                this.gui1.incTotal();
                break;
            case "group2":
                this.gui2.addToTextField(received, numSensor);
                this.gui2.incSensor(numSensor);
                this.gui2.incTotal();
                break;
            case "group3":
                this.gui3.addToTextField(received, numSensor);
                this.gui3.incSensor(numSensor);
                this.gui3.incTotal();
                break;
        }
        
        float value = Float.parseFloat(received.split(",")[1]);
        
        if (value > this.max){
            this.max = value;
            this.results.setMax(this.max);
        }
        
        if (value < this.min){
            this.min = value;
            this.results.setMin(this.min);
        }
        
        
                
    }

       
}
