package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

/**
 * Implementation of non-contextual corrector rules
 * Only shows description for mistakes that are non-contextual (without any context of by-standing words)
 */
public class CorrectorRulesNoContext implements CorrectorRules {

    @Override
    public Mistake applyRules(String markedString) {
        return null;
    }
}
