package UC1.PConsumer;

public class PConsumer extends Thread{
    
    private PConsumerGUI gui;
    
    private kafkaConsumer consumer;
    
    
    public void main(){  
        
        /* graphical interface initialization */
        this.gui = new PConsumerGUI();
        this.gui.setVisible(true);
        
        this.consumer = new kafkaConsumer("localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, "
                + "localhost:9096, localhost:9097", "consumer",
                this);
        
        this.consumer.subscribeTopic("Sensor");
        
        this.consumer.start();
    }
    
    
    String id = "";
    
    /**
     * Update Sensor count (total and by sensor)
     * @param received 
     */
    public void updateSensor(String received){
        
        this.gui.addToTextField(received);

        this.gui.incTotal();

        id = received.split(",")[0];

        switch(id){
            case "000001":
                this.gui.incSensor1(); break;
            case "000002":
                this.gui.incSensor2(); break;
            case "000003":
                this.gui.incSensor3(); break;
            case "000004":
                this.gui.incSensor4(); break;
            case "000005":
                this.gui.incSensor5(); break;
            case "000006":
                this.gui.incSensor6(); break;
        }
                
    }

       
}
