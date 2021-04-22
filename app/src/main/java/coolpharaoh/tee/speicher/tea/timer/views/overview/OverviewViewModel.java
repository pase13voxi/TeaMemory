package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class OverviewViewModel extends ViewModel {

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private final MutableLiveData<List<Tea>> teas;
    private final ActualSettings actualSettings;

    OverviewViewModel(Application application) {
        this(application, new TeaRepository(application), new InfusionRepository(application),
                new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    OverviewViewModel(Application application, TeaRepository teaRepository, InfusionRepository infusionRepository,
                      ActualSettingsRepository actualSettingsRepository) {
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.actualSettingsRepository = actualSettingsRepository;

        if (actualSettingsRepository.getCountItems() == 0) {
            createDefaultTeas(application);
            createDefaultSettings();
        }

        actualSettings = actualSettingsRepository.getSettings();
        teas = new MutableLiveData<>();

        refreshTeas();
    }

    // Defaults
    private void createDefaultTeas(Application application) {
        Tea tea1 = new Tea("Earl Grey", application.getResources().getStringArray(R.array.new_tea_variety_codes)[0], 5, "Ts", ColorConversation.getVarietyColor(0, application), 0, CurrentDate.getDate());
        long teaId1 = teaRepository.insertTea(tea1);
        Infusion infusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
        infusionRepository.insertInfusion(infusion1);

        Tea tea2 = new Tea("Pai Mu Tan", application.getResources().getStringArray(R.array.new_tea_variety_codes)[3], 4, "Ts", ColorConversation.getVarietyColor(3, application), 0, CurrentDate.getDate());
        long teaId2 = teaRepository.insertTea(tea2);
        Infusion infusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
        infusionRepository.insertInfusion(infusion2);

        Tea tea3 = new Tea("Sencha", application.getResources().getStringArray(R.array.new_tea_variety_codes)[1], 4, "Ts", ColorConversation.getVarietyColor(1, application), 0, CurrentDate.getDate());
        long teaId3 = teaRepository.insertTea(tea3);
        Infusion infusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
        infusionRepository.insertInfusion(infusion3);
    }

    private void createDefaultSettings(){
        ActualSettings defaultSettings = new ActualSettings();
        defaultSettings.setMusicChoice("content://settings/system/ringtone");
        defaultSettings.setMusicName("Default");
        defaultSettings.setVibration(true);
        defaultSettings.setAnimation(true);
        defaultSettings.setTemperatureUnit("Celsius");
        defaultSettings.setMainRateAlert(true);
        defaultSettings.setMainRateCounter(0);
        defaultSettings.setMainUpdateAlert(false);
        defaultSettings.setShowTeaAlert(true);
        defaultSettings.setSettingsPermissionAlert(true);
        defaultSettings.setSort(0);
        actualSettingsRepository.insertSettings(defaultSettings);
    }

    // Teas
    LiveData<List<Tea>> getTeas() {
        return teas;
    }

    Tea getTeaByPosition(int position) {
        return Objects.requireNonNull(teas.getValue()).get(position);
    }

    void deleteTea(int position) {
        teaRepository.deleteTea(Objects.requireNonNull(teas.getValue()).get(position));

        refreshTeas();
    }

    void visualizeTeasBySearchString(String searchString) {
        if ("".equals(searchString)) {
            refreshTeas();
        } else {
            teas.setValue(teaRepository.getTeasBySearchString(searchString));
        }
    }

    // Settings
    int getSort() {
        return actualSettings.getSort();
    }

    void setSort(int sort) {
        actualSettings.setSort(sort);
        actualSettingsRepository.updateSettings(actualSettings);

        refreshTeas();
    }

    boolean isMainRateAlert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainRateAlert(boolean mainRateAlert) {
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    int getMainRatecounter() {
        return actualSettings.getMainRateCounter();
    }

    void resetMainRatecounter() {
        actualSettings.setMainRateCounter(0);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void incrementMainRatecounter() {
        actualSettings.setMainRateCounter(actualSettings.getMainRateCounter() + 1);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isMainUpdateAlert() {
        return actualSettings.isMainUpdateAlert();
    }

    void setMainUpdateAlert(final boolean updateAlert) {
        actualSettings.setMainUpdateAlert(updateAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void refreshTeas() {
        switch (actualSettings.getSort()) {
            //activity
            case 0:
                teas.setValue(teaRepository.getTeasOrderByActivity());
                break;
            //alphabetic
            case 1:
                teas.setValue(teaRepository.getTeasOrderByAlphabetic());
                break;
            //variety
            case 2:
                teas.setValue(teaRepository.getTeasOrderByVariety());
                break;
            //rating
            case 3:
                teas.setValue(teaRepository.getTeasOrderByRating());
                break;
            default:
        }
    }
}
