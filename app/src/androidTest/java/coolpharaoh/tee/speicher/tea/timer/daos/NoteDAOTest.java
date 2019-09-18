package coolpharaoh.tee.speicher.tea.timer.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

import static org.junit.Assert.assertEquals;

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
        assertEquals(mNoteDAO.getNotes().size(), 0);

        mTeaDAO.insert(createTea());
        long teaId = mTeaDAO.getTeas().get(0).getId();

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDAO.insert(noteBefore);

        assertEquals(mNoteDAO.getNotes().size(), 1);

        Note noteAfter = mNoteDAO.getNotes().get(0);
        assertEquals(noteAfter.getTeaId(), noteBefore.getTeaId());
        assertEquals(noteAfter.getPosition(), noteBefore.getPosition());
        assertEquals(noteAfter.getHeader(), noteBefore.getHeader());
        assertEquals(noteAfter.getDescription(), noteBefore.getDescription());
    }

    @Test
    public void updateNote(){
        assertEquals(mNoteDAO.getNotes().size(), 0);

        mTeaDAO.insert(createTea());
        long teaId = mTeaDAO.getTeas().get(0).getId();

        Note noteBefore = new Note(teaId, 1, "header", "description");
        mNoteDAO.insert(noteBefore);

        assertEquals(mNoteDAO.getNotes().size(), 1);

        Note noteUpdate = mNoteDAO.getNoteByTeaId(teaId);
        noteUpdate.setPosition(2);
        noteUpdate.setHeader("HeaderChanged");
        noteUpdate.setDescription("DesciptionChanged");
        mNoteDAO.update(noteUpdate);

        Note noteAfter = mNoteDAO.getNoteByTeaId(teaId);
        assertEquals(noteAfter.getTeaId(), noteUpdate.getTeaId());
        assertEquals(noteAfter.getPosition(), noteUpdate.getPosition());
        assertEquals(noteAfter.getHeader(), noteUpdate.getHeader());
        assertEquals(noteAfter.getDescription(), noteUpdate.getDescription());
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
