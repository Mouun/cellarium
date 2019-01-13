package bdd;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
/**
Classe regroupant l'ensemble des méthodes relatives aux hashage et salage des mots de passe utilisateur
 */

public class Password {
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256; // taille du hash

    /**
     * Génération aleatoire d'un grain de sel
     * @return tableau aleatoire de 16 octets
     */

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Méthode regroupant hashage et salage d'un password
     * @param password le mot de passe utilisateur fournis lors de l'inscription
     * @param salt un grain de sel généré
     * @return le hash généré avec le grain de sel
     */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     *
     * méthode similaire qui supporte un String en paramètre
     */
    public static byte[] hash(String password, byte[] salt) {
        char[] mdp = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(mdp, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(mdp, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }


    /**
     * Permet de vérifier si le mot de passe fournis en clair correspond bien à expectedHash
     * @param password
     * @param salt
     * @param expectedHash
     * @return boolean
     */
    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }
    /**
     *
     * méthode similaire qui supporte un String en paramètre
     */
    public static boolean isExpectedPassword(String password, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);
        char[] mdp = password.toCharArray();
        Arrays.fill(mdp, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }
    /**
     *
     * Convertion d'un hash en String
     */
    public static String toString(byte[] hash){
        String temp ="";
        for(int i =0; i<hash.length;i++){
            temp+=hash[i];
        }
        return temp;
    }
}

