package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@ExtendWith(MockitoExtension.class)
class DataTransformViewModelTest {

    private DataTransformViewModel dataTransformViewModel;

    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    CounterRepository counterRepository;
    @Mock
    NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        dataTransformViewModel = new DataTransformViewModel(teaRepository, infusionRepository,
                counterRepository, noteRepository);
    }

    @Test
    void getTeaList() {
        final Date date = new GregorianCalendar(2020, 1, 18).getTime();

        final List<Tea> teasBefore = new ArrayList<>();
        final Tea tea1 = new Tea("Tea1", "Variety1", 1, "Kind1", 1, 1, date);
        teasBefore.add(tea1);
        final Tea tea2 = new Tea("Tea2", "Variety2", 2, "Kind2", 2, 2, date);
        teasBefore.add(tea2);
        when(teaRepository.getTeas()).thenReturn(teasBefore);

        final List<Tea> teasAfter = dataTransformViewModel.getTeaList();

        assertThat(teasAfter).isEqualTo(teasBefore);
    }

    @Test
    void insertTea() {
        final Date date = new GregorianCalendar(2020, 1, 18).getTime();

        final Tea teaBefore = new Tea("Tea", "Variety", 1, "Kind", 1, 1, date);

        dataTransformViewModel.insertTea(teaBefore);

        final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaRepository).insertTea(captor.capture());
        final Tea teaAfter = captor.getValue();

        assertThat(teaAfter).isEqualTo(teaBefore);
    }

    @Test
    void deleteAll() {
        dataTransformViewModel.deleteAllTeas();
        verify(teaRepository).deleteAllTeas();
    }

    @Test
    void getInfusionList() {
        final List<Infusion> infusionsBefore = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1L, 1, "1", "1", 1, 1);
        infusionsBefore.add(infusion1);
        final Infusion infusion2 = new Infusion(2L, 2, "2", "2", 2, 2);
        infusionsBefore.add(infusion2);

        when(infusionRepository.getInfusions()).thenReturn(infusionsBefore);

        final List<Infusion> infusionsAfter = dataTransformViewModel.getInfusionList();

        assertThat(infusionsAfter).isEqualTo(infusionsBefore);
    }

    @Test
    void insertInfusion() {
        final Infusion infusionBefore = new Infusion(1L, 1, "1", "1", 1, 1);

        dataTransformViewModel.insertInfusion(infusionBefore);

        final ArgumentCaptor<Infusion> captor = ArgumentCaptor.forClass(Infusion.class);
        verify(infusionRepository).insertInfusion(captor.capture());
        final Infusion infusionAfter = captor.getValue();

        assertThat(infusionAfter).isEqualTo(infusionBefore);
    }

    @Test
    void getCounterList() {
        final Date date = new GregorianCalendar(2020, 1, 18).getTime();

        final List<Counter> countersBefore = new ArrayList<>();
        final Counter counter1 = new Counter(1L, 1, 1, 1, 1L, date, date, date);
        countersBefore.add(counter1);
        final Counter counter2 = new Counter(2L, 2, 2, 2, 2L, date, date, date);
        countersBefore.add(counter2);

        when(counterRepository.getCounters()).thenReturn(countersBefore);

        final List<Counter> countersAfter = dataTransformViewModel.getCounterList();

        assertThat(countersAfter).isEqualTo(countersBefore);
    }

    @Test
    void insertCounter() {
        final Date date = new GregorianCalendar(2020, 1, 18).getTime();
        final Counter counterBefore = new Counter(1L, 1, 1, 1, 1L, date, date, date);

        dataTransformViewModel.insertCounter(counterBefore);

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository).insertCounter(captor.capture());
        final Counter counterAfter = captor.getValue();

        assertThat(counterAfter).isEqualTo(counterBefore);
    }

    @Test
    void getNoteList() {
        final List<Note> notesBefore = new ArrayList<>();
        final Note note1 = new Note(1L, 1, "Header1", "Description1");
        notesBefore.add(note1);
        final Note note2 = new Note(2L, 2, "Header2", "Description2");
        notesBefore.add(note2);

        when(noteRepository.getNotes()).thenReturn(notesBefore);

        final List<Note> notesAfter = dataTransformViewModel.getNoteList();

        assertThat(notesAfter).isEqualTo(notesBefore);
    }

    @Test
    void insertNote() {
        final Note noteBefore = new Note(1L, 1, "Header", "Description");

        dataTransformViewModel.insertNote(noteBefore);

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).insertNote(captor.capture());
        final Note noteAfter = captor.getValue();

        assertThat(noteAfter).isEqualTo(noteBefore);
    }
}
