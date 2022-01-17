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

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode;
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
                new SharedSettings(application));
    }

    @VisibleForTesting
    OverviewViewModel(final Application application, final TeaRepository teaRepository, final InfusionRepository infusionRepository,
                      final SharedSettings sharedSettings) {
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.sharedSettings = sharedSettings;

        if (sharedSettings.isFirstStart()) {
            createDefaultTeas(application);
            sharedSettings.setFirstStart(false);
            sharedSettings.setOverviewUpdateAlert(false);
        }

        teas = new MutableLiveData<>();

        refreshTeas();
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
