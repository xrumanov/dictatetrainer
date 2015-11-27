/**
 * Prepared Trial entities used for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.trial;


import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.user.model.Student;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.gates;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.mrkvicka;
import static cz.muni.fi.dictatetrainer.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

@Ignore
public class TrialsForTestRepository {

    private TrialsForTestRepository() {
    }

    public static Trial trialPerformed1() {
        final Trial trial = new Trial();
        trial.setStudent((Student) gates());
        trial.setDictate(interpunkcia());
        trial.setTrialText("Nebudem to pisat lebo sa mi to nechce");

        return trial;
    }

    public static Trial trialPerformed2() {
        final Trial trial = new Trial();
        trial.setStudent((Student) mrkvicka());
        trial.setDictate(vybraneSlova1());
        trial.setTrialText("Lorem Ipsum blablabla");

        return trial;
    }

    public static Trial trialPerformed3() {
        final Trial trial = new Trial();
        trial.setStudent((Student) gates());
        trial.setDictate(velkePismena());
        trial.setTrialText("Toto je diktat ktory je testovy.");

        return trial;
    }

    public static Trial trialPerformed4() {
        final Trial trial = new Trial();
        trial.setStudent((Student) mrkvicka());
        trial.setDictate(vybraneSlova2());
        trial.setTrialText("Lorem Ipsum dva.");

        return trial;
    }

    public static Trial trialWithId(final Trial trial, final Long id) {
        trial.setId(id);
        return trial;
    }

    public static Trial trialPerformedAt(final Trial trial, final String dateTime) {
        trial.setPerformed(DateUtils.getAsDateTime(dateTime));
        return trial;
    }

    public static List<Trial> allTrials() {
        return Arrays.asList(trialPerformed1(), trialPerformed2(), trialPerformed3(), trialPerformed4());
    }

    public static Trial normalizeDependencies(final Trial trial, final EntityManager em) {
        trial.setStudent(findByPropertyNameAndValue(em, Student.class, "email", trial.getStudent().getEmail()));
        trial.setDictate(findByPropertyNameAndValue(em, Dictate.class, "name", trial.getDictate().getName()));

        return trial;
    }
}
