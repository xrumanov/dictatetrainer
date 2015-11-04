package cz.muni.fi.dictatetrainer.error.model;

import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Entity object for Error, repack the Mistake object from Corrector module
 * and add Student who does the mistake and dictate in which the mistake was done
 *
 * Mainly for sake of generating statistic data about students and dictates
 *
 * Holds this fields:
 * -------------------
 *
 * id: id of the object
 * wordPosition: position of the word in the dictate counted as token number from beginning of the transcript
 * correctWord: word from the dictate transcript
 * writtenWord: word written by the student
 * errorPriority: priority of mistake - 1 is lowest 100 is highest priority
 * errorDescription: Description of error in the word, for example clarification why is the other variant correct
 * dictate: dictate in which the error was present
 * student: student who wrote the error
 *
 */
@Entity
@Table(name = "dt_error")
public class Error implements Serializable {
    private static final long serialVersionUID = -7739078176125621949L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "word_position")
    private Integer wordPosition;

    @NotNull
    @Column(name = "correct_word")
    private String correctWord;

    @NotNull
    @Column(name = "written_word")
    private String writtenWord;

    @NotNull
    @Column(name = "error_priority")
    private int errorPriority;

    @NotNull
    @Column(name = "description")
    private String errorDescription;

    @ManyToOne
    @JoinColumn(name = "dictate_id")
    @NotNull
    private Dictate dictate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull
    private User student;

    public Error() {
    }

    public Error(final Dictate dictate, final User student) {
        this.dictate = dictate;
        this.student = student;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWordPosition() { return wordPosition; }

    public void setWordPosition(Integer wordPosition) { this.wordPosition = wordPosition;  }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getWrittenWord() {
        return writtenWord;
    }

    public void setWrittenWord(String writtenWord) {
        this.writtenWord = writtenWord;
    }

    public int getErrorPriority() {
        return errorPriority;
    }

    public void setErrorPriority(int errorPriority) {
        this.errorPriority = errorPriority;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }


    public Dictate getDictate() {
        return dictate;
    }

    public void setDictate(final Dictate dictate) {
        this.dictate = dictate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dictate == null) ? 0 : dictate.hashCode());
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
        final Error other = (Error) obj;
        if (dictate == null) {
            if (other.dictate != null)
                return false;
        } else if (!dictate.equals(other.dictate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Error [dictate=" + dictate + "]";
    }

}
