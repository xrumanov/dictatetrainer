package cz.muni.fi.dictatetrainer.corrector.rules;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;

/**
 * Interface for Rules definition used by corrector
 */
public interface CorrectorRules {

    /**
     * Apply rules to given marked String
     * @return Mistake
     */
    Mistake applyRules(Mistake mistake);

}
