package coolpharaoh.tee.speicher.tea.timer.core.note;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(Application application) {
        TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(application);
        noteDao = database.getNoteDao();
    }

    public void insertNote(Note note) {
        noteDao.insert(note);
    }

    public void updateNote(Note note) {
        noteDao.update(note);
    }

    public List<Note> getNotes() {
        return noteDao.getNotes();
    }

    public Note getNoteByTeaId(long teaId) {
        return noteDao.getNoteByTeaId(teaId);
    }

    NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
