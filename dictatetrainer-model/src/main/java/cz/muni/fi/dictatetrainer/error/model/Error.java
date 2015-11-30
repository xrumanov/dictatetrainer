package cz.muni.fi.dictatetrainer.error.model;

import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Entity object for Error, repack the Mistake object from Corrector module
 * and add Student who does the mistake and dictate in which the mistake was done
 * <p>
 * Mainly for sake of generating statistic data about students and dictates
 * <p>
 * Holds this fields:
 * -------------------
 * id: id of the object
 * mistakeCharPosInWord: position of first mistaken character (beginning with 1)
 * correctChars: correct characters from transcript
 * writtenChars: incorrect characters written by student
 * correctWord: word from the dictate transcript
 * writtenWord: word written by the student
 * previousWord: correct word from transcript that precede the mistaken word
 * nextWord: correct word from transcript that is next to the mistaken word
 * wordPosition: position of the word in the dictate counted as token number from beginning of the transcript
 * lemma: lemma of the error word
 * posTag: part of speech tag
 * sentence: whole sentence in which mistake occurs, for future use
 * <p>
 * errorType: enum, can be SURPLUS_WORD, MISSING_WORD or MISTAKE
 * errorPriority: priority of mistake - 1 is lowest 100 is highest priority
 * errorDescription: Description of error in the word, for example clarification why is the other variant correct
 * <p>
 * dictate: dictate in which the error was present
 * student: student who wrote the error
 * trial: trial in which the error was done
 */
@Entity
@Table(name = "dt_error")
public class Error implements Serializable {
    private static final long serialVersionUID = -7739078176125621949L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "char_position")
    private Integer mistakeCharPosInWord;

    @Column(name = "correct_chars")
    private String correctChars;

    @Column(name = "written_chars")
    private String writtenChars;

    @Column(name = "correct_word")
    private String correctWord;

    @Column(name = "written_word")
    private String writtenWord;

    @Column(name = "previous_word")
    public String previousWord;

    @Column(name = "next_word")
    public String nextWord;

    @NotNull
    @Column(name = "word_position")
    private Integer wordPosition;

    @NotNull
    @Column(name = "lemma")
    private String lemma;

    @NotNull
    @Column(name = "pos_tag")
    private String posTag;

    @NotNull
    @Column(name = "sentence")
    private String sentence;

    @NotNull
    @Column(name = "error_priority")
    private Integer errorPriority;

    public enum ErrorType {
        VYJMENOVANA_SLOVA,
        IY_po_C,
        PREDPONY_S_Z,
        PREDLOZKY_S_Z,
        PREJATA_SLOVA_S_Z,
        DIS_DYS,
        SLOVA_ZAKONCENE_MANIE,
        PSANI_N_NN,
        SPREZKY_SPRAHOVANI,
        SLOZENA_ADJEKTIVA,
        VELKA_PISMENA,
        ADJEKTIVA_ICI,
        ADJEKTIVA_NI_NY,
        TYP_ACKOLI_ACKOLIV,
        VOKALIZACE_PREDLOZEK,
        ZAJMENA_VASI_JI_NI,
        PSANI_BE_VE_PE,
        SOUHLASKY_PAROVE,
        DIAKRITIKA,
        I_PO_MEKKYCH_OBOJETNYCH_SOUHLASKACH,
        ZAJMENA_A_SLOVA_OBSAHUJICI_MNE_ME,
        CHYBEJICI_SLOVO,
        NADBYTECNE_SLOVO,
        OSTATNI
    }

    @NotNull
    @Column(name = "error_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @NotNull
    @Column(name = "description")
    private String errorDescription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dictate_id")
    private Dictate dictate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "trial_id")
    private Trial trial;

    public Error() {
    }

    public Error(final Dictate dictate, final Student student) {
        this.dictate = dictate;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMistakeCharPosInWord() {
        return mistakeCharPosInWord;
    }

    public void setMistakeCharPosInWord(Integer mistakeCharPosInWord) {
        this.mistakeCharPosInWord = mistakeCharPosInWord;
    }

    public String getCorrectChars() {
        return correctChars;
    }

    public void setCorrectChars(String correctChars) {
        this.correctChars = correctChars;
    }

    public String getWrittenChars() {
        return writtenChars;
    }

    public void setWrittenChars(String writtenChars) {
        this.writtenChars = writtenChars;
    }

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

    public String getPreviousWord() {
        return previousWord;
    }

    public void setPreviousWord(String previousWord) {
        this.previousWord = previousWord;
    }

    public String getNextWord() {
        return nextWord;
    }

    public void setNextWord(String nextWord) {
        this.nextWord = nextWord;
    }

    public Integer getWordPosition() {
        return wordPosition;
    }

    public void setWordPosition(Integer wordPosition) {
        this.wordPosition = wordPosition;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Integer getErrorPriority() {
        return errorPriority;
    }

    public void setErrorPriority(Integer errorPriority) {
        this.errorPriority = errorPriority;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
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

    public void setDictate(Dictate dictate) {
        this.dictate = dictate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Trial getTrial() {
        return trial;
    }

    public void setTrial(Trial trial) {
        this.trial = trial;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        final Error error = (Error) obj;

        if (!id.equals(error.id)) return false;
        if (!student.equals(error.student)) return false;
        if (!trial.equals(error.trial)) return false;
        return dictate.equals(error.dictate);
    }

    @Override
    public String toString() {
        return "Error{" +
                "id=" + id +
                ", mistakeCharPosInWord=" + mistakeCharPosInWord +
                ", correctChars='" + correctChars + '\'' +
                ", writtenChars='" + writtenChars + '\'' +
                ", correctWord='" + correctWord + '\'' +
                ", writtenWord='" + writtenWord + '\'' +
                ", previousWord='" + previousWord + '\'' +
                ", nextWord='" + nextWord + '\'' +
                ", wordPosition=" + wordPosition +
                ", lemma='" + lemma + '\'' +
                ", posTag='" + posTag + '\'' +
                ", sentence='" + sentence + '\'' +
                ", errorPriority=" + errorPriority +
                ", errorType=" + errorType +
                ", errorDescription='" + errorDescription + '\'' +
                ", dictate=" + dictate +
                ", student=" + student +
                ", trial=" + trial +
                '}';
    }
}

