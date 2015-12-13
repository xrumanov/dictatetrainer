package cz.muni.fi.dictatetrainer.schoolclass.model;

import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Entity object for School
 * <p>
 * Holds this fields:
 * -------------------
 * id: id of the object
 * name: name of the school
 * school: school to which the class belongs
 * teacher: teacher which administrate the class (A class teacher)
 */
@Entity
@Table(name = "dt_school_class")
public class SchoolClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(unique = true)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @NotNull
    @ManyToOne
    private User teacher;

    public SchoolClass() {
    }

    public SchoolClass(final String name) {
        this.name = name;
    }

    public SchoolClass(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SchoolClass other = (SchoolClass) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", school=" + school +
                ", teacher=" + teacher +
                '}';
    }
}