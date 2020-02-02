package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewTeaViewModelTest {
    private NewTeaViewModel newTeaViewModelEmpty;
    private NewTeaViewModel newTeaViewModelFilled;

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
    @Mock
    TeaMemoryDatabase db;

    private static final long TEA_ID_FILLED = 1L;
    private Tea tea;
    private List<Infusion> infusions;

    @Before
    public void setUp() {
        mockDB();
        mockTea();
        newTeaViewModelEmpty = new NewTeaViewModel(db, context);
        newTeaViewModelFilled = new NewTeaViewModel(TEA_ID_FILLED, db, context);
    }

    private void mockDB() {
        when(db.getTeaDAO()).thenReturn(teaDAO);
        when(db.getInfusionDAO()).thenReturn(infusionDAO);
        when(db.getNoteDAO()).thenReturn(noteDAO);
        when(db.getCounterDAO()).thenReturn(counterDAO);
        when(db.getActualSettingsDAO()).thenReturn(actualSettingsDAO);
    }

    private void mockTea() {
        Date today = Date.from(Instant.now());
        tea = new Tea("TEA", "03_yellow", 3, "ts", 5, 0, today);
        tea.setId(TEA_ID_FILLED);
        when(teaDAO.getTeaById(TEA_ID_FILLED)).thenReturn(tea);

        infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(TEA_ID_FILLED, 0, "2", "0:30", 5, 5);
        infusions.add(infusion1);
        Infusion infusion2 = new Infusion(TEA_ID_FILLED, 1, "4", "1", 50, 100);
        infusions.add(infusion2);
        when(infusionDAO.getInfusionsByTeaId(TEA_ID_FILLED)).thenReturn(infusions);
    }

    @Test
    public void navigateBetweenInfusions() {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureunit("Celsius");
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertEquals(2, newTeaViewModelFilled.getInfusionSize());
        assertEquals(0, newTeaViewModelFilled.getInfusionIndex());

        assertEquals(infusions.get(0).getTime(), newTeaViewModelFilled.getInfusionTime());
        assertEquals(infusions.get(0).getCooldowntime(), newTeaViewModelFilled.getInfusionCooldowntime());
        assertEquals(infusions.get(0).getTemperaturecelsius(), newTeaViewModelFilled.getInfusionTemperature());

        newTeaViewModelFilled.nextInfusion();

        assertEquals(1, newTeaViewModelFilled.getInfusionIndex());

        actualSettings.setTemperatureunit("Fahrenheit");

        assertEquals(infusions.get(1).getTime(), newTeaViewModelFilled.getInfusionTime());
        assertEquals(infusions.get(1).getCooldowntime(), newTeaViewModelFilled.getInfusionCooldowntime());
        assertEquals(infusions.get(1).getTemperaturefahrenheit(), newTeaViewModelFilled.getInfusionTemperature());

        newTeaViewModelFilled.previousInfusion();

        assertEquals(0, newTeaViewModelFilled.getInfusionIndex());

        assertEquals(infusions.get(0).getTime(), newTeaViewModelFilled.getInfusionTime());
        assertEquals(infusions.get(0).getCooldowntime(), newTeaViewModelFilled.getInfusionCooldowntime());
        assertEquals(infusions.get(0).getTemperaturecelsius(), newTeaViewModelFilled.getInfusionTemperature());
    }

    @Test
    public void takeInfusionInformation() {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureunit("Celsius");
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertEquals(infusions.get(0).getTime(), newTeaViewModelFilled.getInfusionTime());
        assertEquals(infusions.get(0).getCooldowntime(), newTeaViewModelFilled.getInfusionCooldowntime());
        assertEquals(infusions.get(0).getTemperaturecelsius(), newTeaViewModelFilled.getInfusionTemperature());

        String newTime1 = "15";
        String newCooldowntime1 = "15";
        int newCelsiusTemperature1 = 0;
        int newFahrenheitTemperature1 = 32;
        newTeaViewModelFilled.takeInfusionInformation(newTime1, newCooldowntime1, newCelsiusTemperature1);

        assertEquals(newTime1, newTeaViewModelFilled.getInfusionTime());
        assertEquals(newCooldowntime1, newTeaViewModelFilled.getInfusionCooldowntime());
        assertEquals(newCelsiusTemperature1, newTeaViewModelFilled.getInfusionTemperature());

        actualSettings.setTemperatureunit("Fahrenheit");

        assertEquals(newFahrenheitTemperature1, newTeaViewModelFilled.getInfusionTemperature());

        int newFahrenheitTemperature2 = 212;
        int newCelsiusTemperature2 = 100;
        newTeaViewModelFilled.takeInfusionInformation(newTime1, newCooldowntime1, newFahrenheitTemperature2);

        assertEquals(newFahrenheitTemperature2, newTeaViewModelFilled.getInfusionTemperature());

        actualSettings.setTemperatureunit("Celsius");

        assertEquals(newCelsiusTemperature2, newTeaViewModelFilled.getInfusionTemperature());
    }

    @Test
    public void addAndRemoveInfusion() {
        assertEquals(1, newTeaViewModelEmpty.getInfusionSize());

        newTeaViewModelEmpty.addInfusion();

        assertEquals(2, newTeaViewModelEmpty.getInfusionSize());

        newTeaViewModelEmpty.deleteInfusion();

        assertEquals(1, newTeaViewModelEmpty.getInfusionSize());
    }

    @Test
    public void editTea() {
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        assertEquals(tea.getId().longValue(), newTeaViewModelFilled.getTeaId());
        assertEquals(tea.getName(), newTeaViewModelFilled.getName());
        assertEquals(tea.getVariety(), newTeaViewModelFilled.getVariety());
        assertEquals(tea.getAmount(), newTeaViewModelFilled.getAmount());
        assertEquals(tea.getAmountkind(), newTeaViewModelFilled.getAmountkind());
        assertEquals(tea.getColor(), newTeaViewModelFilled.getColor());

        String newName = "NEW_TEA";
        String newVariety = "Green tea";
        String newVariety_Code = "02_green";
        int newAmount = 14;
        String newAmountkind = "NEW_AMOUNT_KIND";
        int newColor = 15;
        newTeaViewModelFilled.editTea(newName, newVariety, newAmount, newAmountkind, newColor);


        assertEquals(tea.getId().longValue(), newTeaViewModelFilled.getTeaId());
        assertEquals(newName, newTeaViewModelFilled.getName());
        assertEquals(newVariety_Code, newTeaViewModelFilled.getVariety());
        assertEquals(newAmount, newTeaViewModelFilled.getAmount());
        assertEquals(newAmountkind, newTeaViewModelFilled.getAmountkind());
        assertEquals(newColor, newTeaViewModelFilled.getColor());

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).update((captor.capture()));
        Tea newTea = captor.getValue();

        assertEquals(newName, newTea.getName());

        verify(infusionDAO).deleteInfusionByTeaId(TEA_ID_FILLED);

        verify(infusionDAO, times(infusions.size())).insert(any(Infusion.class));
    }

    @Test
    public void createNewTea() {
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        assertNull(newTeaViewModelEmpty.getName());

        String newName = "NEW_TEA";
        String newVariety = "Rooibus tea";
        String newVariety_Code = "09_rooibus";
        int newAmount = 14;
        String newAmountkind = "NEW_AMOUNT_KIND";
        int newColor = 15;
        newTeaViewModelEmpty.createNewTea(newName, newVariety, newAmount, newAmountkind, newColor);

        assertEquals(newName, newTeaViewModelEmpty.getName());
        assertEquals(newVariety_Code, newTeaViewModelEmpty.getVariety());
        assertEquals(newAmount, newTeaViewModelEmpty.getAmount());
        assertEquals(newAmountkind, newTeaViewModelEmpty.getAmountkind());
        assertEquals(newColor, newTeaViewModelEmpty.getColor());

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).insert((captor.capture()));
        Tea newTea = captor.getValue();

        assertEquals(newName, newTea.getName());

        verify(infusionDAO).deleteInfusionByTeaId(anyLong());
        verify(infusionDAO).insert(any(Infusion.class));
        verify(counterDAO).insert(any(Counter.class));
        verify(noteDAO).insert(any(Note.class));
    }
}
