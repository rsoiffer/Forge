package lobster;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable {

    private long currentTime;
    private long previousTime;
    private long elapsedAccum;
    private long keepAlive;
    private boolean disconnected;
    
    private Socket socket;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;
    public final int id;
    
    public final ConcurrentLinkedQueue<String> received;
    public final ConcurrentLinkedQueue<String> queued;
    
    public ClientHandler(Socket sock, int i) throws IOException {
        received = new ConcurrentLinkedQueue<>();
        queued = new ConcurrentLinkedQueue<>();
        
        socket = sock;
        disconnected = false;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        id = i;
        
        currentTime = System.currentTimeMillis();
        previousTime = currentTime;
        elapsedAccum = 0;
        keepAlive = 10000; // ten seconds
    }
    
    public boolean isConnected() {
        return !disconnected;
    }
    
    @Override
    public void run() {
        currentTime = System.currentTimeMillis();
        previousTime = currentTime;
        
        try {
            while (!disconnected) {
                currentTime = System.currentTimeMillis();
                long elapsed = currentTime - previousTime;
                if (elapsed < 0) {
                    previousTime = currentTime;
                    continue;
                }
                
                elapsedAccum += elapsed;
                keepAlive -= elapsed;
                
                if (keepAlive < 0) {
                    disconnected = true;
                }
                
                if (elapsedAccum > 1000) {
                    queued.offer("PING");
                    elapsedAccum = 0;
                }
                
                if (streamIn.available() > 0) {
                    String lineWithNewline = streamIn.readUTF();
                    String line = lineWithNewline.substring(0, lineWithNewline.length()-1);
                    
                    if (line.equals("PING")) {
                        keepAlive = 10000; // ten seconds
                    } else received.offer(line);
                }
                
                String toSend;
                boolean sent = false;
                while ((toSend = queued.poll()) != null) {
                    streamOut.writeUTF(toSend + "\n");
                    sent = true;
                }
                if (sent) streamOut.flush();
                
                previousTime = currentTime;
                
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
            
            System.out.println("[INFO] Client " + id + " timed out and has been disconnected.");
        } catch (IOException e) {
            disconnected = true;
            System.out.println("[INFO] Client " + id + " disconnected and created the exception: " + e + ".");
        }
        
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
