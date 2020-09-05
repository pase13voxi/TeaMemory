package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class MainViewModel extends ViewModel {


    private final TeaRepository teaRepository;
    private final InfusionRepository infusionRepository;
    private final NoteRepository noteRepository;
    private final CounterRepository counterRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    private final MutableLiveData<List<Tea>> teas;
    private final ActualSettings actualSettings;

    MainViewModel(Application application) {

        teaRepository = new TeaRepository(application);
        infusionRepository = new InfusionRepository(application);
        noteRepository = new NoteRepository(application);
        counterRepository = new CounterRepository(application);
        actualSettingsRepository = new ActualSettingsRepository(application);

        if (actualSettingsRepository.getCountItems() == 0) {
            createDefaultTeas(application);
            createDefaultSettings();
        }

        actualSettings = actualSettingsRepository.getSettings();
        teas = new MutableLiveData<>();

        refreshTeas();
    }

    // Defaults
    private void createDefaultTeas(Context context){
        Tea ntea1 = new Tea("Earl Grey", context.getResources().getStringArray(R.array.variety_codes)[0], 5, "Ts", ColorConversation.getVarietyColor(0, context), 0, CurrentDate.getDate());
        long teaId1 = teaRepository.insertTea(ntea1);
        Infusion ninfusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
        infusionRepository.insertInfusion(ninfusion1);
        Counter ncounter1 = new Counter(teaId1, 0, 0, 0, 0, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counterRepository.insertCounter(ncounter1);
        Note nnote1 = new Note(teaId1, 1, null, "");
        noteRepository.insertNote(nnote1);

        Tea ntea2 = new Tea("Pai Mu Tan", context.getResources().getStringArray(R.array.variety_codes)[3], 4, "Ts", ColorConversation.getVarietyColor(3, context), 0, CurrentDate.getDate());
        long teaId2 = teaRepository.insertTea(ntea2);
        Infusion ninfusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
        infusionRepository.insertInfusion(ninfusion2);
        Counter ncounter2 = new Counter(teaId2, 0, 0, 0, 0, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counterRepository.insertCounter(ncounter2);
        Note nnote2 = new Note(teaId2, 1, null, "");
        noteRepository.insertNote(nnote2);

        Tea ntea3 = new Tea("Sencha", context.getResources().getStringArray(R.array.variety_codes)[1], 4, "Ts", ColorConversation.getVarietyColor(1, context), 0, CurrentDate.getDate());
        long teaId3 = teaRepository.insertTea(ntea3);
        Infusion ninfusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
        infusionRepository.insertInfusion(ninfusion3);
        Counter ncounter3 = new Counter(teaId3, 0, 0, 0, 0, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counterRepository.insertCounter(ncounter3);
        Note nnote3 = new Note(teaId3, 1, null, "");
        noteRepository.insertNote(nnote3);
    }

    private void createDefaultSettings(){
        ActualSettings defaultSettings = new ActualSettings();
        defaultSettings.setMusicChoice("content://settings/system/ringtone");
        defaultSettings.setMusicName("Default");
        defaultSettings.setVibration(true);
        defaultSettings.setAnimation(true);
        defaultSettings.setTemperatureUnit("Celsius");
        defaultSettings.setShowTeaAlert(true);
        defaultSettings.setMainRateAlert(true);
        defaultSettings.setMainRateCounter(0);
        defaultSettings.setSettingsPermissionAlert(true);
        defaultSettings.setSort(0);
        actualSettingsRepository.insertSettings(defaultSettings);
    }

    // Teas
    LiveData<List<Tea>> getTeas() {
        return teas;
    }

    Tea getTeaByPosition(int position) {
        return Objects.requireNonNull(teas.getValue()).get(position);
    }

    void deleteTea(int position) {
        teaRepository.deleteTea(Objects.requireNonNull(teas.getValue()).get(position));

        refreshTeas();
    }

    void visualizeTeasBySearchString(String searchString) {
        if ("".equals(searchString)) {
            refreshTeas();
        } else {
            teas.setValue(teaRepository.getTeasBySearchString(searchString));
        }
    }

    // Settings
    int getSort() {
        return actualSettings.getSort();
    }

    void setSort(int sort) {
        actualSettings.setSort(sort);
        actualSettingsRepository.updateSettings(actualSettings);

        refreshTeas();
    }

    boolean isMainRateAlert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainRateAlert(boolean mainRateAlert) {
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    int getMainRatecounter() {
        return actualSettings.getMainRateCounter();
    }

    void resetMainRatecounter() {

        actualSettings.setMainRateCounter(0);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void incrementMainRatecounter() {
        actualSettings.setMainRateCounter(actualSettings.getMainRateCounter() + 1);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void refreshTeas() {
        switch (actualSettings.getSort()) {
            //activity
            case 0:
                teas.setValue(teaRepository.getTeasOrderByActivity());
                break;
            //alphabetic
            case 1:
                teas.setValue(teaRepository.getTeasOrderByAlphabetic());
                break;
            //variety
            case 2:
                teas.setValue(teaRepository.getTeasOrderByVariety());
                break;
            default:
        }
    }

}
