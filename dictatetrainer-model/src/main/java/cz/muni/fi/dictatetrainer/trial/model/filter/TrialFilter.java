package cz.muni.fi.dictatetrainer.trial.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;

import java.util.Date;

/**
 * Filtering entity Trial by given parameters
 *
 * Available params:
 *  startDate: shows only trials performed from given date
 *  endDate:  shows only trials performed till given date
 *  studentId: shows only trials performed by student with given id
 *  dictateId:  shows only trials of one particular dictate id of which is given
 */
public class TrialFilter extends GenericFilter {
    private Date startDate;
    private Date endDate;
    private Long studentId;
    private Long dictateId;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getDictateId() {
        return dictateId;
    }

    public void setDictateId(Long dictateId) {
        this.dictateId = dictateId;
    }

    @Override
    public String toString() {
        return "TrialFilter{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", studentId=" + studentId +
                ", dictateId=" + dictateId +
                super.toString() +
                '}';
    }
}