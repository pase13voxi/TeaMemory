package coolpharaoh.tee.speicher.tea.timer.core.note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteRepositoryTest {
    @Mock
    NoteDao noteDao;

    private NoteRepository noteRepository;

    @Before
    public void setUp() {
        noteRepository = new NoteRepository(noteDao);
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
    public void getNoteByTeaId() {
        int teaId = 2;
        Note note = new Note();
        when(noteDao.getNoteByTeaId(teaId)).thenReturn(note);

        Note noteByTeaId = noteRepository.getNoteByTeaId(teaId);

        assertThat(noteByTeaId).isEqualTo(note);
    }
}
