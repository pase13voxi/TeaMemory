package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.Application;

import java.util.Calendar;
import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.models.repository.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.views.utils.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.views.utils.RefreshCounter;

class ShowTeaViewModel {

    private final Application application;

    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final NoteRepository noteRepository;
    private final CounterRepository counterRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private final long teaId;
    private int infusionIndex = 0;

    ShowTeaViewModel(long teaId, Application application) {
        this.teaId = teaId;
        this.application = application;

        teaRepository = new TeaRepository(application);
        infusionRepository = new InfusionRepository(application);
        noteRepository = new NoteRepository(application);
        counterRepository = new CounterRepository(application);
        actualSettingsRepository = new ActualSettingsRepository(application);
    }

    // Tea
    boolean teaExists() {
        return teaRepository.getTeaById(teaId) != null;
    }

    long getTeaId() {
        return teaRepository.getTeaById(teaId).getId();
    }

    String getName() {
        return teaRepository.getTeaById(teaId).getName();
    }

    String getVariety() {
        if (teaRepository.getTeaById(teaId).getVariety().equals("")) {
            return "-";
        } else {
            return LanguageConversation.convertCodeToVariety(teaRepository.getTeaById(teaId).getVariety(), application);
        }
    }

    int getAmount() {
        return teaRepository.getTeaById(teaId).getAmount();
    }

    String getAmountKind() {
        return teaRepository.getTeaById(teaId).getAmountKind();
    }

    int getColor() {
        return teaRepository.getTeaById(teaId).getColor();
    }

    void setCurrentDate() {
        Tea tea = teaRepository.getTeaById(teaId);
        tea.setDate(Calendar.getInstance().getTime());
        teaRepository.updateTea(tea);
    }

    int getNextInfusion() {
        return teaRepository.getTeaById(teaId).getNextInfusion();
    }

    void updateNextInfusion() {
        Tea tea = teaRepository.getTeaById(teaId);
        if ((infusionIndex + 1) >= getInfusionSize()) {
            tea.setNextInfusion(0);
        } else {
            tea.setNextInfusion(infusionIndex + 1);
        }
        teaRepository.updateTea(tea);
    }

    // Infusion
    TimeHelper getTime() {
        return TimeHelper.getMinutesAndSeconds(infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTime());
    }

    TimeHelper getCooldowntime() {
        return TimeHelper.getMinutesAndSeconds(infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getCoolDownTime());
    }

    int getTemperature() {
        if (getTemperatureunit().equals("Celsius")) {
            return infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperatureCelsius();
        } else {
            return infusionRepository.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperatureFahrenheit();
        }
    }

    int getInfusionSize() {
        return infusionRepository.getInfusionsByTeaId(teaId).size();
    }

    int getInfusionIndex() {
        return infusionIndex;
    }

    void setInfusionIndex(int infusionIndex) {
        this.infusionIndex = infusionIndex;
    }

    void incrementInfusionIndex() {
        infusionIndex++;
    }

    // Notes
    Note getNote() {
        return noteRepository.getNoteByTeaId(teaId);
    }

    void setNote(String noteText) {
        Note note = noteRepository.getNoteByTeaId(teaId);
        note.setDescription(noteText);
        noteRepository.updateNote(note);
    }

    //Counter
    void countCounter() {
        Counter counter = counterRepository.getCounterByTeaId(teaId);
        RefreshCounter.refreshCounter(counter);
        Date currentDate = Calendar.getInstance().getTime();
        counter.setMonthDate(currentDate);
        counter.setWeekDate(currentDate);
        counter.setDayDate(currentDate);
        counter.setOverall(counter.getOverall() + 1);
        counter.setMonth(counter.getMonth() + 1);
        counter.setWeek(counter.getWeek() + 1);
        counter.setDay(counter.getDay() + 1);
        counterRepository.updateCounter(counter);
    }

    public Counter getCounter() {
        Counter counter = counterRepository.getCounterByTeaId(teaId);
        RefreshCounter.refreshCounter(counter);
        counterRepository.updateCounter(counter);
        return counter;
    }

    // Settings
    boolean isAnimation() {
        return actualSettingsRepository.getSettings().isAnimation();
    }

    boolean isShowteaalert() {
        return actualSettingsRepository.getSettings().isShowTeaAlert();
    }

    void setShowteaalert(boolean showteaalert) {
        ActualSettings actualSettings = actualSettingsRepository.getSettings();
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getTemperatureunit() {
        return actualSettingsRepository.getSettings().getTemperatureUnit();
    }
}
