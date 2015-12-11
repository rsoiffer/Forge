package lobster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import util.Log;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> new Server(55555));
        t.start();
        t.join();
    }

    private ServerSocket server;
    private List<ClientHandler> handlers;
    private int currentId;
    private int authoritarian = 0;

    public Server(int port) {
        try {
            currentId = 100; // arbitrary number
            handlers = new LinkedList();

            System.out.println("[INFO] Binding to port " + port + ".");
            server = new ServerSocket(port);
            System.out.println("[INFO] Server started: " + server + ".");

            Thread hc = new Thread(() -> {
                while (true) {
                    synchronized (this) {
                        handlers.removeIf(x -> !x.isConnected());
                        if (!handlers.stream().anyMatch(h -> h.id == authoritarian)) {
                            authoritarian = 0;
                        }

                        if (authoritarian == 0) {
                            if (!handlers.isEmpty()) {
                                authoritarian = handlers.get(0).id;
                                handlers.forEach(i -> handlers.get(0).queued.offer("DICTATOR " + authoritarian));
                            }
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException i) {
                    }
                }
            });

            hc.setDaemon(true);
            hc.start();

            Thread handl = new Thread(() -> {
                while (true) {
                    synchronized (this) {
                        handlers.forEach((handler) -> {
                            String message;
                            while ((message = handler.received.poll()) != null) {
                                String m2 = message;
                                handlers.forEach(handler2 -> {
                                    if (handler != handler2) {
                                        handler2.queued.offer(m2 + " client." + handler.id);
                                    }
                                });
                            }
                        });
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException i) {
                    }
                }
            });

            handl.setDaemon(true);
            handl.start();

            while (true) {
                Socket socket = server.accept();
                System.out.println("[INFO] Client has connected: " + socket);
                ClientHandler handle = new ClientHandler(socket, ++currentId);
                handle.queued.offer("SETID " + currentId);
                handle.queued.offer("DICTATOR " + authoritarian);
                Thread thread = new Thread(handle);
                synchronized (this) {
                    handlers.add(handle);
                    thread.setDaemon(true);
                    thread.start();
                }
            }
        } catch (IOException e) {
            Log.error(e);
        }
    }
}
