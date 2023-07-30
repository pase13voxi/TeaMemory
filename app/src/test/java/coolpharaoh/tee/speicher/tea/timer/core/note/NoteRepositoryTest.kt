package coolpharaoh.tee.speicher.tea.timer.core.note

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class NoteRepositoryTest {
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var noteDao: NoteDao

    private var noteRepository: NoteRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.noteDao } returns noteDao

        noteRepository = NoteRepository(Application())
    }

    @Test
    fun insertNote() {
        val note = Note()

        noteRepository!!.insertNote(note)

        verify { noteDao.insert(note) }
    }

    @Test
    fun updateNote() {
        val note = Note()

        noteRepository!!.updateNote(note)

        verify { noteDao.update(note) }
    }

    @Test
    fun getNotes() {
        every { noteDao.notes } returns listOf(Note(), Note())

        val notes = noteRepository!!.notes

        verify { noteDao.notes }
        assertThat(notes).hasSize(2)
    }

    @Test
    fun getNoteByTeaIdAndPosition() {
        val teaId: Long = 2
        val note = Note()
        every { noteDao.getNoteByTeaIdAndPosition(teaId, 1) } returns note

        val noteByTeaId = noteRepository!!.getNoteByTeaIdAndPosition(teaId, 1)

        assertThat(noteByTeaId).isEqualTo(note)
    }

    @Test
    fun getNotesByTeaIdAndPositionBiggerZero() {
        val teaId: Long = 2
        val notes = listOf(Note(), Note())
        every { noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId) } returns notes

        val notesByTeaId = noteRepository!!.getNotesByTeaIdAndPositionBiggerZero(teaId)

        assertThat(notesByTeaId).isEqualTo(notes)
    }

    @Test
    fun deleteNoteByTeaIdAndPosition() {
        val teaId: Long = 2
        val position = 1

        noteRepository!!.deleteNoteByTeaIdAndPosition(teaId, position)

        verify { noteDao.deleteNoteByTeaIdAndPosition(teaId, position) }
    }
}