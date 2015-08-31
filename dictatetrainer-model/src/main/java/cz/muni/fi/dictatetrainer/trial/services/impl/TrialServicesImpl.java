package cz.muni.fi.dictatetrainer.trial.services.impl;

import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.utils.ValidationUtils;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import cz.muni.fi.dictatetrainer.trial.repository.TrialRepository;
import cz.muni.fi.dictatetrainer.trial.services.TrialServices;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class TrialServicesImpl implements TrialServices {

    @Inject
    TrialRepository trialRepository;

    @Inject
    UserServices userServices;

    @Inject
    DictateServices dictateServices;

    @Inject
    Validator validator;

    @Resource
    SessionContext sessionContext;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Trial add(final Trial trial) {
        checkStudentAndSetItOnTrial(trial);
        checkDictateAndSetItOnTrial(trial);

        ValidationUtils.validateEntityFields(validator, trial);

        return trialRepository.add(trial);
    }

    @Override
    public Trial findById(final Long id) {
        final Trial trial = trialRepository.findById(id);
        if (trial == null) {
            throw new TrialNotFoundException();
        }
        return trial;
    }

    @Override
    public PaginatedData<Trial> findByFilter(final TrialFilter trialFilter) {
        return trialRepository.findByFilter(trialFilter);
    }

    private void checkStudentAndSetItOnTrial(final Trial trial) {
        final User user = userServices.findByEmail(sessionContext.getCallerPrincipal().getName()); // get the caller
        trial.setStudent((Student) user);
    }

    private void checkDictateAndSetItOnTrial(final Trial trial) {
        final Dictate dictate = dictateServices.findById(trial.getDictate().getId());
        trial.setDictate(dictate);
    }
}
