package coolpharaoh.tee.speicher.tea.timer.core.note;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class NoteRepository {

    private final NoteDao noteDao;

    public NoteRepository(final Application application) {
        final TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(application);
        noteDao = database.getNoteDao();
    }

    public void insertNote(final Note note) {
        noteDao.insert(note);
    }

    public void updateNote(final Note note) {
        noteDao.update(note);
    }

    public List<Note> getNotes() {
        return noteDao.getNotes();
    }

    public Note getNoteByTeaIdAndPosition(final long teaId, final int position) {
        return noteDao.getNoteByTeaIdAndPosition(teaId, position);
    }

    public List<Note> getNotesByTeaIdAndPositionBiggerZero(final long teaId) {
        return noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId);
    }

    public void deleteNoteByTeaIdAndPosition(final long teaId, final int position) {
        noteDao.deleteNoteByTeaIdAndPosition(teaId, position);
    }
}
