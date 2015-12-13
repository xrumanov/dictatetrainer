package cz.muni.fi.dictatetrainer.schoolclass.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;

/**
 * Filtering entity SchoolClass by given parameters
 * <p>
 * Available params:
 * teacherId: shows only schoolClasses belonging to teacher with given id
 */
public class SchoolClassFilter extends GenericFilter {
    private Long teacherId;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "DictateFilter [teacherId=" + teacherId +
                super.toString() + "]";
    }

}