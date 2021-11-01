package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class DataTransformViewModel extends ViewModel {

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final CounterRepository counterRepository;
    private final NoteRepository noteRepository;

    DataTransformViewModel(final Application application) {
        this(new TeaRepository(application), new InfusionRepository(application),
                new CounterRepository(application), new NoteRepository(application));
    }

    DataTransformViewModel(final TeaRepository teaRepository, final InfusionRepository infusionRepository,
                           final CounterRepository counterRepository, final NoteRepository noteRepository) {
        this.teaRepository = teaRepository;
        this.infusionRepository = infusionRepository;
        this.counterRepository = counterRepository;
        this.noteRepository = noteRepository;
    }

    //Teas
    List<Tea> getTeaList() {
        return teaRepository.getTeas();
    }

    long insertTea(final Tea tea) {
        return teaRepository.insertTea(tea);
    }

    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }

    //Infusions
    List<Infusion> getInfusionList() {
        return infusionRepository.getInfusions();
    }

    public void insertInfusion(final Infusion infusion) {
        infusionRepository.insertInfusion(infusion);
    }

    //Counters
    List<Counter> getCounterList() {
        return counterRepository.getCounters();
    }

    void insertCounter(final Counter counter) {
        counterRepository.insertCounter(counter);
    }

    //Notes
    List<Note> getNoteList() {
        return noteRepository.getNotes();
    }

    void insertNote(final Note note) {
        noteRepository.insertNote(note);

    }

}
