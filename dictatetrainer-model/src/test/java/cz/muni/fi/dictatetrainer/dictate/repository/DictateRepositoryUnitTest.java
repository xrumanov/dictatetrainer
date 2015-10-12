package cz.muni.fi.dictatetrainer.dictate.repository;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.allCategories;
import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.vybraneSlovaCat;
import static cz.muni.fi.dictatetrainer.commontests.dictate.DictatesForTestRepository.*;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.admin;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.allUsers;
import static cz.muni.fi.dictatetrainer.commontests.user.UsersForTestRepository.mrkvicka;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DictateRepositoryUnitTest extends TestBaseRepository {

    private DictateRepository dictateRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        dictateRepository = new DictateRepository();
        dictateRepository.em = em;

        loadCategoriesAndUploaders();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addDictateAndFindIt() {
        final Long dictateAddedId = dbCommandExecutor.executeCommand(() -> {
            return dictateRepository.add(normalizeDependencies(interpunkcia(), em)).getId();
        });

        assertThat(dictateAddedId, is(notNullValue()));

        final Dictate dictate = dictateRepository.findById(dictateAddedId);

        assertThat(dictate.getName(), is(equalTo(interpunkcia().getName())));
        assertThat(dictate.getDescription(), is(equalTo(interpunkcia().getDescription())));
        assertThat(dictate.getCategory().getName(), is(equalTo(interpunkcia().getCategory().getName())));
        assertThat(dictate.getUploader().getEmail(), is(equalTo(interpunkcia().getUploader().getEmail())));
        assertThat(dictate.getFilename(), is(equalTo(interpunkcia().getFilename())));
        assertThat(dictate.getTranscript(), is(equalTo(interpunkcia().getTranscript())));
    }

    @Test
    public void findDictateByIdNotFound() {
        final Dictate dictate = dictateRepository.findById(999L);
        assertThat(dictate, is(nullValue()));
    }

    @Test
    public void updateDictate() {
        final Dictate interpunkcia = normalizeDependencies(interpunkcia(), em);
        final Long dictateAddedId = dbCommandExecutor.executeCommand(() -> {
            return dictateRepository.add(interpunkcia).getId();
        });

        assertThat(dictateAddedId, is(notNullValue()));
        final Dictate dictate = dictateRepository.findById(dictateAddedId);
        assertThat(dictate.getName(), is(equalTo(interpunkcia().getName())));

        dictate.setName("Vybrane slova 3");
        dbCommandExecutor.executeCommand(() -> {
            dictateRepository.update(dictate);
            return null;
        });

        final Dictate dictateAfterUpdate = dictateRepository.findById(dictateAddedId);
        assertThat(dictateAfterUpdate.getName(), is(equalTo("Vybrane slova 3")));
    }

    @Test
    public void existsById() {
        final Dictate interpunkcia = normalizeDependencies(interpunkcia(), em);
        final Long dictateAddedId = dbCommandExecutor.executeCommand(() -> {
            return dictateRepository.add(interpunkcia).getId();
        });

        assertThat(dictateRepository.existsById(dictateAddedId), is(equalTo(true)));
        assertThat(dictateRepository.existsById(999l), is(equalTo(false)));
    }

    @Test
    public void findByFilterNoFilter() {
        loadDictatesForFindByFilter();

        final PaginatedData<Dictate> result = dictateRepository.findByFilter(new DictateFilter());
        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(4)));
        assertThat(result.getRow(0).getName(), is(equalTo(vybraneSlova1().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(vybraneSlova2().getName())));
        assertThat(result.getRow(2).getName(), is(equalTo(velkePismena().getName())));
        assertThat(result.getRow(3).getName(), is(equalTo(interpunkcia().getName())));
    }

    @Test
    public void findByFilterWithPaging() {
        loadDictatesForFindByFilter();

        final DictateFilter dictateFilter = new DictateFilter();
        dictateFilter.setPaginationData(new PaginationData(0, 3, "name", OrderMode.DESCENDING));
        PaginatedData<Dictate> result = dictateRepository.findByFilter(dictateFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(3)));
        assertThat(result.getRow(0).getName(), is(equalTo(vybraneSlova2().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(vybraneSlova1().getName())));
        assertThat(result.getRow(2).getName(), is(equalTo(velkePismena().getName())));

        dictateFilter.setPaginationData(new PaginationData(3, 3, "name", OrderMode.DESCENDING));
        result = dictateRepository.findByFilter(dictateFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getName(), is(equalTo(interpunkcia().getName())));
    }

    @Test
    public void findByFilterFilteringByCategoryAndUploader() {
        loadDictatesForFindByFilter();

        final Dictate dictate = new Dictate();
        dictate.setCategory(vybraneSlovaCat());
        dictate.setUploader(admin());

        final DictateFilter dictateFilter = new DictateFilter();
        dictateFilter.setCategoryId(normalizeDependencies(dictate, em).getCategory().getId());
        dictateFilter.setUploaderId(normalizeDependencies(dictate, em).getUploader().getId());
        dictateFilter.setPaginationData(new PaginationData(0, 3, "name", OrderMode.ASCENDING));
        final PaginatedData<Dictate> result = dictateRepository.findByFilter(dictateFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(2)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getName(), is(equalTo(vybraneSlova1().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(vybraneSlova2().getName())));
    }

    private void loadDictatesForFindByFilter() {
        dbCommandExecutor.executeCommand(() -> {
            allDictates().forEach((dictate) -> dictateRepository.add(normalizeDependencies(dictate, em)));
            return null;
        });
    }

    private void loadCategoriesAndUploaders() {
        dbCommandExecutor.executeCommand(() -> {
            allCategories().forEach(em::persist);
            allUsers().forEach(em::persist);
            return null;
        });
    }
}
