package cz.muni.fi.dictatetrainer.dictate.resource;


import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;

public class DictateFilterExtractorFromUrlTest {

        @Mock
        private UriInfo uriInfo;

        @Before
        public void initTestCase() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void onlyDefaultValues() {
            setUpUriInfo(null, null, null, null, null, null);

            final DictateFilterExtractorFromUrl extractor = new DictateFilterExtractorFromUrl(uriInfo);
            final DictateFilter dictateFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(dictateFilter.getPaginationData(), new PaginationData(0, 10, "name",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(dictateFilter, null, null, null);
        }

        @Test
        public void withPaginationAndUploaderIdAndCategoryIdAndFilenameAndSortAscending() {
            setUpUriInfo("2", "5", "1", "1", "sample_dictate.mp3", "id");

            final DictateFilterExtractorFromUrl extractor = new DictateFilterExtractorFromUrl(uriInfo);
            final DictateFilter dictateFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(dictateFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(dictateFilter, 1L, 1L, "sample_dictate.mp3");
        }

        @Test
        public void withPaginationAndFilenameAndSortAscendingWithPrefix() {
            setUpUriInfo("2", "5", null, null, "sample.mp3", "+id");

            final DictateFilterExtractorFromUrl extractor = new DictateFilterExtractorFromUrl(uriInfo);
            final DictateFilter dictateFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(dictateFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(dictateFilter, null, null, "sample.mp3");
        }

        @Test
        public void withPaginationUploaderIdAndCategoryIdAndFilenameSortDescending() {
            setUpUriInfo("2", "5", "1", "10", "filename.mp3", "-id");

            final DictateFilterExtractorFromUrl extractor = new DictateFilterExtractorFromUrl(uriInfo);
            final DictateFilter dictateFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(dictateFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.DESCENDING));
            assertFieldsOnFilter(dictateFilter, 1L, 10L, "filename.mp3");
        }

        private void setUpUriInfo(final String page, final String perPage, final String uploaderId,
                                  final String categoryId, String filename, final String sort) {
            final Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put("page", page);
            parameters.put("per_page", perPage);
            parameters.put("uploaderId", uploaderId);
            parameters.put("categoryId", categoryId);
            parameters.put("filename", filename);
            parameters.put("sort", sort);

            setUpUriInfoWithMap(uriInfo, parameters);
        }

        private void assertFieldsOnFilter(final DictateFilter dictateFilter, final Long uploaderId,
                                          final Long categoryId, final String filename) {
            assertThat(dictateFilter.getUploaderId(), is(equalTo(uploaderId)));
            assertThat(dictateFilter.getCategoryId(), is(equalTo(categoryId)));
            assertThat(dictateFilter.getFilename(), is(equalTo(filename)));
        }
}
