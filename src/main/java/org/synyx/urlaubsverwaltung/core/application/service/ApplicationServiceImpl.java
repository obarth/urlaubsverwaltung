
package org.synyx.urlaubsverwaltung.core.application.service;

import org.joda.time.DateMidnight;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.util.Assert;

import org.synyx.urlaubsverwaltung.core.application.dao.ApplicationDAO;
import org.synyx.urlaubsverwaltung.core.application.domain.Application;
import org.synyx.urlaubsverwaltung.core.application.domain.ApplicationStatus;
import org.synyx.urlaubsverwaltung.core.person.Person;
import org.synyx.urlaubsverwaltung.core.settings.SettingsService;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Implementation of interface {@link ApplicationService}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
@Service
class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationDAO applicationDAO;
    private final SettingsService settingsService;

    @Autowired
    ApplicationServiceImpl(ApplicationDAO applicationDAO,
                           SettingsService settingsService) {

        this.applicationDAO = applicationDAO;
        this.settingsService = settingsService;
    }

    @Override
    public Optional<Application> getApplicationById(Integer id) {

        return Optional.ofNullable(applicationDAO.findOne(id));
    }


    @Override
    public void save(Application application) {

        applicationDAO.save(application);
    }


    @Override
    public List<Application> getApplicationsForACertainState(ApplicationStatus state) {

        return applicationDAO.getApplicationsForACertainState(state);
    }


    @Override
    public List<Application> getApplicationsForACertainPeriodAndPerson(DateMidnight startDate, DateMidnight endDate,
        Person person) {

        return applicationDAO.getApplicationsForACertainTimeAndPerson(startDate.toDate(), endDate.toDate(), person);
    }


    @Override
    public List<Application> getApplicationsForACertainPeriodAndState(DateMidnight startDate, DateMidnight endDate,
        ApplicationStatus status) {

        return applicationDAO.getApplicationsForACertainTimeAndState(startDate.toDate(), endDate.toDate(), status);
    }


    @Override
    public List<Application> getApplicationsForACertainPeriodAndPersonAndState(DateMidnight startDate,
        DateMidnight endDate, Person person, ApplicationStatus status) {

        return applicationDAO.getApplicationsForACertainTimeAndPersonAndState(startDate.toDate(), endDate.toDate(),
                person, status);
    }

    @Override
    public List<Application> getAllLongWaitingApplications() {

        List<Application> allWaitingApplications = this.getApplicationsForACertainState(ApplicationStatus.WAITING);

        List<Application> longWaitingApplications = allWaitingApplications.stream()
                .filter(isLongWaitingApplications())
                .collect(Collectors.toList());

        return longWaitingApplications;
    }

    private Predicate<Application> isLongWaitingApplications() {
        return application -> {

            DateMidnight remindDate = application.getRemindDate();
            if (remindDate == null) {
                Integer daysBeforeRemindForWaitingApplications =
                        settingsService.getSettings().getAbsenceSettings().getDaysBeforeRemindForWaitingApplications();

                // never reminded before
                DateMidnight minDateForNotification = application.getApplicationDate()
                        .plusDays(daysBeforeRemindForWaitingApplications);

                // true -> remind!
                // false -> to early for notification
                return minDateForNotification.isBeforeNow();
            } else {
                // true -> not reminded today
                // false -> already reminded today
                return !remindDate.isEqual(DateMidnight.now());
            }
        };
    }


    @Override
    public BigDecimal getTotalOvertimeReductionOfPerson(Person person) {

        Assert.notNull(person, "Person to get overtime reduction for must be given.");

        Optional<BigDecimal> overtimeReduction = Optional.ofNullable(applicationDAO.calculateTotalOvertimeOfPerson(
                    person));

        if (overtimeReduction.isPresent()) {
            return overtimeReduction.get();
        }

        return BigDecimal.ZERO;
    }
}
