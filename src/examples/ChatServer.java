package examples;

import java.util.LinkedList;
import java.util.List;
import network.Connection;
import network.NetworkUtils;
import util.Log;

public class ChatServer {

    private static class ClientInfo {

        Connection conn;
        int id;
        String name;

        public ClientInfo(Connection conn, int id) {
            this.conn = conn;
            this.id = id;
        }
    }

    public static void main(String[] args) {
        List<ClientInfo> clients = new LinkedList();
        NetworkUtils.server(conn -> {
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
        }).start();
    }
}
