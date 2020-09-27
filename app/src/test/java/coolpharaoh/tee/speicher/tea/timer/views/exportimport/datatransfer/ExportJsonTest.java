package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import android.app.Application;
import android.os.Environment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Environment.class)
public class ExportJsonTest {
    public static final String CURRENT_DATE = "2020-09-15T08:09:01.789Z";
    private static final String DB_JSON_DUMP = "[{\"name\":\"name1\",\"variety\":\"variety1\",\"amount\":1," +
            "\"amountKind\":\"Gr\",\"color\":1,\"nextInfusion\":1,\"date\":\"DATE\",\"infusions\":[{\"infusionindex\":0," +
            "\"time\":\"2:00\",\"cooldowntime\":\"5:00\",\"temperaturecelsius\":100,\"temperaturefahrenheit\":212}," +
            "{\"infusionindex\":1,\"time\":\"5:00\",\"cooldowntime\":\"3:00\",\"temperaturecelsius\":90," +
            "\"temperaturefahrenheit\":195}],\"counters\":[{\"day\":1,\"week\":2,\"month\":3,\"overall\":4," +
            "\"daydate\":\"DATE\",\"weekdate\":\"DATE\",\"monthdate\":\"DATE\"}],\"notes\":[{\"position\":0," +
            "\"header\":\"Header\",\"description\":\"Description\"}]},{\"name\":\"name2\",\"variety\":\"variety2\"," +
            "\"amount\":2,\"amountKind\":\"Ts\",\"color\":2,\"nextInfusion\":2,\"date\":\"DATE\",\"infusions\":[{\"infusionindex\":0," +
            "\"time\":\"6:00\",\"cooldowntime\":\"5:00\",\"temperaturecelsius\":100,\"temperaturefahrenheit\":212}," +
            "{\"infusionindex\":1,\"time\":\"7:00\",\"cooldowntime\":\"3:00\",\"temperaturecelsius\":90,\"temperaturefahrenheit\":195}]," +
            "\"counters\":[{\"day\":5,\"week\":6,\"month\":7,\"overall\":8,\"daydate\":\"DATE\",\"weekdate\":\"DATE\",\"monthdate\":\"DATE\"}]," +
            "\"notes\":[{\"position\":0,\"header\":\"Header\",\"description\":\"Description\"}]}]";

    private String exportedDate;

    @Rule
    TemporaryFolder tempFolder = new TemporaryFolder();
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
    DateUtility fixedDate;

    @Before
    public void setUp() throws IOException {
        mockTemporaryFolder();
        mockUsedStrings();
        mockFixedDate();
        mockDB();
        mockExistingTeas();
    }

    private void mockTemporaryFolder() throws IOException {
        PowerMockito.mockStatic(Environment.class);
        when(Environment.getExternalStorageDirectory()).thenReturn(tempFolder.newFolder());
    }

    private void mockUsedStrings() {
        when(application.getString(R.string.exportimport_export_folder_name)).thenReturn("/TeaMemory");
        when(application.getString(R.string.exportimport_export_file_name)).thenReturn("tealist.json");
        when(application.getString(R.string.exportimport_saved)).thenReturn("tealist.json saved");
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
        teas.add(tea0);
        Tea tea1 = new Tea("name2", "variety2", 2, "Ts", 2, 2, CurrentDate.getDate());
        tea1.setId(1L);
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
    public void exportTeas() throws IOException {
        ExportJson exportJson = new ExportJson(application, System.out::println);
        exportJson.write();

        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/TeaMemory");
        assertThat(folder.exists()).isTrue();

        File[] listOfFiles = folder.listFiles();
        assertThat(listOfFiles).hasSize(1);
        assertThat(listOfFiles[0].getName()).isEqualTo("tealist.json");

        String content = new String(Files.readAllBytes(listOfFiles[0].toPath()));
        System.out.println(content);
        assertThat(content).isEqualTo(DB_JSON_DUMP.replace("DATE", exportedDate));
    }
}
