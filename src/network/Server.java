package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import static network.Server.startServer;
import util.Log;

public class Server {

    private static class ClientInfo {

        Connection conn;
        int id;
        String name;

        public ClientInfo(Connection conn, int id) {
            this.conn = conn;
            this.id = id;
        }
    }

    public static final int PORT = 51234;

    public static void main(String[] args) {
        List<ClientInfo> clients = new LinkedList();
        startServer(conn -> {
            ClientInfo info = new ClientInfo(conn, clients.size());
            clients.add(info);

            Log.print("Client " + info.id + " connected");
            conn.onClose(() -> Log.print("Client " + info.id + " disconnected"));

            conn.registerHandler(0, () -> {
                String msg = info.name + ": " + conn.read(String.class);
                System.out.println(msg);
                clients.stream().filter(c -> c.conn != conn).forEach(c -> c.conn.sendMessage(0, msg));
            });
            conn.registerHandler(1, () -> info.name = conn.read(String.class));
        });
    }

    public static void startServer(Consumer<Connection> onConnect) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                Log.print("Server started on port " + PORT);
                while (true) {
                    Connection conn = new Connection(serverSocket.accept());
                    onConnect.accept(conn);
                }
            } catch (IOException ex) {
                Log.error(ex);
            }
        }).start();
    }
}
