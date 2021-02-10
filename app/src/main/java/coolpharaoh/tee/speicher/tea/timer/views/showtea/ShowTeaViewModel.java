package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class ShowTeaViewModel {

    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final CounterRepository counterRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private final long teaId;
    private int infusionIndex = 0;

    ShowTeaViewModel(long teaId, Application application) {
        this(teaId, application, new TeaRepository(application), new InfusionRepository(application),
                new CounterRepository(application), new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    ShowTeaViewModel(long teaId, Application application, TeaRepository teaRepository,
                     InfusionRepository infusionRepository, CounterRepository counterRepository,
                     ActualSettingsRepository actualSettingsRepository) {
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
        if ("".equals(teaRepository.getTeaById(teaId).getVariety())) {
            return "-";
        } else {
            return LanguageConversation.convertCodeToVariety(teaRepository.getTeaById(teaId).getVariety(), application);
        }
    }

    int getAmount() {
        return teaRepository.getTeaById(teaId).getAmount();
    }

    String getAmountKind() {
        return teaRepository.getTeaById(teaId).getAmountKind();
    }

    int getColor() {
        return teaRepository.getTeaById(teaId).getColor();
    }

    void setCurrentDate() {
        Tea tea = teaRepository.getTeaById(teaId);
        tea.setDate(CurrentDate.getDate());
        teaRepository.updateTea(tea);
    }

    int getNextInfusion() {
        return teaRepository.getTeaById(teaId).getNextInfusion();
    }

    void updateNextInfusion() {
        Tea tea = teaRepository.getTeaById(teaId);
        if ((infusionIndex + 1) >= getInfusionSize()) {
            tea.setNextInfusion(0);
        } else {
            tea.setNextInfusion(infusionIndex + 1);
        }
        teaRepository.updateTea(tea);
    }

    void resetNextInfusion() {
        Tea tea = teaRepository.getTeaById(teaId);
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
        if ("Fahrenheit".equals(getTemperatureunit())) {
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

    void setInfusionIndex(int infusionIndex) {
        this.infusionIndex = infusionIndex;
    }

    void incrementInfusionIndex() {
        infusionIndex++;
    }

    //Counter
    void countCounter() {
        Counter counter = getOrCreateCounter();
        RefreshCounter.refreshCounter(counter);
        Date currentDate = CurrentDate.getDate();
        counter.setMonthDate(currentDate);
        counter.setWeekDate(currentDate);
        counter.setDayDate(currentDate);
        counter.setOverall(counter.getOverall() + 1);
        counter.setMonth(counter.getMonth() + 1);
        counter.setWeek(counter.getWeek() + 1);
        counter.setDay(counter.getDay() + 1);
        counterRepository.updateCounter(counter);
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

    void setShowteaAlert(boolean showteaalert) {
        ActualSettings actualSettings = actualSettingsRepository.getSettings();
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getTemperatureunit() {
        return actualSettingsRepository.getSettings().getTemperatureUnit();
    }
}
