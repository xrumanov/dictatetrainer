package cz.muni.fi.dictatetrainer.error.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.allCategories;
import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.error.ErrorsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.error.ErrorsForTestRepository.normalizeDependencies;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository.allTrials;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository.trialPerformed1;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ErrorRepositoryUnitTest extends TestBaseRepository {

    private ErrorRepository errorRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        errorRepository = new ErrorRepository();
        errorRepository.em = em;

        loadStudentsDictatesAndTrials();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addErrorAndFindIt() {
        final Long errorAddedId = dbCommandExecutor.executeCommand(() -> {
            return errorRepository.add(normalizeDependencies(errorManie(), em)).getId();
        });

        assertThat(errorAddedId, is(notNullValue()));

        final Error error = errorRepository.findById(errorAddedId);

        assertThat(error.getMistakeCharPosInWord(), is(equalTo(errorManie().getMistakeCharPosInWord())));
        assertThat(error.getCorrectChars(), is(equalTo(errorManie().getCorrectChars())));
        assertThat(error.getWrittenChars(), is(equalTo(errorManie().getWrittenChars())));
        assertThat(error.getCorrectWord(), is(equalTo(errorManie().getCorrectWord())));
        assertThat(error.getWrittenWord(), is(equalTo(errorManie().getWrittenWord())));
        assertThat(error.getPreviousWord(), is(equalTo(errorManie().getPreviousWord())));
        assertThat(error.getNextWord(), is(equalTo(errorManie().getNextWord())));
        assertThat(error.getWordPosition(), is(equalTo(errorManie().getWordPosition())));
        assertThat(error.getSentence(), is(equalTo(errorManie().getSentence())));
        assertThat(error.getLemma(), is(equalTo(errorManie().getLemma())));
        assertThat(error.getPosTag(), is(equalTo(errorManie().getPosTag())));
        assertThat(error.getStudent().getEmail(), is(equalTo(errorManie().getStudent().getEmail())));
        assertThat(error.getDictate().getName(), is(equalTo(errorManie().getDictate().getName())));
        assertThat(error.getTrial().getTrialText(), is(equalTo(errorManie().getTrial().getTrialText())));

    }

    @Test
    public void findErrorByIdNotFound() {
        final Error error = errorRepository.findById(999L);
        assertThat(error, is(nullValue()));
    }

    @Test
    public void updateError() {
        final Error errorManie = normalizeDependencies(errorManie(), em);
        final Long errorAddedId = dbCommandExecutor.executeCommand(() -> {
            return errorRepository.add(errorManie).getId();
        });

        assertThat(errorAddedId, is(notNullValue()));
        final Error error = errorRepository.findById(errorAddedId);
        assertThat(error.getCorrectWord(), is(equalTo(errorManie().getCorrectWord())));

        error.setCorrectWord("aktualizovane");
        dbCommandExecutor.executeCommand(() -> {
            errorRepository.update(error);
            return null;
        });

        final Error errorAfterUpdate = errorRepository.findById(errorAddedId);
        assertThat(errorAfterUpdate.getCorrectWord(), is(equalTo("aktualizovane")));
    }

    @Test
    public void existsById() {
        final Error interpunkcia = normalizeDependencies(errorPredlozky(), em);
        final Long errorAddedId = dbCommandExecutor.executeCommand(() -> {
            return errorRepository.add(interpunkcia).getId();
        });

        assertThat(errorRepository.existsById(errorAddedId), is(equalTo(true)));
        assertThat(errorRepository.existsById(999l), is(equalTo(false)));
    }

    @Test
    public void findByFilterNoFilter() {
        loadErrorsForFindByFilter();

        final PaginatedData<Error> result = errorRepository.findByFilter(new ErrorFilter());
        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(4)));
        assertThat(result.getRow(0).getId(), is(equalTo(1L)));
        assertThat(result.getRow(1).getId(), is(equalTo(2L)));
        assertThat(result.getRow(2).getId(), is(equalTo(3L)));
        assertThat(result.getRow(3).getId(), is(equalTo(4L)));
    }

    @Test
    public void findByFilterWithPaging() {
        loadErrorsForFindByFilter();

        final ErrorFilter errorFilter = new ErrorFilter();
        errorFilter.setPaginationData(new PaginationData(0, 3, "id", OrderMode.DESCENDING));
        PaginatedData<Error> result = errorRepository.findByFilter(errorFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(3)));
        assertThat(result.getRow(0).getId(), is(equalTo(4L)));
        assertThat(result.getRow(1).getId(), is(equalTo(3L)));
        assertThat(result.getRow(2).getId(), is(equalTo(2L)));

        errorFilter.setPaginationData(new PaginationData(3, 3, "id", OrderMode.DESCENDING));
        result = errorRepository.findByFilter(errorFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getId(), is(equalTo(1L)));
    }

    @Test
    public void findByFilterFilteringByCategoryAndUploader() {
        loadErrorsForFindByFilter();

        final Error error = new Error();
        error.setDictate(vybraneSlova1());
        error.setStudent(mrkvicka());
        error.setTrial(trialPerformed1());

        final ErrorFilter errorFilter = new ErrorFilter();
        errorFilter.setDictateId(normalizeDependencies(error, em).getDictate().getId());
        errorFilter.setStudentId(normalizeDependencies(error, em).getStudent().getId());
        errorFilter.setPaginationData(new PaginationData(0, 3, "id", OrderMode.ASCENDING));
        final PaginatedData<Error> result = errorRepository.findByFilter(errorFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(2)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getId(), is(equalTo(1L)));
        assertThat(result.getRow(1).getId(), is(equalTo(3L)));
    }

    private void loadErrorsForFindByFilter() {
        dbCommandExecutor.executeCommand(() -> {
            allErrors().forEach((error) -> errorRepository.add(normalizeDependencies(error, em)));
            return null;
        });
    }

    private void loadStudentsDictatesAndTrials() {
        dbCommandExecutor.executeCommand(() -> {
            allCategories().forEach(em::persist);
            allUsers().forEach(em::persist);
            allDictates().forEach(dictate -> em.persist(DictatesForTestRepository.normalizeDependencies(dictate, em)));
            allTrials().forEach(trial -> em.persist(TrialsForTestRepository.normalizeDependencies(trial, em)));
            return null;
        });
    }
}
