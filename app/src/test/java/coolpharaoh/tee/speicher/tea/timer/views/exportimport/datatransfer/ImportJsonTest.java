package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
    private static final String DB_JSON_DUMP = "[\n" +
            "  {\n" +
            "    \"name\": \"name1\",\n" +
            "    \"variety\": \"variety1\",\n" +
            "    \"amount\": 1,\n" +
            "    \"amountKind\": \"Gr\",\n" +
            "    \"color\": 1,\n" +
            "    \"nextInfusion\": 1,\n" +
            "    \"date\": \"2020-09-15T10:09:01.789\",\n" +
            "    \"infusions\": [\n" +
            "      {\n" +
            "        \"infusionindex\": 0,\n" +
            "        \"time\": \"2:00\",\n" +
            "        \"cooldowntime\": \"5:00\",\n" +
            "        \"temperaturecelsius\": 100,\n" +
            "        \"temperaturefahrenheit\": 212\n" +
            "      },\n" +
            "      {\n" +
            "        \"infusionindex\": 1,\n" +
            "        \"time\": \"5:00\",\n" +
            "        \"cooldowntime\": \"3:00\",\n" +
            "        \"temperaturecelsius\": 90,\n" +
            "        \"temperaturefahrenheit\": 195\n" +
            "      }\n" +
            "    ],\n" +
            "    \"counters\": [\n" +
            "      {\n" +
            "        \"day\": 1,\n" +
            "        \"week\": 2,\n" +
            "        \"month\": 3,\n" +
            "        \"overall\": 4,\n" +
            "        \"daydate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"weekdate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"monthdate\": \"2020-09-15T10:09:01.789\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"notes\": [\n" +
            "      {\n" +
            "        \"position\": 0,\n" +
            "        \"header\": \"Header\",\n" +
            "        \"description\": \"Description\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"name2\",\n" +
            "    \"variety\": \"variety2\",\n" +
            "    \"amount\": 2,\n" +
            "    \"amountKind\": \"Ts\",\n" +
            "    \"color\": 2,\n" +
            "    \"nextInfusion\": 2,\n" +
            "    \"date\": \"2020-09-15T10:09:01.789\",\n" +
            "    \"infusions\": [\n" +
            "      {\n" +
            "        \"infusionindex\": 0,\n" +
            "        \"time\": \"6:00\",\n" +
            "        \"cooldowntime\": \"5:00\",\n" +
            "        \"temperaturecelsius\": 100,\n" +
            "        \"temperaturefahrenheit\": 212\n" +
            "      },\n" +
            "      {\n" +
            "        \"infusionindex\": 1,\n" +
            "        \"time\": \"7:00\",\n" +
            "        \"cooldowntime\": \"3:00\",\n" +
            "        \"temperaturecelsius\": 90,\n" +
            "        \"temperaturefahrenheit\": 195\n" +
            "      }\n" +
            "    ],\n" +
            "    \"counters\": [\n" +
            "      {\n" +
            "        \"day\": 5,\n" +
            "        \"week\": 6,\n" +
            "        \"month\": 7,\n" +
            "        \"overall\": 8,\n" +
            "        \"daydate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"weekdate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"monthdate\": \"2020-09-15T10:09:01.789\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"notes\": [\n" +
            "      {\n" +
            "        \"position\": 0,\n" +
            "        \"header\": \"Header\",\n" +
            "        \"description\": \"Description\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

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
        assertThat(teas).extracting(
                Tea::getName,
                Tea::getVariety,
                Tea::getAmount,
                Tea::getAmountKind,
                Tea::getColor,
                Tea::getNextInfusion
        ).containsExactly(
                Tuple.tuple("name1", "variety1", 1, "Gr", 1, 1),
                Tuple.tuple("name2", "variety2", 2, "Ts", 2, 2)
        );

        ArgumentCaptor<Infusion> captorInfusion = ArgumentCaptor.forClass(Infusion.class);
        verify(infusionDao, times(4)).insert(captorInfusion.capture());
        List<Infusion> infusions = captorInfusion.getAllValues();
        assertThat(infusions).extracting(
                Infusion::getTeaId,
                Infusion::getInfusionIndex,
                Infusion::getTime,
                Infusion::getCoolDownTime,
                Infusion::getTemperatureCelsius,
                Infusion::getTemperatureFahrenheit
        ).containsExactly(
                Tuple.tuple(0L, 0, "2:00", "5:00", 100, 212),
                Tuple.tuple(0L, 1, "5:00", "3:00", 90, 195),
                Tuple.tuple(1L, 0, "6:00", "5:00", 100, 212),
                Tuple.tuple(1L, 1, "7:00", "3:00", 90, 195)
        );

        ArgumentCaptor<Counter> captorCounter = ArgumentCaptor.forClass(Counter.class);
        verify(counterDao, times(2)).insert(captorCounter.capture());
        List<Counter> counters = captorCounter.getAllValues();
        assertThat(counters).extracting(
                Counter::getTeaId,
                Counter::getDay,
                Counter::getWeek,
                Counter::getMonth,
                Counter::getOverall
        ).containsExactly(
                Tuple.tuple(0L, 1, 2, 3, 4L),
                Tuple.tuple(1L, 5, 6, 7, 8L)
        );

        ArgumentCaptor<Note> captorNote = ArgumentCaptor.forClass(Note.class);
        verify(noteDao, times(2)).insert(captorNote.capture());
        List<Note> notes = captorNote.getAllValues();
        assertThat(notes).extracting(
                Note::getTeaId,
                Note::getPosition,
                Note::getHeader,
                Note::getDescription
        ).containsExactly(
                Tuple.tuple(0L, 0, "Header", "Description"),
                Tuple.tuple(1L, 0, "Header", "Description")
        );
    }
}
