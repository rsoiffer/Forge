/*
 ID: rory.so1
 LANG: JAVA
 TASK: gift1
 */

import java.io.*;
import java.util.*;

class gift1 {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("gift1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("gift1.out")));

        int n = Integer.parseInt(in.readLine());
        Map<String, Integer> nameMap = new LinkedHashMap();
        for (int i = 0; i < n; i++) {
            nameMap.put(in.readLine(), 0);
        }
        while (in.ready()) {
            String name = in.readLine();
            List<String> giving = tokenize(in.readLine());
            int money = Integer.parseInt(giving.get(0));
            int giveTo = Integer.parseInt(giving.get(1));
            for (int i = 0; i < giveTo; i++) {
                String other = in.readLine();
                nameMap.put(other, nameMap.get(other) + money / giveTo);
                nameMap.put(name, nameMap.get(name) - money / giveTo);
            }
        }

        String output = "";
        for (String name : nameMap.keySet()) {
            output += name + " " + nameMap.get(name) + "\n";
        }
        output = output.substring(0, output.length() - 1);

        out.println(output);
        out.close();
    }

    public static List<String> tokenize(String s) {
        List<String> r = new LinkedList();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            r.add(st.nextToken());
        }
        return r;
    }
}
