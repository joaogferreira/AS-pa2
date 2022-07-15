package UC3.PProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PProducer {
    
    /* Socket servers for each thread */
    private ServerSocket server1;
    
    
    private ServerSocket server2;
    
    private ServerSocket server3;
    
    /* Socket clients for each thread */
    private Socket client1;
    
    private Socket client2;
    
    private Socket client3;
    
    /* Variables for input/output */
    private BufferedReader in1;
    private PrintWriter out1;
    private BufferedReader in2;
    private PrintWriter out2;
    private BufferedReader in3;
    private PrintWriter out3;
    
    /* threads that will receive data */
    private Thread receiver1;
    
    private Thread receiver2;
    
    private Thread receiver3;
    
    
    private String id;
    
    /* Kafka producers threads */
    private kafkaProducer producer1;
    private kafkaProducer producer2;   
    private kafkaProducer producer3;
    
    private PProducerGUI gui;
    
    
    public void main() throws IOException {
        
        /* start kafka producers */
        String servers = "localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097";
        
        
        /* Producer properties mentioned in the course slides - values : default */
        int maxInFlightRequestsPerConnection = 5;
        
        /**
         * Maximum throughput
         * Source: https://docs.confluent.io/cloud/current/client-apps/optimizing/throughput.html
         * https://granulate.io/optimizing-kafka-performance/
         * https://strimzi.io/blog/2021/01/07/consumer-tuning/
         */
        int batchSize = 200000;
        int lingerMs = 100;
        String compressionType = "lz4";
        String ack = "1";
        int bufferMemory = 50000000;
        
        this.gui = new PProducerGUI("", 0, 250);
        this.gui.setVisible(true);
        
        
        /**
         * Properties to avoid data loss when commiting offsets
         * Source: https://strimzi.io/blog/2021/01/07/consumer-tuning/
         * https://blog.softwaremill.com/help-kafka-ate-my-data-ae2e5d3e6576
         */
        //String ack = "all"; // maximum throughput > data loss
        int retries = 2147483647;
        int deliveryTimeoutMs = 2147483647;
        
        boolean enableAutoCommit = false;
        boolean allowAutoCreateTopics = false;
        String autoOffsetReset = "earliest";
        int fetchMinBytes = 10000;  
        
        /**
         * Producer threads 
         */
        
        /* Producer for sensor 1 and 4 */
        this.producer1 = new kafkaProducer( servers, ack, retries, maxInFlightRequestsPerConnection, 
                batchSize, lingerMs, compressionType, bufferMemory, deliveryTimeoutMs, enableAutoCommit, allowAutoCreateTopics, autoOffsetReset, fetchMinBytes) ;
        this.producer1.start();
        
        /* Producer for sensor 2 and 5 */
        this.producer2 = new kafkaProducer( servers, ack, retries, maxInFlightRequestsPerConnection, 
                batchSize, lingerMs, compressionType, bufferMemory, deliveryTimeoutMs, enableAutoCommit, allowAutoCreateTopics, autoOffsetReset, fetchMinBytes) ;
        this.producer2.start();

        /* Producer for sensor 3 and 6 */
        this.producer3 = new kafkaProducer( servers, ack, retries, maxInFlightRequestsPerConnection, 
                batchSize, lingerMs, compressionType, bufferMemory, deliveryTimeoutMs, enableAutoCommit, allowAutoCreateTopics, autoOffsetReset, fetchMinBytes) ;
        this.producer3.start();
        
        
        /**
         * Each thread will receive data and sent it to kafka topic and partition
         */
        
        this.receiver1 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in1, 1, producer1);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(1, 8881);
        
        /* receiver 2 */
        this.receiver2 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in2, 2, producer2);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(2, 8882);
        
        /* receiver 3 */
        this.receiver3 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in3, 3, producer3);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(3, 8883);
        
        this.receiver1.start();
        
        this.receiver2.start();
        
        this.receiver3.start();
            
    }
    
    /**
     * Initialize socket variables
     * @param id
     * @param port
     * @throws IOException 
     */
    public void createSocket(int id, int port) throws IOException{
        
        switch(id){
            case 1:
                this.server1 = new ServerSocket(port);
                this.client1 = this.server1.accept();
                this.out1 = new PrintWriter( this.client1.getOutputStream(), true);
                this.in1 = new BufferedReader( new InputStreamReader( this.client1.getInputStream() ) );
                break;
            case 2:
                this.server2 = new ServerSocket(port);
                this.client2 = this.server2.accept();
                this.out2 = new PrintWriter( this.client2.getOutputStream(), true);
                this.in2 = new BufferedReader( new InputStreamReader( this.client2.getInputStream() ) );
                break;
            case 3:
                this.server3 = new ServerSocket(port);
                this.client3 = this.server3.accept();
                this.out3 = new PrintWriter( this.client3.getOutputStream(), true);
                this.in3 = new BufferedReader( new InputStreamReader( this.client3.getInputStream() ) );
                break;
            
        }
    }
    
    /**
     * Receive data from socket and send it to kafka topic and partition
     * @param in
     * @param numSensor
     * @param kafkaprod
     * @throws IOException 
     */
    public void receiveDataAndSendToKafka(BufferedReader in, int numSensor, kafkaProducer kafkaprod) throws IOException{
        String received = "";
        
        while( !received.equals("EOF") ){
            received = in.readLine();

            id = received.split(",")[0];

            if(!received.equals("EOF")) {
                
                this.gui.addToTextField(received, numSensor);
                this.gui.incSensor(numSensor);
                this.gui.incTotal();
                
                /* 
                    send data to kafka 
                    topic, partition, key, msg
                    partitions start at 0
                */
                kafkaprod.sendData("Sensor", numSensor-1, "sensor" + numSensor, received);
            }
        }
    }
}
