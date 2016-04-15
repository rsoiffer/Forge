/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cruz
 */
public class CommController {

    private static final List<Command> commands = new ArrayList();

    public static void add(Command c) {

        commands.add(c);
    }

    public static void remove(Command c) {

        commands.remove(c);
    }

    public static void remove(String c) {

        Command r = null;

        for (Command cs : commands) {

            if (cs.getCode().equals(c)) {

                r = cs;
                break;
            }
        }

        commands.remove(r);
    }

    public static String runCommand(String c) {

        if (c != null && c.length() > 1) {

            String comName = getCommandName(c);

            if (comName.charAt(0) == '\\') {

                Command comm = null;

                for (Command cm : commands) {

                    if (cm != null && cm.getCode().equals(comName)) {

                        comm = cm;
                    }
                }

                if (comm != null) {

                    List<String> args = getCommandArgs(comName, c);
                    return comm.run(args);
                }
            }

            return comName + " is not a command.";
        }

        return "Please type a command after \\.";
    }

    private static List<String> getCommandArgs(String cn, String c) {

        int begin = cn.length();
        int end;
        List<String> args = new ArrayList();

        while (begin < c.length()) {

            char test = c.charAt(begin);

            if (test != ' ') {

                end = begin;

                while (end < c.length()) {

                    test = c.charAt(end);

                    if (test == ' ' || end == c.length() - 1) {

                        String a = c.substring(begin, end + (test == ' ' ? 0 : 1));
                        args.add(a);
                        begin = end;
                        break;
                    }

                    end++;
                }
            }

            begin++;
        }

        return args;
    }

    private static String getCommandName(String c) {

        int index = 0;
        char test;

        do {

            test = c.charAt(index);

            if (test == ' ') {

                break;
            } else {

                index++;
            }
        } while (index < c.length());

        return c.substring(0, index);
    }
}
