// Final, Corrected Code for CryptoUtils.java
package com.darshit.javacrypt; // <--- The correct package declaration

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.security.SecureRandom;

public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    public static void encrypt(String key, File inputFile, File outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws Exception {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(key.getBytes("UTF-8"));
            keyBytes = Arrays.copyOf(keyBytes, 16);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            if (cipherMode == Cipher.ENCRYPT_MODE) {
                byte[] iv = new byte[IV_LENGTH];
                new SecureRandom().nextBytes(iv);
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                outputStream.write(iv);
            } else if (cipherMode == Cipher.DECRYPT_MODE) {
                byte[] iv = new byte[IV_LENGTH];
                if (inputStream.read(iv) != IV_LENGTH) {
                    throw new Exception("Invalid file: IV not found or file is too short.");
                }
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            }
            
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
            }
            
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}