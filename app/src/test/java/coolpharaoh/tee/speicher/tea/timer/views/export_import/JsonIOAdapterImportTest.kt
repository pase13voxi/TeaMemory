package coolpharaoh.tee.speicher.tea.timer.views.export_import

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Build.VERSION_CODES
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.init
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.read
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapterFactory.getDataIO
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.setMockedImageController
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException

@ExtendWith(MockKExtension::class)
internal class JsonIOAdapterImportTest {
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase

    @RelaxedMockK
    lateinit var teaDao: TeaDao

    @RelaxedMockK
    lateinit var infusionDao: InfusionDao

    @RelaxedMockK
    lateinit var noteDao: NoteDao

    @RelaxedMockK
    lateinit var counterDao: CounterDao

    @RelaxedMockK
    lateinit var application: Application

    @MockK
    lateinit var contentResolver: ContentResolver

    @RelaxedMockK
    lateinit var imageController: ImageController

    @MockK
    lateinit var systemUtility: SystemUtility

    @MockK
    lateinit var uri: Uri

    @BeforeEach
    @Throws(FileNotFoundException::class)
    fun setUp() {
        mockDB()
        setMockedImageController(imageController)
        setFixedSystem(systemUtility)
        mockFileReader()
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.teaDao } returns teaDao
        every { teaMemoryDatabase.infusionDao } returns infusionDao
        every { teaMemoryDatabase.noteDao } returns noteDao
        every { teaMemoryDatabase.counterDao } returns counterDao
        every { teaDao.insert(any()) } returns 0L andThen 1L
    }

    @Throws(FileNotFoundException::class)
    private fun mockFileReader() {
        every { application.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(any()) } returns ByteArrayInputStream(DB_JSON_DUMP.toByteArray())
    }

    @Test
    fun importTeasAndKeepStoredTeas() {
        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), true)

        verifyImportedTeas()
    }

    @Test
    fun importTeasAndDeleteStoredTeas() {
        every { systemUtility.sdkVersion } returns VERSION_CODES.R
        val tea = Tea()
        tea.id = 1L
        every { teaDao.getTeas() } returns listOf(tea)

        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), false)

        verify { imageController.removeImageByTeaId(any()) }
        verify { teaDao.deleteAll() }
        verifyImportedTeas()
    }

    @Test
    fun importTeasAndDeleteStoredTeasButNotDeleteImagesForVersionsOlderAndroidQ() {
        every { systemUtility.sdkVersion } returns VERSION_CODES.P

        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), false)

        verify(exactly = 0) { imageController.removeImageByTeaId(any()) }
    }

    private fun verifyImportedTeas() {
        val slotsTea = mutableListOf<Tea>()
        verify(exactly = 2) { teaDao.insert(capture(slotsTea)) }
        assertThat(slotsTea).extracting(
            Tea::name,
            Tea::variety,
            Tea::amount,
            Tea::amountKind,
            Tea::color,
            Tea::rating,
            Tea::inStock,
            Tea::nextInfusion
        ).containsExactly(
            Tuple.tuple("name1", "variety1", 1.0, "Gr", 1, 3, true, 1),
            Tuple.tuple("name2", "variety2", 2.5, "Ts", 2, 0, false, 2)
        )

        val slotsInfusion = mutableListOf<Infusion>()
        verify(exactly = 4) { infusionDao.insert(capture(slotsInfusion)) }
        assertThat(slotsInfusion).extracting(
            Infusion::teaId,
            Infusion::infusionIndex,
            Infusion::time,
            Infusion::coolDownTime,
            Infusion::temperatureCelsius,
            Infusion::temperatureFahrenheit
        ).containsExactly(
            Tuple.tuple(0L, 0, "2:00", "5:00", 100, 212),
            Tuple.tuple(0L, 1, "5:00", "3:00", 90, 195),
            Tuple.tuple(1L, 0, "6:00", "5:00", 100, 212),
            Tuple.tuple(1L, 1, "7:00", "3:00", 90, 195)
        )

        val slotsCounter = mutableListOf<Counter>()
        verify(exactly = 2) { counterDao.insert(capture(slotsCounter)) }
        assertThat(slotsCounter).extracting(
            Counter::teaId,
            Counter::week,
            Counter::month,
            Counter::year,
            Counter::overall
        ).containsExactly(
            Tuple.tuple(0L, 1, 2, 3, 4L),
            Tuple.tuple(1L, 5, 6, 7, 8L)
        )

        val slotsNote = mutableListOf<Note>()
        verify(exactly = 2) { noteDao.insert(capture(slotsNote)) }
        assertThat(slotsNote).extracting(
            Note::teaId,
            Note::position,
            Note::header,
            Note::description
        ).containsExactly(
            Tuple.tuple(0L, 0, "Header", "Description"),
            Tuple.tuple(1L, 0, "Header", "Description")
        )
    }

    companion object {
        private const val DB_JSON_DUMP = "[\n" +
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
                "        \"week\": 1,\n" +
                "        \"month\": 2,\n" +
                "        \"year\": 3,\n" +
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
                "        \"week\": 5,\n" +
                "        \"month\": 6,\n" +
                "        \"year\": 7,\n" +
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
                "]"
    }
}