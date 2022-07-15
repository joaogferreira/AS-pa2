package UC4.PConsumer;


public class PConsumer {
    
    private PConsumerGUI gui;
    
    /**
     * Consumer threads 
     */
    private kafkaConsumer consumer1;
    
    private kafkaConsumer consumer2;
      
    private kafkaConsumer consumer3;
    
    private kafkaConsumer consumer4;
   
    private kafkaConsumer consumer5;
        
    private kafkaConsumer consumer6;
    
    public void main(){
        
        this.gui = new PConsumerGUI("", 1000, 250);
        this.gui.setVisible(true); 

        /**
         * Avoid data loss properties
         * Source: https://blog.softwaremill.com/help-kafka-ate-my-data-ae2e5d3e6576
         * https://www.dataflareup.com/how-to-avoid-data-loss-in-apache-kafka/
         */
        boolean enableAutoCommit = false;
        String autoOffsetReset = "earliest";
        
        /* consumer 1 */
        this.consumer1 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 1);
        this.consumer1.subscribeTopic("Sensor", 0);
        this.consumer1.start();
        
        /* consumer 2 */
        this.consumer2 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 2);
        this.consumer2.subscribeTopic("Sensor", 1);
        this.consumer2.start();
        
        /* consumer 3 */
        this.consumer3 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 3);
        this.consumer3.subscribeTopic("Sensor", 2);
        this.consumer3.start();
        
        /* consumer 4 */
        this.consumer4 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 4);
        this.consumer4.subscribeTopic("Sensor", 3);
        this.consumer4.start();
        
        /* consumer 5 */
        this.consumer5 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 5);
        this.consumer5.subscribeTopic("Sensor", 4);
        this.consumer5.start();
        
        /* consumer 6 */
        this.consumer6 = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this, enableAutoCommit, autoOffsetReset, 6);
        this.consumer6.subscribeTopic("Sensor", 5);
        this.consumer6.start();
        
    }
    
    
    String id = "";
    
    /**
     * Update sensor count and total of records received
     * @param received
     * @param numSensor 
     */
    public void updateSensor(String received, int numSensor){

        id = received.split(",")[0];
        
        this.gui.addToTextField(received, numSensor);
        this.gui.incSensor(numSensor);
        this.gui.incTotal();
      
    }


       
}
