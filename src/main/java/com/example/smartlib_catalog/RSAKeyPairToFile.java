package com.example.smartlib_catalog;

import com.example.smartlib_catalog.configuration.RsaConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Base64;


public class RSAKeyPairToFile {

    public static void main(String[] args) {
        try {
            // 1. Initialize the RSA key pair generator
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size

            // 2. Generate the key pair
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 3. Retrieve the public and private keys
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 4. Encode the keys to Base64 for saving in PEM format
            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getMimeEncoder(64, new byte[] {'\n'}).encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----\n";

            String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getMimeEncoder(64, new byte[] {'\n'}).encodeToString(privateKey.getEncoded()) +
                    "\n-----END PRIVATE KEY-----\n";

            // 5. Save the keys to files
            writeToFile("publicKey.pem", publicKeyPEM);
            writeToFile("privateKey.pem", privateKeyPEM);

            System.out.println("Public and private keys generated and saved.");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Method to write a string to a file
    public static void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
