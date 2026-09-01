/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iu.likeherotozero.security;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
/**
 *
 * @author Iskender Dumlu
 */
@ApplicationScoped
public class PasswordService {
    
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private final SecureRandom secureRandom
            = new SecureRandom();

    public String hash(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        byte[] passwordHash
                = generateHash(password, salt, ITERATIONS);

        return ITERATIONS
                + ":"
                + Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(passwordHash);
    }

    public boolean matches(
            String password,
            String storedPasswordHash) {

        if (password == null || storedPasswordHash == null) {
            return false;
        }

        try {
            String[] parts = storedPasswordHash.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash
                    = Base64.getDecoder().decode(parts[2]);

            byte[] actualHash
                    = generateHash(password, salt, iterations);

            return MessageDigest.isEqual(
                    expectedHash,
                    actualHash
            );
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private byte[] generateHash(
            String password,
            byte[] salt,
            int iterations) {

        PBEKeySpec keySpecification = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory keyFactory
                    = SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            return keyFactory
                    .generateSecret(keySpecification)
                    .getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Das Passwort konnte nicht verarbeitet werden.",
                    exception
            );
        } finally {
            keySpecification.clearPassword();
        }
    }
}

