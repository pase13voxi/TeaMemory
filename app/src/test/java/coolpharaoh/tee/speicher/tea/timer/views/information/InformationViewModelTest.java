package coolpharaoh.tee.speicher.tea.timer.views.information;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import edu.emory.mathcs.backport.java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InformationViewModelTest {

    private static final long TEA_ID = 1L;
    private static final String HEADER = "header";
    private static final String DESCRIPTION = "description";
    @Mock
    NoteRepository noteRepository;
    @Mock
    TeaRepository teaRepository;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void getTeaName() {
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);

        assertThat(informationViewModel.getTeaName()).isEqualTo(tea.getName());
    }

    @Test
    public void getTeaRating() {
        final int rating = 3;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        tea.setRating(rating);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);

        assertThat(informationViewModel.getTeaRating()).isEqualTo(tea.getRating());
    }

    @Test
    public void updateTeaRating() {
        final int rating = 3;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);
        informationViewModel.updateTeaRating(rating);

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea(captor.capture());

        assertThat(captor.getValue().getRating()).isEqualTo(rating);
    }

    @Test
    public void getDetails() {
        List<Note> notes = Arrays.asList(new Note[]{new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                new Note(TEA_ID, 1, HEADER, DESCRIPTION)});

        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes);

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);

        assertThat(informationViewModel.getDetails().getValue()).isEqualTo(notes);
    }

    @Test
    public void getDetail() {
        final int position = 0;

        List<Note> notes = Arrays.asList(new Note[]{new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                new Note(TEA_ID, 1, HEADER, DESCRIPTION)});

        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes);

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);

        assertThat(informationViewModel.getDetail(position)).isEqualTo(notes.get(0));
    }

    @Test
    public void addDetail() {
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(Collections.emptyList());

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);
        informationViewModel.addDetail(HEADER, DESCRIPTION);

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).insertNote(captor.capture());
        assertThat(captor.getValue())
                .extracting(Note::getTeaId, Note::getPosition, Note::getHeader, Note::getDescription)
                .contains(TEA_ID, 0, HEADER, DESCRIPTION);
    }

    @Test
    public void updateDetail() {
        final String anotherHeader = "AnotherHeader";
        final String anotherDescription = "AnotherDescription";
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
                .thenReturn(Arrays.asList(new Note[]{new Note(TEA_ID, 0, HEADER, DESCRIPTION)}));

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);
        informationViewModel.updateDetail(0, anotherHeader, anotherDescription);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue())
                .extracting(Note::getHeader, Note::getDescription)
                .contains(anotherHeader, anotherDescription);
    }

    @Test
    public void deleteDetail() {
        int index = 1;
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
                .thenReturn(Arrays.asList(new Note[]{new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                        new Note(TEA_ID, 2, HEADER, DESCRIPTION)}));

        InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository);
        informationViewModel.deleteDetail(index);

        verify(noteRepository).deleteNoteByTeaIdAndPosition(TEA_ID, index);
        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue().getPosition()).isEqualTo(1);
    }

    @Test
    public void getNotes() {
        long teaId = TEA_ID;
        Note note = new Note();
        when(noteRepository.getNoteByTeaIdAndPosition(teaId, -1)).thenReturn(note);

        InformationViewModel informationViewModel = new InformationViewModel(teaId, teaRepository, noteRepository);

        assertThat(informationViewModel.getNotes()).isEqualTo(note);
    }

    @Test
    public void updateNotes() {
        String newNotes = "changed Notes";

        long teaId = TEA_ID;
        when(noteRepository.getNoteByTeaIdAndPosition(teaId, -1)).thenReturn(new Note());

        InformationViewModel informationViewModel = new InformationViewModel(teaId, teaRepository, noteRepository);
        informationViewModel.updateNotes(newNotes);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue().getDescription()).isEqualTo(newNotes);
    }
}