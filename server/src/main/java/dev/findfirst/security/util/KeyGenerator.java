package dev.findfirst.security.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyGenerator {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");
  public static final int LINE_LENGTH = 64;
  private static Logger logger = LoggerFactory.getLogger(KeyGenerator.class);

  public static void generateKeys(String privateKeyPath, String publicKeyPath)
      throws NoSuchAlgorithmException, IOException {

    if (Files.exists(Path.of(privateKeyPath)) && Files.exists(Path.of(publicKeyPath))) {
      return;
    }

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair pair = keyGen.generateKeyPair();
    PrivateKey privateKey = pair.getPrivate();
    PublicKey publicKey = pair.getPublic();

    // Encoder encoder = Base64.getEncoder();
    Base64.Encoder encoder = Base64.getEncoder();

    // Save the public key in PEM format
    String pubKeyBegin = "-----BEGIN PUBLIC KEY-----";
    String pubKeyEnd = "-----END PUBLIC KEY-----";

    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
    String encodedPubKey = pubKeyBegin + System.lineSeparator()
        + encoder.encodeToString(pubKeySpec.getEncoded()) + System.lineSeparator() + pubKeyEnd;
    Files.writeString(Path.of(publicKeyPath), encodedPubKey);

    // Save the private key in PKCS8 format
    String privKeyBegin = "-----BEGIN PRIVATE KEY-----";
    String privKeyEnd = "-----END PRIVATE KEY-----";

    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
    String encodedPrivKey = privKeyBegin + System.lineSeparator()
        + encoder.encodeToString(privKeySpec.getEncoded()) + System.lineSeparator() + privKeyEnd;

    Files.writeString(Path.of(privateKeyPath), encodedPrivKey);

  }

  public static void main(String[] args) {
    String privateKeyPath = "src/main/resources/app.key";
    String publicKeyPath = "src/main/resources/app.pub";

    File privateKeyFile = new File(privateKeyPath);
    File publicKeyFile = new File(publicKeyPath);

    if (!privateKeyFile.exists() && !publicKeyFile.exists()) {
      try {
        generateKeys(privateKeyPath, publicKeyPath);
        logger.info("Keys Generated");
      } catch (NoSuchAlgorithmException | IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Keys already exist.");
    }
  }
}
