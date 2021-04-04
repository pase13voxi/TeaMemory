package coolpharaoh.tee.speicher.tea.timer.core.note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NoteRepositoryTest {

    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    NoteDao noteDao;

    private NoteRepository noteRepository;

    @Before
    public void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);

        noteRepository = new NoteRepository(null);
    }

    @Test
    public void insertNote() {
        Note note = new Note();

        noteRepository.insertNote(note);

        verify(noteDao).insert(note);
    }

    @Test
    public void updateNote() {
        Note note = new Note();

        noteRepository.updateNote(note);

        verify(noteDao).update(note);
    }

    @Test
    public void getNotes() {
        when(noteDao.getNotes()).thenReturn(Arrays.asList(new Note(), new Note()));

        List<Note> notes = noteRepository.getNotes();

        verify(noteDao).getNotes();
        assertThat(notes).hasSize(2);
    }

    @Test
    public void getNoteByTeaIdAndPosition() {
        final long teaId = 2;
        Note note = new Note();
        when(noteDao.getNoteByTeaIdAndPosition(teaId, 1)).thenReturn(note);

        Note noteByTeaId = noteRepository.getNoteByTeaIdAndPosition(teaId, 1);

        assertThat(noteByTeaId).isEqualTo(note);
    }

    @Test
    public void getNotesByTeaIdAndPositionBiggerZero() {
        final long teaId = 2;
        List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId)).thenReturn(notes);

        List<Note> notesByTeaId = noteRepository.getNotesByTeaIdAndPositionBiggerZero(teaId);

        assertThat(notesByTeaId).isEqualTo(notes);
    }

    @Test
    public void deleteNoteByTeaIdAndPosition() {
        final long teaId = 2;
        final int position = 1;

        noteRepository.deleteNoteByTeaIdAndPosition(teaId, position);

        verify(noteDao).deleteNoteByTeaIdAndPosition(teaId, position);
    }
}
