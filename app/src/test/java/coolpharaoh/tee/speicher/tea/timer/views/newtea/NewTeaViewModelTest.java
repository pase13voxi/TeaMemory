package coolpharaoh.tee.speicher.tea.timer.views.newtea;

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
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.assertj.core.api.Assertions.assertThat;
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
    InfusionDao infusionDAO;
    @Mock
    NoteDao noteDAO;
    @Mock
    CounterDao counterDAO;
    @Mock
    ActualSettingsDao actualSettingsDAO;
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
        when(db.getTeaDao()).thenReturn(teaDAO);
        when(db.getInfusionDao()).thenReturn(infusionDAO);
        when(db.getNoteDao()).thenReturn(noteDAO);
        when(db.getCounterDao()).thenReturn(counterDAO);
        when(db.getActualSettingsDao()).thenReturn(actualSettingsDAO);
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
        actualSettings.setTemperatureUnit("Celsius");
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertThat(newTeaViewModelFilled.getInfusionSize()).isEqualTo(2);
        assertThat(newTeaViewModelFilled.getInfusionIndex()).isEqualTo(0);

        assertThat(newTeaViewModelFilled.getInfusionTime()).isEqualTo(infusions.get(0).getTime());
        assertThat(newTeaViewModelFilled.getInfusionCooldowntime()).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(infusions.get(0).getTemperatureCelsius());

        newTeaViewModelFilled.nextInfusion();

        assertThat(newTeaViewModelFilled.getInfusionIndex()).isEqualTo(1);

        actualSettings.setTemperatureUnit("Fahrenheit");

        assertThat(newTeaViewModelFilled.getInfusionTime()).isEqualTo(infusions.get(1).getTime());
        assertThat(newTeaViewModelFilled.getInfusionCooldowntime()).isEqualTo(infusions.get(1).getCoolDownTime());
        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(infusions.get(1).getTemperatureFahrenheit());

        newTeaViewModelFilled.previousInfusion();

        assertThat(newTeaViewModelFilled.getInfusionIndex()).isEqualTo(0);

        assertThat(newTeaViewModelFilled.getInfusionTime()).isEqualTo(infusions.get(0).getTime());
        assertThat(newTeaViewModelFilled.getInfusionCooldowntime()).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(infusions.get(0).getTemperatureCelsius());
    }

    @Test
    public void takeInfusionInformation() {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit("Celsius");
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertThat(newTeaViewModelFilled.getInfusionTime()).isEqualTo(infusions.get(0).getTime());
        assertThat(newTeaViewModelFilled.getInfusionCooldowntime()).isEqualTo(infusions.get(0).getCoolDownTime());
        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(infusions.get(0).getTemperatureCelsius());

        String newTime1 = "15";
        String newCooldowntime1 = "15";
        int newCelsiusTemperature1 = 0;
        int newFahrenheitTemperature1 = 32;
        newTeaViewModelFilled.takeInfusionInformation(newTime1, newCooldowntime1, newCelsiusTemperature1);

        assertThat(newTeaViewModelFilled.getInfusionTime()).isEqualTo(newTime1);
        assertThat(newTeaViewModelFilled.getInfusionCooldowntime()).isEqualTo(newCooldowntime1);
        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(newCelsiusTemperature1);

        actualSettings.setTemperatureUnit("Fahrenheit");

        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(newFahrenheitTemperature1);

        int newFahrenheitTemperature2 = 212;
        int newCelsiusTemperature2 = 100;
        newTeaViewModelFilled.takeInfusionInformation(newTime1, newCooldowntime1, newFahrenheitTemperature2);

        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(newFahrenheitTemperature2);

        actualSettings.setTemperatureUnit("Celsius");

        assertThat(newTeaViewModelFilled.getInfusionTemperature()).isEqualTo(newCelsiusTemperature2);
    }

    @Test
    public void addAndRemoveInfusion() {
        assertThat(newTeaViewModelEmpty.getInfusionSize()).isEqualTo(1);

        newTeaViewModelEmpty.addInfusion();

        assertThat(newTeaViewModelEmpty.getInfusionSize()).isEqualTo(2);

        newTeaViewModelEmpty.deleteInfusion();

        assertThat(newTeaViewModelEmpty.getInfusionSize()).isEqualTo(1);
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

        assertThat(newTeaViewModelFilled.getTeaId()).isEqualTo(tea.getId().longValue());
        assertThat(newTeaViewModelFilled.getName()).isEqualTo(tea.getName());
        assertThat(newTeaViewModelFilled.getVariety()).isEqualTo(tea.getVariety());
        assertThat(newTeaViewModelFilled.getAmount()).isEqualTo(tea.getAmount());
        assertThat(newTeaViewModelFilled.getAmountkind()).isEqualTo(tea.getAmountKind());
        assertThat(newTeaViewModelFilled.getColor()).isEqualTo(tea.getColor());

        String newName = "NEW_TEA";
        String newVariety = "Green tea";
        String newVariety_Code = "02_green";
        int newAmount = 14;
        String newAmountkind = "NEW_AMOUNT_KIND";
        int newColor = 15;
        newTeaViewModelFilled.editTea(newName, newVariety, newAmount, newAmountkind, newColor);


        assertThat(newTeaViewModelFilled.getTeaId()).isEqualTo(tea.getId().longValue());
        assertThat(newTeaViewModelFilled.getName()).isEqualTo(newName);
        assertThat(newTeaViewModelFilled.getVariety()).isEqualTo(newVariety_Code);
        assertThat(newTeaViewModelFilled.getAmount()).isEqualTo(newAmount);
        assertThat(newTeaViewModelFilled.getAmountkind()).isEqualTo(newAmountkind);
        assertThat(newTeaViewModelFilled.getColor()).isEqualTo(newColor);

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).update((captor.capture()));
        Tea newTea = captor.getValue();

        assertThat(newTea.getName()).isEqualTo(newName);

        verify(infusionDAO).deleteInfusionsByTeaId(TEA_ID_FILLED);

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

        assertThat(newTeaViewModelEmpty.getName()).isNull();

        String newName = "NEW_TEA";
        String newVariety = "Rooibus tea";
        String newVariety_Code = "09_rooibus";
        int newAmount = 14;
        String newAmountkind = "NEW_AMOUNT_KIND";
        int newColor = 15;
        newTeaViewModelEmpty.createNewTea(newName, newVariety, newAmount, newAmountkind, newColor);

        assertThat(newTeaViewModelEmpty.getName()).isEqualTo(newName);
        assertThat(newTeaViewModelEmpty.getVariety()).isEqualTo(newVariety_Code);
        assertThat(newTeaViewModelEmpty.getAmount()).isEqualTo(newAmount);
        assertThat(newTeaViewModelEmpty.getAmountkind()).isEqualTo(newAmountkind);
        assertThat(newTeaViewModelEmpty.getColor()).isEqualTo(newColor);

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).insert((captor.capture()));
        Tea newTea = captor.getValue();

        assertThat(newTea.getName()).isEqualTo(newName);

        verify(infusionDAO).deleteInfusionsByTeaId(anyLong());
        verify(infusionDAO).insert(any(Infusion.class));
        verify(counterDAO).insert(any(Counter.class));
        verify(noteDAO).insert(any(Note.class));
    }
}
