package cz.muni.fi.dictatetrainer.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Auxiliary class for password encrypting
 */
public final class PasswordUtils {

    private PasswordUtils() {
    }

    public static String encryptPassword(final String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        md.update(password.getBytes());
        return Base64.getMimeEncoder().encodeToString(md.digest());
    }

}