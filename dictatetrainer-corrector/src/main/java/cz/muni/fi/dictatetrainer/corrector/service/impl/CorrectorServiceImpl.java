package cz.muni.fi.dictatetrainer.corrector.service.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;
import cz.muni.fi.dictatetrainer.corrector.service.CorrectorService;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of CorrectorService
 */
public class CorrectorServiceImpl implements CorrectorService {

    private static final String charWithDiac = "a-zA-Z0-9.,?!áäÁÄčČďĎéÉěĚíÍĺľĹĽňóôöőÓÔÖŐŕŔřŘšŠťŤůúüűÚÜŰýÝžŽ";

    /**
     * Apply diff on transcript and userText and create marked string
     *
     * @param transcript correct transcript of the dictate
     * @param userText   text written by user
     * @return marked string
     */
    @Override
    public String markInput(String transcript, String userText) {

        //creates a new instance of diff_match_patch
        DiffMatchPatch dmp = new DiffMatchPatch();

        //store Diffs into LinkedList
        LinkedList<Diff> diffList = dmp.diffMain(transcript, userText, false);

        //offer more human readable format of diffs
        dmp.diffCleanupSemantic(diffList);

        // create markup text and then perform some cleanup on it
        // TODO if mistake is missing or surplus blankspace, mark it like za()to or za<>to
        String markedInput = cleanupMarkedText(createMarkupFromDiffList(diffList, dmp));

        return markedInput;
    }

    /**
     * Create tokens from marked text, also interpunction is token by itself
     *
     * @param markedText output of method markInput
     * @param delimiter  delimiter used to create tokens (normally blank space)
     * @return array of tokens
     */
    @Override
    public String[] tokenizeAndReturnTokens(String markedText, String delimiter) {
        return markedText.split(delimiter);
    }

    /**
     * Method that takes marked input and create list of sentences
     *
     * @param markedText   text with marked mistakes
     * @return List of sentences
     */
    @Override
    public String[] sentencizedAndReturnSentences(String markedText) {
        return markedText.split("(?=\\.)|(?=!)|(?=\\?)");
    }

    /**
     * Create Mistake object for every mistake and use the information stored in the object to apply corrector rules
     *
     * @param tokens         tokenized marked text in array
     * @param correctorRules rules that will be applied
     * @return list of mistake objects
     */
    @Override
    public List<Mistake> createMistakeObjectsAndApplyCorrectorRules(String[] tokens, String[] sentences,
                                                                    CorrectorRules correctorRules) {

        List<Mistake> mistakeList = new ArrayList<>();
        int sentenceCounter = 0;
        int numberOfSurplusWords = 0;
        int numberOfMissingWords = 0;

        for (int wordNumber = 0; wordNumber < tokens.length; wordNumber++) {

            //initialize variables
            String markedWord = tokens[wordNumber];
            String nextWord;
            String previousWord;

            //to prevent failing at the first token
            if (wordNumber > 0) {
                previousWord = tokens[wordNumber - 1];
            } else {
                previousWord = "";
            }

            // to prevent failing at the last token
            if (wordNumber + 1 < tokens.length) {
                nextWord = tokens[wordNumber + 1];
            } else {
                nextWord = "";
            }


            // suppose that these three are the only end-of-sentence characters and that there is no abbreviations in dictate
            if (wordNumber > 0 && (previousWord.contains(".") || previousWord.contains("!") || previousWord.contains("?"))) {
                sentenceCounter++;
            }

            if (!markedWord.contains("<") && !markedWord.contains("(")) {
                //everything is ok, word is correct

            } else {

                if ((markedWord.indexOf('(') == 0) && (markedWord.indexOf(')') == markedWord.length())) {
                    // missing word
                    Mistake mistake = new Mistake();
                    mistake.setMistakeCharPosInWord(0);
                    mistake.setCorrectChars(getCorrectWordForMistake(markedWord));
                    mistake.setWrittenChars(""); //between < and >
                    mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                    mistake.setWrittenWord("");
                    mistake.setPreviousWord(previousWord);
                    mistake.setNextWord(nextWord);
                    mistake.setWordPosition(-1);
                    mistake.setSentence(sentences[sentenceCounter]);
                    mistake.setLemma("");
                    mistake.setPosTag("");
                    mistake.setMistakeType(MistakeType.CHYBEJICI_SLOVO);
                    mistake.setPriority(5);
                    mistake.setMistakeDescription("Tohle slovo chybí.");

                    mistakeList.add(mistake);
                    numberOfMissingWords++;

                } else if ((markedWord.indexOf('<') == 0) && (markedWord.indexOf('>') == markedWord.length())) {
                    //surplus word
                    Mistake mistake = new Mistake();
                    mistake.setMistakeCharPosInWord(0);
                    mistake.setCorrectChars("");
                    mistake.setWrittenChars(getWrittenWordForMistake(markedWord)); //between < and >
                    mistake.setCorrectWord("");
                    mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                    mistake.setPreviousWord(previousWord);
                    mistake.setNextWord(nextWord);
                    mistake.setWordPosition(0);
                    mistake.setSentence(sentences[sentenceCounter]);
                    mistake.setLemma("");
                    mistake.setPosTag("");
                    mistake.setMistakeType(MistakeType.NADBYTECNE_SLOVO);
                    mistake.setPriority(5);
                    mistake.setMistakeDescription("Tohle slovo je navíc.");

                    mistakeList.add(mistake);
                    numberOfSurplusWords++;

                } else {

                    int parenthPosition = 0;
                    int chevronPosition = 0;
                    String corectWord = getCorrectWordForMistake(markedWord);

                    while (chevronPosition != -1 && parenthPosition != -1) {

                        parenthPosition = markedWord.indexOf('(');
                        int parenthClosingPosition = markedWord.indexOf(')');
                        chevronPosition = markedWord.indexOf('<');
                        int chevronClosingPosition = markedWord.indexOf('>');

                        if (parenthPosition == -1) {
                            if (parenthClosingPosition - parenthPosition == 1) {
                                // missing space between words
                                Mistake mistake = new Mistake();
                                mistake.setMistakeCharPosInWord(0);
                                mistake.setCorrectChars(" ");
                                mistake.setWrittenChars(""); //between < and >
                                mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                                mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                                mistake.setPreviousWord(previousWord);
                                mistake.setNextWord(nextWord);
                                mistake.setWordPosition(wordNumber - numberOfSurplusWords + numberOfMissingWords);
                                mistake.setSentence(sentences[sentenceCounter]);
                                mistake.setLemma(getLemmaForMistake(corectWord));
                                mistake.setPosTag(getTagForMistake(corectWord));

                                mistakeList.add(correctorRules.applyRules(mistake));

                            } else {
                                //surplus characters
                                Mistake mistake = new Mistake();
                                mistake.setMistakeCharPosInWord((chevronPosition * (-1)) - 1);
                                mistake.setCorrectChars("");
                                mistake.setWrittenChars(markedWord.substring(chevronPosition + 1, chevronClosingPosition)); //between < and >
                                mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                                mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                                mistake.setPreviousWord(previousWord);
                                mistake.setNextWord(nextWord);
                                mistake.setWordPosition(wordNumber - numberOfSurplusWords + numberOfMissingWords);
                                mistake.setSentence(sentences[sentenceCounter]);
                                mistake.setLemma(getLemmaForMistake(corectWord));
                                mistake.setPosTag(getTagForMistake(corectWord));

                                mistakeList.add(correctorRules.applyRules(mistake));
                            }

                            markedWord = markedWord.replace(markedWord.substring(chevronPosition, chevronClosingPosition + 1), "");

                        } else if (parenthClosingPosition + 1 == chevronPosition) {
                            //normal mistake
                            Mistake mistake = new Mistake();
                            mistake.setMistakeCharPosInWord(parenthPosition + 1);
                            mistake.setCorrectChars(markedWord.substring(parenthPosition + 1, parenthClosingPosition));
                            mistake.setWrittenChars(markedWord.substring(chevronPosition + 1, chevronClosingPosition)); //between < and >
                            mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                            mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                            mistake.setPreviousWord(previousWord);
                            mistake.setNextWord(nextWord);
                            mistake.setWordPosition(wordNumber - numberOfSurplusWords + numberOfMissingWords);
                            mistake.setSentence(sentences[sentenceCounter]);
                            mistake.setLemma(getLemmaForMistake(corectWord));
                            mistake.setPosTag(getTagForMistake(corectWord));

                            mistakeList.add(correctorRules.applyRules(mistake));

                            markedWord = markedWord.replace(markedWord.substring(parenthClosingPosition, chevronClosingPosition + 1), "").replaceFirst("\\(", "");
                        } else {
                            if (chevronClosingPosition - chevronPosition == 1) {
                                // surplus space between words
                                Mistake mistake = new Mistake();
                                mistake.setMistakeCharPosInWord(0);
                                mistake.setCorrectChars("");
                                mistake.setWrittenChars(" "); //between < and >
                                mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                                mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                                mistake.setPreviousWord(previousWord);
                                mistake.setNextWord(nextWord);
                                mistake.setWordPosition(wordNumber - numberOfSurplusWords + numberOfMissingWords);
                                mistake.setSentence(sentences[sentenceCounter]);
                                mistake.setLemma(getLemmaForMistake(corectWord));
                                mistake.setPosTag(getTagForMistake(corectWord));

                                mistakeList.add(correctorRules.applyRules(mistake));

                            } else {
                                //missing character
                                Mistake mistake = new Mistake();
                                mistake.setMistakeCharPosInWord((parenthPosition * (-1)) - 1);
                                mistake.setCorrectChars(markedWord.substring(parenthPosition + 1, parenthClosingPosition));
                                mistake.setWrittenChars(""); //between < and >
                                mistake.setCorrectWord(getCorrectWordForMistake(markedWord));
                                mistake.setWrittenWord(getWrittenWordForMistake(markedWord));
                                mistake.setPreviousWord(previousWord);
                                mistake.setNextWord(nextWord);
                                mistake.setWordPosition(wordNumber - numberOfSurplusWords + numberOfMissingWords);
                                mistake.setSentence(sentences[sentenceCounter]);
                                mistake.setLemma(getLemmaForMistake(corectWord));
                                mistake.setPosTag(getTagForMistake(corectWord));

                                mistakeList.add(correctorRules.applyRules(mistake));
                            }
                            markedWord = markedWord.replaceFirst("\\(", "").replaceFirst("\\)", "");
                        }
                        parenthPosition = markedWord.indexOf('(');
                        chevronPosition = markedWord.indexOf('<');
                    }
                }
            }
        }
        return mistakeList;
    }

    // --------- private methods -------------

    /**
     * Create personalized markup using diff-match-patch diff library
     * Correct word: dobrý
     * Mistake in the word: dobr(ý)<í>, where (xxx) is correct string and <xxx> is wrong string inside the word
     *
     * @param diffList LinkedList of Diff objects produced by diffMain method of diff-match-patch library
     * @param dmp      diff-match-patch services object
     * @return marked string
     */
    private String createMarkupFromDiffList(LinkedList<Diff> diffList, DiffMatchPatch dmp) {
        String markedText = "";
        for (int i = 0; i < diffList.size(); i++) {
            Diff d = diffList.get(i);

            if (d.operation.toString().equals("DELETE")) {
                if (dmp.matchMain(d.text, " ", 0) == d.text.length()) {
                    //surplus word
                    markedText += "(" + d.text + ")";
                    markedText = markedText.replace(" )", ") ");
                } else {
                    markedText += "(" + d.text + ")";
                }
            } else if (d.operation.toString().equals("INSERT")) {
                markedText += "<" + d.text + ">";
                markedText = markedText.replaceAll(" >", "> ");
            } else {//"EQUAL":
                markedText += d.text;
            }
        }
        return markedText;
    }

    /**
     * Perform cleanup on marked string text to avoid bugs
     *
     * @param markedText text marked by createMarkupFromDiffList method
     * @return cleaned markup
     */
    private String cleanupMarkedText(String markedText) {
        return markedText
                .replaceAll("([" + charWithDiac + "]*\\)<[" + charWithDiac + "]*) ", "\\) \\($1> <") //for [..)<..] ~> [ )(..) <..> <]
                .replaceAll("< ", " <") //for [< ..] ~> [ <..]
                .replaceAll("\\(\\) ", "") //for removal of [() ]
                .replaceAll("<>", " <~>") //adding information about missing space char
                .replaceAll("\\( ", " \\(") // for [( ] ~> [ (]
                .replaceAll(" \\)", "\\) "); // for [ )] ~> [) ]
    }

    //------------Mistake attribute definition----------------------------------

    /**
     * Get the correct version of marked word
     * For example, if marked word is dobr(ý)<í>, then the correct version is "dobrý"
     *
     * @param markedWord marked word
     * @return correct word
     */
    public String getCorrectWordForMistake(String markedWord) {
        markedWord = markedWord.replaceAll("\\(\\)", " "); //because of missing space
        return markedWord.replaceAll("<[" + charWithDiac + "]*>", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }

    /**
     * Get the written version of marked word
     * For example, if marked word is dobr(ý)<í>, then the correct version is "dobrí"
     *
     * @param markedWord marked word
     * @return correct word
     */
    public String getWrittenWordForMistake(String markedWord) {
        markedWord = markedWord.replaceAll("<>", " ");
        return markedWord.replaceAll("\\([" + charWithDiac + "]*\\)", "").replaceAll("<", "").replaceAll(">", "");
    }

    /**
     * Get lemma from the majka REST API using getLemmaAndTagForMistake() method
     *
     * @param correctWord correct version of word where the mistake is present
     * @return lemma for the given correct word
     */
    public String getLemmaForMistake(String correctWord) {
        return getLemmaAndTagForMistake(correctWord)[0];
    }

    /**
     * Get tag from the majka REST API using getLemmaAndTagForMistake() method
     *
     * @param correctWord correct version of word where the mistake is present
     * @return tag for the given correct word
     */
    public String getTagForMistake(String correctWord) {
        return getLemmaAndTagForMistake(correctWord)[1];
    }

    /**
     * Get lemma and tag of the correct word using majka
     * Uses majka public REST API
     * (currently http://athena.fi.muni.cz:8080/lt)
     *
     * @param correctWord
     * @return string array consists of two parts
     * [lemma of the given word] on the 0th position of the array
     * [Tag] on the 1st position of an array
     * <p>
     * If more than one possible pair lemma:tag is returned, the first one is taken
     * TODO zobrazit prvy tag s najviac sa vyskytujucim slovnym druhom
     * If nothing is found, NOT_FOUND is returned
     * More about tag structure available at
     * <a>https://nlp.fi.muni.cz/projekty/ajka/tags.pdf</a>
     */
    public String[] getLemmaAndTagForMistake(String correctWord) {
        // remove all non-letter characters and then make it lowercase
        correctWord = correctWord.replaceAll("[^\\p{L}\\p{Nd}]+", "").toLowerCase();
        final String MORPH_ANALYZER_REST_URL = "http://athena.fi.muni.cz:8080/lt";
        final String URL_WITH_CORRECT_WORD = MORPH_ANALYZER_REST_URL + "/" + correctWord;
        final String NOT_FOUND = "NOT FOUND";
        final String NOT_FOUND_CONN = "NOT FOUND CONN";

        Client client = ClientBuilder.newClient();

        try {
            String response = client.target(URL_WITH_CORRECT_WORD)
                    .request("text/html")
                    .accept("text/html")
                    .acceptEncoding("gzip")
                    .get(String.class);


            if (!response.equals("")) {         // if nothing is found return NOT_FOUND
                return response.split("\\s+")[0].split(":");
            } else {
                String[] notFoundString = new String[2];
                notFoundString[0] = NOT_FOUND;
                notFoundString[1] = NOT_FOUND;
                return notFoundString;
            }

        } catch (ProcessingException pe) {
            String[] notFoundString = new String[2];
            notFoundString[0] = NOT_FOUND_CONN;
            notFoundString[1] = NOT_FOUND_CONN;
            return notFoundString;
        }
    }
}
