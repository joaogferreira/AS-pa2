package UC1.PProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PProducer {
    
    /* graphical interface */
    private static PProducerGUI gui;
    
    /* socket variables */
    private static ServerSocket server;
    private static Socket client;
    private static PrintWriter out;
    
    private static BufferedReader in;
    
    private static kafkaProducer producer;
   
    
    public static void main() {
        
        /* graphical interface initialization */
        gui = new PProducerGUI();
        gui.setVisible(true);
        
        
        String servers = "localhost:9092, localhost:9093, "
                + "localhost:9094, localhost:9095, localhost:9096, localhost:9097";
        
        /* Producer properties mentioned in the course slides - values : default */
        String ack = "all";
        int retries = 2147483647;
        int maxInFlightRequestsPerConnection = 5;
        
        /* Some producer properties that we considered useful */
        long bufferMemory = 33554432;
        int batchSize = 16384;
        int deliveryTimeoutMs = 120000;
        
        /* start kafka producer */
        producer = new kafkaProducer( servers, ack, retries, maxInFlightRequestsPerConnection, bufferMemory, batchSize, deliveryTimeoutMs) ;
        producer.start();
        
        /* open socket */
        createSocket();
        
        /* thread to receive data from psource */
        Thread receiver;
        receiver = new Thread(() -> {
            readDataAndUpdateGUI();         
        }); 
        
        receiver.start();
            
    }
    
    /**
     * Open socket on port 8888
     */
    public static void createSocket(){
        try {
            server = new ServerSocket(8888);
            
            client = server.accept();
            
            out = new PrintWriter(client.getOutputStream(), true);
            
            in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
            
        } catch (IOException ex) {
            Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get data from source
     * Update interface
     * Send it to kafka 
     */
    public static void readDataAndUpdateGUI(){
        
        String received = "";
        String id;
        id = "";
        
        /* get data from psource */
        while( !received.equals("EOF") ){
            try {
                received = in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(PProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
             
            id = received.split(",")[0];
            
            if( !received.equals("EOF") ) {
                
                /* show text in gui */
                gui.addToTextField(received);
                
                /* update count by sensor */
                switch(id){
                    case "000001":
                        gui.incSensor1(); break;
                    case "000002":
                        gui.incSensor2(); break;
                    case "000003":
                        gui.incSensor3(); break;
                    case "000004":
                        gui.incSensor4(); break;
                    case "000005":
                        gui.incSensor5(); break;
                    case "000006":
                        gui.incSensor6(); break;
                }
                /* send data to kafka */
                producer.sendData("Sensor", "record", received);
                
                /* update total */
                gui.incTotal();
            }
        }
    }
}
