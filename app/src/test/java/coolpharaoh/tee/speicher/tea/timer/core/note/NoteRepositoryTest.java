package coolpharaoh.tee.speicher.tea.timer.core.note;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

@ExtendWith(MockitoExtension.class)
class NoteRepositoryTest {

    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    NoteDao noteDao;

    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);

        noteRepository = new NoteRepository(new Application());
    }

    @Test
    void insertNote() {
        final Note note = new Note();

        noteRepository.insertNote(note);

        verify(noteDao).insert(note);
    }

    @Test
    void updateNote() {
        final Note note = new Note();

        noteRepository.updateNote(note);

        verify(noteDao).update(note);
    }

    @Test
    void getNotes() {
        when(noteDao.getNotes()).thenReturn(Arrays.asList(new Note(), new Note()));

        final List<Note> notes = noteRepository.getNotes();

        verify(noteDao).getNotes();
        assertThat(notes).hasSize(2);
    }

    @Test
    void getNoteByTeaIdAndPosition() {
        final long teaId = 2;
        final Note note = new Note();
        when(noteDao.getNoteByTeaIdAndPosition(teaId, 1)).thenReturn(note);

        final Note noteByTeaId = noteRepository.getNoteByTeaIdAndPosition(teaId, 1);

        assertThat(noteByTeaId).isEqualTo(note);
    }

    @Test
    void getNotesByTeaIdAndPositionBiggerZero() {
        final long teaId = 2;
        final List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteDao.getNotesByTeaIdAndPositionBiggerZero(teaId)).thenReturn(notes);

        final List<Note> notesByTeaId = noteRepository.getNotesByTeaIdAndPositionBiggerZero(teaId);

        assertThat(notesByTeaId).isEqualTo(notes);
    }

    @Test
    void deleteNoteByTeaIdAndPosition() {
        final long teaId = 2;
        final int position = 1;

        noteRepository.deleteNoteByTeaIdAndPosition(teaId, position);

        verify(noteDao).deleteNoteByTeaIdAndPosition(teaId, position);
    }
}
