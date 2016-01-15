package network;

import engine.Core;
import engine.Signal;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
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
    //public Consumer<Byte> defaultHandler = null;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            Signal<Byte> message = new Signal(null);

            onClose(Core.update.filter(dt -> !closed).filter(message.map(Objects::nonNull))
                    .forEach(dt -> new Thread(() -> {
                        processMessage(message.get());
                        message.set((Byte) null);
                    }))::destroy);

            message.filter(message.map(Objects::isNull)).onEvent(() -> new Thread(() -> {
                try {
                    byte id = input.readByte();
                    if (Core.running) {
                        message.set(id);
                    } else {
                        processMessage(id);
                        message.set((Byte) null);
                    }
                } catch (IOException ex) {
                    close();
                }
            }).start());

            message.sendEvent();
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
        if (handlerMap.containsKey(id)) {
            handlerMap.get(id).run();
            //} else if (defaultHandler != null) {
            //    defaultHandler.accept(id);
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

    public void sendMessage(int id, Object... data) {
        sendMessage(id, () -> write(data));
    }

    public void sendMessage(int id, Runnable printer) {
        if (!closed) {
            try {
                output.writeByte(id);
                printer.run();
            } catch (IOException ex) {
                close();
            }
        }
    }

    public void write(Object... a) {
        if (!closed) {
            try {
                for (Object o : a) {
                    WRITERS.get(o.getClass()).write(this, o);
                }
            } catch (IOException ex) {
                close();
            }
        }
    }
}
