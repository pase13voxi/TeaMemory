package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapterFactory;

@ExtendWith(MockitoExtension.class)
class JsonIOAdapterImportTest {
    private static final String DB_JSON_DUMP = "[\n" +
            "  {\n" +
            "    \"name\": \"name1\",\n" +
            "    \"variety\": \"variety1\",\n" +
            "    \"amount\": 1.0,\n" +
            "    \"amountKind\": \"Gr\",\n" +
            "    \"color\": 1,\n" +
            "    \"rating\": 3,\n" +
            "    \"inStock\": true,\n" +
            "    \"nextInfusion\": 1,\n" +
            "    \"date\": \"2020-09-15T10:09:01.789\",\n" +
            "    \"infusions\": [\n" +
            "      {\n" +
            "        \"infusionIndex\": 0,\n" +
            "        \"time\": \"2:00\",\n" +
            "        \"coolDownTime\": \"5:00\",\n" +
            "        \"temperatureCelsius\": 100,\n" +
            "        \"temperatureFahrenheit\": 212\n" +
            "      },\n" +
            "      {\n" +
            "        \"infusionIndex\": 1,\n" +
            "        \"time\": \"5:00\",\n" +
            "        \"coolDownTime\": \"3:00\",\n" +
            "        \"temperatureCelsius\": 90,\n" +
            "        \"temperatureFahrenheit\": 195\n" +
            "      }\n" +
            "    ],\n" +
            "    \"counters\": [\n" +
            "      {\n" +
            "        \"day\": 1,\n" +
            "        \"week\": 2,\n" +
            "        \"month\": 3,\n" +
            "        \"overall\": 4,\n" +
            "        \"dayDate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"weekDate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"monthDate\": \"2020-09-15T10:09:01.789\"\n" +
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
            "    \"amount\": 2.5,\n" +
            "    \"amountKind\": \"Ts\",\n" +
            "    \"color\": 2,\n" +
            "    \"rating\": 0,\n" +
            "    \"inStock\": false,\n" +
            "    \"nextInfusion\": 2,\n" +
            "    \"date\": \"2020-09-15T10:09:01.789\",\n" +
            "    \"infusions\": [\n" +
            "      {\n" +
            "        \"infusionIndex\": 0,\n" +
            "        \"time\": \"6:00\",\n" +
            "        \"coolDownTime\": \"5:00\",\n" +
            "        \"temperatureCelsius\": 100,\n" +
            "        \"temperatureFahrenheit\": 212\n" +
            "      },\n" +
            "      {\n" +
            "        \"infusionIndex\": 1,\n" +
            "        \"time\": \"7:00\",\n" +
            "        \"coolDownTime\": \"3:00\",\n" +
            "        \"temperatureCelsius\": 90,\n" +
            "        \"temperatureFahrenheit\": 195\n" +
            "      }\n" +
            "    ],\n" +
            "    \"counters\": [\n" +
            "      {\n" +
            "        \"day\": 5,\n" +
            "        \"week\": 6,\n" +
            "        \"month\": 7,\n" +
            "        \"overall\": 8,\n" +
            "        \"dayDate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"weekDate\": \"2020-09-15T10:09:01.789\",\n" +
            "        \"monthDate\": \"2020-09-15T10:09:01.789\"\n" +
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

    @BeforeEach
    void setUp() throws FileNotFoundException {
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
    void importTeasAndKeepStoredTeas() {
        JsonIOAdapter.init(application, System.out::println);
        JsonIOAdapter.read(DataIOAdapterFactory.getDataIO(application, System.out::println, Uri.EMPTY), true);

        verifyImportedTeas();
    }

    @Test
    void importTeasAndDeleteStoredTeas() {
        JsonIOAdapter.init(application, System.out::println);
        JsonIOAdapter.read(DataIOAdapterFactory.getDataIO(application, System.out::println, Uri.EMPTY), false);

        verify(teaDao).deleteAll();
        verifyImportedTeas();
    }

    private void verifyImportedTeas() {
        final ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
        verify(teaDao, times(2)).insert(captorTea.capture());
        final List<Tea> teas = captorTea.getAllValues();
        assertThat(teas).extracting(
                Tea::getName,
                Tea::getVariety,
                Tea::getAmount,
                Tea::getAmountKind,
                Tea::getColor,
                Tea::getRating,
                Tea::isInStock,
                Tea::getNextInfusion
        ).containsExactly(
                Tuple.tuple("name1", "variety1", 1.0, "Gr", 1, 3, true, 1),
                Tuple.tuple("name2", "variety2", 2.5, "Ts", 2, 0, false, 2)
        );

        final ArgumentCaptor<Infusion> captorInfusion = ArgumentCaptor.forClass(Infusion.class);
        verify(infusionDao, times(4)).insert(captorInfusion.capture());
        final List<Infusion> infusions = captorInfusion.getAllValues();
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

        final ArgumentCaptor<Counter> captorCounter = ArgumentCaptor.forClass(Counter.class);
        verify(counterDao, times(2)).insert(captorCounter.capture());
        final List<Counter> counters = captorCounter.getAllValues();
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

        final ArgumentCaptor<Note> captorNote = ArgumentCaptor.forClass(Note.class);
        verify(noteDao, times(2)).insert(captorNote.capture());
        final List<Note> notes = captorNote.getAllValues();
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
