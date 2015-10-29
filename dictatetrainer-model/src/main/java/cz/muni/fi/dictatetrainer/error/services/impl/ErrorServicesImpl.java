package cz.muni.fi.dictatetrainer.error.services.impl;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.error.exception.ErrorNotFoundException;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.repository.ErrorRepository;
import cz.muni.fi.dictatetrainer.error.services.ErrorServices;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.services.UserServices;

import javax.inject.Inject;
import javax.validation.Validator;

public class ErrorServicesImpl implements ErrorServices {

    @Inject
    ErrorRepository errorRepository;

    @Inject
    Validator validator;

    @Inject
    UserServices userServices;

    @Inject
    DictateServices dictateServices;

    @Override
    public Error add(final Error error) {
        ValidationUtils.validateEntityFields(validator, error);

        checkDictateAndSetItOnError(error);
        checkUserAndSetHimOnError(error);

        return errorRepository.add(error);
    }

    @Override
    public void update(final Error error) {
        ValidationUtils.validateEntityFields(validator, error);

        if (!errorRepository.existsById(error.getId())) {
            throw new ErrorNotFoundException();
        }

        checkDictateAndSetItOnError(error);
        checkUserAndSetHimOnError(error);

        errorRepository.update(error);
    }

    @Override
    public Error findById(final Long id) {
        final cz.muni.fi.dictatetrainer.error.model.Error error = errorRepository.findById(id);
        if (error == null) {
            throw new ErrorNotFoundException();
        }
        return error;
    }

    @Override
    public PaginatedData<Error> findByFilter(final ErrorFilter errorFilter) {
        return errorRepository.findByFilter(errorFilter);
    }

    private void checkUserAndSetHimOnError(final Error error) {
        final User student = userServices.findById(error.getStudent().getId());
        error.setStudent(student);

    }

    private void checkDictateAndSetItOnError(final Error error) {
        final Dictate dictate = dictateServices.findById(error.getDictate().getId());
        error.setDictate(dictate);
    }

}
