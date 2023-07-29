package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToFahrenheit;
import static coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.fahrenheitToCelsius;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.FAHRENHEIT;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class NewTeaViewModel {
    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final SharedSettings sharedSettings;

    private MutableLiveData<Integer> infusionIndex;
    private Tea tea;
    private List<Infusion> infusions;

    NewTeaViewModel(final Application application) {
        this(null, application, new TeaRepository(application),
                new InfusionRepository(application), new SharedSettings(application));
    }

    NewTeaViewModel(final long teaId, final Application application) {
        this(teaId, application, new TeaRepository(application),
                new InfusionRepository(application), new SharedSettings(application));
    }

    @VisibleForTesting
    NewTeaViewModel(final Long teaId, final Application application, final TeaRepository teaRepository,
                    final InfusionRepository infusionRepository,
                    final SharedSettings sharedSettings) {
        this.application = application;
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.sharedSettings = sharedSettings;

        initializeTeaAndInfusions(teaId);
    }

    private void initializeTeaAndInfusions(final Long teaId) {
        infusionIndex = new MutableLiveData<>(0);
        if (teaId == null) {
            tea = new Tea();
            tea.setVariety("01_black");
            tea.setColor(-15461296);
            tea.setAmount(-500);
            tea.setAmountKind("Ts");
            tea.setRating(0);
            tea.setInStock(true);
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

    String getVarietyAsText() {
        return Variety.convertStoredVarietyToText(tea.getVariety(), application);
    }

    void setVariety(final String variety) {
        tea.setVariety(Variety.convertTextToStoredVariety(variety, application));
        signalDataChanged();
    }

    Variety getVariety() {
        return Variety.fromStoredText(tea.getVariety());
    }

    void setAmount(final double amount, final AmountKind amountKind) {
        tea.setAmount(amount);
        tea.setAmountKind(amountKind.getText());
        signalDataChanged();
    }

    double getAmount() {
        return tea.getAmount();
    }

    AmountKind getAmountKind() {
        return AmountKind.fromText(tea.getAmountKind());
    }

    int getColor() {
        return tea.getColor();
    }

    void setColor(final int color) {
        tea.setColor(color);
        signalDataChanged();
    }

    // Infusion
    LiveData<Integer> dataChanges() {
        return infusionIndex;
    }

    int getInfusionIndex() {
        if (infusionIndex != null) {
            return infusionIndex.getValue();
        } else {
            infusionIndex = new MutableLiveData<>(0);
            return infusionIndex.getValue();
        }
    }

    void addInfusion() {
        final Infusion infusion = new Infusion();
        infusion.setTemperatureCelsius(-500);
        infusion.setTemperatureFahrenheit(-500);
        infusions.add(infusion);
        if (getInfusionSize() > 1) {
            infusionIndex.setValue(getInfusionIndex() + 1);
        }
        signalDataChanged();
    }

    void deleteInfusion() {
        if (getInfusionSize() > 1) {
            infusions.remove(getInfusionIndex());
            if (getInfusionIndex() == getInfusionSize()) {
                infusionIndex.setValue(getInfusionIndex() - 1);
            }
            signalDataChanged();
        }
    }

    void previousInfusion() {
        if (getInfusionIndex() - 1 >= 0) {
            infusionIndex.setValue(getInfusionIndex() - 1);
        }
    }

    void nextInfusion() {
        if (getInfusionIndex() + 1 < getInfusionSize()) {
            infusionIndex.setValue(getInfusionIndex() + 1);
        }
    }

    int getInfusionSize() {
        return infusions.size();
    }

    void setInfusionTemperature(final int temperature) {
        if (isFahrenheit()) {
            infusions.get(getInfusionIndex()).setTemperatureFahrenheit(temperature);
            infusions.get(getInfusionIndex()).setTemperatureCelsius(fahrenheitToCelsius(temperature));
        } else {
            infusions.get(getInfusionIndex()).setTemperatureCelsius(temperature);
            infusions.get(getInfusionIndex()).setTemperatureFahrenheit(celsiusToFahrenheit(temperature));
        }
        signalDataChanged();
    }

    int getInfusionTemperature() {
        if (isFahrenheit()) {
            return infusions.get(getInfusionIndex()).getTemperatureFahrenheit();
        } else {
            return infusions.get(getInfusionIndex()).getTemperatureCelsius();
        }
    }

    void setInfusionTime(final String time) {
        infusions.get(getInfusionIndex()).setTime(time);
        signalDataChanged();
    }

    String getInfusionTime() {
        return infusions.get(getInfusionIndex()).getTime();
    }

    void setInfusionCoolDownTime(final String time) {
        infusions.get(getInfusionIndex()).setCoolDownTime(time);
        signalDataChanged();
    }

    void resetInfusionCoolDownTime() {
        infusions.get(getInfusionIndex()).setCoolDownTime(null);
    }

    String getInfusionCoolDownTime() {
        return infusions.get(getInfusionIndex()).getCoolDownTime();
    }

    // Settings
    TemperatureUnit getTemperatureUnit() {
        return sharedSettings.getTemperatureUnit();
    }

    // Overall
    void saveTea(final String name) {
        // if id is null a new Tea will be created
        if (tea.getId() == null) {
            insertTea(name);
        } else {
            updateTea(name);
        }
    }

    private void updateTea(final String name) {
        setTeaInformation(name);

        teaRepository.updateTea(tea);

        saveInfusions(tea.getId());
    }

    private void insertTea(final String name) {
        setTeaInformation(name);
        tea.setNextInfusion(0);

        final long teaId = teaRepository.insertTea(tea);

        saveInfusions(teaId);
    }

    private void setTeaInformation(final String name) {
        tea.setName(name);
        tea.setDate(CurrentDate.getDate());
    }

    private void saveInfusions(final long teaId) {
        infusionRepository.deleteInfusionsByTeaId(teaId);
        for (int i = 0; i < getInfusionSize(); i++) {
            infusions.get(i).setTeaId(teaId);
            infusions.get(i).setInfusionIndex(i);
            infusionRepository.insertInfusion(infusions.get(i));
        }
    }

    private boolean isFahrenheit() {
        return FAHRENHEIT.equals(getTemperatureUnit());
    }

    private void signalDataChanged() {
        infusionIndex.setValue(getInfusionIndex());
    }
}