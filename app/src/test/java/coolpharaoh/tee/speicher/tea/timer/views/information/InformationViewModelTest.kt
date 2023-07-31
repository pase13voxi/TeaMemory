package coolpharaoh.tee.speicher.tea.timer.views.information

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, TaskExecutorExtension::class)
internal class InformationViewModelTest {
    @RelaxedMockK
    lateinit var noteRepository: NoteRepository
    @RelaxedMockK
    lateinit var teaRepository: TeaRepository
    @RelaxedMockK
    lateinit var counterRepository: CounterRepository
    @MockK
    lateinit var application: Application
    @MockK
    lateinit var resources: Resources

    @Test
    fun getTeaIdt() {
        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaId).isEqualTo(TEA_ID)
    }

    @Test
    fun getTeaName() {
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaName).isEqualTo(tea.name)
    }

    @Test
    fun getTeaVariety() {
        val varieties = arrayOf("Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other")
        every { application.resources } returns resources
        every { resources.getStringArray(R.array.new_tea_variety_teas) } returns varieties

        val tea = Tea("name", "03_yellow", 0.0, null, 0, 0, null)
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.varietyAsText).isEqualTo("Yellow tea")
    }

    @Test
    fun getTeaRating() {
        val rating = 3
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        tea.rating = rating
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaRating).isEqualTo(tea.rating)
    }

    @Test
    fun updateTeaRating() {
        val rating = 3
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateTeaRating(rating)

        val slotTea = slot<Tea>()
        verify { teaRepository.updateTea(capture(slotTea)) }
        assertThat(slotTea.captured.rating).isEqualTo(rating)
    }

    @Test
    fun isTeaInStock() {
        val inStock = true
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        tea.inStock = inStock
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.isInStock).isEqualTo(tea.inStock)
    }

    @Test
    fun updateTeaInStock() {
        val inStock = true
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateTeaInStock(inStock)

        val slotTea = slot<Tea>()
        verify { teaRepository.updateTea(capture(slotTea)) }
        assertThat(slotTea.captured.inStock).isEqualTo(inStock)
    }

    @Test
    fun getDate() {
        val tea = Tea("name", null, 0.0, null, 0, 0, CurrentDate.getDate())
        every { teaRepository.getTeaById(TEA_ID) } returns tea

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.date).isEqualTo(tea.date)
    }

    @Test
    fun getDetails() {
        val notes = listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION), Note(TEA_ID, 1, HEADER, DESCRIPTION))

        every { noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns notes

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.getDetails().value).isEqualTo(notes)
    }

    @Test
    fun getDetail() {
        val position = 0
        val notes = listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION),
            Note(TEA_ID, 1, HEADER, DESCRIPTION))

        every { noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns notes

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.getDetail(position)).isEqualTo(notes[0])
    }

    @Test
    fun addDetail() {
        every { noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns emptyList()

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.addDetail(HEADER, DESCRIPTION)

        val slotNote = slot<Note>()
        verify { noteRepository.insertNote(capture(slotNote)) }
        assertThat(slotNote.captured)
            .extracting(Note::teaId, Note::position, Note::header, Note::description)
            .contains(TEA_ID, 0, HEADER, DESCRIPTION)
    }

    @Test
    fun updateDetail() {
        val anotherHeader = "AnotherHeader"
        val anotherDescription = "AnotherDescription"
        every { noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION))

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateDetail(0, anotherHeader, anotherDescription)

        val slotNote = slot<Note>()
        verify { noteRepository.updateNote(capture(slotNote)) }
        assertThat(slotNote.captured)
            .extracting(Note::header, Note::description)
            .contains(anotherHeader, anotherDescription)
    }

    @Test
    fun deleteDetail() {
        val index = 1
        every { noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION), Note(TEA_ID, 2, HEADER, DESCRIPTION))

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.deleteDetail(index)

        verify { noteRepository.deleteNoteByTeaIdAndPosition(TEA_ID, index) }

        val slotNote = slot<Note>()
        verify { noteRepository.updateNote(capture(slotNote)) }
        assertThat(slotNote.captured.position).isEqualTo(1)
    }

    @Test
    fun getNotes() {
        val note = Note()
        every { noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1) } returns note

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.notes).isEqualTo(note)
    }

    @Test
    fun getNotesAndNotesAreNull() {
        every { noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1)} returns null

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.notes)
            .extracting(
                Note::position,
                Note::header,
                Note::description)
            .containsExactly(-1, "01_notes", "")
    }

    @Test
    fun updateNotes() {
        val newNotes = "changed Notes"

        every { noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1) } returns Note()

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateNotes(newNotes)

        val slotNote = slot<Note>()
        verify { noteRepository.updateNote(capture(slotNote)) }
        assertThat(slotNote.captured.description).isEqualTo(newNotes)
    }

    @Test
    fun getCounter() {
        val currentDate = CurrentDate.getDate()
        val counterBefore = Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate)
        every { counterRepository.getCounterByTeaId(TEA_ID) } returns counterBefore

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        val counterAfter = informationViewModel.counter

        assertThat(counterAfter).isEqualTo(counterBefore)
    }

    @Test
    fun getCounterAndCounterIsNull() {
        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.counter

        val slotCounter = slot<Counter>()
        verify { counterRepository.updateCounter(capture(slotCounter)) }
        assertThat(slotCounter.captured)
            .extracting(Counter::week, Counter::month, Counter::year, Counter::overall)
            .containsExactly(0, 0, 0, 0L)
    }

    companion object {
        private const val TEA_ID = 1L
        private const val HEADER = "header"
        private const val DESCRIPTION = "description"
    }
}