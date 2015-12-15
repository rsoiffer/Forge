package examples;

import java.util.Scanner;
import network.Connection;
import network.NetworkUtils;

public class ChatClient {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = in.nextLine();
        System.out.println("Enter the ip adress to connect to:");

        Connection conn = NetworkUtils.connect(in.nextLine());
        if (conn == null) {
            return;
        }

        System.out.println("Connected to server");
        conn.onClose(() -> System.out.println("Disconnected from server"));

        conn.registerHandler(0, () -> System.out.println(conn.read(String.class)));

        conn.sendMessage(1, name);
        while (!conn.isClosed()) {
            conn.sendMessage(0, in.nextLine());
        }
    }
}
