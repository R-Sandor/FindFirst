package dev.findfirst.security.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class KeyGeneratorTest {

  @TempDir
  Path tempDir;

  @Test
  void testGenerateKeysProducesValidKeys() throws NoSuchAlgorithmException, IOException {
    Path privateKeyPath = tempDir.resolve("test.key");
    Path publicKeyPath = tempDir.resolve("test.pub");

    KeyGenerator.generateKeys(privateKeyPath.toString(), publicKeyPath.toString());

    String pubKeyContent = Files.readString(publicKeyPath);
    assertTrue(pubKeyContent.contains("-----BEGIN PUBLIC KEY-----"), "Public key should start with PEM header");
    assertTrue(pubKeyContent.contains("-----END PUBLIC KEY-----"), "Public key should end with PEM footer");

    String privKeyContent = Files.readString(privateKeyPath);
    assertTrue(privKeyContent.contains("-----BEGIN PRIVATE KEY-----"), "Private key should start with PEM header");
    assertTrue(privKeyContent.contains("-----END PRIVATE KEY-----"), "Private key should end with PEM footer");

    String pubKeyBase64 = pubKeyContent
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", ""); 
    byte[] pubKeyBytes = Base64.getDecoder().decode(pubKeyBase64);
    assertTrue(pubKeyBytes.length > 0, "Decoded public key should not be empty");
  }

  @Test
  void testGenerateKeysDoesNotOverwriteExistingKeys() throws NoSuchAlgorithmException, IOException {
    Path privateKeyPath = tempDir.resolve("existing.key");
    Path publicKeyPath = tempDir.resolve("existing.pub");
    Files.writeString(privateKeyPath, "dummy private key");
    Files.writeString(publicKeyPath, "dummy public key");

    KeyGenerator.generateKeys(privateKeyPath.toString(), publicKeyPath.toString());

    assertEquals("dummy private key", Files.readString(privateKeyPath), "Private key file should not be overwritten");
    assertEquals("dummy public key", Files.readString(publicKeyPath), "Public key file should not be overwritten");
  }

  @Test
  void testGenerateKeysProducesDifferentKeysOnSeparateRuns() throws NoSuchAlgorithmException, IOException {
    Path privateKeyPath1 = tempDir.resolve("test1.key");
    Path publicKeyPath1 = tempDir.resolve("test1.pub");
    Path privateKeyPath2 = tempDir.resolve("test2.key");
    Path publicKeyPath2 = tempDir.resolve("test2.pub");
    
    KeyGenerator.generateKeys(privateKeyPath1.toString(), publicKeyPath1.toString());
    KeyGenerator.generateKeys(privateKeyPath2.toString(), publicKeyPath2.toString());

    String pubKeyContent1 = Files.readString(publicKeyPath1);
    String pubKeyContent2 = Files.readString(publicKeyPath2);

    String pubKeyBase64_1 = pubKeyContent1
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "");
    String pubKeyBase64_2 = pubKeyContent2
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "");

    assertNotEquals(pubKeyBase64_1, pubKeyBase64_2, "Each generated public key should be unique");

  }
}
