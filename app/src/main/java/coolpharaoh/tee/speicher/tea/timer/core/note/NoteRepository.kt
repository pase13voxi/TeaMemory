package coolpharaoh.tee.speicher.tea.timer.core.note

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase

class NoteRepository(application: Application) {
    private val noteDao: NoteDao

    init {
        val database = TeaMemoryDatabase.getDatabase(application)
        noteDao = database.noteDao
    }

    fun insertNote(note: Note) {
        noteDao.insert(note)
    }

    fun updateNote(note: Note) {
        noteDao.update(note)
    }

    val notes: List<Note>
        get() = noteDao.notes

    fun getNoteByTeaIdAndPosition(teaId: Long, position: Int): Note? {
        return noteDao.getNoteByTeaIdAndPosition(teaId, position)
    }

    fun getNotesByTeaIdAndPositionBiggerZero(teaId: Long): List<Note> {
        return noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId)
    }

    fun deleteNoteByTeaIdAndPosition(teaId: Long, position: Int) {
        noteDao.deleteNoteByTeaIdAndPosition(teaId, position)
    }
}