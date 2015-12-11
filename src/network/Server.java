package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import util.Log;

public class Server {

    public static final int PORT = 51234;

    public static void main(String[] args) {
        startServer((clientID, conn) -> {
            System.out.println("Client " + clientID + " connected");
            conn.onClose(() -> System.out.println("Client " + clientID + " disconnected"));

            conn.registerHandler(1, i -> conn.sendMessage(1, i.readUTF()));
        });
    }

    public static List<Connection> startServer(BiConsumer<Integer, Connection> onConnect) {
        List<Connection> clients = Collections.synchronizedList(new LinkedList());
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                int clientNum = 0;
                while (true) {
                    Connection conn = new Connection(serverSocket.accept());
                    clients.add(conn);
                    onConnect.accept(clientNum++, conn);
                }
            } catch (IOException ex) {
                Log.error(ex);
            }
        }).start();
        return clients;
    }
}
