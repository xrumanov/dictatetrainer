package cz.muni.fi.dictatetrainer.dictate.model;

import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Entity object for Dictate
 *
 * Holds this fields:
 * -------------------
 * id: id of the object
 * name: name of the dictate
 * category: category in which the dictate belongs
 * uploader: person who uploads the dictate
 * description: short description about the dictate
 * filename: filename of dictate on the server
 * transcript: correct text transcript of the dictate
 * defaultRepetitionForDictate: default number of times dictate will be repeated
 * defaultRepetitionForSentences: default number of times each sentence will be repeated
 * defaultPauseBetweenSentences: default pause length between sentences
 * sentenceEndings: list of endtimes for each sentence devided by semicolon
 *
 */
@Entity
@Table(name = "dt_dictate")
public class Dictate implements Serializable {
    private static final long serialVersionUID = 2941495501095209533L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10, max = 150)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @NotNull
    @Size(min = 10)
    private String description;

    @NotNull
    private String filename;

    @NotNull
    private String transcript;

    @NotNull
    @Column(name = "rep_dictate")
    private Integer defaultRepetitionForDictate;

    @NotNull
    @Column(name = "rep_sentences")
    private Integer defaultRepetitionForSentences;

    @NotNull
    @Column(name = "pause_sentences")
    private Integer defaultPauseBetweenSentences;

    @Column(name = "sentence_endings")
    @NotNull
    private String sentenceEndings;

    public Dictate() {
    }

    public Dictate(final Long id) {
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

    public void setName(final String title) {
        this.name = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(final User uploader) {
        this.uploader = uploader;
    }

    public Integer getDefaultRepetitionForDictate() {
        return defaultRepetitionForDictate;
    }

    public void setDefaultRepetitionForDictate(Integer defaultRepetitionForDictate) {
        this.defaultRepetitionForDictate = defaultRepetitionForDictate;
    }

    public Integer getDefaultRepetitionForSentences() {
        return defaultRepetitionForSentences;
    }

    public void setDefaultRepetitionForSentences(Integer defaultRepetitionForSentences) {
        this.defaultRepetitionForSentences = defaultRepetitionForSentences;
    }

    public Integer getDefaultPauseBetweenSentences() {
        return defaultPauseBetweenSentences;
    }

    public void setDefaultPauseBetweenSentences(Integer defaultPauseBetweenSentences) {
        this.defaultPauseBetweenSentences = defaultPauseBetweenSentences;
    }

    public String getSentenceEndings() {
        return sentenceEndings;
    }

    public void setSentenceEndings(String sentenceEndings) {
        this.sentenceEndings = sentenceEndings;
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
        final Dictate other = (Dictate) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dictate [id=" + id + ", name=" + name + "]";
    }

}