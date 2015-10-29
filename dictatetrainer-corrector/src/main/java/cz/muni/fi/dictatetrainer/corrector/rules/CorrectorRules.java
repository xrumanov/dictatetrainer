package cz.muni.fi.dictatetrainer.corrector.rules;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;


public interface CorrectorRules {

    /**
     * apply rules to given marked String
     * @return Mistake
     */
    Mistake applyRules(String markedString);

}
