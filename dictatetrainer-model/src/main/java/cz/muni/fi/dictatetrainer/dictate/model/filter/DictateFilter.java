package cz.muni.fi.dictatetrainer.dictate.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;

/**
 * Filtering entity Dictate by given parameters
 *
 * Available params:
 *  uploaderId: shows only dictates uploaded by uploader with given id
 *  categoryId: shows only dictates in given category
 *
 */
public class DictateFilter extends GenericFilter {
    private Long uploaderId;
    private Long categoryId;

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

    @Override
    public String toString() {
        return "DictateFilter [uploaderId=" + uploaderId + ", categoryId=" + categoryId + ", toString()=" + super.toString() + "]";
    }

}