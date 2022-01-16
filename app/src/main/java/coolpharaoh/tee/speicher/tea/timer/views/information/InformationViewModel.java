package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class InformationViewModel extends ViewModel {

    private final Application application;
    private final TeaRepository teaRepository;
    private final NoteRepository noteRepository;
    private final CounterRepository counterRepository;
    private final long teaId;
    private final MutableLiveData<List<Note>> details;

    InformationViewModel(final long teaId, final Application application) {
        this(teaId, new TeaRepository(application), new NoteRepository(application),
                new CounterRepository(application), application);
    }

    @VisibleForTesting
    InformationViewModel(final long teaId, final TeaRepository teaRepository, final NoteRepository noteRepository,
                         final CounterRepository counterRepository, final Application application) {
        this.application = application;
        this.teaRepository = teaRepository;
        this.noteRepository = noteRepository;
        this.counterRepository = counterRepository;
        this.teaId = teaId;
        details = new MutableLiveData<>();
        // Notes with position over 0 contains tea information
        updateDetails(teaId, noteRepository);
    }

    // Teas
    public long getTeaId() {
        return teaId;
    }

    String getTeaName() {
        final Tea tea = teaRepository.getTeaById(teaId);
        if (tea == null) {
            throw new NoSuchElementException("No tea found for tea id " + teaId);
        } else {
            return tea.getName();
        }
    }

    String getVarietyAsText() {
        final Tea tea = teaRepository.getTeaById(teaId);
        return Variety.convertStoredVarietyToText(tea.getVariety(), application);
    }

    int getTeaRating() {
        final Tea tea = teaRepository.getTeaById(teaId);
        return tea.getRating();
    }

    void updateTeaRating(final int rating) {
        final Tea tea = teaRepository.getTeaById(teaId);
        tea.setRating(rating);
        teaRepository.updateTea(tea);
    }

    boolean isInStock() {
        final Tea tea = teaRepository.getTeaById(teaId);
        return tea.isInStock();
    }

    void updateTeaInStock(final boolean inStock) {
        final Tea tea = teaRepository.getTeaById(teaId);
        tea.setInStock(inStock);
        teaRepository.updateTea(tea);
    }

    Date getDate() {
        final Tea tea = teaRepository.getTeaById(teaId);
        return tea.getDate();
    }

    public String getImageUri() {
        final Tea tea = teaRepository.getTeaById(teaId);
        return tea.getImageUri();
    }

    public void updateImageUri(final String imageUri) {
        final Tea tea = teaRepository.getTeaById(teaId);
        tea.setImageUri(imageUri);
        teaRepository.updateTea(tea);
    }

    // Notes
    LiveData<List<Note>> getDetails() {
        return details;
    }

    Note getDetail(final int position) {
        return details.getValue().get(position);
    }

    void addDetail(final String header, final String description) {
        final Note note = new Note(teaId, details.getValue().size(), header, description);
        noteRepository.insertNote(note);
        updateDetails(teaId, noteRepository);
    }

    void updateDetail(final int position, final String header, final String description) {
        final Note note = details.getValue().get(position);
        note.setHeader(header);
        note.setDescription(description);
        noteRepository.updateNote(note);
        updateDetails(teaId, noteRepository);
    }

    void deleteDetail(final int index) {
        noteRepository.deleteNoteByTeaIdAndPosition(teaId, index);
        updateDetails(teaId, noteRepository);
        for (int i = index; i < details.getValue().size(); i++) {
            details.getValue().get(i).setPosition(i);
            noteRepository.updateNote(details.getValue().get(i));
        }
    }

    Note getNotes() {
        // Note with position -1 contains notes
        Note notes = noteRepository.getNoteByTeaIdAndPosition(teaId, -1);
        if (notes == null) {
            notes = new Note(teaId, -1, "01_notes", "");
            noteRepository.insertNote(notes);
        }
        return notes;
    }

    void updateNotes(final String insertedNotes) {
        // Note with position -1 contains notes
        final Note notes = noteRepository.getNoteByTeaIdAndPosition(teaId, -1);
        notes.setDescription(insertedNotes);

        noteRepository.updateNote(notes);
    }

    private void updateDetails(final long teaId, final NoteRepository noteRepository) {
        details.setValue(noteRepository.getNotesByTeaIdAndPositionBiggerZero(teaId));
    }

    // Counter
    public Counter getCounter() {
        final Counter counter = getOrCreateCounter();
        RefreshCounter.refreshCounter(counter);
        counterRepository.updateCounter(counter);
        return counter;
    }

    private Counter getOrCreateCounter() {
        Counter counter = counterRepository.getCounterByTeaId(teaId);
        if (counter == null) {
            counter = new Counter();
            counter.setTeaId(teaId);
            counter.setDay(0);
            counter.setWeek(0);
            counter.setMonth(0);
            counter.setOverall(0);
            counter.setDayDate(CurrentDate.getDate());
            counter.setWeekDate(CurrentDate.getDate());
            counter.setMonthDate(CurrentDate.getDate());
            counter.setId(counterRepository.insertCounter(counter));
        }

        return counter;
    }
}
