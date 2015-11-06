package cz.muni.fi.dictatetrainer.dictate.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;

/**
 * Filtering entity Dictate by given parameters
 * <p>
 * Available params:
 * uploaderId: shows only dictates uploaded by uploader with given id
 * categoryId: shows only dictates in given category
 * filename: shows only dictate by given filename
 */
public class DictateFilter extends GenericFilter {
    private Long uploaderId;
    private Long categoryId;
    private String filename;

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "DictateFilter [uploaderId=" + uploaderId + ", " +
                                "categoryId=" + categoryId + ", " +
                                "filename=" + filename + ", " +
                                "toString()=" + super.toString() + "]";
    }

}