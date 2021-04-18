package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.app.Application;
import android.content.res.Resources;

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

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {
    private MainViewModel mainActivityViewModel;
    @Mock
    Application application;
    @Mock
    Resources resources;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private List<Tea> teas;
    private ActualSettings actualSettings;

    @Before
    public void setUp() {
        mockResources();
        mockSettings();
        mockTeas();
        mainActivityViewModel = new MainViewModel(application, teaRepository, infusionRepository,
                actualSettingsRepository);
    }

    private void mockResources() {
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_codes)).thenReturn(varietyCodes);
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
        List<Tea> teasAfter = mainActivityViewModel.getTeas().getValue();
        assertThat(teasAfter).isEqualTo(teas);
    }

    @Test
    public void getTeaByPosition() {
        int position = 1;
        Tea tea = mainActivityViewModel.getTeaByPosition(position);
        assertThat(tea).isEqualToComparingFieldByField(teas.get(position));
    }

    @Test
    public void deleteTea() {
        int position = 1;
        mainActivityViewModel.deleteTea(position);
        verify(teaRepository).deleteTea(any(Tea.class));
    }

    @Test
    public void showTeasBySearchString() {
        String searchString = "search";
        mainActivityViewModel.visualizeTeasBySearchString(searchString);
        verify(teaRepository).getTeasBySearchString(searchString);
    }

    @Test
    public void showTeasByEmptySearchString() {
        actualSettings.setSort(0);
        String searchString = "";

        mainActivityViewModel.visualizeTeasBySearchString(searchString);

        verify(teaRepository, never()).getTeasBySearchString(any());
        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity();
    }

    @Test
    public void getSort() {
        int sort = mainActivityViewModel.getSort();
        assertThat(sort).isEqualTo(actualSettings.getSort());
    }

    @Test
    public void setSort() {
        int sort = 2;
        mainActivityViewModel.setSort(sort);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void isMainRateAlert() {
        assertThat(mainActivityViewModel.isMainRateAlert()).isEqualTo(actualSettings.isMainRateAlert());
    }

    @Test
    public void setMainRateAlert() {
        boolean alert = false;
        mainActivityViewModel.setMainRateAlert(alert);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void getMainRatecounter() {
        int counter = mainActivityViewModel.getMainRatecounter();
        assertThat(counter).isEqualTo(actualSettings.getMainRateCounter());
    }

    @Test
    public void resetMainRatecounter() {
        mainActivityViewModel.resetMainRatecounter();
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void incrementMainRatecounter() {
        mainActivityViewModel.incrementMainRatecounter();
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void isMainUpdateAlert() {
        assertThat(mainActivityViewModel.isMainUpdateAlert()).isEqualTo(actualSettings.isMainUpdateAlert());
    }

    @Test
    public void setMainUpdateAlert() {
        boolean alert = true;
        mainActivityViewModel.setMainUpdateAlert(alert);
        verify(actualSettingsRepository).updateSettings(any(ActualSettings.class));
    }

    @Test
    public void refreshTeasWithSort0() {
        actualSettings.setSort(0);
        mainActivityViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByActivity();
    }

    @Test
    public void refreshTeasWithSort1() {
        actualSettings.setSort(1);
        mainActivityViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByAlphabetic();
    }

    @Test
    public void refreshTeasWithSort2() {
        actualSettings.setSort(2);
        mainActivityViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByVariety();
    }

    @Test
    public void refreshTeasWithSort3() {
        actualSettings.setSort(3);
        mainActivityViewModel.refreshTeas();
        verify(teaRepository, atLeastOnce()).getTeasOrderByRating();
    }

}
