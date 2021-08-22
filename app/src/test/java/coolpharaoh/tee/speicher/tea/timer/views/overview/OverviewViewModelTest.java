package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@RunWith(MockitoJUnitRunner.class)
public class OverviewViewModelTest {
    private OverviewViewModel overviewViewModel;
    @Mock
    Application application;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    SharedSettings sharedSettings;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private List<Tea> teas;
    private ActualSettings actualSettings;

    @Before
    public void setUp() {
        mockSettings();
        mockTeas();
        overviewViewModel = new OverviewViewModel(application, teaRepository, infusionRepository,
                actualSettingsRepository, sharedSettings);
    }

    private void mockSettings() {
        actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("content://settings/system/ringtone");
        actualSettings.setMusicName("Default");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Celsius");
        actualSettings.setMainRateAlert(true);
        actualSettings.setMainRateCounter(0);
        actualSettings.setShowTeaAlert(true);
        actualSettings.setSettingsPermissionAlert(true);
        actualSettings.setSort(0);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);
        when(actualSettingsRepository.getCountItems()).thenReturn(1);
    }

    private void mockTeas() {
        teas = new ArrayList<>();
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        when(teaRepository.getTeasOrderByActivity()).thenReturn(teas);
        when(teaRepository.getTeasOrderByVariety()).thenReturn(teas);
        when(teaRepository.getTeasOrderByAlphabetic()).thenReturn(teas);
        when(teaRepository.getTeasOrderByRating()).thenReturn(teas);
    }

    @Test
    public void getTeas() {
        final List<Tea> teasAfter = overviewViewModel.getTeas().getValue();
        assertThat(teasAfter).isEqualTo(teas);
    }

    @Test
    public void deleteTea() {
        final long teaId = 1;

        overviewViewModel.deleteTea(teaId);
        verify(teaRepository).deleteTeaById(teaId);
    }

    @Test
    public void showTeasBySearchString() {
        final String searchString = "search";
        overviewViewModel.visualizeTeasBySearchString(searchString);
        verify(teaRepository).getTeasBySearchString(searchString);
    }

    @Test
    public void showTeasByEmptySearchString() {
        actualSettings.setSort(0);
        final String searchString = "";

        overviewViewModel.visualizeTeasBySearchString(searchString);

        verify(teaRepository, never()).getTeasBySearchString(any());
        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity();
    }

    @Test
    public void getSort() {
        final int sort = overviewViewModel.getSort();
        assertThat(sort).isEqualTo(actualSettings.getSort());
    }

    @Test
    public void setSort() {
        final int sort = 2;
        overviewViewModel.setSort(sort);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void setOverviewHeader() {
        final boolean overviewHeader = true;
        overviewViewModel.setOverviewHeader(overviewHeader);
        verify(sharedSettings).setOverviewHeader(overviewHeader);
    }

    @Test
    public void getOverviewHeader() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);
        final boolean isOverviewHeader = overviewViewModel.isOverviewHeader();
        assertThat(isOverviewHeader).isFalse();
    }

    @Test
    public void isMainRateAlert() {
        assertThat(overviewViewModel.isMainRateAlert()).isEqualTo(actualSettings.isMainRateAlert());
    }

    @Test
    public void setMainRateAlert() {
        final boolean alert = false;
        overviewViewModel.setMainRateAlert(alert);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void getMainRatecounter() {
        final int counter = overviewViewModel.getMainRatecounter();
        assertThat(counter).isEqualTo(actualSettings.getMainRateCounter());
    }

    @Test
    public void resetMainRatecounter() {
        overviewViewModel.resetMainRatecounter();
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void incrementMainRatecounter() {
        overviewViewModel.incrementMainRatecounter();
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void isMainUpdateAlert() {
        assertThat(overviewViewModel.isMainUpdateAlert()).isEqualTo(actualSettings.isMainUpdateAlert());
    }

    @Test
    public void setMainUpdateAlert() {
        final boolean alert = true;
        overviewViewModel.setMainUpdateAlert(alert);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void refreshTeasWithSort0() {
        actualSettings.setSort(0);
        overviewViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity();
    }

    @Test
    public void refreshTeasWithSort1() {
        actualSettings.setSort(1);
        overviewViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByAlphabetic();
    }

    @Test
    public void refreshTeasWithSort2() {
        actualSettings.setSort(2);
        overviewViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByVariety();
    }

    @Test
    public void refreshTeasWithSort3() {
        actualSettings.setSort(3);
        overviewViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByRating();
    }

    @Test
    public void getSortWithHeaderIsSort3() {
        when(sharedSettings.isOverviewHeader()).thenReturn(true);
        actualSettings.setSort(3);
        overviewViewModel.refreshTeas();
        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(3);
    }

    @Test
    public void getSortWithHeaderIsFalse() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);
        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(-1);
    }

    @Test
    public void getSortWithHeaderIsInSearchMode() {
        final String searchString = "search";
        overviewViewModel.visualizeTeasBySearchString(searchString);
        assertThat(overviewViewModel.getSortWithHeader()).isEqualTo(-1);
    }

}
