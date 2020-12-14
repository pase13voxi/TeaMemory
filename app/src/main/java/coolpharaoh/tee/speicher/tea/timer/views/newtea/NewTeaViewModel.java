package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class NewTeaViewModel {
    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final CounterRepository counterRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private Tea tea;
    private List<Infusion> infusions;

    private int infusionIndex = 0;

    NewTeaViewModel(Application application) {
        this(null, application, new TeaRepository(application),
                new InfusionRepository(application), new CounterRepository(application),
                new ActualSettingsRepository(application));
    }

    NewTeaViewModel(long teaId, Application application) {
        this(teaId, application, new TeaRepository(application),
                new InfusionRepository(application), new CounterRepository(application),
                new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    NewTeaViewModel(Long teaId, Application application, TeaRepository teaRepository,
                    InfusionRepository infusionRepository, CounterRepository counterRepository,
                    ActualSettingsRepository actualSettingsRepository) {
        this.application = application;
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.counterRepository = counterRepository;
        this.actualSettingsRepository = actualSettingsRepository;

        initializeTeaAndInfusions(teaId);
    }

    private void initializeTeaAndInfusions(Long teaId) {
        if (teaId == null) {
            tea = new Tea();
            infusions = new ArrayList<>();
            addInfusion();
        } else {
            tea = teaRepository.getTeaById(teaId);
            infusions = infusionRepository.getInfusionsByTeaId(teaId);
        }
    }

    // Tea
    long getTeaId() {
        return tea.getId();
    }

    String getName() {
        return tea.getName();
    }

    String getVariety() {
        return tea.getVariety();
    }

    int getAmount() {
        return tea.getAmount();
    }

    String getAmountkind() {
        return tea.getAmountKind();
    }

    int getColor() {
        return tea.getColor();
    }

    // Infusion
    void addInfusion() {
        Infusion infusion = new Infusion();
        infusion.setTemperatureCelsius(-500);
        infusion.setTemperatureFahrenheit(-500);
        infusions.add(infusion);
        if (getInfusionSize() > 1) {
            infusionIndex++;
        }
    }

    void takeInfusionInformation(String time, String cooldowntime, int temperature) {
        infusions.get(infusionIndex).setTime(time);
        infusions.get(infusionIndex).setCoolDownTime(cooldowntime);
        if ("Fahrenheit".equals(actualSettingsRepository.getSettings().getTemperatureUnit())) {
            infusions.get(infusionIndex).setTemperatureFahrenheit(temperature);
            infusions.get(infusionIndex).setTemperatureCelsius(TemperatureConversation.fahrenheitToCelsius(temperature));
        } else {
            infusions.get(infusionIndex).setTemperatureCelsius(temperature);
            infusions.get(infusionIndex).setTemperatureFahrenheit(TemperatureConversation.celsiusToFahrenheit(temperature));
        }
    }

    void deleteInfusion() {
        if (getInfusionSize() > 1) {
            infusions.remove(infusionIndex);
            if (infusionIndex == getInfusionSize()) {
                infusionIndex--;
            }
        }
    }

    String getInfusionTime() {
        return infusions.get(infusionIndex).getTime();
    }

    String getInfusionCooldowntime() {
        return infusions.get(infusionIndex).getCoolDownTime();
    }

    int getInfusionTemperature() {
        if ("Fahrenheit".equals(getTemperatureunit())) {
            return infusions.get(infusionIndex).getTemperatureFahrenheit();
        } else {
            return infusions.get(infusionIndex).getTemperatureCelsius();
        }
    }

    void previousInfusion() {
        if (infusionIndex - 1 >= 0) {
            infusionIndex--;
        }
    }

    void nextInfusion() {
        if (infusionIndex + 1 < getInfusionSize()) {
            infusionIndex++;
        }
    }

    int getInfusionIndex() {
        return infusionIndex;
    }

    int getInfusionSize() {
        return infusions.size();
    }

    // Settings
    String getTemperatureunit() {
        return actualSettingsRepository.getSettings().getTemperatureUnit();
    }

    // Overall
    void editTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);

        teaRepository.updateTea(tea);

        setInfusionInformation(tea.getId());
    }

    void createNewTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);
        tea.setNextInfusion(0);

        long teaId = teaRepository.insertTea(tea);

        setInfusionInformation(teaId);

        // create new counter
        Counter counter = new Counter();
        counter.setTeaId(teaId);
        counter.setDay(0);
        counter.setWeek(0);
        counter.setMonth(0);
        counter.setOverall(0);
        counter.setDayDate(CurrentDate.getDate());
        counter.setWeekDate(CurrentDate.getDate());
        counter.setMonthDate(CurrentDate.getDate());

        counterRepository.insertCounter(counter);
    }

    void setTeaInformation(String name, String variety, int amount, String amountKind, int color) {
        tea.setName(name);
        tea.setVariety(LanguageConversation.convertVarietyToCode(variety, application));
        tea.setAmount(amount);
        tea.setAmountKind(amountKind);
        tea.setColor(color);
        tea.setDate(CurrentDate.getDate());
    }

    void setInfusionInformation(long teaId) {
        infusionRepository.deleteInfusionsByTeaId(teaId);
        for (int i = 0; i < getInfusionSize(); i++) {
            infusions.get(i).setTeaId(teaId);
            infusions.get(i).setInfusionIndex(i);
            infusionRepository.insertInfusion(infusions.get(i));
        }
    }
}