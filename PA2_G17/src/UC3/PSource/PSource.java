package UC3.PSource;

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

public class PSource {
    
    /* gui */
    private UC3.PSource.PSourceGUI gui;
    
    /**
     * Socket variables
     */
    private Socket client1;
    
    private Socket client2;
    
    private Socket client3;
    
    private BufferedReader in1;
    private PrintWriter out1;
    
    private BufferedReader in2;
    private PrintWriter out2;
    
    private BufferedReader in3;
    private PrintWriter out3;
    
    /**
     * Threads that will send data to pproducer
     */
    private static Thread sender1;
    
    
    private static Thread sender2;
    
    private static Thread sender3;
        
    
    private static BufferedReader br;   
    
    private static String record = "";
    
    private static String id;
    
    /**
     * Locks used for concurrency between sender threads 
     */
    private ReentrantLock rl1;
    
    private ReentrantLock rl2;
    
    private ReentrantLock rl3;
    
    /**
     * Conditions used by sender threads
     */
    private Condition condition1;
    
    private Condition condition2;
    
    private Condition condition3;
    
    
    public void main() throws IOException, InterruptedException {
        
        this.rl1 = new ReentrantLock(true);
        this.condition1 = this.rl1.newCondition();
        
        this.rl2 = new ReentrantLock(true);
        this.condition2 = this.rl2.newCondition();
        
        this.rl3 = new ReentrantLock(true);
        this.condition3 = this.rl3.newCondition();
        
        /* interface */
        this.gui = new UC3.PSource.PSourceGUI();
        gui.setVisible(true);
        
        /**
         * Thread that will send data to pproducer
         * Threads will wait for signal() to send a record to pproducer
         */
        
        /* sender 1 */
        sender1 = new Thread(() -> {
            while(true){
                //threadSendToSocket(out1, record);
         
                rl1.lock();
                try {
                    condition1.await();
                    threadSendToSocket(out1, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl1.unlock();
                }
            }
            
            //threadSendToSocket(out1, record);
        });  
        createSocket(1, 8881);
        sender1.start();

        /* sender 2 */
        sender2 = new Thread(() -> {
            while(true){
                //threadSendToSocket(out2, record); // null é aqui
                
                rl2.lock();
                try {
                    condition2.await();
                    threadSendToSocket(out2, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl2.unlock();
                }
            }         
        });  
        createSocket(2, 8882);
        sender2.start();
        
        /* sender 3 */
        sender3 = new Thread(() -> {
            while(true){
                //threadSendToSocket(out3, record);
                rl3.lock();
                try {
                    condition3.await();
                    threadSendToSocket(out3, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl3.unlock();
                }
            }         
        });  
        createSocket(3, 8883);
        sender3.start();
        
        this.gui.connected();
        
        openFile();
        
        readFileAndSendDataToSocket();
        
        this.gui.dataSent();
        
        
    }
    
    /**
     * Create socket variables: client, input, output
     * @param id
     * @param port
     * @throws IOException 
     */
    public void createSocket(int id, int port ) throws IOException{
        
        /* connect socket */
        switch(id){
            case 1:
                while(true){
                        try{
                            this.client1 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out1  = new PrintWriter(this.client1.getOutputStream(), true);
                this.in1  = new BufferedReader(new InputStreamReader(this.client1.getInputStream()));
                break;
                
            case 2:
                while(true){
                        try{
                            this.client2 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out2  = new PrintWriter(this.client2.getOutputStream(), true);
                this.in2  = new BufferedReader(new InputStreamReader(this.client2.getInputStream()));
                break;
                
            case 3:
                while(true){
                        try{
                            this.client3 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out3  = new PrintWriter(this.client3.getOutputStream(), true);
                this.in3  = new BufferedReader(new InputStreamReader(this.client3.getInputStream()));
                break;
             
        }
        
    }
    

    /**
     * Open file sensor.txt
     */
    public static void openFile(){
        try {
            br = new BufferedReader(
                    new FileReader(new File("src/sensorData/sensor.txt")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Read file and wake up thread to send message to pproducer
     * message format: sensor, value, timestamp
     */
    public void readFileAndSendDataToSocket(){
        int i = 1; 
        String aux ;

        
        try{
            while( (aux= br.readLine()) != null){
                switch (i) {
                    case 1:
                        record += aux + ",";
                        id = aux;
                        i++;
                        break;
                    case 2:
                        record += aux + ",";
                        i++;
                        break;
                    case 3:
                        record += aux; // sensor, value, timestamp 
                        switch(id){
                            case "000001":
                                this.rl1.lock();
                                try {
                                    this.condition1.signal();
                                }
                                finally {
                                    this.rl1.unlock();
                                }
                                break;
                            case "000002":
                                this.rl2.lock();
                                try {
                                    this.condition2.signal();
                                }
                                finally {
                                    this.rl2.unlock();
                                }
                                break;
                            case "000003":
                                this.rl3.lock();
                                try {
                                    this.condition3.signal();
                                }
                                finally {
                                    this.rl3.unlock();
                                }
                                break;
                            case "000004":
                                this.rl1.lock();
                                try {
                                    this.condition1.signal();
                                }
                                finally {
                                    this.rl1.unlock();
                                }  
                                
                                break;
                            case "000005":
                                this.rl2.lock();
                                try {
                                    this.condition2.signal();
                                }
                                finally {
                                    this.rl2.unlock();
                                }
                                
                                break;
                            case "000006":
                              
                                this.rl3.lock();
                                try {
                                    this.condition3.signal();
                                }
                                finally {
                                    this.rl3.unlock();
                                }
                                
                                break;
                        }
                        
                        gui.incTotal();
                        record = "";
                        i = 1;
                        break;
                    default:
                        break;
                }                
            }
            record = "EOF";
            
            this.rl1.lock();
            try {
                this.condition1.signal();
            }
            finally {
                this.rl1.unlock();
            }
            
            
            this.rl2.lock();
            try {
                this.condition2.signal();
            }
            finally {
                this.rl2.unlock();
            }
            
            this.rl3.lock();
            try {
                this.condition3.signal();
            }
            finally {
                this.rl3.unlock();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Thread send record to socket
     * @param out
     * @param record 
     */
    public static void threadSendToSocket(PrintWriter out, String record){
        out.println(record);
    }
    
    /**
     * Close socket variables
     * @throws IOException 
     */
    public void closeSocket() throws IOException{
        this.in1.close();
        this.in2.close();
        this.in3.close();
        
        this.out1.close();
        this.out2.close();
        this.out3.close();
        
        this.client1.close();
        this.client2.close();
        this.client3.close();
    }
    
    
}
