
import java.io.*;

/*
 ID: rory.so1
 LANG: JAVA
 TASK: beads
 */
public class beads {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("beads.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("beads.out")));

        int n = Integer.parseInt(in.readLine());
        String beads = in.readLine();
        String output = "";

        char color = 'w';
        for (int i = 0; i < n; i++) {
            char nc = beads.charAt(i);
            if (nc != color && nc != 'w' && color != 'w') {
                break;
            }
            if (color == 'w') {
                color = nc;
            }
            if (i == n - 1) {
                output += n;
            }
        }

        if (output.length() == 0) {
            int max = 0;
            for (int i = 0; i < n; i++) {
                int c = count(beads, i + 1, true, 'w') + count(beads, i, false, 'w');
                max = Math.max(max, c);
                if (max > beads.length()) {
                    max = beads.length();
                }
            }
            output += max;
        }

        out.println(output);
        out.close();
    }

    public static char at(String beads, int index) {
        if (index < 0) {
            return at(beads, index + beads.length());
        }
        return beads.charAt(index % beads.length());
    }

    public static int count(String beads, int index, boolean forwards, char color) {
        char newColor = at(beads, index);
        if (newColor != color && newColor != 'w' && color != 'w') {
            return 0;
        }
        return 1 + count(beads, index + (forwards ? 1 : -1), forwards, newColor == 'w' ? color : newColor);
    }
}
