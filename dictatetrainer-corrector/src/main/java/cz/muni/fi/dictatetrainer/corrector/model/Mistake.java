package cz.muni.fi.dictatetrainer.corrector.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mistake object that is returned as JSON response when the text is corrected
 * <p>
 * Holds this fields:
 * -------------------
 * id: id of the object
 * mistakeCharPosInWord: position of first mistaken character (beginning with 1) TODO 0=surplus char, -1=missing char
 * correctChars: correct characters from transcript TODO empty if surplus char
 * writtenChars: incorrect characters written by student TODO empty if missing char
 * correctWord: word from the dictate transcript
 * writtenWord: word written by the student
 * wordPosition: position of the word in the dictate counted as token number from begining of the transcript
 *  (beginning with 1) TODO how to deal with surplus and missing words? There are more tokens or less tokens then
 *  0 = surplus word, -1 = missing word
 * lemma: lemma of the error word
 * posTag: part of speech tag
 * sentence: whole sentence in which mistake occurs, for future use
 * priority: priority of mistake - 1 is lowest 100 is highest priority
 * mistakeType: enum, can be SURPLUS_WORD, MISSING_WORD or MISTAKE
 * mistakeDescription: Description of mistake in the word, for example clarification why is the other variant correct
 */
public class Mistake implements Serializable {
    private static final long serialVersionUID = 1802650487392607943L;
    private static AtomicLong counter = new AtomicLong(-1);

    public Mistake() {
        this.id = counter.incrementAndGet();
    }

    public Long id;

    public Integer mistakeCharPosInWord;

    public String correctChars;

    public String writtenChars;

    public String correctWord;

    public String writtenWord;

    public Integer wordPosition;

    public String lemma;

    public String posTag;

    public String sentence;

    public int priority;

    public MistakeType mistakeType;

    public String mistakeDescription;

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

    public void setCorrectChars(String correctChar) {
        this.correctChars = correctChar;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public MistakeType getMistakeType() {
        return mistakeType;
    }

    public void setMistakeType(MistakeType mistakeType) {
        this.mistakeType = mistakeType;
    }

    public String getMistakeDescription() {
        return mistakeDescription;
    }

    public void setMistakeDescription(String mistakeDescription) {
        this.mistakeDescription = mistakeDescription;
    }

    /**
     * Constructor for the first step - adding data about mistake
     * to use them in CorrectorRules
     * @param definition of the parameters on the top of this class
     */
    public Mistake(Integer mistakeCharPosInWord, String correctChars, String writtenChars, String correctWord,
                   String writtenWord, Integer wordPosition, String lemma, String posTag, String sentence) {
        this.mistakeCharPosInWord = mistakeCharPosInWord;
        this.correctChars = correctChars;
        this.writtenChars = writtenChars;
        this.correctWord = correctWord;
        this.writtenWord = writtenWord;
        this.wordPosition = wordPosition;
        this.lemma = lemma;
        this.posTag = posTag;
        this.sentence = sentence;
    }

    @Override
    public String toString() {
        return "Mistake{" +
                "id=" + id +
                ", mistakeCharPosInWord=" + mistakeCharPosInWord +
                ", correctChars='" + correctChars + '\'' +
                ", writtenChars='" + writtenChars + '\'' +
                ", correctWord='" + correctWord + '\'' +
                ", writtenWord='" + writtenWord + '\'' +
                ", wordPosition=" + wordPosition +
                ", lemma='" + lemma + '\'' +
                ", posTag='" + posTag + '\'' +
                ", sentence='" + sentence + '\'' +
                ", priority=" + priority +
                ", mistakeType=" + mistakeType +
                ", mistakeDescription='" + mistakeDescription + '\'' +
                '}';
    }
}
