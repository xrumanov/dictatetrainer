package cz.muni.fi.dictatetrainer.category.repository;

import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.commontests.utils.TestBaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class CategoryRepositoryUnitTest extends TestBaseRepository {
    private CategoryRepository categoryRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        categoryRepository = new CategoryRepository();
        categoryRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void addCategoryAndFindIt() {
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(interpunkce()).getId();
        });

        assertThat(categoryAddedId, is(notNullValue()));

        final Category category = categoryRepository.findById(categoryAddedId);
        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is(equalTo(interpunkce().getName())));
    }

    @Test
    public void findCategoryByIdNotFound() {
        final Category category = categoryRepository.findById(999L);
        assertThat(category, is(nullValue()));
    }

    @Test
    public void findCategoryByIdNullId() {
        final Category category = categoryRepository.findById(null);
        assertThat(category, is(nullValue()));
    }

    @Test
    public void updateCategory() {
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(interpunkce()).getId();
        });

        final Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
        assertThat(categoryAfterAdd.getName(), is(equalTo(interpunkce().getName())));

        categoryAfterAdd.setName(velkaPismena().getName());
        dbCommandExecutor.executeCommand(() -> {
            categoryRepository.update(categoryAfterAdd);
            return null;
        });

        final Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
        assertThat(categoryAfterUpdate.getName(), is(equalTo(velkaPismena().getName())));
    }

    @Test
    public void findAllCategories() {
        dbCommandExecutor.executeCommand(() -> {
            allCategories().forEach(categoryRepository::add);
            return null;
        });

        final List<Category> categories = categoryRepository.findAll("name");
        assertThat(categories.size(), is(equalTo(3)));
        assertThat(categories.get(0).getName(), is(equalTo(interpunkce().getName())));
        assertThat(categories.get(1).getName(), is(equalTo(velkaPismena().getName())));
        assertThat(categories.get(2).getName(), is(equalTo(vyjmenovanaSlovaCat().getName())));
    }

    @Test
    public void alreadyExistsWhileAdding() {
        dbCommandExecutor.executeCommand(() -> {
            categoryRepository.add(interpunkce());
            return null;
        });

        assertThat(categoryRepository.alreadyExists(interpunkce()), is(equalTo(true)));
        assertThat(categoryRepository.alreadyExists(velkaPismena()), is(equalTo(false)));
    }

    @Test
    public void categoryWithIdAlreadyExists() {
        final Category interpunkcia = dbCommandExecutor.executeCommand(() -> {
            categoryRepository.add(velkaPismena());
            return categoryRepository.add(interpunkce());
        });

        assertThat(categoryRepository.alreadyExists(interpunkcia), is(equalTo(false)));

        interpunkcia.setName(velkaPismena().getName());
        assertThat(categoryRepository.alreadyExists(interpunkcia), is(equalTo(true)));

        interpunkcia.setName(vyjmenovanaSlovaCat().getName());
        assertThat(categoryRepository.alreadyExists(interpunkcia), is(equalTo(false)));
    }

    @Test
    public void existsById() {
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(interpunkce()).getId();
        });

        assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
        assertThat(categoryRepository.existsById(999L), is(equalTo(false)));
    }

}
