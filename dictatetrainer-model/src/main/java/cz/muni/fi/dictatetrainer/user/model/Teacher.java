package cz.muni.fi.dictatetrainer.user.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * Entity object for entity Student (subclass of User)
 *
 * sets the default role to TEACHER
 *
 */
@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {
    private static final long serialVersionUID = 8976498066151628068L;

    public Teacher() {
        setUserType(UserType.TEACHER);
        setSchoolClass(null);
    }

    @Override
    protected List<Roles> getDefaultRoles() {
        return Arrays.asList(Roles.TEACHER);
    }

}