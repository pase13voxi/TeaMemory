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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.*")
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
    NoteRepository noteRepository;
    @Mock
    CounterRepository counterRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private List<Tea> teas;
    private ActualSettings actualSettings;

    @Before
    public void setUp() throws Exception {
        mockResources();
        mockRepositories();
        mockSettings();
        mockTeas();
        mainActivityViewModel = new MainViewModel(application);
    }

    private void mockResources() {
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
    }

    private void mockRepositories() throws Exception {
        whenNew(TeaRepository.class).withAnyArguments().thenReturn(teaRepository);
        whenNew(InfusionRepository.class).withAnyArguments().thenReturn(infusionRepository);
        whenNew(NoteRepository.class).withAnyArguments().thenReturn(noteRepository);
        whenNew(CounterRepository.class).withAnyArguments().thenReturn(counterRepository);
        whenNew(ActualSettingsRepository.class).withAnyArguments().thenReturn(actualSettingsRepository);
    }

    private void mockSettings() {
        actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("content://settings/system/ringtone");
        actualSettings.setMusicName("Default");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Celsius");
        actualSettings.setShowTeaAlert(true);
        actualSettings.setMainRateAlert(true);
        actualSettings.setMainRateCounter(0);
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
        verify(teaRepository).getTeasBySearchString(eq(searchString));
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

}
