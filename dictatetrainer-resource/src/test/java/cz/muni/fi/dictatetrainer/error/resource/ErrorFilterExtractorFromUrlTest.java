package cz.muni.fi.dictatetrainer.error.resource;


import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.resource.ErrorFilterExtractorFromUrl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.UriInfo;
import java.util.LinkedHashMap;
import java.util.Map;

import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.assertActualPaginationDataWithExpected;
import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.setUpUriInfoWithMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ErrorFilterExtractorFromUrlTest {

        @Mock
        private UriInfo uriInfo;

        @Before
        public void initTestCase() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void onlyDefaultValues() {
            setUpUriInfo(null, null, null, null, null, null);

            final ErrorFilterExtractorFromUrl extractor = new ErrorFilterExtractorFromUrl(uriInfo);
            final ErrorFilter errorFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(errorFilter.getPaginationData(), new PaginationData(0, 10, "id",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(errorFilter, null, null, null);
        }

        @Test
        public void withPaginationAndDictateIdAndStudentIdAndTrialIdAndSortAscending() {
            setUpUriInfo("2", "5", "1", "1", "1", "id");

            final ErrorFilterExtractorFromUrl extractor = new ErrorFilterExtractorFromUrl(uriInfo);
            final ErrorFilter errorFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(errorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(errorFilter, 1L, 1L, 1L);
        }

        @Test
        public void withPaginationAndTrialIdAndSortAscendingWithPrefix() {
            setUpUriInfo("2", "5", null, null, "1", "+id");

            final ErrorFilterExtractorFromUrl extractor = new ErrorFilterExtractorFromUrl(uriInfo);
            final ErrorFilter errorFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(errorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.ASCENDING));
            assertFieldsOnFilter(errorFilter, null, null, 1L);
        }

        @Test
        public void withPaginationStudentIdAndDictateIdAndTrialIdSortDescending() {
            setUpUriInfo("2", "5", "1", "10", "1", "-id");

            final ErrorFilterExtractorFromUrl extractor = new ErrorFilterExtractorFromUrl(uriInfo);
            final ErrorFilter errorFilter = extractor.getFilter();

            assertActualPaginationDataWithExpected(errorFilter.getPaginationData(), new PaginationData(10, 5, "id",
                    OrderMode.DESCENDING));
            assertFieldsOnFilter(errorFilter, 1L, 10L, 1L);
        }

        private void setUpUriInfo(final String page, final String perPage, final String dictateId,
                                  final String studentId, final String trialId, final String sort) {
            final Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put("page", page);
            parameters.put("per_page", perPage);
            parameters.put("dictateId", dictateId);
            parameters.put("studentId", studentId);
            parameters.put("trialId", trialId);
            parameters.put("sort", sort);

            setUpUriInfoWithMap(uriInfo, parameters);
        }

        private void assertFieldsOnFilter(final ErrorFilter errorFilter, final Long dictateId,
                                          final Long studentId, final Long trialId) {
            assertThat(errorFilter.getDictateId(), is(equalTo(dictateId)));
            assertThat(errorFilter.getStudentId(), is(equalTo(studentId)));
            assertThat(errorFilter.getTrialId(), is(equalTo(trialId)));
        }
}
