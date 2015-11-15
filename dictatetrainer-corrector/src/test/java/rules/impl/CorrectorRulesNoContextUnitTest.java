package rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;
import cz.muni.fi.dictatetrainer.corrector.rules.impl.CorrectorRulesNoContext;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrectorRulesNoContextUnitTest {

    @Test
    public void correctMistakeTypeReturned(){
        CorrectorRules cr = new CorrectorRulesNoContext();
        Mistake mistake = new Mistake();

        Mistake mistakeWithType = cr.applyRules(mistake);

        assertThat(mistakeWithType.getMistakeType().toString(), is(equalTo("OSTATNI")));
    }
}
