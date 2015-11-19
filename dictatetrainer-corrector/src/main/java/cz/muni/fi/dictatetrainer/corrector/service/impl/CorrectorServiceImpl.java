package cz.muni.fi.dictatetrainer.corrector.service.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Method that takes marked input and create list of sentences for future use
     *
     * @param markedText   text with marked mistakes
     * @param languageCode language code according to IANA Language Subtag Registry (see language)
     * @return List of sentences
     */
    @Override
    public List<String> sentencizedAndReturnSentences(String markedText, String languageCode) {
        List<String> sentences = new ArrayList<String>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale(languageCode));
        iterator.setText(markedText);
        int start = iterator.first();

        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            sentences.add(markedText.substring(start, end));
        }
        return sentences;
    }

    /**
     * Create Mistake object for every mistake and use the information stored in the object to apply corrector rules
     *
     * @param tokens         tokenized marked text in array
     * @param correctorRules rules that will be applied
     * @return list of mistake objects
     */
    @Override
    public List<Mistake> createMistakeObjectsAndApplyCorrectorRules(String[] tokens, List<String> sentences,
                                                                    CorrectorRules correctorRules) {

        List<Mistake> mistakeList = new ArrayList<>();
        int sentenceCounter = 0;

        for (int tokenNumber = 0; tokenNumber < tokens.length; tokenNumber++) {

            //initialize variables
            String markedWord = tokens[tokenNumber];
            String nextWord;
            String previousWord;

            //to prevent failing at the first token
            if (tokenNumber > 0) {
                previousWord = tokens[tokenNumber - 1];
            } else {
                previousWord = "";
            }

            // to prevent failing at the last token
            if (tokenNumber+1 < tokens.length) {
                nextWord = tokens[tokenNumber + 1];
            } else {
                nextWord = "";
            }


            // suppose that these three are the only end-of-sentence characters and that there is no abbreviations in dictate
            if (tokenNumber > 0 && (previousWord.contains(".") || previousWord.contains("!") || previousWord.contains("?"))) {
                sentenceCounter++;
            }

            if (!markedWord.contains("<") && !markedWord.contains("(")) { //everything is ok, word is correct
            } else {
                if (markedWord.contains("(") && !markedWord.contains("<")) { //surplus word
                    while (!markedWord.contains(")")) {
                        //Add mistake creation here for surplus word
                        tokenNumber++;
                    }
                    //Add mistake creation here...last surplus word

                } else if (!markedWord.contains("(") && markedWord.contains("<")) { //missing word
                    //TODO can be also missing character!!! FIX
                    while (!markedWord.contains(">")) {
                        //Add mistake creation here for missing word
                        tokenNumber++;
                    }
                    //Add mistake creation here...last missing word

                } else if (markedWord.contains("<~>")) { //missing space character

                    //Add mistake creation here for missing space character

                } else if ((markedWord.contains("(") && markedWord.contains(")") //one word or part of the word is incorrect
                        && (markedWord.contains("<") && markedWord.contains(">")))
                        | (((markedWord.contains("<") && markedWord.contains(">"))
                        && (((markedWord.indexOf(">") - markedWord.indexOf("<")) < (markedWord.length() - 1)))))
                        | (((markedWord.contains("(") && markedWord.contains(")"))
                        && (((markedWord.indexOf(")") - markedWord.indexOf("(")) < (markedWord.length() - 1)))))
                        | (markedWord.contains(")<"))) {

                    for (int mistakeInWordIterator = 0; mistakeInWordIterator < getMistakeCharPosInWordForMistake(markedWord).size(); mistakeInWordIterator++) {
                        Mistake mistake = new Mistake(
                                getMistakeCharPosInWordForMistake(markedWord).get(mistakeInWordIterator),
                                getCorrectCharsForMistake(markedWord).get(mistakeInWordIterator),
                                getWrittenCharsForMistake(markedWord).get(mistakeInWordIterator),
                                getCorrectWordForMistake(markedWord),
                                getWrittenWordForMistake(markedWord),
                                getCorrectWordForMistake(previousWord),
                                getCorrectWordForMistake(nextWord),
                                tokenNumber,
                                getLemmaForMistake(getCorrectWordForMistake(markedWord)),
                                getTagForMistake(getCorrectWordForMistake(markedWord)),
                                sentences.get(sentenceCounter)
                        );

                        // pass the mistake to CorrectorRules to get priority, mistakeType and mistakeDescription
                        mistake = correctorRules.applyRules(mistake);

                        // add mistakes to the List
                        mistakeList.add(mistake);
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
     * Get position of mistaken characters
     * for example ab(cd)e(fg)h the given positions are c=2, f=5
     * TODO 0 if character is surplus, -1 if character is missing
     *
     * @param markedWord
     * @return
     */
    public List<Integer> getMistakeCharPosInWordForMistake(String markedWord) {
        // delete all characters between <> to ease the counting
        markedWord = markedWord.replaceAll("\\s*\\<[^\\>]*\\>\\s*", " ");

        // auxillary integer to help to cope with ()
        // first index is index of brace, second is behind two braces (-2)
        int n = 0;

        List<Integer> charPos = new ArrayList<>();
        for (int i = -1; (i = markedWord.indexOf("(", i + 1)) != -1; ) {
            charPos.add(i - n + 1);
            n = n + 3;
        }
        return charPos;
    }

    /**
     * Get the correct characters for mistake
     * for example if correct word is <dobrý> and written word is <dobrí>
     * the correct char for mistake will be "ý"
     *
     * @param markedWord marked word
     * @return List of correct chars for one token
     */
    public List<String> getCorrectCharsForMistake(String markedWord) {
        List<String> correctChars = new ArrayList<>();

        //get all Strings between '(' and ')'
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(markedWord);
        while (m.find()) {
            correctChars.add(m.group(1));
        }
        return correctChars;
    }

    /**
     * Get the characters for mistake written by user
     * for example if correct word is <dobrý> and written word is <dobrí>
     * the correct char for mistake will be "í"
     *
     * @param markedWord marked word
     * @return List of written chars for one token
     */
    public List<String> getWrittenCharsForMistake(String markedWord) {
        List<String> writtenChars = new ArrayList<>();

        //get all Strings between '<' and '>'
        Matcher m = Pattern.compile("\\<([^>]+)\\>").matcher(markedWord);
        while (m.find()) {
            writtenChars.add(m.group(1));
        }
        return writtenChars;
    }

    /**
     * Get the correct version of marked word
     * For example, if marked word is dobr(ý)<í>, then the correct version is "dobrý"
     *
     * @param markedWord marked word
     * @return correct word
     */
    public String getCorrectWordForMistake(String markedWord) {
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
