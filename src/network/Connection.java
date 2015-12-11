package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import util.Log;

public class Connection {

    @FunctionalInterface
    public interface MessageReader<T> {

        public T readMessage(DataInputStream inputStream) throws IOException;
    }

    @FunctionalInterface
    public interface MessageWriter<T> {

        public void writeMessage(DataOutputStream outputStream, T t) throws IOException;
    }

    private static final Map<Class, MessageReader> readers = new HashMap();
    private static final Map<Class, MessageWriter> writers = new HashMap();

    static {
        registerType(Boolean.class, DataInputStream::readBoolean, DataOutputStream::writeBoolean);
        registerType(Byte.class, DataInputStream::readByte, (o, b) -> o.writeByte(b));
        registerType(Float.class, DataInputStream::readFloat, DataOutputStream::writeFloat);
        registerType(Double.class, DataInputStream::readDouble, DataOutputStream::writeDouble);
        registerType(Integer.class, DataInputStream::readInt, DataOutputStream::writeInt);
        registerType(String.class, i -> i.readUTF(), DataOutputStream::writeUTF);
        //registerType(Vec2.class, i -> new Vec2(i.readDouble(), i.readDouble()), (o, v) -> o.writeDouble(v.x));
        //}
    }

    private final Map<Byte, Runnable> handlerMap = Collections.synchronizedMap(new HashMap());

    private DataInputStream input;
    private DataOutputStream output;
    private boolean closed;
    private final List<Runnable> onClose = new LinkedList();

    public Connection(Socket socket) {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (!closed) {
                    try {
                        byte id = input.readByte();
                        if (handlerMap.containsKey(id)) {
                            handlerMap.get(id).run();
                        } else {
                            Log.error("Unknown message id: " + id + " is not one of known messages types " + handlerMap.keySet() + " of connection " + this);
                            close();
                        }
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

    public <T> T read(Class<T> c) {
        try {
            return (T) readers.get(c).readMessage(input);
        } catch (IOException ex) {
            close();
            return null;
        }
    }

    public void registerHandler(int id, Runnable handler) {
        handlerMap.put((byte) id, handler);
    }

    public static <T> void registerType(Class<T> c, MessageReader<T> reader, MessageWriter<T> writer) {
        readers.put(c, reader);
        writers.put(c, writer);
    }

    public void sendMessage(int id, Object... data) {
        sendMessage(id, Arrays.asList(data));
    }

    public void sendMessage(int id, Iterable data) {
        sendMessage(id, () -> data.forEach(this::write));
    }

    public void sendMessage(int id, Runnable printer) {
        try {
            output.writeByte(id);
            printer.run();
        } catch (IOException ex) {
            close();
        }
    }

    public void write(Object o) {
        try {
            writers.get(o.getClass()).writeMessage(output, o);
        } catch (IOException ex) {
            close();
        }
    }
}
