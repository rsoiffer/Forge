package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import util.Color4;
import util.Log;
import util.Vec2;
import util.Vec3;

public abstract class NetworkUtils {

    //Starting and connecting to servers
    public static final int PORT = 51243;

    public static Connection connect(String ip) {
        if (ip.equals("")) {
            ip = "localhost";
        }
        try {
            Connection c = new Connection(new Socket(ip, PORT));
            System.out.println("Connected to server");
            c.onClose(() -> System.out.println("Disconnected from server"));
//            //Going too fast sometimes means the message handlers haven't registered
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException ex) {
//                Log.error(ex);
//            }
            return c;
        } catch (IOException ex) {
            System.out.println("Count not connect to server: " + ip);
            return null;
        }
    }

    public static Connection connectManual() {
        System.out.println("Enter the ip address to connect to:");
        Connection c = connect(new Scanner(System.in).nextLine());
        if (c == null) {
            throw new RuntimeException("Failed to connect to server");
        }
        return c;
    }

    public static Thread server(Consumer<Connection> onConnect) {
        return new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                Log.print("Server started on port " + PORT);
                while (true) {
                    Connection conn = new Connection(serverSocket.accept());
                    //Going too fast somtimes means the message handlers haven't registered
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Log.error(ex);
                    }
                    onConnect.accept(conn);
                }
            } catch (IOException ex) {
                Log.error(ex);
            }
        });
    }

    //Readers and writers
    static final Map<Class, Reader<Connection, Object>> READERS = new HashMap();
    static final Map<Class, Writer<Connection, Object>> WRITERS = new HashMap();

    static {
        registerBasicType(Boolean.class, DataInputStream::readBoolean, DataOutputStream::writeBoolean);
        registerBasicType(Byte.class, DataInputStream::readByte, (o, b) -> o.writeByte(b));
        registerBasicType(Float.class, DataInputStream::readFloat, DataOutputStream::writeFloat);
        registerBasicType(Double.class, DataInputStream::readDouble, DataOutputStream::writeDouble);
        registerBasicType(Integer.class, DataInputStream::readInt, DataOutputStream::writeInt);
        registerBasicType(String.class, i -> i.readUTF(), DataOutputStream::writeUTF);

        registerType(Color4.class, c -> new Color4(c.read(Double.class), c.read(Double.class), c.read(Double.class), c.read(Double.class)), (c, o) -> c.write(o.r, o.g, o.b, o.a));
        registerType(Vec2.class, c -> new Vec2(c.read(Double.class), c.read(Double.class)), (c, v) -> c.write(v.x, v.y));
        registerType(Vec3.class, c -> new Vec3(c.read(Double.class), c.read(Double.class), c.read(Double.class)), (c, v) -> c.write(v.x, v.y, v.z));
    }

    private static <T> void registerBasicType(Class<T> c, Reader<DataInputStream, T> reader, Writer<DataOutputStream, T> writer) {
        registerType(c, conn -> reader.read(conn.input), (conn, t) -> writer.write(conn.output, t));
    }

    public static <T> void registerType(Class<T> c, Reader<Connection, T> reader, Writer<Connection, T> writer) {
        READERS.put(c, (Reader) reader);
        WRITERS.put(c, (Writer) writer);
    }

    @FunctionalInterface
    public static interface Reader<T, R> {

        public R read(T t) throws IOException;
    }

    @FunctionalInterface
    public static interface Writer<T, R> {

        public void write(T t, R r) throws IOException;
    }
}
