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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor

@ExtendWith(MockitoExtension::class, TaskExecutorExtension::class)
internal class InformationViewModelTest {

    @Mock
    lateinit var noteRepository: NoteRepository

    @Mock
    lateinit var teaRepository: TeaRepository

    @Mock
    lateinit var counterRepository: CounterRepository

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @Test
    fun getTeaIdt() {
        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaId).isEqualTo(TEA_ID)
    }

    @Test
    fun getTeaName() {
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaName).isEqualTo(tea.name)
    }

    @Test
    fun getTeaVariety() {
        val varieties = arrayOf("Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other")
        `when`(application.resources).thenReturn(resources)
        `when`(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varieties)

        val tea = Tea("name", "03_yellow", 0.0, null, 0, 0, null)
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.varietyAsText).isEqualTo("Yellow tea")
    }

    @Test
    fun getTeaRating() {
        val rating = 3
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        tea.rating = rating
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.teaRating).isEqualTo(tea.rating)
    }

    @Test
    fun updateTeaRating() {
        val rating = 3
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateTeaRating(rating)

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            assertThat(lastValue.rating).isEqualTo(rating)
        }
    }

    @Test
    fun isTeaInStock() {
        val inStock = true
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        tea.inStock = inStock
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.isInStock).isEqualTo(tea.inStock)
    }

    @Test
    fun updateTeaInStock() {
        val inStock = true
        val tea = Tea("name", null, 0.0, null, 0, 0, null)
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateTeaInStock(inStock)

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            assertThat(lastValue.inStock).isEqualTo(inStock)
        }
    }

    @Test
    fun getDate() {
        val tea = Tea("name", null, 0.0, null, 0, 0, CurrentDate.getDate())
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.date).isEqualTo(tea.date)
    }

    @Test
    fun getDetails() {
        val notes = listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION),
            Note(TEA_ID, 1, HEADER, DESCRIPTION))

        `when`(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.getDetails().value).isEqualTo(notes)
    }

    @Test
    fun getDetail() {
        val position = 0
        val notes = listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION),
            Note(TEA_ID, 1, HEADER, DESCRIPTION))

        `when`(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.getDetail(position)).isEqualTo(notes[0])
    }

    @Test
    fun addDetail() {
        `when`(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(emptyList())

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.addDetail(HEADER, DESCRIPTION)

        argumentCaptor<Note>().apply {
            verify(noteRepository).insertNote(capture())
            assertThat(lastValue)
                .extracting(Note::teaId, Note::position, Note::header, Note::description)
                .contains(TEA_ID, 0, HEADER, DESCRIPTION)
        }
    }

    @Test
    fun updateDetail() {
        val anotherHeader = "AnotherHeader"
        val anotherDescription = "AnotherDescription"
        `when`(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
            .thenReturn(listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION)))

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateDetail(0, anotherHeader, anotherDescription)

        argumentCaptor<Note>().apply {
            verify(noteRepository).updateNote(capture())
            assertThat(lastValue)
                .extracting(Note::header, Note::description)
                .contains(anotherHeader, anotherDescription)
        }
    }

    @Test
    fun deleteDetail() {
        val index = 1
        `when`(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
            .thenReturn(listOf(Note(TEA_ID, 0, HEADER, DESCRIPTION),
                Note(TEA_ID, 2, HEADER, DESCRIPTION)))

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.deleteDetail(index)

        verify(noteRepository).deleteNoteByTeaIdAndPosition(TEA_ID, index)

        argumentCaptor<Note>().apply {
            verify(noteRepository).updateNote(capture())
            assertThat(lastValue.position).isEqualTo(1)
        }
    }

    @Test
    fun getNotes() {
        val note = Note()
        `when`(noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1)).thenReturn(note)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)

        assertThat(informationViewModel.notes).isEqualTo(note)
    }

    @Test
    fun getNotesAndNotesAreNull() {
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

        `when`(noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1)).thenReturn(Note())

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.updateNotes(newNotes)

        argumentCaptor<Note>().apply {
            verify(noteRepository).updateNote(capture())
            assertThat(lastValue.description).isEqualTo(newNotes)
        }

    }

    @Test
    fun getCounter() {
        val currentDate = CurrentDate.getDate()
        val counterBefore = Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate)
        `when`(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore)

        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        val counterAfter = informationViewModel.counter

        assertThat(counterAfter).isEqualTo(counterBefore)
    }

    @Test
    fun getCounterAndCounterIsNull() {
        val informationViewModel = InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application)
        informationViewModel.counter

        argumentCaptor<Counter>().apply {
            verify(counterRepository).updateCounter(capture())
            assertThat(lastValue)
                .extracting(Counter::week, Counter::month, Counter::year, Counter::overall)
                .containsExactly(0, 0, 0, 0L)
        }
    }

    companion object {
        private const val TEA_ID = 1L
        private const val HEADER = "header"
        private const val DESCRIPTION = "description"
    }
}