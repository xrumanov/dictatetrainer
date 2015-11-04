package cz.muni.fi.dictatetrainer.trial.model;

import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.user.model.Student;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity object for Trial - one access of an user to particular dictate
 *
 * Holds this fields:
 * -------------------
 * id: id of the object
 * performed: date on which the trial was performed
 * trialText: text written by a student
 * student: student who accessed the dictate to test him/herself
 * dictate: dictate accessed
 * TODO errors: errors done in this trial
 *
 */
@Entity
@Table(name = "dt_trial")
public class Trial implements Serializable {
    private static final long serialVersionUID = -3438595678954686072L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "performed_at", updatable = false)
    private Date performed;

    @Lob
    @Column(name = "trial_text")
    @NotNull
    private String trialText;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull
    private Student student;

    @ManyToOne
    @JoinColumn(name = "dictate_id")
    @NotNull
    private Dictate dictate;

    public Trial() {
        this.performed = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getPerformed() {
        return performed;
    }

    public void setPerformed(Date performed) {
        this.performed = performed;
    }

    public String getTrialText() {
        return trialText;
    }

    public void setTrialText(String trialText) {
        this.trialText = trialText;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Dictate getDictate() {
        return dictate;
    }

    public void setDictate(Dictate dictate) {
        this.dictate = dictate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trial trial = (Trial) o;

        if (!id.equals(trial.id)) return false;
        if (!student.equals(trial.student)) return false;
        return dictate.equals(trial.dictate);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Trial{" +
                "id=" + id +
                ", performed=" + performed +
                ", trialText='" + trialText + '\'' +
                ", student=" + student +
                ", dictate=" + dictate +
                '}';
    }
}
