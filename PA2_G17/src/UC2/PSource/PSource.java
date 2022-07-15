package UC2.PSource;

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
    
    /**
     * Socket variables 
     * One client, one buffered reader and one printwriter for
     * each thread sending data
     */
    
    /**
     * Client Sockets
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
     * Threads that will send data to pproducer
     */
    private static Thread sender1;
    
    private static Thread sender2;
    
    private static Thread sender3;
    
    private static Thread sender4;
    
    private static Thread sender5;
    
    private static Thread sender6;
    
    
    private static BufferedReader br;
    
    private static String record = "";
    
    private static String id;
    
    private static UC2.PSource.PSourceGUI gui;
    
    /**
     * ReentrantLocks used for sending data to pproducer
     */
    private ReentrantLock rl1;
    
    private ReentrantLock rl2;
    
    private ReentrantLock rl3;
    
    private ReentrantLock rl4;
    
    private ReentrantLock rl5;
    
    private ReentrantLock rl6;
    
    /**
     * Conditions where threads will wait for new message to send
     */
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
        
        
        /* graphical interface initialize */
        gui = new UC2.PSource.PSourceGUI();
        gui.setVisible(true);
        
        /**
         * Threads that will send data to pproducer
         * One thread per each Producer
         */
        
        /* sender 1 */
        sender1 = new Thread(() -> {
            //threadSendToSocket(out1, record);
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
            //threadSendToSocket(out2, record);
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
            //threadSendToSocket(out3, record);
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
            //threadSendToSocket(out4, record);
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
            //threadSendToSocket(out5, record);
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
            //threadSendToSocket(out6, record);
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
        
        /* display that socket is connected */
        gui.connected();
        
        /* open txt file */
        openFile();
        
        /* read data from file and send it to pproducer (each thread will do it */
        readFileAndSendDataToSocket();
        
        /* display that all data have been sent */
        gui.dataSent();
        
        
    }
    
    /**
     * Create socket - client, input and output
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
     * Read data from txt file 
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
     * Read file and send data via socket
     * Type of message: sensor, value, timestamp
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
     * Send data via socket 
     * @param out
     * @param record 
     */
    public void threadSendToSocket(PrintWriter out, String record){
        if(!this.record.equals("")){
            out.println(record);
        }
        //out.println(record);
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
