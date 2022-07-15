package UC2.PProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PProducer{
    
    /**
     * Socket variables 
     * One server, one client, one buffered reader and one printwriter for
     * each thread receiving data
     */
    
    /**
     * Server Sockets
     */
    private ServerSocket server1;
    
    private ServerSocket server2;
    
    private ServerSocket server3;
    
    private ServerSocket server4;
    
    private ServerSocket server5;
    
    private ServerSocket server6;
    
    /**
     * Sockets 
     */
    private Socket client1;
    
    private Socket client2;
    
    private Socket client3;
    
    private Socket client4;
    
    private Socket client5;
    
    private Socket client6;
    
    /**
     * Input and Output objects
     */
    private BufferedReader in1;
    private PrintWriter out1;
    
    private BufferedReader in2;
    private PrintWriter out2;
    
    private BufferedReader in3;
    private PrintWriter out3;
    
    private BufferedReader in4;
    private PrintWriter out4;
    
    private BufferedReader in5;
    private PrintWriter out5;
    
    private BufferedReader in6;
    private PrintWriter out6;

    /**
     * Threads that will receive data from psource
     */
    private Thread receiver1;
    
    private Thread receiver2;
    
    private Thread receiver3;
    
    private Thread receiver4;
    
    private Thread receiver5;
    
    private Thread receiver6;
    
    
    
    private String id;
    
    /**
     * Kafka Producers 
     */
    private kafkaProducer producer1;
    
    private kafkaProducer producer2;   
    
    private kafkaProducer producer3;
    
    private kafkaProducer producer4;
    
    private kafkaProducer producer5;
    
    private kafkaProducer producer6;    
    
    /**
     * Graphical Interface
     */
    private PProducerGUI gui;
    
    /**
     * Time used to determine messages from this "Run"
     */
    private final long time;
    
    /**
     * Constructor
     * @param time 
     */
    public PProducer(long time){
        this.time = time;
    }
    
        
    public void main() throws IOException {

        
        String servers = "localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097";
        
        /* Producer properties mentioned in the course slides - values : default */
        int maxInFlightRequestsPerConnection = 5;
        
        /**
         * Minimize latency 
         * Source : https://docs.confluent.io/cloud/current/client-apps/optimizing/latency.html
        */
        int lingerMs = 0;
        String compressionType = "none";
        String ack = "1";
        
        
         /**
          * Avoid data loss properties
          * Source: https://blog.softwaremill.com/help-kafka-ate-my-data-ae2e5d3e6576
        */
         
        //String ack = "all";  // minimize latency > data loss
        int retries = 2147483647;
        int deliveryTimeoutMs = 2147483647;
        
        /* Graphical interface initialize */
        this.gui = new PProducerGUI("", 0, 250);
        this.gui.setVisible(true);
        
        /**
         * Start Kafka producers
         */
        this.producer1 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs ) ;
        this.producer1.start();
        
        this.producer2 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs) ;
        this.producer2.start();
        
        this.producer3 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs) ;
        this.producer3.start();

        this.producer4 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs) ;
        this.producer4.start();
        
        this.producer5 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs) ;
        this.producer5.start();
        
        this.producer6 = new kafkaProducer( servers, ack, retries, 
                maxInFlightRequestsPerConnection, lingerMs, compressionType, 
                deliveryTimeoutMs) ;
        this.producer6.start();
        
        /**
         * Threads that will receive data from psource
         * One thread per each Producer
         */
        
        /* receiver 1 */
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
        
        /* receiver 4 */
        this.receiver4 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in4, 4, producer4);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(4, 8884);
        
        /* receiver 5 */
        this.receiver5 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in5, 5, producer5);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(5,8885);
        
        /* receiver 6 */
        this.receiver6 = new Thread(() -> {
            try {
                receiveDataAndSendToKafka(in6, 6, producer6);
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }         
        });  
        this.createSocket(6, 8886);
        
        
        this.receiver1.start();
        
        this.receiver2.start();
        
        this.receiver3.start();
        
        this.receiver4.start();
        
        this.receiver5.start();
        
        this.receiver6.start();
        
    }
    
    /**
     * Create Socket - server, client, input and output
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
            case 4:
                this.server4 = new ServerSocket(port);
                this.client4 = this.server4.accept();
                this.out4 = new PrintWriter( this.client4.getOutputStream(), true);
                this.in4 = new BufferedReader( new InputStreamReader( this.client4.getInputStream() ) );
                break;
            case 5:
                this.server5 = new ServerSocket(port);
                this.client5 = this.server5.accept();
                this.out5 = new PrintWriter( this.client5.getOutputStream(), true);
                this.in5 = new BufferedReader( new InputStreamReader( this.client5.getInputStream() ) );
                break;
            case 6:
                this.server6 = new ServerSocket(port);
                this.client6 = this.server6.accept();
                this.out6 = new PrintWriter( this.client6.getOutputStream(), true);
                this.in6 = new BufferedReader( new InputStreamReader( this.client6.getInputStream() ) );
                break;
        }
        

    }
    
    /**
     * Receive data from psource and sent it to kafka topic and partition
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
                
                /**
                 * Send data to kafka
                 * topic, partition, key, message
                 * partitions start at 0
                 */
                kafkaprod.sendData("Sensor", numSensor-1, "sensor" + numSensor, received+":"+this.time);
            }
        }
    }
}
