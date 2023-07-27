package coolpharaoh.tee.speicher.tea.timer.views.information

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter.refreshCounter
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import java.util.Date

internal class InformationViewModel
    @VisibleForTesting
    constructor(val teaId: Long, private val teaRepository: TeaRepository,
                private val noteRepository: NoteRepository,
                private val counterRepository: CounterRepository,
                private val application: Application?) : ViewModel() {

    constructor(teaId: Long, application: Application?) :
            this(teaId, TeaRepository(application!!), NoteRepository(application), CounterRepository(application), application)

    private val details: MutableLiveData<List<Note>>

    init {
        details = MutableLiveData()
        // Notes with position over 0 contains tea information
        updateDetails(teaId, noteRepository)
    }

    val teaName: String?
        get() {
            val tea = teaRepository.getTeaById(teaId)
            return if (tea == null) {
                throw NoSuchElementException("No tea found for tea id $teaId")
            } else {
                tea.name
            }
        }

    val varietyAsText: String?
        get() {
            val tea = teaRepository.getTeaById(teaId)
            return convertStoredVarietyToText(tea!!.variety, application!!)
        }

    val teaRating: Int
        get() {
            val tea = teaRepository.getTeaById(teaId)
            return tea!!.rating
        }

    fun updateTeaRating(rating: Int) {
        val tea = teaRepository.getTeaById(teaId)
        tea!!.rating = rating
        teaRepository.updateTea(tea)
    }

    val isInStock: Boolean
        get() {
            val tea = teaRepository.getTeaById(teaId)
            return tea!!.inStock
        }

    fun updateTeaInStock(inStock: Boolean) {
        val tea = teaRepository.getTeaById(teaId)
        tea!!.inStock = inStock
        teaRepository.updateTea(tea)
    }

    val date: Date?
        get() {
            val tea = teaRepository.getTeaById(teaId)
            return tea!!.date
        }

    fun getDetails(): LiveData<List<Note>> {
        return details
    }

    fun getDetail(position: Int): Note {
        return details.value!![position]
    }

    fun addDetail(header: String?, description: String?) {
        val note = Note(
            teaId, details.value!!.size, header, description
        )
        noteRepository.insertNote(note)
        updateDetails(teaId, noteRepository)
    }

    fun updateDetail(position: Int, header: String?, description: String?) {
        val note = details.value!![position]
        note.header = header
        note.description = description
        noteRepository.updateNote(note)
        updateDetails(teaId, noteRepository)
    }

    fun deleteDetail(index: Int) {
        noteRepository.deleteNoteByTeaIdAndPosition(teaId, index)
        updateDetails(teaId, noteRepository)
        for (i in index until details.value!!.size) {
            details.value!![i].position = i
            noteRepository.updateNote(details.value!![i])
        }
    }

    val notes: Note
        get() {
            // Note with position -1 contains notes
            var notes = noteRepository.getNoteByTeaIdAndPosition(teaId, -1)
            if (notes == null) {
                notes = Note(teaId, -1, "01_notes", "")
                noteRepository.insertNote(notes)
            }
            return notes
        }

    fun updateNotes(insertedNotes: String?) {
        // Note with position -1 contains notes
        val notes = noteRepository.getNoteByTeaIdAndPosition(teaId, -1)
        notes!!.description = insertedNotes

        noteRepository.updateNote(notes)
    }

    private fun updateDetails(teaId: Long, noteRepository: NoteRepository) {
        details.value = noteRepository.getNotesByTeaIdAndPositionBiggerZero(teaId)
    }

    // Counter
    val counter: Counter
        get() {
            val counter = orCreateCounter
            refreshCounter(counter)
            counterRepository.updateCounter(counter)
            return counter
        }

    private val orCreateCounter: Counter
        get() {
            var counter = counterRepository.getCounterByTeaId(teaId)
            if (counter == null) {
                counter = Counter()
                counter.teaId = teaId
                counter.week = 0
                counter.month = 0
                counter.year = 0
                counter.overall = 0
                counter.weekDate = getDate()
                counter.monthDate = getDate()
                counter.yearDate = getDate()
                counter.id = counterRepository.insertCounter(counter)
            }

            return counter
        }
}