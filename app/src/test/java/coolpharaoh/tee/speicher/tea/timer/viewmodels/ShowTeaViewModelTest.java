package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TimeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ShowTeaViewModelTest {
    private ShowTeaViewModel showTeaViewModel;

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

    private static final long TEA_ID = 1L;

    String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
    "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
    String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

    @Before
    public void setUp() {
        mockDB();
        showTeaViewModel = new ShowTeaViewModel(TEA_ID, db, context);
    }

    private void mockDB(){
        when(db.getTeaDAO()).thenReturn(teaDAO);
        when(db.getInfusionDAO()).thenReturn(infusionDAO);
        when(db.getNoteDAO()).thenReturn(noteDAO);
        when(db.getCounterDAO()).thenReturn(counterDAO);
        when(db.getActualSettingsDAO()).thenReturn(actualSettingsDAO);
    }

    @Test
    public void getTeaId(){
        long teaIdBefore = 1L;

        Tea tea = new Tea();
        tea.setId(teaIdBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        long teaIdAfter = showTeaViewModel.getTeaId();

        assertEquals(teaIdBefore, teaIdAfter);
    }

    @Test
    public void getName(){
        String teaNameBefore = "TEA";

        Tea tea = new Tea();
        tea.setName(teaNameBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        String teaNameAfter = showTeaViewModel.getName();

        assertEquals(teaNameBefore, teaNameAfter);
    }

    @Test
    public void getVariety(){
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        int varietyIndex = 4;
        String varietyCode = varietyCodes[varietyIndex];
        String varietyBefore = varietyTeas[varietyIndex];

        Tea tea = new Tea();
        tea.setVariety(varietyCode);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertEquals(varietyBefore, varietyAfter);
    }

    @Test
    public void getUnkownVariety(){
        String[] varietyCodes = {"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        String[] varietyTeas = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

        String varietyBefore = "VARIETY";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(varietyCodes);
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varietyTeas);

        String varietyAfter = showTeaViewModel.getVariety();

        assertEquals(varietyBefore, varietyAfter);
    }

    @Test
    public void getEmptyVariety(){
        String varietyBefore = "";

        Tea tea = new Tea();
        tea.setVariety(varietyBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        String varietyAfter = showTeaViewModel.getVariety();

        assertEquals("-", varietyAfter);
    }

    @Test
    public void getAmount(){
        int amountBefore = 1;

        Tea tea = new Tea();
        tea.setAmount(amountBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        int amountAfter = showTeaViewModel.getAmount();

        assertEquals(amountBefore, amountAfter);
    }

    @Test
    public void getAmountKind(){
        String amountKindBefore = "AMOUNT_KIND";

        Tea tea = new Tea();
        tea.setAmountkind(amountKindBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        String amountKindAfter = showTeaViewModel.getAmountkind();

        assertEquals(amountKindBefore, amountKindAfter);
    }

    @Test
    public void getColor(){
        int colorBefore = 1;

        Tea tea = new Tea();
        tea.setColor(colorBefore);
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(tea);

        int colorAfter = showTeaViewModel.getColor();

        assertEquals(colorBefore, colorAfter);
    }

    @Test
    public void setCurrentDate(){
        Tea teaBefore = new Tea();
        when(teaDAO.getTeaById(TEA_ID)).thenReturn(teaBefore);

        showTeaViewModel.setCurrentDate();

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).update((captor.capture()));
        Tea teaAfter = captor.getValue();
        Calendar teaDateAfter = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        teaDateAfter.setTime(teaAfter.getDate());

        Calendar currentDate = Calendar.getInstance();

        assertEquals(currentDate.get(Calendar.DAY_OF_MONTH), teaDateAfter.get(Calendar.DAY_OF_MONTH));
        assertEquals(currentDate.get(Calendar.MONTH), teaDateAfter.get(Calendar.MONTH));
        assertEquals(currentDate.get(Calendar.YEAR), teaDateAfter.get(Calendar.YEAR));
    }

    @Test
    public void navigateBetweenInfusions(){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureunit("Celsius");
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, "1", "2", 1, 32);
        infusions.add(infusion1);

        Infusion infusion2 = new Infusion(1L, 2, "2:30", "5:30", 2, 33);
        infusions.add(infusion2);

        when(infusionDAO.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        assertEquals(infusions.size(), showTeaViewModel.getInfusionSize());
        assertEquals(0, showTeaViewModel.getInfusionIndex());

        TimeHelper time1 = showTeaViewModel.getTime();
        TimeHelper cooldownTime1 = showTeaViewModel.getCooldowntime();
        int temperature1 = showTeaViewModel.getTemperature();

        assertEquals(infusions.get(0).getTime(), time1.time);
        assertEquals(1, time1.minutes);
        assertEquals(0, time1.seconds);
        assertEquals(infusions.get(0).getCooldowntime(), cooldownTime1.time);
        assertEquals(2, cooldownTime1.minutes);
        assertEquals(0, cooldownTime1.seconds);
        assertEquals(infusions.get(0).getTemperaturecelsius(), temperature1);

        showTeaViewModel.incrementInfusionIndex();
        assertEquals(1, showTeaViewModel.getInfusionIndex());

        TimeHelper time2 = showTeaViewModel.getTime();
        TimeHelper cooldownTime2 = showTeaViewModel.getCooldowntime();
        int temperature2 = showTeaViewModel.getTemperature();

        assertEquals(infusions.get(1).getTime(), time2.time);
        assertEquals(2, time2.minutes);
        assertEquals(30, time2.seconds);
        assertEquals(infusions.get(1).getCooldowntime(), cooldownTime2.time);
        assertEquals(5, cooldownTime2.minutes);
        assertEquals(30, cooldownTime2.seconds);
        assertEquals(infusions.get(1).getTemperaturecelsius(), temperature2);

        actualSettings.setTemperatureunit("Fahrenheit");
        showTeaViewModel.setInfusionIndex(0);
        assertEquals(0, showTeaViewModel.getInfusionIndex());

        int temperature3 = showTeaViewModel.getTemperature();
        assertEquals(infusions.get(0).getTemperaturefahrenheit(), temperature3);
    }

    @Test
    public void getEmptyTime(){
        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, null, null, 1, 1);
        infusions.add(infusion1);

        when(infusionDAO.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);

        TimeHelper timeAfter = showTeaViewModel.getTime();

        assertNull( timeAfter.time);
        assertEquals(0, timeAfter.minutes);
        assertEquals(0, timeAfter.seconds);
    }

    @Test
    public void getNote(){
        Note noteBefore = new Note(1L, 1, "HEADER", "DESCRIPTION");
        when(noteDAO.getNoteByTeaId(TEA_ID)).thenReturn(noteBefore);

        Note noteAfter = showTeaViewModel.getNote();

        assertEquals(noteBefore, noteAfter);
    }

    @Test
    public void setNote(){
        String differentNote = "DIFFERENT_NOTE";

        Note noteBefore = new Note(1L, 1, "HEADER", "DESCRIPTION");
        when(noteDAO.getNoteByTeaId(TEA_ID)).thenReturn(noteBefore);

        showTeaViewModel.setNote(differentNote);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteDAO).update((captor.capture()));
        Note noteAfter = captor.getValue();

        assertEquals(differentNote, noteAfter.getDescription());
    }

    @Test
    public void countCounter(){
        Date currentDate = Calendar.getInstance().getTime();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterDAO.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        showTeaViewModel.countCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterDAO).update((captor.capture()));
        Counter counterAfter = captor.getValue();

        assertEquals(2, counterAfter.getDay());
        assertEquals(2, counterAfter.getWeek());
        assertEquals(2, counterAfter.getMonth());
        assertEquals(2, counterAfter.getOverall());
    }

    @Test
    public void getCounter(){
        Date currentDate = Calendar.getInstance().getTime();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterDAO.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        Counter counterAfter = showTeaViewModel.getCounter();

        assertEquals(counterBefore, counterAfter);
    }

    @Test
    public void isAnimation(){
        boolean animationBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setAnimation(animationBefore);
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        boolean animationAfter = showTeaViewModel.isAnimation();

        assertEquals(animationBefore, animationAfter);
    }

    @Test
    public void isShowTeaAlert(){
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setShowteaalert(showTeaAlertBefore);
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        boolean showTeaAlertAfter = showTeaViewModel.isShowteaalert();

        assertEquals(showTeaAlertBefore, showTeaAlertAfter);
    }

    @Test
    public void setShowTeaAlert(){
        boolean showTeaAlertBefore = true;

        ActualSettings actualSettings = new ActualSettings();
        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        showTeaViewModel.setShowteaalert(showTeaAlertBefore);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsDAO).update((captor.capture()));
        ActualSettings actualSettingsAfter = captor.getValue();

        assertEquals(showTeaAlertBefore, actualSettingsAfter.isShowteaalert());
    }
}