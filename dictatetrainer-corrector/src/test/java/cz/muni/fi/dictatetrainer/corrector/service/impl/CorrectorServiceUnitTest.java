package cz.muni.fi.dictatetrainer.corrector.service.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;
import cz.muni.fi.dictatetrainer.corrector.rules.impl.CorrectorRulesNoContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrectorServiceUnitTest {

    CorrectorServiceImpl correctorService = new CorrectorServiceImpl();

    @Test
    public void mistakeCharPosInWordSimpleCase() {
        // TODO add hamcrest to pom.xml?
        String markedWord = "Okamžit(ě)<e>";
        List<Integer> positions = correctorService.getMistakeCharPosInWordForMistake(markedWord);

        assertThat(positions.size(), is(equalTo(1)));
        assertThat(positions.get(0), is(equalTo(8)));
    }

    @Test
    public void mistakeCharPosInWordSurplusChar() {
        String markedWord = "Ran<n>ý";
        List<Integer> positions = correctorService.getMistakeCharPosInWordForMistake(markedWord);

        assertThat(positions.size(), is(equalTo(1)));
        assertThat(positions.get(0), is(equalTo(0)));
    }

    @Test
    public void mistakeCharPosInWordMissingChar() {
        String markedWord = "Strun(n)ý";
        List<Integer> positions = correctorService.getMistakeCharPosInWordForMistake(markedWord);

        assertThat(positions.size(), is(equalTo(1)));
        assertThat(positions.get(0), is(equalTo(-1)));
    }

    @Test
    public void mistakeCharPosInWordMoreCharsMistake() {
        String markedWord = "(O)<o>kamžit(ě)<e>";
        List<Integer> positions = correctorService.getMistakeCharPosInWordForMistake(markedWord);

        assertThat(positions.size(), is(equalTo(2)));
        assertThat(positions.get(0), is(equalTo(1)));
        assertThat(positions.get(1), is(equalTo(8)));
    }

    @Test
    public void correctCharsOneMistake() {
        String markedWord = "Okamžit(ě)<e>";
        List<String> correctChars = correctorService.getCorrectCharsForMistake(markedWord);

        assertThat(correctChars.size(), is(equalTo(1)));
        assertThat(correctChars.get(0), is(equalTo("ě")));
    }

    @Test
    public void correctCharsMoreMistakes() {
        String markedWord = "(O)<o>kamžit(ě)<e>";
        List<String> correctChars = correctorService.getCorrectCharsForMistake(markedWord);

        assertThat(correctChars.size(), is(equalTo(2)));
        assertThat(correctChars.get(0), is(equalTo("O")));
        assertThat(correctChars.get(1), is(equalTo("ě")));
    }

    @Test
    public void correctCharsMissingChars() {
        String markedWord = "Strun(n)ý";
        List<String> correctChars = correctorService.getCorrectCharsForMistake(markedWord);

        assertThat(correctChars.size(), is(equalTo(1)));
        assertThat(correctChars.get(0), is(equalTo("n")));
    }

    @Test
    public void correctCharsSurplusChars() {
        //TODO make Error correctChars field NotNull=false
        String markedWord = "Ran<n>ý";
        List<String> correctChars = correctorService.getCorrectCharsForMistake(markedWord);

        assertThat(correctChars.size(), is(equalTo(0)));
    }

    @Test
    public void writtenCharsOneMistake() {
        String markedWord = "Okamžit(ě)<e>";
        List<String> writtenChars = correctorService.getWrittenCharsForMistake(markedWord);

        assertThat(writtenChars.size(), is(equalTo(1)));
        assertThat(writtenChars.get(0), is(equalTo("e")));
    }

    @Test
    public void writtenCharsMoreMistakes() {
        String markedWord = "(O)<o>kamžit(ě)<e>";
        List<String> writtenChars = correctorService.getWrittenCharsForMistake(markedWord);

        assertThat(writtenChars.size(), is(equalTo(2)));
        assertThat(writtenChars.get(0), is(equalTo("o")));
        assertThat(writtenChars.get(1), is(equalTo("e")));
    }

    @Test
    public void writtenCharsSurplusChars() {
        String markedWord = "Ran<n>ý";
        List<String> writtenChars = correctorService.getWrittenCharsForMistake(markedWord);

        assertThat(writtenChars.size(), is(equalTo(1)));
        assertThat(writtenChars.get(0), is(equalTo("n")));
    }

    @Test
    public void writtenCharsMissingChars() {
        //TODO make Error writtenChars field NotNull=false
        String markedWord = "Strun(n)ý";
        List<String> writtenChars = correctorService.getWrittenCharsForMistake(markedWord);

        assertThat(writtenChars.size(), is(equalTo(0)));
    }

    @Test
    public void correctWordOneMistakeMissingChars() {
        String markedWord = "Strun(n)ý";
        String correctWord = correctorService.getCorrectWordForMistake(markedWord);

        assertThat(correctWord, is(equalTo("Strunný")));

    }

    @Test
    public void correctWordMoreMistakesDifferentTypes() {
        String markedWord = "(O)kamžit(ě)<e>";
        String correctWord = correctorService.getCorrectWordForMistake(markedWord);

        assertThat(correctWord, is(equalTo("Okamžitě")));
    }

    @Test
    public void writtenWordOneMistakeSurplusChars() {
        String markedWord = "Ran<n>ý";
        String writtenWord = correctorService.getWrittenWordForMistake(markedWord);

        assertThat(writtenWord, is(equalTo("Ranný")));
    }

    @Test
    public void writtenWordMoreMistakesDifferentTypes() {
        String markedWord = "(O)kamžit(ě)<e>";
        String correctWord = correctorService.getWrittenWordForMistake(markedWord);

        assertThat(correctWord, is(equalTo("kamžite")));
    }

    @Test
    public void numberOfMistakesSimpleCase() {
        String[] tokens = new String[6];
        List<String> markedSentences = new ArrayList<>();

        tokens[0] = "Okamžitě";
        tokens[1] = "m(n)ě";
        tokens[2] = "to";
        tokens[3] = "dej";
        tokens[4] = "na";
        tokens[5] = "židli!";

        markedSentences.add("Okamžitě m(n)ě to dej na židli!");

        CorrectorRules cs = new CorrectorRulesNoContext();

        List<Mistake> mistakes =
                correctorService.createMistakeObjectsAndApplyCorrectorRules(tokens, markedSentences, cs);

        assertThat(mistakes.get(0).getWordPosition(), is(equalTo(2)));
    }

    @Test
    public void wordPositionSimpleCase() {
        String[] tokens = new String[6];
        List<String> markedSentences = new ArrayList<>();

        tokens[0] = "Okamžitě";
        tokens[1] = "m(n)ě";
        tokens[2] = "to";
        tokens[3] = "dej";
        tokens[4] = "na";
        tokens[5] = "židli!";

        markedSentences.add("Okamžitě m(n)ě to dej na židli!");

        CorrectorRules cs = new CorrectorRulesNoContext();

        List<Mistake> mistakes =
                correctorService.createMistakeObjectsAndApplyCorrectorRules(tokens, markedSentences, cs);

        assertThat(mistakes.get(0).getWordPosition(), is(equalTo(2)));
    }

    @Test
    public void wordPositionSurplusWord() {
        String[] tokens = new String[7];
        List<String> markedSentences = new ArrayList<>();

        tokens[0] = "Okamžitě";
        tokens[1] = "mně";
        tokens[2] = "to";
        tokens[3] = "<už>";
        tokens[4] = "dej";
        tokens[5] = "na";
        tokens[6] = "židl(i)<y>!";

        markedSentences.add("Okamžitě mně to <už> dej na židl(i)<y>!");

        CorrectorRules cs = new CorrectorRulesNoContext();

        List<Mistake> mistakes =
                correctorService.createMistakeObjectsAndApplyCorrectorRules(tokens, markedSentences, cs);

        assertThat(mistakes.size(), is(equalTo(2)));
        assertThat(mistakes.get(0).getWordPosition(), is(equalTo(0)));
        assertThat(mistakes.get(1).getWordPosition(), is(equalTo(6)));
    }

    @Test
    public void wordPositionMissingWord() {
        String[] tokens = new String[6];
        List<String> markedSentences = new ArrayList<>();

        tokens[0] = "Okamžitě";
        tokens[1] = "(mně)";
        tokens[2] = "to";
        tokens[3] = "dej";
        tokens[4] = "na";
        tokens[5] = "židl(i)<y>!";

        markedSentences.add("Okamžitě (mně) to dej na židl(i)<y>!");

        CorrectorRules cs = new CorrectorRulesNoContext();

        List<Mistake> mistakes =
                correctorService.createMistakeObjectsAndApplyCorrectorRules(tokens, markedSentences, cs);

        assertThat(mistakes.size(), is(equalTo(2)));
        assertThat(mistakes.get(0).getWordPosition(), is(equalTo(-1)));
        assertThat(mistakes.get(1).getWordPosition(), is(equalTo(6)));
    }

    @Test
    public void sentencesSplitting() {
        String markedText = "Jsem doma. Já va(ř)<r>ím! Co?";
        String LANGUAGE = "cs";

        List<String> sentences = correctorService.sentencizedAndReturnSentences(markedText, LANGUAGE);

        assertThat(sentences.size(), is(equalTo(3)));
        assertThat(sentences.get(0), is(equalTo("Jsem doma. ")));
        assertThat(sentences.get(1), is(equalTo("Já va(ř)<r>ím! ")));
        assertThat(sentences.get(2), is(equalTo("Co?")));

    }

    @Test
    public void sentenceNumberOfMistake() {
        String[] tokens = new String[7];
        List<String> markedSentences = new ArrayList<>();

        tokens[0] = "Okamžitě";
        tokens[1] = "mně";
        tokens[2] = "to";
        tokens[3] = "dej";
        tokens[4] = "na";
        tokens[5] = "židl(i)<y>!";
        tokens[6] = "N(e)<e>.";

        markedSentences.add("Okamžitě mně to dej na židl(i)<y>!");
        markedSentences.add("N(e)<e>.");

        CorrectorRules cs = new CorrectorRulesNoContext();

        List<Mistake> mistakes =
                correctorService.createMistakeObjectsAndApplyCorrectorRules(tokens, markedSentences, cs);

        assertThat(mistakes.size(), is(equalTo(2)));
        assertThat(mistakes.get(0).getSentence(), is(equalTo("Okamžitě mně to dej na židl(i)<y>!")));
        assertThat(mistakes.get(1).getSentence(), is(equalTo("N(e)<e>.")));
    }

    @Test
    public void lemmaAndTagForMistakeMorePossibilities() {
        String correctWord = "dobrou"; //ADJ
        String[] lemmaTagTuple = correctorService.getLemmaAndTagForMistake(correctWord);

        assertThat(lemmaTagTuple.length, is(equalTo(2)));
        assertThat(lemmaTagTuple[0], is(equalTo("dobrý")));
        assertThat(lemmaTagTuple[1], is(equalTo("k2eAgFnPc1d1"))); //it should return the postag which is most probable
    }

    @Test
    public void lemmaAndTagForMistakeWordNotFound() {
        String correctWord = "jibberJabber"; //nonsensical word
        String[] lemmaTagTuple = correctorService.getLemmaAndTagForMistake(correctWord);

        assertThat(lemmaTagTuple.length, is(equalTo(2)));
        assertThat(lemmaTagTuple[0], is(equalTo("NOT FOUND")));
        assertThat(lemmaTagTuple[1], is(equalTo("NOT FOUND")));
    }

    @Test
    public void lemmaAndTagForMistakeConnectionError() {
        String correctWord = "jibberJabber"; //nonsensical word
        String[] lemmaTagTuple = correctorService.getLemmaAndTagForMistake(correctWord);

        assertThat(lemmaTagTuple.length, is(equalTo(2)));
        assertThat(lemmaTagTuple[0], is(equalTo("NOT FOUND CONN")));
        assertThat(lemmaTagTuple[1], is(equalTo("NOT FOUND CONN")));

    }

    @Test
    public void lemmaAndTagForMistakeOnePossibility() {
        String correctWord = "květinou";
        String[] lemmaTagTuple = correctorService.getLemmaAndTagForMistake(correctWord);

        assertThat(lemmaTagTuple.length, is(equalTo(2)));
        assertThat(lemmaTagTuple[0], is(equalTo("květina")));
        assertThat(lemmaTagTuple[1], is(equalTo("k1gFnSc7")));
    }
}
