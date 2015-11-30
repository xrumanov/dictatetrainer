package cz.muni.fi.dictatetrainer.category.services.impl;


import cz.muni.fi.dictatetrainer.category.exception.CategoryExistentException;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.repository.CategoryRepository;
import cz.muni.fi.dictatetrainer.category.services.CategoryServices;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cz.muni.fi.dictatetrainer.commontests.category.CategoriesForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class CategoryServicesUnitTest {

        private CategoryServices categoryServices;
        private CategoryRepository categoryRepository;
        private Validator validator;

        @Before
        public void initTestCase() {
            validator = Validation.buildDefaultValidatorFactory().getValidator();

            categoryRepository = mock(CategoryRepository.class);

            categoryServices = new CategoryServicesImpl();
            ((CategoryServicesImpl) categoryServices).validator = validator;
            ((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
        }

        @Test
        public void addCategoryWithNullName() {
            addCategoryWithInvalidName(null);
        }

        @Test
        public void addCategoryWithShortName() {
            addCategoryWithInvalidName("A");
        }

        @Test
        public void addCategoryWithLongName() {
            addCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
        }

        @Test(expected = CategoryExistentException.class)
        public void addCategoryWithExistentName() {
            when(categoryRepository.alreadyExists(vyjmenovanaSlovaCat())).thenReturn(true);

            categoryServices.add(vyjmenovanaSlovaCat());
        }

        @Test
        public void addValidCategory() {
            when(categoryRepository.alreadyExists(vyjmenovanaSlovaCat())).thenReturn(false);
            when(categoryRepository.add(vyjmenovanaSlovaCat())).thenReturn(categoryWithId(vyjmenovanaSlovaCat(), 1L));

            final Category categoryAdded = categoryServices.add(vyjmenovanaSlovaCat());
            assertThat(categoryAdded.getId(), is(equalTo(1L)));
        }

        @Test
        public void updateWithNullName() {
            updateCategoryWithInvalidName(null);
        }

        @Test
        public void updateCategoryWithShortName() {
            updateCategoryWithInvalidName("A");
        }

        @Test
        public void updateCategoryWithLongName() {
            updateCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
        }

        @Test(expected = CategoryExistentException.class)
        public void updateCategoryWithExistentName() {
            when(categoryRepository.alreadyExists(categoryWithId(vyjmenovanaSlovaCat(), 1L))).thenReturn(true);

            categoryServices.update(categoryWithId(vyjmenovanaSlovaCat(), 1L));
        }

        @Test(expected = CategoryNotFoundException.class)
        public void updateCategoryNotFound() {
            when(categoryRepository.alreadyExists(categoryWithId(vyjmenovanaSlovaCat(), 1L))).thenReturn(false);
            when(categoryRepository.existsById(1L)).thenReturn(false);

            categoryServices.update(categoryWithId(vyjmenovanaSlovaCat(), 1L));
        }

        @Test
        public void updateValidCategory() {
            when(categoryRepository.alreadyExists(categoryWithId(vyjmenovanaSlovaCat(), 1L))).thenReturn(false);
            when(categoryRepository.existsById(1L)).thenReturn(true);

            categoryServices.update(categoryWithId(vyjmenovanaSlovaCat(), 1L));

            verify(categoryRepository).update(categoryWithId(vyjmenovanaSlovaCat(), 1L));
        }

        @Test
        public void findCategoryById() {
            when(categoryRepository.findById(1L)).thenReturn(categoryWithId(vyjmenovanaSlovaCat(), 1L));

            final Category category = categoryServices.findById(1L);
            assertThat(category, is(notNullValue()));
            assertThat(category.getId(), is(equalTo(1L)));
            assertThat(category.getName(), is(equalTo(vyjmenovanaSlovaCat().getName())));
        }

        @Test(expected = CategoryNotFoundException.class)
        public void findCategoryByIdNotFound() {
            when(categoryRepository.findById(1L)).thenReturn(null);

            categoryServices.findById(1L);
        }

        @Test
        public void findAllNoCategories() {
            when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());

            final List<Category> categories = categoryServices.findAll();
            assertThat(categories.isEmpty(), is(equalTo(true)));
        }

        @Test
        public void findAllCategories() {
            when(categoryRepository.findAll("name")).thenReturn(
                    Arrays.asList(categoryWithId(vyjmenovanaSlovaCat(), 1L), categoryWithId(velkaPismena(), 2L), categoryWithId(interpunkce(), 3L)));

            final List<Category> categories = categoryServices.findAll();
            assertThat(categories.size(), is(equalTo(3)));
            assertThat(categories.get(0).getName(), is(equalTo(vyjmenovanaSlovaCat().getName())));
            assertThat(categories.get(1).getName(), is(equalTo(velkaPismena().getName())));
            assertThat(categories.get(2).getName(), is(equalTo(interpunkce().getName())));
        }

        private void addCategoryWithInvalidName(final String name) {
            try {
                categoryServices.add(new Category(name));
                fail("An error should have been thrown");
            } catch (final FieldNotValidException e) {
                assertThat(e.getFieldName(), is(equalTo("name")));
            }
        }

        private void updateCategoryWithInvalidName(final String name) {
            try {
                categoryServices.update(new Category(name));
                fail("An error should have been thrown");
            } catch (final FieldNotValidException e) {
                assertThat(e.getFieldName(), is(equalTo("name")));
            }
        }

    }
