package coolpharaoh.tee.speicher.tea.timer.views.export_import

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.init
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.write
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class JsonIOAdapterExportTest {
    private var exportedDate: String? = null

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var teaMemoryDatabase: TeaMemoryDatabase

    @Mock
    lateinit var teaDao: TeaDao

    @Mock
    lateinit var infusionDao: InfusionDao

    @Mock
    lateinit var noteDao: NoteDao

    @Mock
    lateinit var counterDao: CounterDao

    @Mock
    lateinit var fixedDate: DateUtility

    @Mock
    lateinit var dataIOAdapter: DataIOAdapter

    @Before
    fun setUp() {
        mockFixedDate()
        mockDB()
        mockExistingTeas()
    }

    private fun mockFixedDate() {
        val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
        val instant = Instant.now(clock)
        val date = Date.from(instant)
        `when`(fixedDate.date).thenReturn(date)
        setFixedDate(fixedDate)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        exportedDate = formatter.format(date)
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase.teaDao).thenReturn(teaDao)
        `when`(teaMemoryDatabase.infusionDao).thenReturn(infusionDao)
        `when`(teaMemoryDatabase.noteDao).thenReturn(noteDao)
        `when`(teaMemoryDatabase.counterDao).thenReturn(counterDao)
    }

    private fun mockExistingTeas() {
        val teas: MutableList<Tea> = ArrayList()
        val tea0 = Tea("name1", "variety1", 1.5, "Gr", 1, 1, getDate())
        tea0.id = 0L
        tea0.rating = 3
        tea0.inStock = true
        teas.add(tea0)
        val tea1 = Tea("name2", "variety2", 2.0, "Ts", 2, 2, getDate())
        tea1.id = 1L
        tea0.rating = 5
        teas.add(tea1)
        `when`(teaDao.getTeas()).thenReturn(teas)

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion00 = Infusion(0L, 0, "2:00", "5:00", 100, 212)
        infusion00.id = 0L
        infusions.add(infusion00)
        val infusion01 = Infusion(0L, 1, "5:00", "3:00", 90, 195)
        infusion01.id = 1L
        infusions.add(infusion01)
        val infusion10 = Infusion(1L, 0, "6:00", "5:00", 100, 212)
        infusion10.id = 2L
        infusions.add(infusion10)
        val infusion11 = Infusion(1L, 1, "7:00", "3:00", 90, 195)
        infusion11.id = 3L
        infusions.add(infusion11)
        `when`(infusionDao.getInfusions()).thenReturn(infusions)

        val notes: MutableList<Note> = ArrayList()
        val note0 = Note(0L, 0, "Header", "Description")
        note0.id = 0L
        notes.add(note0)
        val note1 = Note(1L, 0, "Header", "Description")
        notes.add(note1)
        note0.id = 1L
        `when`(noteDao.notes).thenReturn(notes)
        val counters: MutableList<Counter> = ArrayList()
        val counter0 = Counter(0L, 1, 2, 3, 4, getDate(), getDate(), getDate())
        counter0.id = 0L
        counters.add(counter0)
        val counter1 = Counter(1L, 5, 6, 7, 8, getDate(), getDate(), getDate())
        counter1.id = 1L
        counters.add(counter1)
        `when`(counterDao.getCounters()).thenReturn(counters)
    }

    @Test
    fun exportTeas() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        init((context as Application), object : Printer { override fun print(message: String?) { println(message) } })
        write(dataIOAdapter)

        verify(dataIOAdapter).write(DB_JSON_DUMP.replace("DATE", exportedDate!!))
    }

    companion object {
        const val CURRENT_DATE = "2020-09-15T08:09:01.789Z"
        private const val DB_JSON_DUMP = "[{\"name\":\"name1\",\"variety\":\"variety1\",\"amount\":1.5," +
                "\"amountKind\":\"Gr\",\"color\":1,\"rating\":5,\"inStock\":true,\"nextInfusion\":1," +
                "\"date\":\"DATE\",\"infusions\":[{\"infusionIndex\":0,\"time\":\"2:00\",\"coolDownTime\":" +
                "\"5:00\",\"temperatureCelsius\":100,\"temperatureFahrenheit\":212},{\"infusionIndex\"" +
                ":1,\"time\":\"5:00\",\"coolDownTime\":\"3:00\",\"temperatureCelsius\":90,\"temperatureFahrenheit\"" +
                ":195}],\"counters\":[{\"week\":1,\"month\":2,\"year\":3,\"overall\":4,\"weekDate\":\"DATE\"," +
                "\"monthDate\":\"DATE\",\"yearDate\":\"DATE\"}],\"notes\":[{\"position\":0,\"header\":\"Header\"" +
                ",\"description\":\"Description\"}]},{\"name\":\"name2\",\"variety\":\"variety2\",\"amount\"" +
                ":2.0,\"amountKind\":\"Ts\",\"color\":2,\"rating\":0,\"inStock\":false,\"nextInfusion\":2,\"" +
                "date\":\"DATE\",\"infusions\":[{\"infusionIndex\":0,\"time\":\"6:00\",\"coolDownTime\":" +
                "\"5:00\",\"temperatureCelsius\":100,\"temperatureFahrenheit\":212},{\"infusionIndex\":1," +
                "\"time\":\"7:00\",\"coolDownTime\":\"3:00\",\"temperatureCelsius\":90,\"temperatureFahrenheit\"" +
                ":195}],\"counters\":[{\"week\":5,\"month\":6,\"year\":7,\"overall\":8,\"weekDate\":\"DATE\"," +
                "\"monthDate\":\"DATE\",\"yearDate\":\"DATE\"}],\"notes\":[{\"position\":0,\"header\":\"Header\"," +
                "\"description\":\"Description\"}]}]"
    }
}