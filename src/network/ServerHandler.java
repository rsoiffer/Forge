package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerHandler implements Runnable {

    private long currentTime;
    private long previousTime;
    private long elapsedAccum;
    private long keepAlive;

    private Socket socket;
    private boolean disconnected;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;

    public final Queue<String> received;
    public final Queue<String> queued;

    public ServerHandler(Socket sock) throws IOException {
        received = new ConcurrentLinkedQueue<>();
        queued = new ConcurrentLinkedQueue<>();

        socket = sock;
        disconnected = false;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        currentTime = System.currentTimeMillis();
        previousTime = currentTime;
        elapsedAccum = 0;
        keepAlive = 10000; // ten seconds
    }

    public boolean isConnected() {
        return !disconnected;
    }

    public void disconnect() {
        disconnected = true;
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
                    String line = lineWithNewline.substring(0, lineWithNewline.length() - 1);

                    if (line.equals("PING")) {
                        keepAlive = 10000; // ten seconds
                    } else {
                        received.offer(line);
                    }
                }

                String toSend;
                boolean sent = false;
                while ((toSend = queued.poll()) != null) {
                    streamOut.writeUTF(toSend + "\n");
                    sent = true;
                }
                if (sent) {
                    streamOut.flush();
                }

                previousTime = currentTime;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }

            System.out.println("[INFO] Server timed out and has been disconnected.");
        } catch (IOException e) {
            disconnected = true;
            System.out.println("[INFO] Server disconnected and created the exception: " + e + ".");
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
