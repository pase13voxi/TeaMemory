package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class NewTeaViewModel {
    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private MutableLiveData<Integer> infusionIndex;
    private Tea tea;
    private List<Infusion> infusions;

    NewTeaViewModel(Application application) {
        this(null, application, new TeaRepository(application),
                new InfusionRepository(application), new ActualSettingsRepository(application));
    }

    NewTeaViewModel(long teaId, Application application) {
        this(teaId, application, new TeaRepository(application),
                new InfusionRepository(application), new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    NewTeaViewModel(Long teaId, Application application, TeaRepository teaRepository,
                    InfusionRepository infusionRepository,
                    ActualSettingsRepository actualSettingsRepository) {
        this.application = application;
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.actualSettingsRepository = actualSettingsRepository;

        initializeTeaAndInfusions(teaId);
    }

    private void initializeTeaAndInfusions(Long teaId) {
        infusionIndex = new MutableLiveData<>(0);
        if (teaId == null) {
            tea = new Tea();
            tea.setVariety("01_black");
            tea.setAmount(-500);
            tea.setAmountKind("Ts");
            tea.setRating(0);
            tea.setFavorite(false);
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
        return LanguageConversation.convertCodeToVariety(tea.getVariety(), application);
    }

    void setVariety(final String variety) {
        tea.setVariety(LanguageConversation.convertVarietyToCode(variety, application));
        signalDataChanged();
    }

    void setAmount(final int amount, final String amountKind) {
        tea.setAmount(amount);
        tea.setAmountKind(amountKind);
        signalDataChanged();
    }

    int getAmount() {
        return tea.getAmount();
    }

    String getAmountKind() {
        return tea.getAmountKind();
    }

    int getColor() {
        return tea.getColor();
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
        } else {
            infusions.get(getInfusionIndex()).setTemperatureCelsius(temperature);
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
    String getTemperatureUnit() {
        return actualSettingsRepository.getSettings().getTemperatureUnit();
    }

    // Overall
    void saveTea(final String name, final int color) {
        // if id is null a new Tea will be created
        if (tea.getId() == null) {
            insertTea(name, color);
        } else {
            updateTea(name, color);
        }
    }

    private void updateTea(final String name, final int color) {
        setTeaInformation(name, color);

        teaRepository.updateTea(tea);

        saveInfusions(tea.getId());
    }

    private void insertTea(final String name, final int color) {
        setTeaInformation(name, color);
        tea.setNextInfusion(0);

        final long teaId = teaRepository.insertTea(tea);

        saveInfusions(teaId);
    }

    private void setTeaInformation(final String name, final int color) {
        tea.setName(name);
        tea.setColor(color);
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
        return "Fahrenheit".equals(getTemperatureUnit());
    }

    private void signalDataChanged() {
        infusionIndex.setValue(getInfusionIndex());
    }
}