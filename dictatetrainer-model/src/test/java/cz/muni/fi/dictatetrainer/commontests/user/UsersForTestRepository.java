/**
 * Prepared User entities used for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.user;

import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.common.utils.PasswordUtils;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

@Ignore
public class UsersForTestRepository {

    public static User mrkvicka() {
        final User user = new Student();
        user.setName("Jozko Mrkvicka");
        user.setEmail("mrkvicka@domain.com");
        user.setPassword("123456");

        return user;
    }

    public static User gates() {
        final User user = new Student();
        user.setName("Billi Gates");
        user.setEmail("gates@domain.com");
        user.setPassword("987789");

        return user;
    }

    public static User admin() {
        final User user = new Teacher();
        user.setName("Admin");
        user.setEmail("admin@domain.com");
        user.setPassword("654321");
        user.setRoles(Arrays.asList(User.Roles.TEACHER, User.Roles.ADMINISTRATOR));

        return user;
    }

    public static List<User> allUsers() {
        return Arrays.asList(admin(), mrkvicka(), gates());
    }

    public static User userWithIdAndCreatedAt(final User user, final Long id) {
        user.setId(id);
        user.setCreatedAt(DateUtils.getAsDateTime("2015-11-03T22:35:42Z"));

        return user;
    }

    public static User userWithEncryptedPassword(final User user) {
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
        return user;
    }
}