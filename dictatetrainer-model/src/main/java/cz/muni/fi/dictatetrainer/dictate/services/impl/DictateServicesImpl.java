package cz.muni.fi.dictatetrainer.dictate.services.impl;

import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.services.CategoryServices;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import cz.muni.fi.dictatetrainer.dictate.repository.DictateRepository;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.services.UserServices;

import javax.inject.Inject;
import javax.validation.Validator;

/**
 * Implementation of service methods for Dictate entity
 */
public class DictateServicesImpl implements DictateServices {

    @Inject
    DictateRepository dictateRepository;

    @Inject
    Validator validator;

    @Inject
    UserServices userServices;

    @Inject
    CategoryServices categoryServices;

    @Override
    public Dictate add(final Dictate dictate) {
        ValidationUtils.validateEntityFields(validator, dictate);

        checkCategoryAndSetItOnDictate(dictate);
        checkUserAndSetHimOnDictate(dictate);

        return dictateRepository.add(dictate);
    }

    @Override
    public void update(final Dictate dictate) {
        ValidationUtils.validateEntityFields(validator, dictate);

        if (!dictateRepository.existsById(dictate.getId())) {
            throw new DictateNotFoundException();
        }

        checkCategoryAndSetItOnDictate(dictate);
        checkUserAndSetHimOnDictate(dictate);

        dictateRepository.update(dictate);
    }

    @Override
    public void delete(final Long id) {
        if (!dictateRepository.existsById(id)) {
            throw new DictateNotFoundException();
        }
        dictateRepository.delete(id);
    }

    @Override
    public Dictate findById(final Long id) {
        final Dictate dictate = dictateRepository.findById(id);
        if (dictate == null) {
            throw new DictateNotFoundException();
        }
        return dictate;
    }

    @Override
    public PaginatedData<Dictate> findByFilter(final DictateFilter dictateFilter) {
        return dictateRepository.findByFilter(dictateFilter);
    }

    private void checkUserAndSetHimOnDictate(final Dictate dictate) {
        final User uploader = userServices.findById(dictate.getUploader().getId()); //TODO zmenit na caller?
        dictate.setUploader(uploader);

    }

    private void checkCategoryAndSetItOnDictate(final Dictate dictate) {
        final Category category = categoryServices.findById(dictate.getCategory().getId());
        dictate.setCategory(category);
    }

}