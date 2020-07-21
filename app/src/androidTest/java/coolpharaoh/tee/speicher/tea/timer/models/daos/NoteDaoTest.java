package coolpharaoh.tee.speicher.tea.timer.models.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

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

        Note noteUpdate = mNoteDao.getNoteByTeaId(teaId);
        noteUpdate.setPosition(2);
        noteUpdate.setHeader("HeaderChanged");
        noteUpdate.setDescription("DesciptionChanged");
        mNoteDao.update(noteUpdate);

        Note noteAfter = mNoteDao.getNoteByTeaId(teaId);
        assertThat(noteAfter).isEqualToComparingFieldByField(noteUpdate);
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
