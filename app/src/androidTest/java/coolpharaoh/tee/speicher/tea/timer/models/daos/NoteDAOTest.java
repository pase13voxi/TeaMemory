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
public class NoteDAOTest {
    private NoteDAO mNoteDAO;
    private TeaDAO mTeaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mNoteDAO = db.getNoteDAO();
        mTeaDAO = db.getTeaDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertNote(){
        assertThat(mNoteDAO.getNotes()).hasSize(0);

        long teaId = mTeaDAO.insert(createTea());

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDAO.insert(noteBefore);

        assertThat(mNoteDAO.getNotes()).hasSize(1);

        Note noteAfter = mNoteDAO.getNotes().get(0);
        assertThat(noteAfter).isEqualToIgnoringGivenFields(noteBefore, "id");
    }

    @Test
    public void updateNote(){
        assertThat(mNoteDAO.getNotes()).hasSize(0);

        long teaId = mTeaDAO.insert(createTea());

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDAO.insert(noteBefore);

        assertThat(mNoteDAO.getNotes()).hasSize(1);

        Note noteUpdate = mNoteDAO.getNoteByTeaId(teaId);
        noteUpdate.setPosition(2);
        noteUpdate.setHeader("HeaderChanged");
        noteUpdate.setDescription("DesciptionChanged");
        mNoteDAO.update(noteUpdate);

        Note noteAfter = mNoteDAO.getNoteByTeaId(teaId);
        assertThat(noteAfter).isEqualToComparingFieldByField(noteUpdate);
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
