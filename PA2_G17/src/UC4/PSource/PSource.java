package UC4.PSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PSource {
    
    /**
     * Socket variables to be used by each thread 
     */
    private Socket client1;
    
    private Socket client2;
    
    private Socket client3;
    
    private Socket client4;
    
    private Socket client5;
    
    private Socket client6;
    
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
     * Threads that will send data to pproducer 
     */
    private Thread sender1;
    
    private Thread sender2;
    
    private Thread sender3;
    
    private Thread sender4;
    
    private Thread sender5;
    
    private Thread sender6;
    
    
    private BufferedReader br;
    
    private String record = "";
    
    private String id;
    
    /* gui */
    private UC4.PSource.PSourceGUI gui;
    
    /**
     * Locks & Conditions that will be used by threads
     */
    private ReentrantLock rl1;
    
    private ReentrantLock rl2;
    
    private ReentrantLock rl3;
    
    private ReentrantLock rl4;
    
    private ReentrantLock rl5;
    
    private ReentrantLock rl6;
    
    private Condition condition1;
    
    private Condition condition2;
    
    private Condition condition3;
    
    private Condition condition4;
    
    private Condition condition5;
    
    private Condition condition6;
    
    
    public void main() throws IOException {
        
        this.rl1 = new ReentrantLock(true);
        this.condition1 = this.rl1.newCondition();
        
        this.rl2 = new ReentrantLock(true);
        this.condition2 = this.rl2.newCondition();
        
        this.rl3 = new ReentrantLock(true);
        this.condition3 = this.rl3.newCondition();
        
        this.rl4 = new ReentrantLock(true);
        this.condition4 = this.rl4.newCondition();
        
        this.rl5 = new ReentrantLock(true);
        this.condition5 = this.rl5.newCondition();
        
        this.rl6 = new ReentrantLock(true);
        this.condition6 = this.rl6.newCondition();
        
        /* interface */
        this.gui = new PSourceGUI();
        gui.setVisible(true);
        
        /**
        * Threads that will send data to pproducer 
        * They will wait for signal() and then send the message ( while(true) )
        */
        
        /* sender 1 */
        sender1 = new Thread(() -> {
            while(true){
                
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
        });  
        createSocket(1, 8881);
        sender1.start();
        
        /* sender 2 */
        sender2 = new Thread(() -> {
            while(true){
                
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
        
        /*sender 4 */
        sender4 = new Thread(() -> {
            while(true){
                
                rl4.lock();
                try {
                    condition4.await();
                    threadSendToSocket(out4, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl4.unlock();
                }
            }         
        });  
        createSocket(4, 8884);
        sender4.start();
        
        /* sender 5 */
        sender5 = new Thread(() -> {
            while(true){

                rl5.lock();
                try {
                    condition5.await();
                    threadSendToSocket(out5, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl5.unlock();
                }
            }         
        });  
        createSocket(5, 8885);
        sender5.start();
        
        /* sender 6 */
        sender6 = new Thread(() -> {
            while(true){

                rl6.lock();
                try {
                    condition6.await();
                    threadSendToSocket(out6, record);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    rl6.unlock();
                }
            }         
        });  
        createSocket(6, 8886);
        sender6.start();
        
        gui.connected();
        
        openFile();
        
        readFileAndSendDataToSocket();
        gui.dataSent();
        
    }
    
    /**
     * Create socket variables: client, output, input
     * @param id
     * @param port
     * @throws IOException 
     */
    public void createSocket(int id, int port ) throws IOException{

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
            case 4:
                while(true){
                        try{
                            this.client4 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out4  = new PrintWriter(this.client4.getOutputStream(), true);
                this.in4  = new BufferedReader(new InputStreamReader(this.client4.getInputStream()));
                break;
            case 5:
                while(true){
                        try{
                            this.client5 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out5  = new PrintWriter(this.client5.getOutputStream(), true);
                this.in5  = new BufferedReader(new InputStreamReader(this.client5.getInputStream()));
                break;
            case 6:
                while(true){
                        try{
                            this.client6 = new Socket("localhost", port);
                            break;
                        } catch(IOException e){}

                }

                this.out6  = new PrintWriter(this.client6.getOutputStream(), true);
                this.in6  = new BufferedReader(new InputStreamReader(this.client6.getInputStream()));
                break;
             
        }
        
    }
    
    /**
     * Open file sensor.txt
     */
    public void openFile(){
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
        // record type of message: sensor, value, timestamp
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
                                this.rl4.lock();
                                try {
                                    this.condition4.signal();
                                }
                                finally {
                                    this.rl4.unlock();
                                }  
                                
                                break;
                            case "000005":
                                this.rl5.lock();
                                try {
                                    this.condition5.signal();
                                }
                                finally {
                                    this.rl5.unlock();
                                }
                                
                                break;
                            case "000006":
                              
                                this.rl6.lock();
                                try {
                                    this.condition6.signal();
                                }
                                finally {
                                    this.rl6.unlock();
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
            
        } catch (IOException ex) {
            Logger.getLogger(PSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Thread send data to pproduce
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
        this.in4.close();
        this.in5.close();
        this.in6.close();
        
        this.out1.close();
        this.out2.close();
        this.out3.close();
        this.out4.close();
        this.out5.close();
        this.out6.close();
        
        this.client1.close();
        this.client2.close();
        this.client3.close();
        this.client4.close();
        this.client5.close();
        this.client6.close();
    }
}
