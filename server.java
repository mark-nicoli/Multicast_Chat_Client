import java.io.*; 
import java.util.*; 
import java.net.*; 
import java.util.Scanner;
  
// Server class 
public class server  
{ 
    static Vector<ClientHandler> ar = new Vector<>(); //active clients
      
    // counter for clients 
    static int i = 1; 
    public static void main(String[] args) throws IOException  
    { 
        ServerSocket ss = new ServerSocket(1201); 
        Socket s; 
        while (true)  
        { 
            s = ss.accept(); 
            System.out.println("joined client details : " + s); 
            DataInputStream dataIn = new DataInputStream(s.getInputStream()); 
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream()); 
            ClientHandler mtch = new ClientHandler(s,"client " + i, dataIn, dataOut); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
            ar.add(mtch); 
            t.start(); 
            i++; 
        } 
    } 
} 
class ClientHandler implements Runnable  { 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dataIn; 
    final DataOutputStream dataOut; 
    Socket s; 
    boolean isloggedin; 
    public ClientHandler(Socket s, String name, DataInputStream dataIn, DataOutputStream dataOut) { 
        this.dataIn = dataIn; 
        this.dataOut = dataOut; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
  
    @Override
    public void run() { 
        String received; 
        while (true)  { 
            try{ 
                received = dataIn.readUTF(); 
                System.out.println(received); 
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.s.close(); 
                    break; 
                } 
                //stating the recipient
                StringTokenizer st = new StringTokenizer(received, "@"); 
                String message = st.nextToken(); 
                String recipient = st.nextToken(); 
               //search for recipient stated by sender
                for (ClientHandler mc : server.ar)  { 
                    if (mc.name.equals(recipient) && mc.isloggedin==true) { 
                        mc.dataOut.writeUTF("["+this.name+"] : "+message); 
                        break; 
                    } 
                } 
            } catch (IOException e) {   
                e.printStackTrace(); 
            }     
        } 
        try{ 
            this.dataIn.close(); 
            this.dataOut.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
}