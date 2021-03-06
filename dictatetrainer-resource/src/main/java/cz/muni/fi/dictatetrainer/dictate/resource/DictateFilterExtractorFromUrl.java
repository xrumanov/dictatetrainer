package cz.muni.fi.dictatetrainer.dictate.resource;

import cz.muni.fi.dictatetrainer.common.resource.AbstractFilterExtractorFromUrl;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;

import javax.ws.rs.core.UriInfo;

/**
 * Extract the filter settings from the URL for Dictate resource
 */
public class DictateFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public DictateFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public DictateFilter getFilter() {
        final DictateFilter dictateFilter = new DictateFilter();

        dictateFilter.setPaginationData(extractPaginationData());

        final String uploaderIdStr = getUriInfo().getQueryParameters().getFirst("uploaderId");
        if (uploaderIdStr != null) {
            dictateFilter.setUploaderId(Long.valueOf(uploaderIdStr));
        }

        final String categoryIdStr = getUriInfo().getQueryParameters().getFirst("categoryId");
        if (categoryIdStr != null) {
            dictateFilter.setCategoryId(Long.valueOf(categoryIdStr));
        }

        final String filenameStr = getUriInfo().getQueryParameters().getFirst("filename");
        if (filenameStr != null) {
            dictateFilter.setFilename(filenameStr);
        }

        return dictateFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "name";
    }

}