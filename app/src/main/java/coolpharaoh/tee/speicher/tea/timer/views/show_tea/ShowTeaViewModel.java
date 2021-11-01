package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class ShowTeaViewModel {

    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final CounterRepository counterRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private final long teaId;
    private int infusionIndex = 0;

    ShowTeaViewModel(final long teaId, final Application application) {
        this(teaId, application, new TeaRepository(application), new InfusionRepository(application),
                new CounterRepository(application), new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    ShowTeaViewModel(final long teaId, final Application application, final TeaRepository teaRepository,
                     final InfusionRepository infusionRepository, final CounterRepository counterRepository,
                     final ActualSettingsRepository actualSettingsRepository) {
        this.teaId = teaId;
        this.application = application;
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.counterRepository = counterRepository;
        this.actualSettingsRepository = actualSettingsRepository;
    }

    // Tea
    boolean teaExists() {
        return teaRepository.getTeaById(teaId) != null;
    }

    long getTeaId() {
        return teaRepository.getTeaById(teaId).getId();
    }

    String getName() {
        return teaRepository.getTeaById(teaId).getName();
    }

    String getVariety() {
        final String variety = teaRepository.getTeaById(teaId).getVariety();
        if (variety == null || "".equals(variety)) {
            return "-";
        } else {
            return Variety.convertStoredVarietyToText(variety, application);
        }
    }

    double getAmount() {
        return teaRepository.getTeaById(teaId).getAmount();
    }

    AmountKind getAmountKind() {
        final String amountKind = teaRepository.getTeaById(teaId).getAmountKind();
        return AmountKind.fromText(amountKind);
    }

    int getColor() {
        return teaRepository.getTeaById(teaId).getColor();
    }

    void setCurrentDate() {
        final Tea tea = teaRepository.getTeaById(teaId);
        tea.setDate(CurrentDate.getDate());
        teaRepository.updateTea(tea);
    }

    int getNextInfusion() {
        return teaRepository.getTeaById(teaId).getNextInfusion();
    }

    void updateNextInfusion() {
        final Tea tea = teaRepository.getTeaById(teaId);
        if ((infusionIndex + 1) >= getInfusionSize()) {
            tea.setNextInfusion(0);
        } else {
            tea.setNextInfusion(infusionIndex + 1);
        }
        teaRepository.updateTea(tea);
    }

    void resetNextInfusion() {
        final Tea tea = teaRepository.getTeaById(teaId);
        tea.setNextInfusion(0);
        teaRepository.updateTea(tea);
    }

    // Infusion
    TimeConverter getTime() {
        return new TimeConverter(infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTime());
    }

    TimeConverter getCoolDownTime() {
        return new TimeConverter(infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getCoolDownTime());
    }

    int getTemperature() {
        if (FAHRENHEIT.equals(getTemperatureUnit())) {
            return infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperatureFahrenheit();
        } else {
            return infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperatureCelsius();
        }
    }

    int getInfusionSize() {
        return infusionRepository.getInfusionsByTeaId(teaId).size();
    }

    int getInfusionIndex() {
        return infusionIndex;
    }

    void setInfusionIndex(final int infusionIndex) {
        this.infusionIndex = infusionIndex;
    }

    void incrementInfusionIndex() {
        infusionIndex++;
    }

    //Counter
    void countCounter() {
        final Counter counter = getOrCreateCounter();
        RefreshCounter.refreshCounter(counter);
        final Date currentDate = CurrentDate.getDate();
        counter.setMonthDate(currentDate);
        counter.setWeekDate(currentDate);
        counter.setDayDate(currentDate);
        counter.setOverall(counter.getOverall() + 1);
        counter.setMonth(counter.getMonth() + 1);
        counter.setWeek(counter.getWeek() + 1);
        counter.setDay(counter.getDay() + 1);
        counterRepository.updateCounter(counter);
    }

    long getOverallCounter() {
        final Counter counter = getOrCreateCounter();
        return counter.getOverall();
    }

    private Counter getOrCreateCounter() {
        Counter counter = counterRepository.getCounterByTeaId(teaId);
        if (counter == null) {
            counter = new Counter();
            counter.setTeaId(teaId);
            counter.setDay(0);
            counter.setWeek(0);
            counter.setMonth(0);
            counter.setOverall(0);
            counter.setDayDate(CurrentDate.getDate());
            counter.setWeekDate(CurrentDate.getDate());
            counter.setMonthDate(CurrentDate.getDate());
            counter.setId(counterRepository.insertCounter(counter));
        }

        return counter;
    }

    // Settings
    boolean isAnimation() {
        return actualSettingsRepository.getSettings().isAnimation();
    }

    boolean isShowteaAlert() {
        return actualSettingsRepository.getSettings().isShowTeaAlert();
    }

    void setShowteaAlert(final boolean showteaalert) {
        final ActualSettings actualSettings = actualSettingsRepository.getSettings();
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    TemperatureUnit getTemperatureUnit() {
        return TemperatureUnit.fromText(actualSettingsRepository.getSettings().getTemperatureUnit());
    }
}
