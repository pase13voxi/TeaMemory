package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.GREEN_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.WHITE_TEA;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class OverviewViewModel extends ViewModel {

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final ActualSettingsRepository actualSettingsRepository;
    private final SharedSettings sharedSettings;

    private final MutableLiveData<List<Tea>> teas;
    private final ActualSettings actualSettings;
    private boolean searchMode = false;

    OverviewViewModel(final Application application) {
        this(application, new TeaRepository(application), new InfusionRepository(application),
                new ActualSettingsRepository(application), new SharedSettings(application));
    }

    @VisibleForTesting
    OverviewViewModel(final Application application, final TeaRepository teaRepository, final InfusionRepository infusionRepository,
                      final ActualSettingsRepository actualSettingsRepository, final SharedSettings sharedSettings) {
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.actualSettingsRepository = actualSettingsRepository;
        this.sharedSettings = sharedSettings;

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
        Tea tea1 = new Tea("Earl Grey", BLACK_TEA.getCode(), 5, TEA_SPOON.getText(), ContextCompat.getColor(application, BLACK_TEA.getColor()), 0, CurrentDate.getDate());
        long teaId1 = teaRepository.insertTea(tea1);
        Infusion infusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
        infusionRepository.insertInfusion(infusion1);

        Tea tea2 = new Tea("Pai Mu Tan", WHITE_TEA.getCode(), 4, TEA_SPOON.getText(), ContextCompat.getColor(application, WHITE_TEA.getColor()), 0, CurrentDate.getDate());
        long teaId2 = teaRepository.insertTea(tea2);
        Infusion infusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
        infusionRepository.insertInfusion(infusion2);

        Tea tea3 = new Tea("Sencha", GREEN_TEA.getCode(), 4, TEA_SPOON.getText(), ContextCompat.getColor(application, GREEN_TEA.getColor()), 0, CurrentDate.getDate());
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

    void deleteTea(long id) {
        teaRepository.deleteTeaById(id);

        refreshTeas();
    }

    void visualizeTeasBySearchString(String searchString) {
        if ("".equals(searchString)) {
            refreshTeas();
        } else {
            searchMode = true;
            teas.setValue(teaRepository.getTeasBySearchString(searchString));
        }
    }

    // Settings
    SortMode getSort() {
        return SortMode.fromIndex(actualSettings.getSort());
    }

    void setSort(SortMode sortMode) {
        actualSettings.setSort(sortMode.getIndex());
        actualSettingsRepository.updateSettings(actualSettings);

        refreshTeas();
    }

    void setOverviewHeader(final boolean overviewHeader) {
        sharedSettings.setOverviewHeader(overviewHeader);
    }

    boolean isOverviewHeader() {
        return sharedSettings.isOverviewHeader();
    }

    int getSortWithHeader() {
        return isOverviewHeader() && !searchMode ? getSort().getIndex() : -1;
    }

    boolean isMainRateAlert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainRateAlert(final boolean mainRateAlert) {
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
        searchMode = false;
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
