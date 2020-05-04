import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class client  { 
    final static int ServerPort = 1201; 
    public static void main(String args[]) throws UnknownHostException, IOException  { 
        Scanner scan = new Scanner(System.in); 
        InetAddress ip = InetAddress.getByName("localhost"); 
        Socket s = new Socket(ip, ServerPort); 
        DataInputStream dataIn = new DataInputStream(s.getInputStream()); 
        DataOutputStream dataOut = new DataOutputStream(s.getOutputStream()); 

        Thread send = new Thread(new Runnable() { 
            @Override
            public void run() { 
                while (true) { 
                    String msg = scan.nextLine(); 
                    try { 
                        dataOut.writeUTF(msg); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
        
        Thread rec = new Thread(new Runnable()  { 
            @Override
            public void run() { 
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dataIn.readUTF(); 
                        System.out.println(msg); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
        send.start(); 
        rec.start(); 
  
    } 
} 