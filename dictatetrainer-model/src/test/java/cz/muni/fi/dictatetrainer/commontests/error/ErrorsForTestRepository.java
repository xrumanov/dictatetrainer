/**
 * Prepared Error entities used for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.error;

import cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.user.model.Student;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

@Ignore
public class ErrorsForTestRepository {

    private ErrorsForTestRepository() {
    }

    public static Error errorPredpony() {

        final Error error = new Error();
        error.setMistakeCharPosInWord(1);
        error.setCorrectChars("S");
        error.setWrittenChars("Z");
        error.setCorrectWord("Správa");
        error.setWrittenWord("Zpráva");
        error.setPreviousWord("");
        error.setNextWord("domu.");
        error.setWordPosition(1);
        error.setSentence("(S)<Z>práva domu.");
        error.setLemma("správa");
        error.setPosTag("k5eAaPmFrD");
        error.setErrorDescription( "Některá slova lze psát jak s předponou s-, tak s předponou z-. " +
                "U většiny z nich je mezi oběma podobami zřetelný významový rozdíl. (IJP) správa (domu) x (novinová) zpráva");
        error.setErrorType(Error.ErrorType.PREDPONY_S_Z);
        error.setErrorPriority(7);
        error.setStudent(UsersForTestRepository.mrkvicka());
        error.setDictate(DictatesForTestRepository.vybraneSlova1());
        error.setTrial(TrialsForTestRepository.trialPerformed1());

        return error;
    }

    public static Error errorVyjmenovanaSlova() {

        final Error error = new Error();
        error.setMistakeCharPosInWord(2);
        error.setCorrectChars("ý");
        error.setWrittenChars("í");
        error.setCorrectWord("pýchavku");
        error.setWrittenWord("píchavku");
        error.setPreviousWord("lese");
        error.setNextWord("");
        error.setWordPosition(5);
        error.setSentence("Našel jsem v lese p(ý)<í>chavku.");
        error.setLemma("pýchavka");
        error.setPosTag("k1gFnSc4");
        error.setErrorDescription("pýchavka (houba) x píchat (bodat)");
        error.setErrorType(Error.ErrorType.VYJMENOVANA_SLOVA);
        error.setErrorPriority(10);
        error.setStudent(UsersForTestRepository.mrkvicka());
        error.setDictate(DictatesForTestRepository.vybraneSlova1());
        error.setTrial(TrialsForTestRepository.trialPerformed2());

        return error;

    }

    public static Error errorPredlozky() {

        final Error error = new Error();
        error.setMistakeCharPosInWord(1);
        error.setCorrectChars("z");
        error.setWrittenChars("s");
        error.setCorrectWord("z");
        error.setWrittenWord("z");
        error.setPreviousWord("Vstal");
        error.setNextWord("postele.");
        error.setWordPosition(2);
        error.setSentence("Vstal (z)<s> postele.");
        error.setLemma("z");
        error.setPosTag("k7c2");
        error.setErrorDescription("S druhým pádem se pojí předložka z (ze). " +
                "Např. vstal z postele; vyšel z banky. Se sedmým pádem se pojí jedině předložka s (se). " +
                "Např. trávil čas s knihou; s pilinami si lze hrát. (IJP)");
        error.setErrorType(Error.ErrorType.PREDLOZKY_S_Z);
        error.setErrorPriority(7);
        error.setStudent(UsersForTestRepository.mrkvicka());
        error.setDictate(DictatesForTestRepository.velkePismena());
        error.setTrial(TrialsForTestRepository.trialPerformed3());

        return error;
    }

    public static Error errorManie() {

        final Error error = new Error();
        error.setMistakeCharPosInWord(2);
        error.setCorrectChars("á");
        error.setWrittenChars("a"); //between < and >
        error.setCorrectWord("mánie.");
        error.setWrittenWord("manie.");
        error.setPreviousWord("stala");
        error.setNextWord("");
        error.setWordPosition(6);
        error.setSentence("Z jeho cestom(á)<a>nie se stala m(á)<a>nie.");
        error.setLemma("mánie");
        error.setPosTag("k1gFnPc1");
        error.setErrorDescription("Přípustná je pouze varianta mánie. Pouze pokud jde o slovo složené " +
                "(např. cestománie, pokemanie) je možné psát krátce i dlouze. (VOJ)");
        error.setErrorType(Error.ErrorType.SLOVA_ZAKONCENE_MANIE);
        error.setErrorPriority(6);
        error.setStudent(UsersForTestRepository.gates());
        error.setDictate(DictatesForTestRepository.vybraneSlova2());
        error.setTrial(TrialsForTestRepository.trialPerformed4());

        return error;
    }

    public static Error errorWithId(final Error error, final Long id) {
        error.setId(id);
        return error;
    }

    public static List<Error> allErrors() {
        return Arrays.asList(errorPredpony(), errorVyjmenovanaSlova(), errorPredlozky(), errorManie());
    }

    public static Error normalizeDependencies(final Error error, final EntityManager em) {
        error.setStudent(findByPropertyNameAndValue(em, Student.class, "email", error.getStudent().getEmail()));
        error.setDictate(findByPropertyNameAndValue(em, Dictate.class, "name", error.getDictate().getName()));
        error.setTrial(findByPropertyNameAndValue(em, Trial.class, "trialText", error.getTrial().getTrialText()));
        return error;
    }
}
