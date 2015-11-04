package cz.muni.fi.dictatetrainer.trial.services;

import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;

/**
 * Interface to service methods for Trial entity (CRUD and filtering)
 */
public interface TrialServices {

    Trial add(Trial trial) throws UserNotFoundException, DictateNotFoundException, FieldNotValidException;

    Trial findById(Long id) throws TrialNotFoundException;

    PaginatedData<Trial> findByFilter(TrialFilter trialFilter);

}
