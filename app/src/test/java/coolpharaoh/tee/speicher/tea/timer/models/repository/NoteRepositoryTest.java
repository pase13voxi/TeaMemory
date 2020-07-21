package coolpharaoh.tee.speicher.tea.timer.models.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.models.*")
public class NoteRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    NoteDao noteDao;

    private NoteRepository noteRepository;

    @Before
    public void setUp() {
        mockNoteDao();
        noteRepository = new NoteRepository(null);
    }

    private void mockNoteDao() {
        initMocks(TeaMemoryDatabase.class);
        mockStatic(TeaMemoryDatabase.class);
        when(TeaMemoryDatabase.getDatabaseInstance(any())).thenReturn(teaMemoryDatabase);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
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
