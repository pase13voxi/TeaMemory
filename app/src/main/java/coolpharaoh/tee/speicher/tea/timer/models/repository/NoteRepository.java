package coolpharaoh.tee.speicher.tea.timer.models.repository;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(Application application) {
        TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(application);
        noteDao = database.getNoteDAO();
    }

    public void insert(Note note) {
        noteDao.insert(note);
    }

    public void update(Note note) {
        noteDao.update(note);
    }

    public List<Note> getNotes() {
        return noteDao.getNotes();
    }

    public Note getNoteByTeaId(long teaId) {
        return noteDao.getNoteByTeaId(teaId);
    }
}
