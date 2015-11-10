package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

/**
 * Implementation of non-contextual corrector rules
 * Only shows description for mistakes that are non-contextual (without any context of by-standing words)
 */
public class CorrectorRulesNoContext implements CorrectorRules {


    public CorrectorRulesNoContext(){

    }

    @Override
    public Mistake applyRules(String markedString) {
        return null;
    }

    //-------------Pravopis----------------

    private void vyjmenovanaSlova() {

    }

    private void iyPoPismenuC() {

    }

    private void predponySZ() {

    }

    private void predlozkySZ() {

    }

    private void prejataSlovaSZ() {

    }

    private void psaniDisDys() {

    }

    private void psaniSeZakoncenimManie() {

    }

    private void psaniNnN() {

    }

    private void psaniSprezekSprahovani() {

    }

    private void slozenaPridavniJmena() {

    }

    private void velkaPismena() {

    }

    //------------Slovotvorba-------------------

    private void adjZakoncenaIci() {

    }

    private void adjZakoncenaNi() {

    }

    private void typAckoliAckoliv() {

    }

    private void vokalizacePredlozek() {

    }

    private void zajmenaVasiJiNi() {

    }

    private void psaniBeBjeVeVjePe() {

    }

    private void souhlaskyParove() {

    }

    private void diakritika() {

    }

    private void iPoMekkychaObojetnychSouhlaskach() {

    }

    private void zajmenaMneMeASlovaJeObsahujici() {

    }
}
