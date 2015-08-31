/**
 * Student entity class
 */
package cz.muni.fi.dictatetrainer.user.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    private static final long serialVersionUID = -6100894877953675646L;

    public Student() {
        setUserType(UserType.STUDENT);
    }

    @Override
    protected List<Roles> getDefaultRoles() {
        return Arrays.asList(Roles.STUDENT);
    }

}