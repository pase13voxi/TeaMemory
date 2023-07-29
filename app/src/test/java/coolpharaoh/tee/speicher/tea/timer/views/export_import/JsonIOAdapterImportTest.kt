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
import org.assertj.core.api.Assertions.*
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException

@ExtendWith(MockitoExtension::class)
internal class JsonIOAdapterImportTest {
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
    lateinit var application: Application

    @Mock
    lateinit var contentResolver: ContentResolver

    @Mock
    lateinit var imageController: ImageController

    @Mock
    lateinit var systemUtility: SystemUtility

    @Mock
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
        `when`(teaMemoryDatabase.teaDao).thenReturn(teaDao)
        `when`(teaMemoryDatabase.infusionDao).thenReturn(infusionDao)
        `when`(teaMemoryDatabase.noteDao).thenReturn(noteDao)
        `when`(teaMemoryDatabase.counterDao).thenReturn(counterDao)
        `when`(teaDao.insert(any())).thenReturn(0L).thenReturn(1L)
    }

    @Throws(FileNotFoundException::class)
    private fun mockFileReader() {
        `when`(application.contentResolver).thenReturn(contentResolver)
        `when`(contentResolver.openInputStream(any())).thenReturn(ByteArrayInputStream(DB_JSON_DUMP.toByteArray()))
    }

    @Test
    fun importTeasAndKeepStoredTeas() {
        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), true)

        verifyImportedTeas()
    }

    @Test
    fun importTeasAndDeleteStoredTeas() {
        `when`(systemUtility.sdkVersion).thenReturn(VERSION_CODES.R)
        val tea = Tea()
        tea.id = 1L
        `when`(teaDao.getTeas()).thenReturn(listOf(tea))

        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), false)

        verify(imageController).removeImageByTeaId(ArgumentMatchers.anyLong())
        verify(teaDao).deleteAll()
        verifyImportedTeas()
    }

    @Test
    fun importTeasAndDeleteStoredTeasButNotDeleteImagesForVersionsOlderAndroidQ() {
        `when`(systemUtility.sdkVersion).thenReturn(VERSION_CODES.P)

        init(application, object : Printer { override fun print(message: String?) { println(message) } })
        read(getDataIO(application, object : Printer { override fun print(message: String?) { println(message) } }, uri), false)

        verify(imageController, never()).removeImageByTeaId(ArgumentMatchers.anyLong())
    }

    private fun verifyImportedTeas() {
        argumentCaptor<Tea>().apply {
            verify(teaDao, times(2)).insert(capture())
            assertThat(allValues).extracting(
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
        }

        argumentCaptor<Infusion>().apply {
            verify(infusionDao, times(4)).insert(capture())
            assertThat(allValues).extracting(
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
        }

        argumentCaptor<Counter>().apply {
            verify(counterDao, times(2)).insert(capture())
            assertThat(allValues).extracting(
                Counter::teaId,
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            ).containsExactly(
                Tuple.tuple(0L, 1, 2, 3, 4L),
                Tuple.tuple(1L, 5, 6, 7, 8L)
            )
        }

        argumentCaptor<Note>().apply {
            verify(noteDao, times(2)).insert(capture())
            assertThat(allValues).extracting(
                Note::teaId,
                Note::position,
                Note::header,
                Note::description
            ).containsExactly(
                Tuple.tuple(0L, 0, "Header", "Description"),
                Tuple.tuple(1L, 0, "Header", "Description")
            )
        }
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