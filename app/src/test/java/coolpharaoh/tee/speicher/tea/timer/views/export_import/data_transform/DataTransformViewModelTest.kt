package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.GregorianCalendar

@ExtendWith(MockKExtension::class)
internal class DataTransformViewModelTest {
    @RelaxedMockK
    lateinit var teaRepository: TeaRepository
    @RelaxedMockK
    lateinit var infusionRepository: InfusionRepository
    @RelaxedMockK
    lateinit var counterRepository: CounterRepository
    @RelaxedMockK
    lateinit var noteRepository: NoteRepository
    @RelaxedMockK
    lateinit var imageController: ImageController
    @InjectMockKs
    lateinit var dataTransformViewModel: DataTransformViewModel

    @Test
    fun getTeaList() {
        val date = GregorianCalendar(2020, 1, 18).time

        val teasBefore: MutableList<Tea> = ArrayList()
        val tea1 = Tea("Tea1", "Variety1", 1.0, "Kind1", 1, 1, date)
        teasBefore.add(tea1)
        val tea2 = Tea("Tea2", "Variety2", 2.0, "Kind2", 2, 2, date)
        teasBefore.add(tea2)
        every { teaRepository.teas } returns teasBefore

        val teasAfter = dataTransformViewModel.teas

        assertThat(teasAfter).isEqualTo(teasBefore)
    }

    @Test
    fun insertTea() {
        val date = GregorianCalendar(2020, 1, 18).time

        val teaBefore = Tea("Tea", "Variety", 1.0, "Kind", 1, 1, date)

        dataTransformViewModel.insertTea(teaBefore)

        val slotTea = slot<Tea>()
        verify { teaRepository.insertTea(capture(slotTea))}
        assertThat(slotTea.captured).isEqualTo(teaBefore)
    }

    @Test
    fun deleteAll() {
        dataTransformViewModel.deleteAllTeas()
        verify { teaRepository.deleteAllTeas() }
    }

    @Test
    fun deleteAllTeaImages() {
        val tea1 = Tea()
        tea1.id = 1L
        val tea2 = Tea()
        tea2.id = 2L
        val teas = listOf(tea1, tea2)
        every { teaRepository.teas } returns teas

        dataTransformViewModel.deleteAllTeaImages()

        verify { imageController.removeImageByTeaId(1L) }
        verify { imageController.removeImageByTeaId(2L) }
    }

    @Test
    fun getInfusionList() {
        val infusionsBefore: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1L, 1, "1", "1", 1, 1)
        infusionsBefore.add(infusion1)
        val infusion2 = Infusion(2L, 2, "2", "2", 2, 2)
        infusionsBefore.add(infusion2)

        every { infusionRepository.infusions } returns infusionsBefore

        val infusionsAfter = dataTransformViewModel.infusions

        assertThat(infusionsAfter).isEqualTo(infusionsBefore)
    }

    @Test
    fun insertInfusion() {
        val infusionBefore = Infusion(1L, 1, "1", "1", 1, 1)

        dataTransformViewModel.insertInfusion(infusionBefore)

        val slotInfusion = slot<Infusion>()
        verify { infusionRepository.insertInfusion(capture(slotInfusion)) }
        assertThat(slotInfusion.captured).isEqualTo(infusionBefore)
    }

    @Test
    fun getCounterList() {
        val date = GregorianCalendar(2020, 1, 18).time

        val countersBefore: MutableList<Counter> = ArrayList()
        val counter1 = Counter(1L, 1, 1, 1, 1L, date, date, date)
        countersBefore.add(counter1)
        val counter2 = Counter(2L, 2, 2, 2, 2L, date, date, date)
        countersBefore.add(counter2)

        every { counterRepository.counters } returns countersBefore

        val countersAfter = dataTransformViewModel.counters

        assertThat(countersAfter).isEqualTo(countersBefore)
    }

    @Test
    fun insertCounter() {
        val date = GregorianCalendar(2020, 1, 18).time
        val counterBefore = Counter(1L, 1, 1, 1, 1L, date, date, date)

        dataTransformViewModel.insertCounter(counterBefore)

        val slotCounter = slot<Counter>()
        verify { counterRepository.insertCounter(capture(slotCounter)) }
        assertThat(slotCounter.captured).isEqualTo(counterBefore)
    }

    @Test
    fun getNoteList() {
        val notesBefore: MutableList<Note> = ArrayList()
        val note1 = Note(1L, 1, "Header1", "Description1")
        notesBefore.add(note1)
        val note2 = Note(2L, 2, "Header2", "Description2")
        notesBefore.add(note2)

        every { noteRepository.notes } returns notesBefore

        val notesAfter = dataTransformViewModel.notes

        assertThat(notesAfter).isEqualTo(notesBefore)
    }

    @Test
    fun insertNote() {
        val noteBefore = Note(1L, 1, "Header", "Description")

        dataTransformViewModel.insertNote(noteBefore)

        val slotNote = slot<Note>()
        verify { noteRepository.insertNote(capture(slotNote)) }
        assertThat(slotNote.captured).isEqualTo(noteBefore)
    }
}