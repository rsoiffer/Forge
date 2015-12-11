package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import static network.Server.PORT;

public class Client {

    public static void main(String[] args) {
        Connection conn = connect("localhost");
        if (conn == null) {
            return;
        }
        System.out.println("Connected to server");
        conn.onClose(() -> System.out.println("Disconnected from server"));

        conn.registerHandler(1, i -> System.out.println(i.readUTF()));

        Scanner in = new Scanner(System.in);
        while (true) {
            conn.sendMessage(1, in.nextLine());
        }
    }

    public static Connection connect(String ip) {
        try {
            return new Connection(new Socket(ip, PORT));
        } catch (IOException ex) {
            System.out.println("Count not connect to server: " + ip);
            return null;
        }
    }
}
