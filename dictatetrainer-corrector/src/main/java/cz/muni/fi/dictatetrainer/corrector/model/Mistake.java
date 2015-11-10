package cz.muni.fi.dictatetrainer.corrector.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mistake object that is returned as JSON response when the text is corrected
 * <p>
 * Holds this fields:
 * -------------------
 * id: id of the object
 * mistakeCharPosInWord: position of mistake in characters (beginning with 1)
 * correctChar: correct character from transcript
 * writtenChar: incorrect character written by student
 * correctWord: word from the dictate transcript
 * writtenWord: word written by the student
 * wordPosition: position of the word in the dictate counted as token number from begining of the transcript (beginning with 1)
 * lemma: lemma of the error word
 * sentence: whole sentence in which mistake occurs, for future use
 * priority: priority of mistake - 1 is lowest 100 is highest priority
 * mistakeType: enum, can be SURPLUS_WORD, MISSING_WORD or MISTAKE
 * mistakeDescription: Description of mistake in the word, for example clarification why is the other variant correct
 */
public class Mistake implements Serializable {
    private static final long serialVersionUID = 1802650487392607943L;
    private static AtomicLong counter = new AtomicLong(-1);

    public enum MistakeType {
        SURPLUS_WORD, MISSING_WORD, MISTAKE;
    }

    public Mistake() {
        this.id = counter.incrementAndGet();
    }

    public Mistake(int wordPosition, String correctWord, String writtenWord, String mistakeDescription, MistakeType mistakeType) {
        this.id = counter.incrementAndGet();
        this.wordPosition = wordPosition;
        this.correctWord = correctWord;
        this.writtenWord = writtenWord;
        this.mistakeDescription = mistakeDescription;
        this.mistakeType = mistakeType;
    }

    public Mistake(int wordPosition, String correctWord, String writtenWord, int priority, MistakeType mistakeType, String mistakeDescription) {
        this.id = counter.incrementAndGet();
        this.wordPosition = wordPosition;
        this.correctWord = correctWord;
        this.writtenWord = writtenWord;
        this.priority = priority;
        this.mistakeType = mistakeType;
        this.mistakeDescription = mistakeDescription;
    }

    public Long id;

    public Integer mistakeCharPosInWord;

    public String correctChar;

    public String writtenChar;

    public String correctWord;

    public String writtenWord;

    public Integer wordPosition;

    public String lemma;

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

    public String getCorrectChar() {
        return correctChar;
    }

    public void setCorrectChar(String correctChar) {
        this.correctChar = correctChar;
    }

    public String getWrittenChar() {
        return writtenChar;
    }

    public void setWrittenChar(String writtenChar) {
        this.writtenChar = writtenChar;
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

    @Override
    public String toString() {
        return "Mistake{" +
                "id=" + id +
                ", wordPosition=" + wordPosition +
                ", correctWord='" + correctWord + '\'' +
                ", writtenWord='" + writtenWord + '\'' +
                ", priority=" + priority +
                ", mistakeType=" + mistakeType +
                ", mistakeDescription='" + mistakeDescription + '\'' +
                '}';
    }
}
