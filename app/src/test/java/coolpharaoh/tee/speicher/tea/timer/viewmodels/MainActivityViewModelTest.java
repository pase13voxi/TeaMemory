package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;
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
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {
    private MainActivityViewModel mainActivityViewModel;
    @Mock
    TeaMemoryDatabase db;
    @Mock
    Context context;
    @Mock
    Resources resources;
    @Mock
    TeaDAO teaDAO;
    @Mock
    InfusionDAO infusionDAO;
    @Mock
    NoteDAO noteDAO;
    @Mock
    CounterDAO counterDAO;
    @Mock
    ActualSettingsDAO actualSettingsDAO;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private List<Tea> teas;
    private ActualSettings actualSettings;

    @Before
    public void setUp() {
        mockResources();
        mockDB();
        mockSettings();
        mockTeas();
        mainActivityViewModel = new MainActivityViewModel(db, context);
    }

    private void mockResources() {
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
    }

    private void mockDB() {
        when(db.getTeaDAO()).thenReturn(teaDAO);
        when(db.getInfusionDAO()).thenReturn(infusionDAO);
        when(db.getNoteDAO()).thenReturn(noteDAO);
        when(db.getCounterDAO()).thenReturn(counterDAO);
        when(db.getActualSettingsDAO()).thenReturn(actualSettingsDAO);
    }

    private void mockSettings() {
        actualSettings = new ActualSettings();
        actualSettings.setMusicchoice("content://settings/system/ringtone");
        actualSettings.setMusicname("Default");
        actualSettings.setVibration(true);
        actualSettings.setNotification(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureunit("Celsius");
        actualSettings.setShowteaalert(true);
        actualSettings.setMainratealert(true);
        actualSettings.setMainratecounter(0);
        actualSettings.setSettingspermissionalert(true);
        actualSettings.setSort(0);
        actualSettingsDAO.insert(actualSettings);
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);
    }

    private void mockTeas() {
        teas = new ArrayList<>();
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        teas.add(new Tea("name", "variety", 5, "amount", 5, 0, null));
        when(teaDAO.getTeasOrderByActivity()).thenReturn(teas);
        when(teaDAO.getTeasOrderByVariety()).thenReturn(teas);
        when(teaDAO.getTeasOrderByAlphabetic()).thenReturn(teas);
    }

    @Test
    public void getTeas() {
        List<Tea> teas = mainActivityViewModel.getTeas().getValue();
        assertThat(teas).isEqualTo(this.teas);
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
        verify(teaDAO).delete(any(Tea.class));
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
        verify(actualSettingsDAO).update(any(ActualSettings.class));
    }

    @Test
    public void isMainRateAlert() {
        assertThat(mainActivityViewModel.isMainRateAlert()).isEqualTo(actualSettings.isMainratealert());
    }

    @Test
    public void setMainRateAlert() {
        boolean alert = false;
        mainActivityViewModel.setMainRateAlert(alert);
        verify(actualSettingsDAO).update(any(ActualSettings.class));
    }

    @Test
    public void getMainRatecounter() {
        int counter = mainActivityViewModel.getMainRatecounter();
        assertThat(counter).isEqualTo(actualSettings.getMainratecounter());
    }

    @Test
    public void resetMainRatecounter() {
        mainActivityViewModel.resetMainRatecounter();
        verify(actualSettingsDAO).update(any(ActualSettings.class));
    }

    @Test
    public void incrementMainRatecounter() {
        mainActivityViewModel.incrementMainRatecounter();
        verify(actualSettingsDAO).update(any(ActualSettings.class));
    }

    @Test
    public void refreshTeasWithSort0() {
        actualSettings.setSort(0);
        mainActivityViewModel.refreshTeas();
        verify(teaDAO, atLeastOnce()).getTeasOrderByActivity();
    }

    @Test
    public void refreshTeasWithSort1() {
        actualSettings.setSort(1);
        mainActivityViewModel.refreshTeas();
        verify(teaDAO, atLeastOnce()).getTeasOrderByAlphabetic();
    }

    @Test
    public void refreshTeasWithSort2() {
        actualSettings.setSort(2);
        mainActivityViewModel.refreshTeas();
        verify(teaDAO, atLeastOnce()).getTeasOrderByVariety();
    }

}
