package br.jus.treto.importador;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) throws IOException {

        System.out.println("-------------------------------------------------------");
        System.out.println(" Utility to import CSV files");
        System.out.println("-------------------------------------------------------");

        if (args.length != 1 && args.length != 5) {
            System.out.println("Parameters missing");
            System.out.println("Usage: ");
            System.out.println("java -jar <file>.jar <host> <SID> <user> <password> <filePath>");
            System.out.println("Example: java -jar importador.jar host1 xe user1 pass123 /tmp/data.csv");
            System.out.println("The file content must be like \"data\";\"data\"");
            return;
        }

        new App().processFile(args[0], args[1], args[2], args[3], args[4]);
    }

    private void processFile(final String host, final String sid, final String user, final String password,
                             final String filePath) throws IOException {

        final File file = new File(filePath);
        final List<String> lines = FileUtils.readLines(file, "UTF-8");

        final int numThreads = 50;
        final int sublinhas = lines.size() / numThreads;

        System.out.println("Started at " + new Date().toString());

        for (int i = 0; i < numThreads; i++) {
            new Insersor(host, sid, user, password, lines.subList(i * sublinhas, ((i + 1) * sublinhas))).start();
        }

        int resto = lines.size() % numThreads;
        if (resto != 0) {
            new Insersor(host, sid, user, password, lines.subList(lines.size() - resto, lines.size())).start();
        }
    }

}
