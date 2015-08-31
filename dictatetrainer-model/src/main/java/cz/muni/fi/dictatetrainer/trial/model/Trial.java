package cz.muni.fi.dictatetrainer.trial.model;

import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.user.model.Student;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

        return id.equals(trial.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
