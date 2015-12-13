package cz.muni.fi.dictatetrainer.school.services.impl;

import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.school.exception.SchoolExistentException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.repository.SchoolRepository;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

/**
 * Implementation of service methods for School entity
 */
@Stateless
public class SchoolServicesImpl implements SchoolServices {

    @Inject
    Validator validator;

    @Inject
    SchoolRepository schoolRepository;

    @Override
    public School add(final School school) {
        validateSchool(school);

        return schoolRepository.add(school);
    }

    @Override
    public void update(final School school) {
        validateSchool(school);

        if (!schoolRepository.existsById(school.getId())) {
            throw new SchoolNotFoundException();
        }

        schoolRepository.update(school);
    }

    @Override
    public School findById(final Long id) throws SchoolNotFoundException {
        final School school = schoolRepository.findById(id);
        if (school == null) {
            throw new SchoolNotFoundException();
        }
        return school;
    }

    @Override
    public List<School> findAll() {
        return schoolRepository.findAll("name");
    }

    private void validateSchool(final School school) {
        ValidationUtils.validateEntityFields(validator, school);

        if (schoolRepository.alreadyExists(school)) {
            throw new SchoolExistentException();
        }
    }

}