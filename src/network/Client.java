package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import static network.Server.PORT;
import util.Log;

public class Client {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = in.nextLine();
        System.out.println("Enter the ip adress to connect to:");

        Connection conn = connect(in.nextLine());
        if (conn == null) {
            return;
        }
        System.out.println("Connected to server");
        conn.onClose(() -> System.out.println("Disconnected from server"));

        conn.registerHandler(0, () -> System.out.println(conn.read(String.class)));

        try {
            Thread.sleep(10); //Going too fast means the message handlers haven't registered
        } catch (InterruptedException ex) {
            Log.error(ex);
        }
        conn.sendMessage(1, name);
        while (!conn.isClosed()) {
            conn.sendMessage(0, in.nextLine());
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
