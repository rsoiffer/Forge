
import java.io.*;

/*
 ID: rory.so1
 LANG: JAVA
 TASK: friday
 */
public class friday {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("friday.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("friday.out")));

        int[] daysInMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int[] days = new int[7];
        int day13 = 0;
        int n = Integer.parseInt(in.readLine());
        for (int year = 1900; year < 1900 + n; year++) {
            for (int month = 0; month < 12; month++) {
                day13 %= 7;
                days[day13]++;
                day13 += daysInMonth[month];
                if (month == 1) {
                    if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                        day13 += 1;
                    }
                }
            }
        }

        String output = "";
        for (int i = 0; i < 7; i++) {
            output += days[i] + (i < 6 ? " " : "");
        }

        out.println(output);
        out.close();
    }
}
