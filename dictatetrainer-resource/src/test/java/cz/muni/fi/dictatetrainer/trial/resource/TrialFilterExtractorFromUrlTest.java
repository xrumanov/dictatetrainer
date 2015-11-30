package cz.muni.fi.dictatetrainer.trial.resource;

import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData;
import cz.muni.fi.dictatetrainer.common.model.filter.PaginationData.OrderMode;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.assertActualPaginationDataWithExpected;
import static cz.muni.fi.dictatetrainer.commontests.utils.FilterExtractorTestUtils.setUpUriInfoWithMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TrialFilterExtractorFromUrlTest {

    @Mock
    private UriInfo uriInfo;

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onlyDefaultValues() {
        setUpUriInfo(null, null, null, null, null, null, null);

        final TrialFilterExtractorFromUrl extractor = new TrialFilterExtractorFromUrl(uriInfo);
        final TrialFilter trialFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(trialFilter.getPaginationData(), new PaginationData(0, 10, "performed",
                OrderMode.DESCENDING));
        assertFieldsOnFilter(trialFilter, null, null, null, null);
    }

    @Test
    public void withPaginationAndDictateIdAndStudentIdStartDateAndEndDateAndSortAscending() {
        setUpUriInfo("2", "5", "1", "1", "2015-11-04T10:10:34Z", "2015-11-05T10:10:34Z", "performed");

        final TrialFilterExtractorFromUrl extractor = new TrialFilterExtractorFromUrl(uriInfo);
        final TrialFilter trialFilter = extractor.getFilter();

        assertActualPaginationDataWithExpected(trialFilter.getPaginationData(), new PaginationData(10, 5, "performed",
                OrderMode.ASCENDING));
        assertFieldsOnFilter(trialFilter, 1L, 1L, DateUtils.getAsDateTime("2015-11-04T10:10:34Z"),
                DateUtils.getAsDateTime("2015-11-05T10:10:34Z"));
    }

    private void setUpUriInfo(final String page, final String perPage,
                              final String dictateId, final String studentId,
                              final String startDate, final String endDate, final String sort) {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("page", page);
        parameters.put("per_page", perPage);
        parameters.put("dictateId", dictateId);
        parameters.put("studentId", studentId);
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        parameters.put("sort", sort);

        setUpUriInfoWithMap(uriInfo, parameters);
    }

    private void assertFieldsOnFilter(final TrialFilter trialFilter, final Long dictateId,
                                      final Long studentId, final Date startDate, final Date endDate) {
        assertThat(trialFilter.getDictateId(), is(equalTo(dictateId)));
        assertThat(trialFilter.getStudentId(), is(equalTo(studentId)));
        assertThat(trialFilter.getStartDate(), is(equalTo(startDate)));
        assertThat(trialFilter.getEndDate(), is(equalTo(endDate)));
    }
}
