package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.BY_VARIETY;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.RATING;

import android.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@ExtendWith({MockitoExtension.class, TaskExecutorExtension.class})
class OverviewViewModelTest {
    private OverviewViewModel overviewViewModel;
    @Mock
    Application application;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    SharedSettings sharedSettings;

    @BeforeEach
    void setUp() {
        mockSettings();
    }

    private void mockSettings() {
        when(sharedSettings.isFirstStart()).thenReturn(false);
        when(sharedSettings.getSortMode()).thenReturn(LAST_USED);
    }

    @Test
    void getTeas() {
        final List<Tea> teas = mockTeas(LAST_USED);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        final List<Tea> teasAfter = overviewViewModel.getTeas().getValue();
        assertThat(teasAfter).isEqualTo(teas);
    }

    @Test
    void getTeaById() {
        final long teaId = 1;

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.getTeaBy(teaId);
        verify(teaRepository).getTeaById(teaId);
    }

    @Test
    void deleteTea() {
        final long teaId = 1;

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.deleteTea(teaId);
        verify(teaRepository).deleteTeaById(teaId);
    }

    @Test
    void isTeaInStock() {
        final long teaId = 1;
        final Tea tea = new Tea();
        tea.setInStock(true);
        when(teaRepository.getTeaById(teaId)).thenReturn(tea);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        assertThat(overviewViewModel.isTeaInStock(teaId)).isTrue();
    }

    @Test
    void updateInStockOfTea() {
        final long teaId = 1;
        when(teaRepository.getTeaById(teaId)).thenReturn(new Tea());

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.updateInStockOfTea(teaId, true);
        verify(teaRepository).updateTea(any(Tea.class));
    }

    @Test
    void showTeasBySearchString() {
        final String searchString = "search";

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.visualizeTeasBySearchString(searchString);

        verify(teaRepository).getTeasBySearchString(searchString);
    }

    @Test
    void showTeasByEmptySearchString() {
        final String searchString = "";

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.visualizeTeasBySearchString(searchString);

        verify(teaRepository, never()).getTeasBySearchString(any());
        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity(false);
    }

    @Test
    void getSort() {
        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        final SortMode sort = overviewViewModel.getSort();

        assertThat(sort).isEqualTo(LAST_USED);
    }

    @Test
    void setSort() {
        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.setSort(BY_VARIETY);

        verify(sharedSettings).setSortMode(BY_VARIETY);
    }

    @Test
    void isOverviewHeader() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        final boolean isOverviewHeader = overviewViewModel.isOverviewHeader();

        assertThat(isOverviewHeader).isFalse();
    }

    @Test
    void setOverviewFavorites() {
        final boolean overviewFavorites = true;

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.setOverviewInStock(overviewFavorites);

        verify(sharedSettings).setOverviewInStock(overviewFavorites);
    }

    @Test
    void isOverviewFavorites() {
        when(sharedSettings.isOverviewInStock()).thenReturn(false);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        final boolean overViewFavorites = overviewViewModel.isOverViewInStock();

        assertThat(overViewFavorites).isFalse();
    }

    @Test
    void isMainUpdateAlert() {
        final boolean overviewUpdateAlert = true;
        when(sharedSettings.isOverviewUpdateAlert()).thenReturn(overviewUpdateAlert);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        assertThat(overviewViewModel.isOverviewUpdateAlert()).isEqualTo(overviewUpdateAlert);
    }

    @Test
    void setOverviewUpdateAlert() {
        final boolean alert = true;

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.setOverviewUpdateAlert(alert);

        verify(sharedSettings).setOverviewUpdateAlert(alert);
    }

    @Test
    void refreshTeasWithSort0() {
        when(sharedSettings.getSortMode()).thenReturn(LAST_USED);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity(false);
    }

    @Test
    void refreshTeasWithSort0OnlyFavorites() {
        when(sharedSettings.isOverviewInStock()).thenReturn(true);
        when(sharedSettings.getSortMode()).thenReturn(LAST_USED);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity(true);
    }

    @Test
    void refreshTeasWithSort1() {
        when(sharedSettings.getSortMode()).thenReturn(ALPHABETICAL);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByAlphabetic(false);
    }

    @Test
    void refreshTeasWithSort1OnlyFavorites() {
        when(sharedSettings.isOverviewInStock()).thenReturn(true);
        when(sharedSettings.getSortMode()).thenReturn(ALPHABETICAL);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByAlphabetic(true);
    }

    @Test
    void refreshTeasWithSort2() {
        when(sharedSettings.getSortMode()).thenReturn(BY_VARIETY);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByVariety(false);
    }

    @Test
    void refreshTeasWithSort2OnlyFavorites() {
        when(sharedSettings.isOverviewInStock()).thenReturn(true);
        when(sharedSettings.getSortMode()).thenReturn(BY_VARIETY);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByVariety(true);
    }

    @Test
    void refreshTeasWithSort3() {
        when(sharedSettings.getSortMode()).thenReturn(RATING);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByRating(false);
    }

    @Test
    void refreshTeasWithSort3OnlyFavorites() {
        when(sharedSettings.isOverviewInStock()).thenReturn(true);
        when(sharedSettings.getSortMode()).thenReturn(RATING);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        verify(teaRepository, atLeastOnce()).getTeasOrderByRating(true);
    }

    @Test
    void getSortWithHeaderIsSort3() {
        mockTeas(RATING);
        when(sharedSettings.isOverviewHeader()).thenReturn(true);
        when(sharedSettings.getSortMode()).thenReturn(RATING);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.refreshTeas();

        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(3);
    }

    @Test
    void getSortWithHeaderIsFalse() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(-1);
    }

    @Test
    void getRandomTeaInStock() {
        final Tea tea = new Tea();
        when(teaRepository.getRandomTeaInStock()).thenReturn(tea);

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        final Tea randomTea = overviewViewModel.getRandomTeaInStock();

        assertThat(randomTea).isEqualTo(tea);
    }

    @Test
    void getSortWithHeaderIsInSearchMode() {
        final String searchString = "search";

        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings);
        overviewViewModel.visualizeTeasBySearchString(searchString);

        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(-1);
    }

    private List<Tea> mockTeas(final SortMode sortMode) {
        final List<Tea> teas = new ArrayList<>();
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        switch (sortMode) {
            case LAST_USED:
                when(teaRepository.getTeasOrderByActivity(false)).thenReturn(teas);
                break;
            case BY_VARIETY:
                when(teaRepository.getTeasOrderByVariety(false)).thenReturn(teas);
                break;
            case ALPHABETICAL:
                when(teaRepository.getTeasOrderByAlphabetic(false)).thenReturn(teas);
                break;
            case RATING:
                when(teaRepository.getTeasOrderByRating(false)).thenReturn(teas);
                break;
        }
        return teas;
    }
}
