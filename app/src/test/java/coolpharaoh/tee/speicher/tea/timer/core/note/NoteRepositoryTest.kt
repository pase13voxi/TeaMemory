package coolpharaoh.tee.speicher.tea.timer.core.note

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class NoteRepositoryTest {

    @Mock
    var teaMemoryDatabase: TeaMemoryDatabase? = null

    @Mock
    var noteDao: NoteDao? = null

    private var noteRepository: NoteRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase!!.noteDao).thenReturn(noteDao)

        noteRepository = NoteRepository(Application())
    }

    @Test
    fun insertNote() {
        val note = Note()

        noteRepository!!.insertNote(note)

        verify(noteDao)?.insert(note)
    }

    @Test
    fun updateNote() {
        val note = Note()

        noteRepository!!.updateNote(note)

        verify(noteDao)?.update(note)
    }

    @Test
    fun getNotes() {
        `when`(noteDao!!.notes).thenReturn(listOf(Note(), Note()))

        val notes = noteRepository!!.notes

        verify(noteDao)?.notes
        assertThat(notes).hasSize(2)
    }

    @Test
    fun getNoteByTeaIdAndPosition() {
        val teaId: Long = 2
        val note = Note()
        `when`(noteDao!!.getNoteByTeaIdAndPosition(teaId, 1)).thenReturn(note)

        val noteByTeaId = noteRepository!!.getNoteByTeaIdAndPosition(teaId, 1)

        assertThat(noteByTeaId).isEqualTo(note)
    }

    @Test
    fun getNotesByTeaIdAndPositionBiggerZero() {
        val teaId: Long = 2
        val notes = listOf(Note(), Note())
        `when`(noteDao!!.getNotesByTeaIdAndPositionBiggerZero(teaId)).thenReturn(notes)

        val notesByTeaId = noteRepository!!.getNotesByTeaIdAndPositionBiggerZero(teaId)

        assertThat(notesByTeaId).isEqualTo(notes)
    }

    @Test
    fun deleteNoteByTeaIdAndPosition() {
        val teaId: Long = 2
        val position = 1

        noteRepository!!.deleteNoteByTeaIdAndPosition(teaId, position)

        verify(noteDao)?.deleteNoteByTeaIdAndPosition(teaId, position)
    }
}