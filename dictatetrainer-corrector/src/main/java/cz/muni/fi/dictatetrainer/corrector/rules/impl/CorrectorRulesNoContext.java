package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

public class CorrectorRulesNoContext implements CorrectorRules {

    @Override
    public Mistake applyRules(String markedString) {
        return null;
    }
}
