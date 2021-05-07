package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class JsonIOAdapterExportTest {
    public static final String CURRENT_DATE = "2020-09-15T08:09:01.789Z";
    private static final String DB_JSON_DUMP = "[{\"name\":\"name1\",\"variety\":\"variety1\",\"amount\":1," +
            "\"amountKind\":\"Gr\",\"color\":1,\"rating\":5,\"favorite\":true,\"nextInfusion\":1," +
            "\"date\":\"DATE\",\"infusions\":[{\"infusionindex\":0,\"time\":\"2:00\",\"cooldowntime\":" +
            "\"5:00\",\"temperaturecelsius\":100,\"temperaturefahrenheit\":212},{\"infusionindex\"" +
            ":1,\"time\":\"5:00\",\"cooldowntime\":\"3:00\",\"temperaturecelsius\":90,\"temperaturefahrenheit\"" +
            ":195}],\"counters\":[{\"day\":1,\"week\":2,\"month\":3,\"overall\":4,\"daydate\":\"DATE\"," +
            "\"weekdate\":\"DATE\",\"monthdate\":\"DATE\"}],\"notes\":[{\"position\":0,\"header\":\"Header\"" +
            ",\"description\":\"Description\"}]},{\"name\":\"name2\",\"variety\":\"variety2\",\"amount\"" +
            ":2,\"amountKind\":\"Ts\",\"color\":2,\"rating\":0,\"favorite\":false,\"nextInfusion\":2,\"" +
            "date\":\"DATE\",\"infusions\":[{\"infusionindex\":0,\"time\":\"6:00\",\"cooldowntime\":" +
            "\"5:00\",\"temperaturecelsius\":100,\"temperaturefahrenheit\":212},{\"infusionindex\":1," +
            "\"time\":\"7:00\",\"cooldowntime\":\"3:00\",\"temperaturecelsius\":90,\"temperaturefahrenheit\"" +
            ":195}],\"counters\":[{\"day\":5,\"week\":6,\"month\":7,\"overall\":8,\"daydate\":\"DATE\"," +
            "\"weekdate\":\"DATE\",\"monthdate\":\"DATE\"}],\"notes\":[{\"position\":0,\"header\":\"Header\"," +
            "\"description\":\"Description\"}]}]";

    private String exportedDate;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
    @Mock
    DateUtility fixedDate;
    @Mock
    DataIO dataIO;

    @Before
    public void setUp() {
        mockFixedDate();
        mockDB();
        mockExistingTeas();
    }

    private void mockFixedDate() {
        Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);
        Date date = Date.from(instant);
        when(this.fixedDate.getDate()).thenReturn(date);
        CurrentDate.setFixedDate(this.fixedDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        exportedDate = formatter.format(date);
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
    }

    private void mockExistingTeas() {
        List<Tea> teas = new ArrayList<>();
        Tea tea0 = new Tea("name1", "variety1", 1, "Gr", 1, 1, CurrentDate.getDate());
        tea0.setId(0L);
        tea0.setRating(3);
        tea0.setFavorite(true);
        teas.add(tea0);
        Tea tea1 = new Tea("name2", "variety2", 2, "Ts", 2, 2, CurrentDate.getDate());
        tea1.setId(1L);
        tea0.setRating(5);
        teas.add(tea1);
        when(teaDao.getTeas()).thenReturn(teas);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion00 = new Infusion(0L, 0, "2:00", "5:00", 100, 212);
        infusion00.setId(0L);
        infusions.add(infusion00);
        Infusion infusion01 = new Infusion(0L, 1, "5:00", "3:00", 90, 195);
        infusion01.setId(1L);
        infusions.add(infusion01);
        Infusion infusion10 = new Infusion(1L, 0, "6:00", "5:00", 100, 212);
        infusion10.setId(2L);
        infusions.add(infusion10);
        Infusion infusion11 = new Infusion(1L, 1, "7:00", "3:00", 90, 195);
        infusion11.setId(3L);
        infusions.add(infusion11);
        when(infusionDao.getInfusions()).thenReturn(infusions);

        List<Note> notes = new ArrayList<>();
        Note note0 = new Note(0L, 0, "Header", "Description");
        note0.setId(0L);
        notes.add(note0);
        Note note1 = new Note(1L, 0, "Header", "Description");
        notes.add(note1);
        note0.setId(1L);
        when(noteDao.getNotes()).thenReturn(notes);

        List<Counter> counters = new ArrayList<>();
        Counter counter0 = new Counter(0L, 1, 2, 3, 4, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counter0.setId(0L);
        counters.add(counter0);
        Counter counter1 = new Counter(1L, 5, 6, 7, 8, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counter1.setId(1L);
        counters.add(counter1);
        when(counterDao.getCounters()).thenReturn(counters);
    }

    @Test
    public void exportTeas() {
        final Context context = ApplicationProvider.getApplicationContext();

        JsonIOAdapter.init((Application) context, System.out::println, dataIO);
        JsonIOAdapter.write(null);

        verify(dataIO).write(eq(DB_JSON_DUMP.replace("DATE", exportedDate)), any());
    }
}
