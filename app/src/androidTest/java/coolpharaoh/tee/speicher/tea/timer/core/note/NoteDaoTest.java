package coolpharaoh.tee.speicher.tea.timer.core.note;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class NoteDaoTest {
    private NoteDao mNoteDao;
    private TeaDao mTeaDao;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mNoteDao = db.getNoteDao();
        mTeaDao = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertNote(){
        assertThat(mNoteDao.getNotes()).hasSize(0);

        long teaId = mTeaDao.insert(createTea());

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDao.insert(noteBefore);

        assertThat(mNoteDao.getNotes()).hasSize(1);

        Note noteAfter = mNoteDao.getNotes().get(0);
        assertThat(noteAfter).isEqualToIgnoringGivenFields(noteBefore, "id");
    }

    @Test
    public void updateNote(){
        assertThat(mNoteDao.getNotes()).hasSize(0);

        long teaId = mTeaDao.insert(createTea());

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDao.insert(noteBefore);

        assertThat(mNoteDao.getNotes()).hasSize(1);

        Note noteUpdate = mNoteDao.getNotesByTeaId(teaId);
        noteUpdate.setPosition(2);
        noteUpdate.setHeader("HeaderChanged");
        noteUpdate.setDescription("DesciptionChanged");
        mNoteDao.update(noteUpdate);

        Note noteAfter = mNoteDao.getNotesByTeaId(teaId);
        assertThat(noteAfter).isEqualToComparingFieldByField(noteUpdate);
    }

    @Test
    public void getNoteByTeaIdAndPosition() {
        assertThat(mNoteDao.getNotes()).hasSize(0);

        long teaId = mTeaDao.insert(createTea());

        List<Note> notesBefore = new ArrayList<>();
        notesBefore.add(new Note(teaId, 1, "header", "description"));
        mNoteDao.insert(notesBefore.get(0));
        notesBefore.add(new Note(teaId, 2, "header", "description"));
        mNoteDao.insert(notesBefore.get(1));

        Note noteAfter = mNoteDao.getNoteByTeaIdAndPosition(teaId, 2);
        assertThat(noteAfter).isEqualToIgnoringGivenFields(notesBefore.get(1), "id");
    }

    @Test
    public void getNotesByTeaIdAndPositionBiggerZero() {
        assertThat(mNoteDao.getNotes()).hasSize(0);

        long teaId = mTeaDao.insert(createTea());

        List<Note> notesBefore = new ArrayList<>();
        notesBefore.add(new Note(teaId, -1, "header", "description"));
        mNoteDao.insert(notesBefore.get(0));
        notesBefore.add(new Note(teaId, 1, "header", "description"));
        mNoteDao.insert(notesBefore.get(1));
        notesBefore.add(new Note(teaId, 2, "header", "description"));
        mNoteDao.insert(notesBefore.get(2));

        List<Note> notesAfter = mNoteDao.getNotesByTeaIdAndPositionBiggerZero(teaId);
        assertThat(notesAfter).hasSize(2);
        assertThat(notesAfter.get(0)).isEqualToIgnoringGivenFields(notesBefore.get(1), "id");
        assertThat(notesAfter.get(1)).isEqualToIgnoringGivenFields(notesBefore.get(2), "id");
    }

    @Test
    public void deleteNoteByPosition() {
        long teaId = mTeaDao.insert(createTea());

        List<Note> notesBefore = new ArrayList<>();
        notesBefore.add(new Note(teaId, 0, "header", "description"));
        mNoteDao.insert(notesBefore.get(0));
        notesBefore.add(new Note(teaId, 1, "header", "description"));
        mNoteDao.insert(notesBefore.get(1));
        notesBefore.add(new Note(teaId, 2, "header", "description"));
        mNoteDao.insert(notesBefore.get(2));

        mNoteDao.deleteNoteByTeaIdAndPosition(teaId, 1);

        List<Note> notesAfter = mNoteDao.getNotes();
        assertThat(notesAfter).hasSize(2);
        assertThat(notesAfter.get(0)).isEqualToIgnoringGivenFields(notesBefore.get(0), "id");
        assertThat(notesAfter.get(1)).isEqualToIgnoringGivenFields(notesBefore.get(2), "id");

    }

    private Tea createTea() {
        return new Tea("name", "variety", 3, "ts", 15, 0, CurrentDate.getDate());
    }
}
