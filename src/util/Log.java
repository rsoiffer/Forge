package util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Log {

    public static final boolean FORCE_SAVE = false;
    public static final boolean PRINT_TEXT = true;
    public static final boolean PRINT_ERRORS = true;

    public static boolean save;
    public static String toWrite = "";

    public static void close() {
        try {
            PrintWriter writer = new PrintWriter("logs/latest.txt", "UTF-8");
            writer.print(toWrite);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (FORCE_SAVE || save) {
            try {
                PrintWriter writer = new PrintWriter("logs/log-" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".txt", "UTF-8");
                writer.print(toWrite);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void error(Object o) {
        if (PRINT_ERRORS) {
            System.out.println(o);
            if (o instanceof Throwable) {
                ((Throwable) o).printStackTrace();
            }
        }
        save = true;
        toWrite += o.toString() + "\n";
    }

    public static void print(Object o) {
        if (PRINT_TEXT) {
            System.out.println(o);
        }
        toWrite += o.toString() + "\n";
    }
}
