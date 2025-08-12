// Final, Corrected Code for JavaCrypt.java
package com.darshit.javacrypt; // <--- The correct package declaration

import java.io.File;
import java.io.Console;
import java.util.Scanner;

public class JavaCrypt {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        if (args.length != 3) {
            System.err.println("Usage: java JavaCrypt <encrypt|decrypt> <inputFile> <outputFile>");
            System.exit(1);
        }

        String mode = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        String password = "";

        if (console == null) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
        } else {
            password = new String(console.readPassword("Enter password: "));
        }
        
        try {
            File input = new File(inputFile);
            File output = new File(outputFile);

            if (mode.equalsIgnoreCase("encrypt")) {
                System.out.println("Encrypting file...");
                CryptoUtils.encrypt(password, input, output);
                System.out.println("File encrypted successfully!");
            } else if (mode.equalsIgnoreCase("decrypt")) {
                System.out.println("Decrypting file...");
                CryptoUtils.decrypt(password, input, output);
                System.out.println("File decrypted successfully!");
            } else {
                System.err.println("Invalid mode. Use 'encrypt' or 'decrypt'.");
                System.exit(1);
            }

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            scanner.close();
        }
    }
}