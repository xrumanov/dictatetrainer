/**
 * Helper method for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.user;

public class UserTestUtils {

    private UserTestUtils() {
    }

    public static String getJsonWithPassword(final String password) {
        return String.format("{\"password\":\"%s\"}", password);
    }

    public static String getJsonWithEmailAndPassword(final String email, final String password) {
        return String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
    }

}
