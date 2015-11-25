package cz.muni.fi.dictatetrainer.corrector.service;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

import javax.ejb.Local;
import java.util.List;

/**
 * Interface to correct given transcript and user text
 *
 * Consists of three phases
 * -------------------------
 * markInput method: marks the given text using f.e. some diff library
 * tokenizeAndReturnTokens method: tokenize the string from the previous method by using given delimiter
 *                                  and returns List of String tokens
 * createMistakeObjectsAndApplyCorrectorRules method: correct the given tokens using the given rules and
 *                                    returns List of Mistake objects
 *
 */
//@Local
public interface CorrectorService {

    String markInput(String transcript, String userText);

    String[] tokenizeAndReturnTokens(String markedText, String delimiter);

    String[] sentencizedAndReturnSentences(String markedText);

    List<Mistake> createMistakeObjectsAndApplyCorrectorRules(String[] tokens, String[] sentences, CorrectorRules rules);

    String[] getLemmaAndTagForMistake(String correctWord);

    String getTagForMistake(String correctWord);
}
