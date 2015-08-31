/**
 * Teacher entity class
 */
package cz.muni.fi.dictatetrainer.user.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {
    private static final long serialVersionUID = 8976498066151628068L;

    public Teacher() {
        setUserType(UserType.TEACHER);
    }

    @Override
    protected List<Roles> getDefaultRoles() {
        return Arrays.asList(Roles.TEACHER);
    }

}