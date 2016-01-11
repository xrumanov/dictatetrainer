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
    private Long schoolId;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "SchoolClassFilter{" +
                "teacherId=" + teacherId +
                ", schoolId=" + schoolId +
                '}';
    }
}