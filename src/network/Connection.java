package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import util.Log;

public class Connection {

    @FunctionalInterface
    public interface MessageHandler {

        public void handleMessage(DataInputStream inputStream) throws IOException;
    }

    @FunctionalInterface
    public interface MessagePrinter {

        public void printMessage(DataOutputStream outputStream) throws IOException;
    }

    private final Map<Byte, MessageHandler> handlerMap = new HashMap();
    protected final Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean closed;
    private final List<Runnable> onClose = new LinkedList();

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (!closed) {
                    try {
                        byte id = input.readByte();
                        handlerMap.get(id).handleMessage(input);
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

    public void registerHandler(int id, MessageHandler handler) {
        handlerMap.put((byte) id, handler);
    }

    public void sendMessage(int id, Object... data) {
        sendMessage(id, Arrays.asList(data));
    }

    public void sendMessage(int id, Iterable data) {
        try {
            output.writeByte(id);
            for (Object o : data) {
                if (o instanceof Boolean) {
                    output.writeBoolean((boolean) o);
                } else if (o instanceof Byte) {
                    output.writeByte((byte) o);
                } else if (o instanceof Character) {
                    output.writeChar((char) o);
                } else if (o instanceof Double) {
                    output.writeDouble((double) o);
                } else if (o instanceof Float) {
                    output.writeFloat((float) o);
                } else if (o instanceof Integer) {
                    output.writeInt((int) o);
                } else if (o instanceof Short) {
                    output.writeShort((short) o);
                } else if (o instanceof String) {
                    output.writeUTF((String) o);
                } else {
                    Log.error("Could not write object: " + o);
                }
            }
        } catch (IOException ex) {
            close();
        }
    }

    public void sendMessage(int id, MessagePrinter printer) {
        try {
            output.writeByte(id);
            printer.printMessage(output);
        } catch (IOException ex) {
            close();
        }
    }
}
