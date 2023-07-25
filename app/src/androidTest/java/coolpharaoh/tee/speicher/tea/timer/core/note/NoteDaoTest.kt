package coolpharaoh.tee.speicher.tea.timer.core.note

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    private var mNoteDao: NoteDao? = null
    private var mTeaDao: TeaDao? = null
    private var db: TeaMemoryDatabase? = null
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, TeaMemoryDatabase::class.java).build()
        mNoteDao = db!!.noteDao
        mTeaDao = db!!.teaDao
    }

    @After
    fun closeDb() {
        db!!.close()
    }

    @Test
    fun insertNote() {
        assertThat(mNoteDao!!.notes).hasSize(0)

        val teaId = mTeaDao!!.insert(createTea())

        val noteBefore = Note(teaId, 1, HEADER, DESCRIPTION)
        mNoteDao!!.insert(noteBefore)

        assertThat(mNoteDao!!.notes).hasSize(1)

        val noteAfter = mNoteDao!!.notes[0]
        assertThat(noteAfter).usingRecursiveComparison().ignoringFields("id").isEqualTo(noteBefore)
    }

    @Test
    fun updateNote() {
        assertThat(mNoteDao!!.notes).hasSize(0)

        val teaId = mTeaDao!!.insert(createTea())

        val noteBefore = Note(teaId, 1, HEADER, DESCRIPTION)
        mNoteDao!!.insert(noteBefore)

        assertThat(mNoteDao!!.notes).hasSize(1)

        val noteUpdate = mNoteDao!!.notes[0]
        noteUpdate.position = 2
        noteUpdate.header = "HeaderChanged"
        noteUpdate.description = "DescriptionChanged"
        mNoteDao!!.update(noteUpdate)

        val noteAfter = mNoteDao!!.notes[0]
        assertThat(noteAfter).usingRecursiveComparison().isEqualTo(noteUpdate)
    }

    @Test
    fun getNoteByTeaIdAndPosition() {
        assertThat(mNoteDao!!.notes).hasSize(0)

        val teaId = mTeaDao!!.insert(createTea())

        val notesBefore: MutableList<Note> = ArrayList()
        notesBefore.add(Note(teaId, 1, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[0])
        notesBefore.add(Note(teaId, 2, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[1])

        val noteAfter = mNoteDao!!.getNoteByTeaIdAndPosition(teaId, 2)
        assertThat(noteAfter).usingRecursiveComparison().ignoringFields("id").isEqualTo(notesBefore[1])
    }

    @Test
    fun getNotesByTeaIdAndPositionBiggerZero() {
        assertThat(mNoteDao!!.notes).hasSize(0)

        val teaId = mTeaDao!!.insert(createTea())

        val notesBefore: MutableList<Note> = ArrayList()
        notesBefore.add(Note(teaId, -1, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[0])
        notesBefore.add(Note(teaId, 1, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[1])
        notesBefore.add(Note(teaId, 2, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[2])

        val notesAfter = mNoteDao!!.getNotesByTeaIdAndPositionBiggerZero(teaId)
        assertThat(notesAfter).hasSize(2)
        assertThat(notesAfter[0]).usingRecursiveComparison().ignoringFields("id").isEqualTo(notesBefore[1])
        assertThat(notesAfter[1]).usingRecursiveComparison().ignoringFields("id").isEqualTo(notesBefore[2])
    }

    @Test
    fun deleteNoteByPosition() {
        val teaId = mTeaDao!!.insert(createTea())

        val notesBefore: MutableList<Note> = ArrayList()
        notesBefore.add(Note(teaId, 0, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[0])
        notesBefore.add(Note(teaId, 1, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[1])
        notesBefore.add(Note(teaId, 2, HEADER, DESCRIPTION))
        mNoteDao!!.insert(notesBefore[2])

        mNoteDao!!.deleteNoteByTeaIdAndPosition(teaId, 1)

        val notesAfter = mNoteDao!!.notes
        assertThat(notesAfter).hasSize(2)
        assertThat(notesAfter[0]).usingRecursiveComparison().ignoringFields("id").isEqualTo(notesBefore[0])
        assertThat(notesAfter[1]).usingRecursiveComparison().ignoringFields("id").isEqualTo(notesBefore[2])
    }

    private fun createTea(): Tea {
        return Tea("name", "variety", 3.0, "ts", 15, 0, getDate())
    }

    companion object {
        private const val HEADER = "header"
        private const val DESCRIPTION = "description"
    }
}