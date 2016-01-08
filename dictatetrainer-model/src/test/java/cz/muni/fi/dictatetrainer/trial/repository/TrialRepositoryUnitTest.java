package cz.muni.fi.dictatetrainer.trial.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.school.SchoolsForTestRepository.allSchools;
import static cz.muni.fi.dictatetrainer.commontests.schoolclass.SchoolClassesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.trial.TrialsForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TrialRepositoryUnitTest extends TestBaseRepository {

        private TrialRepository trialRepository;

        @Before
        public void initTestCase() {
            initializeTestDB();

            trialRepository = new TrialRepository();
            trialRepository.em = em;

            loadDictatesAndStudents();
        }

        @After
        public void setDownTestCase() {
            closeEntityManager();
        }

        @Test
        public void addAnTrialAndFindIt() {
            final Long trialAddedId = dbCommandExecutor.executeCommand(() -> {
                return trialRepository.add(normalizeDependencies(trialPerformed1(), em)).getId();
            });
            assertThat(trialAddedId, is(notNullValue()));

            final Trial trialAdded = trialRepository.findById(trialAddedId);
            assertActualTrialWithExpectedTrial(trialAdded, normalizeDependencies(trialPerformed1(), em));
        }

        @Test
        public void findTrialByIdNotFound() {
            final Trial trial = trialRepository.findById(999L);
            assertThat(trial, is(nullValue()));
        }


        @Test
        public void existsById() {
            final Long trialAddedId = dbCommandExecutor.executeCommand(() -> {
                return trialRepository.add(normalizeDependencies(trialPerformed2(), em)).getId();
            });
            assertThat(trialAddedId, is(notNullValue()));

            assertThat(trialRepository.existsById(trialAddedId), is(equalTo(true)));
            assertThat(trialRepository.existsById(999l), is(equalTo(false)));
        }

        @Test
        public void findByFilterNoFilter() {
            loadForFindByFilter();

            final PaginatedData<Trial> trials = trialRepository.findByFilter(new TrialFilter());
            assertThat(trials.getNumberOfRows(), is(equalTo(3)));
            assertThat(DateUtils.formatDateTime(trials.getRow(0).getPerformed()), is(equalTo("2015-01-08T10:10:21Z")));
            assertThat(DateUtils.formatDateTime(trials.getRow(1).getPerformed()), is(equalTo("2015-01-07T10:10:21Z")));
            assertThat(DateUtils.formatDateTime(trials.getRow(2).getPerformed()), is(equalTo("2015-01-06T10:10:21Z")));
        }

    //update should not be possible

        @Test
        public void findByFilterFilteringByStudentId() {
            loadForFindByFilter();

            final TrialFilter filter = new TrialFilter();
            Long studentId = normalizeDependencies(trialPerformed1(), em).getStudent().getId();
            filter.setStudentId(studentId);

            final PaginatedData<Trial> trials = trialRepository.findByFilter(filter);
            assertThat(filter.getStudentId(), is(notNullValue()));
            assertThat(filter.getStudentId(), is(equalTo(studentId)));
            assertThat(trials.getNumberOfRows(), is(equalTo(1)));
            assertThat(DateUtils.formatDateTime(trials.getRow(0).getPerformed()), is(equalTo("2015-01-06T10:10:21Z")));
        }

    @Test
    public void findByFilterFilteringByDictateId() {
        loadForFindByFilter();

        final TrialFilter filter = new TrialFilter();
        Long dictateId = normalizeDependencies(trialPerformed1(), em).getDictate().getId();
        filter.setDictateId(dictateId);

        final PaginatedData<Trial> trials = trialRepository.findByFilter(filter);
        assertThat(filter.getDictateId(), is(notNullValue()));
        assertThat(filter.getDictateId(), is(equalTo(dictateId)));
        assertThat(trials.getNumberOfRows(), is(equalTo(1)));
        assertThat(DateUtils.formatDateTime(trials.getRow(0).getPerformed()), is(equalTo("2015-01-06T10:10:21Z")));
    }

        @Test
        public void findByFilterFilteringByStudentTrialingByCreationAsc() {
            loadForFindByFilter();

            final TrialFilter filter = new TrialFilter();
            filter.setStudentId(normalizeDependencies(trialPerformed2(), em).getStudent().getId());
            filter.setPaginationData(new PaginationData(0, 10, "performed", OrderMode.ASCENDING));

            final PaginatedData<Trial> trials = trialRepository.findByFilter(filter);
            assertThat(trials.getNumberOfRows(), is(equalTo(2)));
            assertThat(DateUtils.formatDateTime(trials.getRow(0).getPerformed()), is(equalTo("2015-01-07T10:10:21Z")));
            assertThat(DateUtils.formatDateTime(trials.getRow(1).getPerformed()), is(equalTo("2015-01-08T10:10:21Z")));
        }

        @Test
        public void findByFilterFilteringByDate() {
            loadForFindByFilter();

            final TrialFilter filter = new TrialFilter();
            filter.setStartDate(DateUtils.getAsDateTime("2015-01-07T10:10:21Z"));
            filter.setEndDate(DateUtils.getAsDateTime("2015-01-08T10:10:21Z"));

            final PaginatedData<Trial> trials = trialRepository.findByFilter(filter);
            assertThat(trials.getNumberOfRows(), is(equalTo(2)));
            assertThat(DateUtils.formatDateTime(trials.getRow(0).getPerformed()), is(equalTo("2015-01-08T10:10:21Z")));
            assertThat(DateUtils.formatDateTime(trials.getRow(1).getPerformed()), is(equalTo("2015-01-07T10:10:21Z")));
        }

        private void loadForFindByFilter() {
            final Trial trial1 = normalizeDependencies(trialPerformed1(), em);
            trialPerformedAt(trial1, "2015-01-06T10:10:21Z");

            final Trial trial2 = normalizeDependencies(trialPerformed2(), em);
            trialPerformedAt(trial2, "2015-01-07T10:10:21Z");

            final Trial trial3 = normalizeDependencies(trialPerformed2(), em);
            trialPerformedAt(trial3, "2015-01-08T10:10:21Z");

            dbCommandExecutor.executeCommand(() -> {
                trialRepository.add(trial1);
                trialRepository.add(trial2);
                trialRepository.add(trial3);
                return null;
            });
        }

        private void assertActualTrialWithExpectedTrial(final Trial actualTrial, final Trial expectedTrial) {
            assertThat(expectedTrial.getPerformed(), is(notNullValue()));
            assertThat(actualTrial.getStudent(), is(equalTo(expectedTrial.getStudent())));
            assertThat(actualTrial.getDictate(), is(equalTo(expectedTrial.getDictate())));
            assertThat(actualTrial.getTrialText(), is(equalTo(expectedTrial.getTrialText())));

        }

        private void loadDictatesAndStudents() {
            dbCommandExecutor.executeCommand(() -> {
                allSchools().forEach(em::persist);
                allTeachers().forEach(em::persist);
                allSchoolClasses().forEach(schoolClass -> em.persist(normalizeDependencies(schoolClass, em)));
                allCategories().forEach(em::persist);
                allDictates().forEach(dictate -> em.persist(normalizeDependencies(dictate, em)));
                allStudents().forEach(user -> em.persist(normalizeDependencies(user, em)));
                return null;
            });
        }

    }
