package cz.muni.fi.dictatetrainer.schoolclass.services.impl;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;
import cz.muni.fi.dictatetrainer.schoolclass.exception.SchoolClassNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import cz.muni.fi.dictatetrainer.schoolclass.repository.SchoolClassRepository;
import cz.muni.fi.dictatetrainer.schoolclass.services.SchoolClassServices;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.services.UserServices;

import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.core.Context;

/**
 * Implementation of service methods for SchoolClass entity
 */
public class SchoolClassServicesImpl implements SchoolClassServices {

    @Inject
    SchoolClassRepository schoolClassRepository;

    @Inject
    Validator validator;

    @Inject
    UserServices userServices;

    @Inject
    SchoolServices schoolServices;

    @Context
    SessionContext sessionContext;

    @Override
    public SchoolClass add(final SchoolClass schoolClass) {
        ValidationUtils.validateEntityFields(validator, schoolClass);

        checkTeacherAndSetHimOnSchoolClass(schoolClass);
        checkSchoolAndSetItOnSchoolClass(schoolClass);

        return schoolClassRepository.add(schoolClass);
    }

    @Override
    public void update(final SchoolClass schoolClass) {
        ValidationUtils.validateEntityFields(validator, schoolClass);

        if (!schoolClassRepository.existsById(schoolClass.getId())) {
            throw new SchoolClassNotFoundException();
        }

        checkTeacherAndSetHimOnSchoolClass(schoolClass);
        checkSchoolAndSetItOnSchoolClass(schoolClass);

        schoolClassRepository.update(schoolClass);
    }

    @Override
    public SchoolClass findById(final Long id) {
        final SchoolClass schoolClass = schoolClassRepository.findById(id);
        if (schoolClass == null) {
            throw new SchoolClassNotFoundException();
        }
        return schoolClass;
    }

    @Override
    public PaginatedData<SchoolClass> findByFilter(final SchoolClassFilter schoolClassFilter) {
        return schoolClassRepository.findByFilter(schoolClassFilter);
    }

    private void checkTeacherAndSetHimOnSchoolClass(final SchoolClass schoolClass) {
        final User teacher = userServices.findById(schoolClass.getTeacher().getId());
        schoolClass.setTeacher(teacher);

    }

    private void checkSchoolAndSetItOnSchoolClass(final SchoolClass schoolClass) {
        final School school = schoolServices.findById(schoolClass.getSchool().getId());
        schoolClass.setSchool(school);
    }
}