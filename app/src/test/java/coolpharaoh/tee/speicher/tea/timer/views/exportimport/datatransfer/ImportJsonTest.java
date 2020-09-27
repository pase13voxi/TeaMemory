package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImportJsonTest {
    private static final String DB_JSON_DUMP = "[{\"name\":\"name1\",\"variety\":\"variety1\",\"amount\":1," +
            "\"amountKind\":\"Gr\",\"color\":1,\"nextInfusion\":1,\"date\":\"2020-09-15T10:09:01.789Z\"," +
            "\"infusions\":[{\"infusionindex\":0,\"time\":\"2:00\",\"cooldowntime\":\"5:00\",\"temperatur" +
            "ecelsius\":100,\"temperaturefahrenheit\":212},{\"infusionindex\":1,\"time\":\"5:00\",\"cooldow" +
            "ntime\":\"3:00\",\"temperaturecelsius\":90,\"temperaturefahrenheit\":195}],\"counters\":[{\"day\"" +
            ":1,\"week\":2,\"month\":3,\"overall\":4,\"daydate\":\"2020-09-15T10:09:01.789Z\",\"weekdate\":\"" +
            "2020-09-15T10:09:01.789Z\",\"monthdate\":\"2020-09-15T10:09:01.789Z\"}],\"notes\":[{\"position\":" +
            "0,\"header\":\"Header\",\"description\":\"Description\"}]},{\"name\":\"name2\",\"variety\":\"varie" +
            "ty2\",\"amount\":2,\"amountKind\":\"Ts\",\"color\":2,\"nextInfusion\":2,\"date\":\"2020-09-15T10:09:" +
            "01.789Z\",\"infusions\":[{\"infusionindex\":0,\"time\":\"6:00\",\"cooldowntime\":\"5:00\",\"temperatu" +
            "recelsius\":100,\"temperaturefahrenheit\":212},{\"infusionindex\":1,\"time\":\"7:00\",\"cooldownt" +
            "ime\":\"3:00\",\"temperaturecelsius\":90,\"temperaturefahrenheit\":195}],\"counters\":[{\"day\":5," +
            "\"week\":6,\"month\":7,\"overall\":8,\"daydate\":\"2020-09-15T10:09:01.789Z\",\"weekdate\":\"2020" +
            "-09-15T10:09:01.789Z\",\"monthdate\":\"2020-09-15T10:09:01.789Z\"}],\"notes\":[{\"position\":0,\"h" +
            "eader\":\"Header\",\"description\":\"Description\"}]}]";
    public static final String CURRENT_DATE = "2020-09-15T08:09:01.789Z";

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
    Application application;
    @Mock
    ContentResolver contentResolver;


    @Before
    public void setUp() throws FileNotFoundException {
        mockDB();
        mockFileReader();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaDao.insert(any())).thenReturn(0L).thenReturn(1L);
    }

    private void mockFileReader() throws FileNotFoundException {
        when(application.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.openInputStream(any())).thenReturn(new ByteArrayInputStream(DB_JSON_DUMP.getBytes()));
    }

    @Test
    public void importTeasAndKeepStoredTeas() {
        Uri uri = Uri.EMPTY;
        ImportJson importJson = new ImportJson(uri, application, System.out::println);
        importJson.read(true);

        verifyImportedTeas();
    }

    @Test
    public void importTeasAndDeleteStoredTeas() {
        Uri uri = Uri.EMPTY;
        ImportJson importJson = new ImportJson(uri, application, System.out::println);
        importJson.read(false);

        verify(teaDao).deleteAll();
        verifyImportedTeas();
    }

    private void verifyImportedTeas() {
        ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
        verify(teaDao, times(2)).insert(captorTea.capture());
        List<Tea> teas = captorTea.getAllValues();
        assertThat(teas).usingFieldByFieldElementComparator().containsExactly(
                new Tea("name1", "variety1", 1, "Gr", 1, 1, getFixedDate()),
                new Tea("name2", "variety2", 2, "Ts", 2, 2, getFixedDate())
        );

        ArgumentCaptor<Infusion> captorInfusion = ArgumentCaptor.forClass(Infusion.class);
        verify(infusionDao, times(4)).insert(captorInfusion.capture());
        List<Infusion> infusions = captorInfusion.getAllValues();
        assertThat(infusions).usingFieldByFieldElementComparator().containsExactly(
                new Infusion(0L, 0, "2:00", "5:00", 100, 212),
                new Infusion(0L, 1, "5:00", "3:00", 90, 195),
                new Infusion(1L, 0, "6:00", "5:00", 100, 212),
                new Infusion(1L, 1, "7:00", "3:00", 90, 195)
        );

        ArgumentCaptor<Counter> captorCounter = ArgumentCaptor.forClass(Counter.class);
        verify(counterDao, times(2)).insert(captorCounter.capture());
        List<Counter> counters = captorCounter.getAllValues();
        assertThat(counters).usingFieldByFieldElementComparator().containsExactly(
                new Counter(0L, 1, 2, 3, 4, getFixedDate(), getFixedDate(), getFixedDate()),
                new Counter(1L, 5, 6, 7, 8, getFixedDate(), getFixedDate(), getFixedDate())
        );

        ArgumentCaptor<Note> captorNote = ArgumentCaptor.forClass(Note.class);
        verify(noteDao, times(2)).insert(captorNote.capture());
        List<Note> notes = captorNote.getAllValues();
        assertThat(notes).usingFieldByFieldElementComparator().containsExactly(
                new Note(0L, 0, "Header", "Description"),
                new Note(1L, 0, "Header", "Description")
        );
    }

    private Date getFixedDate() {
        Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);
        return Date.from(instant);
    }
}
