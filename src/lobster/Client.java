package lobster;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import util.Log;

public class Client {

    public static void main(String[] args) {
        Client client = new Client("localhost", 55555);
        client.commandDispatch = (int id1, String... command) -> {
        };

        client.queued.offer("JOINED");

        Thread d = new Thread(client::run);
        //d.setDaemon(true);
        d.start();
    }

    private Socket socket;
    private ServerHandler handler;
    private Thread thread;
    private int id;

    public final Queue<String> queued;
    public final Queue<String> received;

    public CommandHandler commandDispatch;
    public boolean dictator = false;
    public int dictatorId = 0;

    public Client(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            handler = new ServerHandler(socket);
        } catch (IOException ex) {
            Log.error(ex);
        }

        queued = handler.queued;
        received = handler.received;

        thread = new Thread(handler);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        boolean start = false;
        while (handler.isConnected() || !start) {
            if (handler.isConnected()) {
                start = true;
            }

            String msg = received.poll();
            if (msg != null) {
                String[] args = msg.split("\\s");
                int messageId = 0;
                if (args[args.length - 1].startsWith("client.")) {
                    messageId = Integer.parseInt(args[args.length - 1].replace("client.", ""));
                    String[] argsCopy = args.clone();
                    args = new String[argsCopy.length - 1];
                    for (int i = 0; i < args.length; ++i) {
                        args[i] = argsCopy[i];
                    }
                }
                switch (args[0]) {
                    case "SETID":
                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("[WARN] Invalid SETID parameter: " + id);
                        }
                        break;
                    case "DICTATOR":
                        try {
                            dictator = Integer.parseInt(args[1]) == id ? true : false;
                            dictatorId = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("[WARN] Invalid DICTATOR parameter: " + dictator + ", " + dictatorId);
                        }
                        break;
                    default:
                        if (commandDispatch != null) {
                            commandDispatch.handle(messageId, args);
                        }
                        break;
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void stop() {
        handler.disconnect();
    }

    @Override
    public void finalize() {
        // not guarenteed to be called, but doesn't hurt
        stop();
    }
}
