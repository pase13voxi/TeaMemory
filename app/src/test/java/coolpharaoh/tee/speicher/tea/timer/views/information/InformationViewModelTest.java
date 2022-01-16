package coolpharaoh.tee.speicher.tea.timer.views.information;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.res.Resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@ExtendWith({MockitoExtension.class, TaskExecutorExtension.class})
class InformationViewModelTest {

    private static final long TEA_ID = 1L;
    private static final String HEADER = "header";
    private static final String DESCRIPTION = "description";
    @Mock
    NoteRepository noteRepository;
    @Mock
    TeaRepository teaRepository;
    @Mock
    CounterRepository counterRepository;
    @Mock
    Application application;
    @Mock
    Resources resources;

    @Test
    void getTeaId() {
        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getTeaId()).isEqualTo(TEA_ID);
    }

    @Test
    void getTeaName() {
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getTeaName()).isEqualTo(tea.getName());
    }

    @Test
    void getTeaVariety() {
        final String[] varieties = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
        when(application.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varieties);

        final Tea tea = new Tea("name", "03_yellow", 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getVarietyAsText()).isEqualTo("Yellow tea");
    }

    @Test
    void getTeaRating() {
        final int rating = 3;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        tea.setRating(rating);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getTeaRating()).isEqualTo(tea.getRating());
    }

    @Test
    void updateTeaRating() {
        final int rating = 3;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.updateTeaRating(rating);

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea(captor.capture());

        assertThat(captor.getValue().getRating()).isEqualTo(rating);
    }

    @Test
    void isTeaInStock() {
        final boolean inStock = true;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        tea.setInStock(inStock);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.isInStock()).isEqualTo(tea.isInStock());
    }

    @Test
    void updateTeaInStock() {
        final boolean inStock = true;
        final Tea tea = new Tea("name", null, 0, null, 0, 0, null);
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.updateTeaInStock(inStock);

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea(captor.capture());

        assertThat(captor.getValue().isInStock()).isEqualTo(inStock);
    }

    @Test
    void getDate() {
        final Tea tea = new Tea("name", null, 0, null, 0, 0, CurrentDate.getDate());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getDate()).isEqualTo(tea.getDate());
    }

    @Test
    void getImageUri() {
        final Tea tea = new Tea("name", null, 0, null, 0, 0, CurrentDate.getDate());
        tea.setImageUri("imageUri");
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getImageUri()).isEqualTo(tea.getImageUri());
    }

    @Test
    void updateImageUri() {
        final String imageUri = "imageUri";
        final Tea tea = new Tea("name", null, 0, null, 0, 0, CurrentDate.getDate());
        when(teaRepository.getTeaById(TEA_ID)).thenReturn(tea);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.updateImageUri(imageUri);

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).updateTea(captor.capture());

        assertThat(captor.getValue().getImageUri()).isEqualTo(imageUri);
    }

    @Test
    void getDetails() {
        final List<Note> notes = Arrays.asList(new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                new Note(TEA_ID, 1, HEADER, DESCRIPTION));

        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getDetails().getValue()).isEqualTo(notes);
    }

    @Test
    void getDetail() {
        final int position = 0;

        final List<Note> notes = Arrays.asList(new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                new Note(TEA_ID, 1, HEADER, DESCRIPTION));

        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(notes);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getDetail(position)).isEqualTo(notes.get(0));
    }

    @Test
    void addDetail() {
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(Collections.emptyList());

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.addDetail(HEADER, DESCRIPTION);

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).insertNote(captor.capture());
        assertThat(captor.getValue())
                .extracting(Note::getTeaId, Note::getPosition, Note::getHeader, Note::getDescription)
                .contains(TEA_ID, 0, HEADER, DESCRIPTION);
    }

    @Test
    void updateDetail() {
        final String anotherHeader = "AnotherHeader";
        final String anotherDescription = "AnotherDescription";
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
                .thenReturn(Collections.singletonList(new Note(TEA_ID, 0, HEADER, DESCRIPTION)));

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.updateDetail(0, anotherHeader, anotherDescription);

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue())
                .extracting(Note::getHeader, Note::getDescription)
                .contains(anotherHeader, anotherDescription);
    }

    @Test
    void deleteDetail() {
        final int index = 1;
        when(noteRepository.getNotesByTeaIdAndPositionBiggerZero(TEA_ID))
                .thenReturn(Arrays.asList(new Note(TEA_ID, 0, HEADER, DESCRIPTION),
                        new Note(TEA_ID, 2, HEADER, DESCRIPTION)));

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.deleteDetail(index);

        verify(noteRepository).deleteNoteByTeaIdAndPosition(TEA_ID, index);
        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue().getPosition()).isEqualTo(1);
    }

    @Test
    void getNotes() {
        final Note note = new Note();
        when(noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1)).thenReturn(note);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getNotes()).isEqualTo(note);
    }

    @Test
    void getNotesAndNotesAreNull() {
        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);

        assertThat(informationViewModel.getNotes())
                .extracting(
                        Note::getPosition,
                        Note::getHeader,
                        Note::getDescription)
                .containsExactly(-1, "01_notes", "");
    }

    @Test
    void updateNotes() {
        final String newNotes = "changed Notes";

        when(noteRepository.getNoteByTeaIdAndPosition(TEA_ID, -1)).thenReturn(new Note());

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.updateNotes(newNotes);

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).updateNote(captor.capture());
        assertThat(captor.getValue().getDescription()).isEqualTo(newNotes);
    }

    @Test
    void getCounter() {
        final Date currentDate = CurrentDate.getDate();
        final Counter counterBefore = new Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate);
        when(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore);

        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        final Counter counterAfter = informationViewModel.getCounter();

        assertThat(counterAfter).isEqualTo(counterBefore);
    }

    @Test
    void getCounterAndCounterIsNull() {
        final InformationViewModel informationViewModel = new InformationViewModel(TEA_ID, teaRepository, noteRepository, counterRepository, application);
        informationViewModel.getCounter();

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).updateCounter((captor.capture()));
        final Counter counterAfter = captor.getValue();

        assertThat(counterAfter)
                .extracting(Counter::getDay, Counter::getWeek, Counter::getMonth, Counter::getOverall)
                .containsExactly(0, 0, 0, 0L);
    }
}