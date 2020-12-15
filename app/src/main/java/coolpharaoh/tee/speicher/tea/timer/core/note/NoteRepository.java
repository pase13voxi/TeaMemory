package coolpharaoh.tee.speicher.tea.timer.core.note;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;

public class NoteRepository {

    private final NoteDao noteDao;

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

    public Note getNoteByTeaIdAndPosition(long teaId, int position) {
        return noteDao.getNoteByTeaIdAndPosition(teaId, position);
    }

    public List<Note> getNotesByTeaIdAndPositionBiggerZero(long teaId) {
        return noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId);
    }

    public void deleteNoteByTeaIdAndPosition(long teaId, int position) {
        noteDao.deleteNoteByTeaIdAndPosition(teaId, position);
    }
}
