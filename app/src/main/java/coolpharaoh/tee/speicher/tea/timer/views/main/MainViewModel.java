package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.views.utils.TemperatureConversation;

class MainViewModel extends ViewModel {


    private final TeaDAO teaDAO;
    private final InfusionDao infusionDAO;
    private final NoteDao noteDAO;
    private final CounterDao counterDAO;
    private final ActualSettingsDao actualSettingsDAO;

    private final MutableLiveData<List<Tea>> teas;
    private final ActualSettings actualSettings;

    MainViewModel(TeaMemoryDatabase database, Context context) {

        teaDAO = database.getTeaDao();
        infusionDAO = database.getInfusionDao();
        noteDAO = database.getNoteDao();
        counterDAO = database.getCounterDao();
        actualSettingsDAO = database.getActualSettingsDao();

        if (actualSettingsDAO.getCountItems() == 0) {
            createDefaultTeas(context);
            createDefaultSettings();
        }

        actualSettings = actualSettingsDAO.getSettings();
        teas = new MutableLiveData<>();

        refreshTeas();
    }

    // Defaults
    private void createDefaultTeas(Context context){
        Tea ntea1 = new Tea("Earl Grey", context.getResources().getStringArray(R.array.variety_codes)[0], 5, "Ts", ColorConversation.getVarietyColor(0, context), 0, Calendar.getInstance().getTime());
        long teaId1 = teaDAO.insert(ntea1);
        Infusion ninfusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
        infusionDAO.insert(ninfusion1);
        Counter ncounter1 = new Counter(teaId1, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        counterDAO.insert(ncounter1);
        Note nnote1 = new Note(teaId1, 1, null, "");
        noteDAO.insert(nnote1);

        Tea ntea2 = new Tea("Pai Mu Tan", context.getResources().getStringArray(R.array.variety_codes)[3], 4, "Ts", ColorConversation.getVarietyColor(3, context), 0, Calendar.getInstance().getTime());
        long teaId2 = teaDAO.insert(ntea2);
        Infusion ninfusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
        infusionDAO.insert(ninfusion2);
        Counter ncounter2 = new Counter(teaId2, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        counterDAO.insert(ncounter2);
        Note nnote2 = new Note(teaId2, 1, null, "");
        noteDAO.insert(nnote2);

        Tea ntea3 = new Tea("Sencha", context.getResources().getStringArray(R.array.variety_codes)[1], 4, "Ts", ColorConversation.getVarietyColor(1, context), 0, Calendar.getInstance().getTime());
        long teaId3 = teaDAO.insert(ntea3);
        Infusion ninfusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
        infusionDAO.insert(ninfusion3);
        Counter ncounter3 = new Counter(teaId3, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        counterDAO.insert(ncounter3);
        Note nnote3 = new Note(teaId3, 1, null, "");
        noteDAO.insert(nnote3);
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
        actualSettingsDAO.insert(defaultSettings);
    }

    // Teas
    LiveData<List<Tea>> getTeas() {
        return teas;
    }

    Tea getTeaByPosition(int position) {
        return Objects.requireNonNull(teas.getValue()).get(position);
    }

    void deleteTea(int position) {
        teaDAO.delete(Objects.requireNonNull(teas.getValue()).get(position));

        refreshTeas();
    }

    void visualizeTeasBySearchString(String searchString) {
        if ("".equals(searchString)) {
            refreshTeas();
        } else {
            teas.setValue(teaDAO.getTeasBySearchString(searchString));
        }
    }

    // Settings
    int getSort() {
        return actualSettings.getSort();
    }

    void setSort(int sort) {
        actualSettings.setSort(sort);
        actualSettingsDAO.update(actualSettings);

        refreshTeas();
    }

    boolean isMainRateAlert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainRateAlert(boolean mainRateAlert) {
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettingsDAO.update(actualSettings);
    }

    int getMainRatecounter() {
        return actualSettings.getMainRateCounter();
    }

    void resetMainRatecounter() {

        actualSettings.setMainRateCounter(0);
        actualSettingsDAO.update(actualSettings);
    }

    void incrementMainRatecounter() {
        actualSettings.setMainRateCounter(actualSettings.getMainRateCounter() + 1);
        actualSettingsDAO.update(actualSettings);
    }

    void refreshTeas() {
        switch (actualSettings.getSort()) {
            //activity
            case 0:
                teas.setValue(teaDAO.getTeasOrderByActivity());
                break;
            //alphabetic
            case 1:
                teas.setValue(teaDAO.getTeasOrderByAlphabetic());
                break;
            //variety
            case 2:
                teas.setValue(teaDAO.getTeasOrderByVariety());
                break;
            default:
        }
    }

}
