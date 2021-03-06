/**
 * Prepared Dictate entities used for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.dictate;

import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

@Ignore
public class DictatesForTestRepository {

    private DictatesForTestRepository() {
    }

    public static Dictate vybraneSlova1() {
        final Dictate dictate = new Dictate();
        dictate.setName("Vyjmenovana slova 1");
        dictate.setDescription("Toto je testovací diktát na vyjmenovaná slova.");
        dictate.setCategory(CategoriesForTestRepository.vyjmenovanaSlovaCat());
        dictate.setFilename("dictate1.mp3");
        dictate.setTranscript("Lorem ipsum bla bla.");
        dictate.setDefaultRepetitionForSentences(1);
        dictate.setDefaultRepetitionForDictate(3);
        dictate.setDefaultPauseBetweenSentences(2);
        dictate.setSentenceEndings("15;25;35");
        dictate.setUploader(UsersForTestRepository.admin());

        return dictate;
    }

    public static Dictate vybraneSlova2() {
        final Dictate dictate = new Dictate();
        dictate.setName("Vyjmenovana slova 2");
        dictate.setDescription("Diktat na precvicovanie vybranych slov po S");
        dictate.setCategory(CategoriesForTestRepository.vyjmenovanaSlovaCat());
        dictate.setFilename("dictate2.mp3");
        dictate.setTranscript("Lorem Ipsum bla bla bla bla.");
        dictate.setDefaultRepetitionForSentences(2);
        dictate.setDefaultRepetitionForDictate(2);
        dictate.setDefaultPauseBetweenSentences(2);
        dictate.setSentenceEndings("10;20;30");
        dictate.setUploader(UsersForTestRepository.admin());

        return dictate;
    }

    public static Dictate velkePismena() {
        final Dictate dictate = new Dictate();
        dictate.setName("Velke pismena");
        dictate.setDescription("Diktat na precvicovanie velkych pismen na zaciatkoch slov");
        dictate.setCategory(CategoriesForTestRepository.velkaPismena());
        dictate.setFilename("dictate3.mp3");
        dictate.setTranscript("Lorem Ipsum bla bla bla bla.");
        dictate.setDefaultRepetitionForSentences(2);
        dictate.setDefaultRepetitionForDictate(2);
        dictate.setDefaultPauseBetweenSentences(2);
        dictate.setSentenceEndings("10;20;30");
        dictate.setUploader(UsersForTestRepository.admin());

        return dictate;
    }

    public static Dictate interpunkcia() {
        final Dictate dictate = new Dictate();
        dictate.setName("Procvičování interpunkce");
        dictate.setDescription("Diktat na procvičování interpunkce");
        dictate.setCategory(CategoriesForTestRepository.interpunkce());
        dictate.setFilename("dictate4.mp3");
        dictate.setTranscript("Lorem Ipsum bla bla bla bla.");
        dictate.setDefaultRepetitionForSentences(2);
        dictate.setDefaultRepetitionForDictate(2);
        dictate.setDefaultPauseBetweenSentences(2);
        dictate.setSentenceEndings("10;20;30");
        dictate.setUploader(UsersForTestRepository.admin());

        return dictate;
    }

    public static Dictate dictateWithId(final Dictate dictate, final Long id) {
        dictate.setId(id);
        return dictate;
    }

    public static List<Dictate> allDictates() {
        return Arrays.asList(vybraneSlova1(), vybraneSlova2(), velkePismena(), interpunkcia());
    }

    public static Dictate normalizeDependencies(final Dictate dictate, final EntityManager em) {
        dictate.setCategory(findByPropertyNameAndValue(em, Category.class, "name", dictate.getCategory().getName()));
        dictate.setUploader(findByPropertyNameAndValue(em, Teacher.class, "email", dictate.getUploader().getEmail()));
        return dictate;
    }
}
