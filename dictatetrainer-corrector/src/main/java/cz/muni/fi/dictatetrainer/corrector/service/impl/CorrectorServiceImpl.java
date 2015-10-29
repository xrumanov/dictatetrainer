package cz.muni.fi.dictatetrainer.corrector.service.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.Mistake.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;
import cz.muni.fi.dictatetrainer.corrector.service.CorrectorService;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CorrectorServiceImpl implements CorrectorService {

    private static final String charWithDiac = "a-zA-Z0-9.,?!áäÁÄčČďĎéÉěĚíÍĺľĹĽňóôöőÓÔÖŐŕŔřŘšŠťŤůúüűÚÜŰýÝžŽ";

    @Override
    public String markInput(String transcript, String userText) {
        String markedInput = "";

        //creates a new instance of diff_match_patch
        DiffMatchPatch dmp = new DiffMatchPatch();

        //store Diffs into LinkedList
        LinkedList<Diff> diff = dmp.diffMain(transcript, userText, false);

        dmp.diffCleanupSemantic(diff);
        for (int i = 0; i < diff.size(); i++) {
            Diff d = diff.get(i);

            if (d.operation.toString().equals("DELETE")) {
                if (dmp.matchMain(d.text, " ", 0) == d.text.length()) {
                    //surplus word
                    markedInput += "(" + d.text + ")";
                    markedInput = markedInput.replace(" )", ") ");
                } else {
                    markedInput += "(" + d.text + ")";
                }
            } else if (d.operation.toString().equals("INSERT")) {
                markedInput += "<" + d.text + ">";
                markedInput = markedInput.replaceAll(" >", "> ");
            } else {//"EQUAL":
                markedInput += d.text;
            }
        }

        //regexps that fix some bugs
        markedInput = markedInput
                //for [..)<..] ~> [ )(..) <..> <]
                .replaceAll("([" + charWithDiac + "]*\\)<[" + charWithDiac + "]*) ", "\\) \\($1> <")
                .replaceAll("< ", " <") //for [< ..] ~> [ <..]
                .replaceAll("\\(\\) ", "") //for removal of [() ]
                .replaceAll("<>", " <~>") //adding information about missing space char
                .replaceAll("\\( ", " \\(") // for [( ] ~> [ (]
                .replaceAll(" \\)", "\\) "); // for [ )] ~> [) ]


        return markedInput;
    }

    @Override
    public String[] tokenizeAndReturnTokens(String markedText, String delimiter) {
        return markedText.split(delimiter);
    }

    @Override
    public List<Mistake> correctUsingCorrectorRules(String[] tokens, CorrectorRules rules) {
        //creates list for structures
        List<Mistake> mistakesList = new ArrayList<Mistake>();

        //creates structure for every word
        //structure includes correctWord from transcript, word that was inputted by user, marked word and type of mistake
        for (int i = 0; i < tokens.length; i++) {
            if ((tokens[i].contains("(") && tokens[i].contains(")") && (tokens[i].contains("<") && tokens[i].contains(">")))
                    | (((tokens[i].contains("<") && tokens[i].contains(">"))
                    && (((tokens[i].indexOf(">") - tokens[i].indexOf("<")) < (tokens[i].length() - 1)))))
                    | (((tokens[i].contains("(") && tokens[i].contains(")"))
                    && (((tokens[i].indexOf(")") - tokens[i].indexOf("(")) < (tokens[i].length() - 1)))))
                    | (tokens[i].contains(")<"))) {

                //one word or part of the word is incorrect
                //place corrector here
                //special regex for non-ascii chars
                this.addToStructure(i, tokens[i].replaceAll("<[" + charWithDiac + "]*>", "").replaceAll("\\(", "").replaceAll("\\)", ""),
                        tokens[i].replaceAll("\\([" + charWithDiac + "]*\\)", "").replaceAll("<", "").replaceAll(">", ""),
                        tokens[i], MistakeType.MISTAKE, mistakesList);

            } else if (tokens[i].contains("<~>")) {
                //missing space character
                //place corrector here
                this.addToStructure(i, "_", " ", "missing space char", MistakeType.MISTAKE, mistakesList);

            } else if (tokens[i].contains("(") && !tokens[i].contains("<")) {
                //word is surplus
                while (!tokens[i].contains(")")) {
                    this.addToStructure(i, tokens[i].replaceAll("\\(", ""),
                            "", "Surplus word", MistakeType.SURPLUS_WORD, mistakesList);
                    i++;
                }
                this.addToStructure(i, tokens[i].replaceAll("\\(", "").replaceAll("\\)", ""),
                        "", "Surplus word", MistakeType.SURPLUS_WORD, mistakesList);

            } else if (!tokens[i].contains("(") && tokens[i].contains("<")) {
                //missing word
                while (!tokens[i].contains(">")) {
                    this.addToStructure(i, "", tokens[i].replaceAll("<", ""),
                            "Missing word", MistakeType.MISSING_WORD, mistakesList);
                    i++;
                }
                this.addToStructure(i, "", tokens[i].replaceAll("<", "").replaceAll(">", ""),
                        "Missing word", MistakeType.MISSING_WORD, mistakesList);
            } else {
                //everything is correct, do nothing
            }
        }

        return mistakesList;
    }


    /**
     * adding token to DictateWord structure
     *
     * @param correctWord        word from transcript
     * @param writtenWord        word, that was imputted by user
     * @param mistakeDescription list of errors
     * @param mistakeType        enumeration type that holds type of Error
     * @param mistakes           structure that will hold DictateWord items
     */
    private void addToStructure(int wordPosition, String correctWord, String writtenWord, String mistakeDescription, MistakeType mistakeType, List<Mistake> mistakes) {
//            List<String> err = new LinkedList<String>();
//            err.add(error);
        Mistake mistake = new Mistake(wordPosition, correctWord, writtenWord, mistakeDescription, mistakeType);
        mistakes.add(mistake);
    }

}
