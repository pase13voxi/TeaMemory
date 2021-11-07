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
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class OverviewViewModel extends ViewModel {

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final SharedSettings sharedSettings;

    private final MutableLiveData<List<Tea>> teas;
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
        this.sharedSettings = sharedSettings;

        migrateActualSettingsToSharedSettings(actualSettingsRepository);

        if (sharedSettings.isFirstStart()) {
            createDefaultTeas(application);
            sharedSettings.setFactorySettings();
        }

        teas = new MutableLiveData<>();

        refreshTeas();
    }

    // Could be removed after the successful migration (In half a year 1.6.2022)
    private void migrateActualSettingsToSharedSettings(final ActualSettingsRepository actualSettingsRepository) {
        if (!sharedSettings.isMigrated() && actualSettingsRepository.getCountItems() > 0) {
            final ActualSettings settings = actualSettingsRepository.getSettings();

            sharedSettings.setFirstStart(false);
            sharedSettings.setMusicChoice(settings.getMusicChoice());
            sharedSettings.setMusicName(settings.getMusicName());
            sharedSettings.setVibration(settings.isVibration());
            sharedSettings.setAnimation(settings.isAnimation());
            sharedSettings.setTemperatureUnit(TemperatureUnit.fromText(settings.getTemperatureUnit()));
            sharedSettings.setOverviewUpdateAlert(settings.isMainUpdateAlert());
            sharedSettings.setShowTeaAlert(settings.isShowTeaAlert());
            sharedSettings.setSettingsPermissionAlert(settings.isSettingsPermissionAlert());
            sharedSettings.setSortMode(SortMode.fromChoice(settings.getSort()));
            sharedSettings.setMigrated(true);
        }
    }

    // Defaults
    private void createDefaultTeas(final Application application) {
        final Tea tea1 = new Tea("Earl Grey", BLACK_TEA.getCode(), 5, TEA_SPOON.getText(), ContextCompat.getColor(application, BLACK_TEA.getColor()), 0, CurrentDate.getDate());
        final long teaId1 = teaRepository.insertTea(tea1);
        final Infusion infusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
        infusionRepository.insertInfusion(infusion1);

        final Tea tea2 = new Tea("Pai Mu Tan", WHITE_TEA.getCode(), 4, TEA_SPOON.getText(), ContextCompat.getColor(application, WHITE_TEA.getColor()), 0, CurrentDate.getDate());
        final long teaId2 = teaRepository.insertTea(tea2);
        final Infusion infusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
        infusionRepository.insertInfusion(infusion2);

        final Tea tea3 = new Tea("Sencha", GREEN_TEA.getCode(), 4, TEA_SPOON.getText(), ContextCompat.getColor(application, GREEN_TEA.getColor()), 0, CurrentDate.getDate());
        final long teaId3 = teaRepository.insertTea(tea3);
        final Infusion infusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
        infusionRepository.insertInfusion(infusion3);
    }

    // Teas
    LiveData<List<Tea>> getTeas() {
        return teas;
    }

    void deleteTea(final long id) {
        teaRepository.deleteTeaById(id);

        refreshTeas();
    }

    boolean isTeaInStock(final long id) {
        final Tea tea = teaRepository.getTeaById(id);
        return tea.isInStock();
    }

    void updateInStockOfTea(final long id, final boolean inStock) {
        final Tea tea = teaRepository.getTeaById(id);
        tea.setInStock(inStock);
        teaRepository.updateTea(tea);

        refreshTeas();
    }

    void visualizeTeasBySearchString(final String searchString) {
        if ("".equals(searchString)) {
            refreshTeas();
        } else {
            searchMode = true;
            teas.setValue(teaRepository.getTeasBySearchString(searchString));
        }
    }

    // Settings
    SortMode getSort() {
        return sharedSettings.getSortMode();
    }

    void setSort(final SortMode sortMode) {
        sharedSettings.setSortMode(sortMode);

        refreshTeas();
    }

    boolean isOverviewHeader() {
        return sharedSettings.isOverviewHeader();
    }

    void setOverviewInStock(final boolean overviewInStock) {
        sharedSettings.setOverviewInStock(overviewInStock);

        refreshTeas();
    }

    boolean isOverViewInStock() {
        return sharedSettings.isOverviewInStock();
    }

    int getSortWithHeader() {
        return isOverviewHeader() && !searchMode ? getSort().getChoice() : -1;
    }

    boolean isOverviewUpdateAlert() {
        return sharedSettings.isOverviewUpdateAlert();
    }

    void setOverviewUpdateAlert(final boolean updateAlert) {
        sharedSettings.setOverviewUpdateAlert(updateAlert);
    }

    void refreshTeas() {
        searchMode = false;
        switch (sharedSettings.getSortMode()) {
            //activity
            case LAST_USED:
                teas.setValue(teaRepository.getTeasOrderByActivity(isOverViewInStock()));
                break;
            //alphabetic
            case ALPHABETICAL:
                teas.setValue(teaRepository.getTeasOrderByAlphabetic(isOverViewInStock()));
                break;
            //variety
            case BY_VARIETY:
                teas.setValue(teaRepository.getTeasOrderByVariety(isOverViewInStock()));
                break;
            //rating
            case RATING:
                teas.setValue(teaRepository.getTeasOrderByRating(isOverViewInStock()));
                break;
            default:
        }
    }
}
