package cz.muni.fi.dictatetrainer.error.model.filter;

import cz.muni.fi.dictatetrainer.common.model.filter.GenericFilter;

/**
 * Filtering entity Error by given parameters
 *
 * Available params:
 *  studentId: shows only errors written by student with given id
 *  dictateId: shows only errors in a dictate with given id
 *  trialId: shows only errors in a trial with given id
 *
 */
public class ErrorFilter extends GenericFilter {
    private Long studentId;
    private Long dictateId;
    private Long trialId;

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

    public Long getTrialId() {
        return trialId;
    }

    public void setTrialId(Long trialId) {
        this.trialId = trialId;
    }

    @Override
    public String toString() {
        return "ErrorFilter{" +
                "studentId=" + studentId +
                ", dictateId=" + dictateId +
                ", trialId=" + trialId +
                '}';
    }
}