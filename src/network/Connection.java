package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static network.NetworkUtils.READERS;
import static network.NetworkUtils.WRITERS;
import util.Log;

public class Connection {

    private Socket socket;
    DataInputStream input;
    DataOutputStream output;

    private boolean closed;
    private final List<Runnable> onClose = new LinkedList();

    private final Map<Byte, Runnable> handlerMap = new HashMap();

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                while (!closed) {
                    try {
                        byte id = input.readByte();
                        processMessage(id);
                    } catch (IOException ex) {
                        close();
                    }
                }
            }).start();
        } catch (IOException ex) {
            close();
        }
    }

    public void close() {
        closed = true;
        try {
            socket.close();
        } catch (IOException ex) {
        }
        handlerMap.clear();
        onClose.forEach(Runnable::run);
        onClose.clear();
    }

    public boolean isClosed() {
        return closed;
    }

    public void onClose(Runnable r) {
        onClose.add(r);
    }

    private void processMessage(byte id) {
//        System.out.println("Received message: " + id);
        if (handlerMap.containsKey(id)) {
            handlerMap.get(id).run();
        } else {
            Log.error("Unknown message id: " + id + " is not one of known messages types " + handlerMap.keySet() + " of connection " + this);
            close();
        }
    }

    public <T> T read(Class<T> c) {
        if (!closed) {
            try {
                return (T) READERS.get(c).read(this);
            } catch (IOException ex) {
                close();
            }
        }
        return null;
    }

    public void registerHandler(int id, Runnable handler) {
        handlerMap.put((byte) id, handler);
    }

    public synchronized void sendMessage(int id, Object... data) {
        sendMessage(id, () -> write(data));
    }

    public synchronized void sendMessage(int id, Runnable printer) {
        //System.out.println("Sent message: " + id);
        if (!closed) {
            try {
                output.writeByte(id);
                printer.run();
            } catch (IOException ex) {
                close();
            }
        }
    }

    public synchronized void write(Object... a) {
        if (!closed) {
            try {
                for (Object o : a) {
                    if (!WRITERS.containsKey(o.getClass())) {
                        throw new RuntimeException("Unknown data type: " + o.getClass());
                    }
                    WRITERS.get(o.getClass()).write(this, o);
                }
            } catch (IOException ex) {
                close();
            }
        }
    }
}
