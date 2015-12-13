package cz.muni.fi.dictatetrainer.schoolclass.resource;


import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.UriInfo;
import java.util.LinkedHashMap;
import java.util.Map;

import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.assertActualPaginationDataWithExpected;
import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.setUpUriInfoWithMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SchoolClassFilterExtractorFromUrlTest {

    private UriInfo uriInfo;

    @Before
    public void initTestCase() {
        uriInfo = mock(UriInfo.class);
    }

    @Test
    public void onlyDefaultValues() {
        setUpUriInfo(null, null, null, null);

        final SchoolClassFilterExtractorFromUrl extractor = new SchoolClassFilterExtractorFromUrl(uriInfo);
        final SchoolClassFilter schoolFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(schoolFilter.getPaginationData(), new PaginationData(0, 10, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(schoolFilter, null);
    }

    @Test
    public void withPaginationAnTeacherIdAndSortAscending() {
        setUpUriInfo("2", "5", "1", "id");

        final SchoolClassFilterExtractorFromUrl extractor = new SchoolClassFilterExtractorFromUrl(uriInfo);
        final SchoolClassFilter schoolClassFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(schoolClassFilter.getPaginationData(), new PaginationData(10, 5, "id",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(schoolClassFilter, 1L);
    }

    private void setUpUriInfo(final String page, final String perPage, final String teacherId,
                              final String sort) {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("page", page);
        parameters.put("per_page", perPage);
        parameters.put("teacherId", teacherId);
        parameters.put("sort", sort);

        setUpUriInfoWithMap(uriInfo, parameters);
    }

    private void assertFieldsOnFilter(final SchoolClassFilter schoolClassFilter, final Long teacherId) {
        assertThat(schoolClassFilter.getTeacherId(), is(equalTo(teacherId)));
    }
}
