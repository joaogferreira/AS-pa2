package UC1.PSource;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PSource{
    
    /* Socket variables */
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    
    /* thread that will send data via socket */
    private Thread sender;
    
    /* object to read from file */
    private BufferedReader br;
    
    /* String to be sent via socket */
    private String record;
    
    /* graphical interface */
    private UC1.PSource.PSourceGUI gui;
    
    /* Lock to be used by the thread sending messages */
    private ReentrantLock rl;
    private Condition condition;
    
    
    public void main() throws IOException {
        
        /* graphical interface initialization */
        this.gui = new UC1.PSource.PSourceGUI();
        this.gui.setVisible(true);
        
        /* lock variables initialization */
        this.rl = new ReentrantLock(true);
        this.condition = this.rl.newCondition();
        
        /* thread that will send data via socket initialization */
        this.sender = new Thread(() -> {
            while(true){
                
                rl.lock();
                try {
                    condition.await();
                    threadSendToSocket( record );
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl.unlock();
                }
            }         
        });  
        
        /* open socket */
        createSocket();
        sender.start();
        
        /* display on interface that the socket is connected */
        this.gui.connected();
        
        /* open file to be read */
        openFile();
        
        /* read file and send data to producer via socket */
        readFileAndSendDataToSocket();
        
        /* display on interface that all data was sent */
        this.gui.dataSent();
        
        /* close socket */
        closeSocket();
        
    }
    
    /**
     * Creates a new socket on port 8888
     * Awaits for producer to be ready (while true)
     * @throws IOException 
     */
    public void createSocket() throws IOException{
        
        /* connect socket */
        while(true){
            try{
                this.client= new Socket("localhost", 8888);
                break;
            } catch(IOException e){}
            
        }
        
        /* write on socket */
        this.out = new PrintWriter(client.getOutputStream(), true);
        
        /* read from socket */
        this.in = new BufferedReader( new InputStreamReader(client.getInputStream() ) );
    }
    
    /**
     * Open file to read (sensor.txt)
     */
    public void openFile(){
        try {
            this.br = new BufferedReader(
                    new FileReader(new File("src/sensorData/sensor.txt"))); /* aux file to test purposes */
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Read file and send data via socket
     * Message format : sensor, value, timestamp
     * condition.signal() will wake up sender and that thread will send a message
     * with the current record
     */
    public void readFileAndSendDataToSocket(){
        int i = 1; 
        String aux ;
        
        try{
            while( (aux= br.readLine()) != null){
                switch (i) {
                    case 1:
                        this.record += aux + ",";
                        i++;
                        break;
                    case 2:
                        this.record += aux + ",";
                        i++;
                        break;
                    case 3:
                        this.record += aux; // sensor, value, timestamp 
                        this.rl.lock();
                        try {
                            this.condition.signal();
                        }
                        finally {
                            this.rl.unlock();
                        }  
                        this.gui.incTotal();
                        this.record = "";
                        i = 1;
                        break;
                    default:
                        break;
                }                
            }
            
            /* Send a message signaling that all messages have been sent */
            this.record = "EOF";
            this.rl.lock();
            try {
                this.condition.signal();
            }
            finally {
                this.rl.unlock();
            }  
            
        } catch (IOException ex) {
            Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Send record via socket
     * @param record 
     */
    public void threadSendToSocket(String record){
        this.out.println(record);
    }
    
    /**
     * Close socket
     * @throws IOException 
     */
    public void closeSocket() throws IOException{
        this.in.close();
        this.out.close();
        this.client.close();
    }
}
