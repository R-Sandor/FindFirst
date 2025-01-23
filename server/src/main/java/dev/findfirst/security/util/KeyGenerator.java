package dev.findfirst.security.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyGenerator {

  public static void generateKeys(String privateKeyPath, String publicKeyPath)
      throws NoSuchAlgorithmException, IOException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair pair = keyGen.generateKeyPair();
    PrivateKey privateKey = pair.getPrivate();
    PublicKey publicKey = pair.getPublic();

    // Save the private key in PKCS8 format
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
    try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
      fos.write(pkcs8EncodedKeySpec.getEncoded());
    }

    // Save the public key in X.509 format
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
    try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
      fos.write(x509EncodedKeySpec.getEncoded());
    }
  }

  public static void main(String[] args) {
    String privateKeyPath = "app.key";
    String publicKeyPath = "app.pub";

    File privateKeyFile = new File(privateKeyPath);
    File publicKeyFile = new File(publicKeyPath);

    if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
      try {
        generateKeys(privateKeyPath, publicKeyPath);
        System.out.println("Keys generated successfully.");
      } catch (NoSuchAlgorithmException | IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Keys already exist.");
    }
  }
}
