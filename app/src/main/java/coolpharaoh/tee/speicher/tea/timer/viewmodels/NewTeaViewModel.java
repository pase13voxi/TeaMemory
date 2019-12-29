package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TemperatureConversation;

public class NewTeaViewModel {

    private final Context context;

    private final TeaDAO teaDAO;
    private final InfusionDAO infusionDAO;
    private final NoteDAO noteDAO;
    private final CounterDAO counterDAO;
    private final ActualSettingsDAO actualSettingsDAO;

    private final Tea tea;
    private final List<Infusion> infusions;
    private final ActualSettings actualSettings;

    private int infusionIndex = 0;

    public NewTeaViewModel(long teaId, Context context) {
        this.context = context;

        TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(context);

        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();

        tea = teaDAO.getTeaById(teaId);
        infusions = infusionDAO.getInfusionsByTeaId(teaId);
        actualSettings = actualSettingsDAO.getSettings();
    }

    public NewTeaViewModel(Context context) {
        this.context = context;

        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();

        tea = new Tea();
        infusions = new ArrayList<>();
        addInfusion(true);
        actualSettings = actualSettingsDAO.getSettings();
    }

    // Tea
    public long getTeaId() {
        return tea.getId();
    }

    public String getName() {
        return tea.getName();
    }

    public String getVariety() {
        return tea.getVariety();
    }

    public int getAmount() {
        return tea.getAmount();
    }

    public String getAmountkind() {
        return tea.getAmountkind();
    }

    public int getColor() {
        return tea.getColor();
    }

    // Infusion
    public void addInfusion(boolean first) {
        Infusion infusion = new Infusion();
        infusion.setTemperaturecelsius(-500);
        infusion.setTemperaturefahrenheit(-500);
        infusions.add(infusion);
        if (!first){
            infusionIndex++;
        }
    }

    public void takeInfusionInformation(String time, String cooldowntime, int temperature) {
        infusions.get(infusionIndex).setTime(time);
        infusions.get(infusionIndex).setCooldowntime(cooldowntime);
        if (actualSettings.getTemperatureunit().equals("Celsius")) {
            infusions.get(infusionIndex).setTemperaturecelsius(temperature);
            infusions.get(infusionIndex).setTemperaturefahrenheit(TemperatureConversation.celsiusToFahrenheit(temperature));
        } else {
            infusions.get(infusionIndex).setTemperaturefahrenheit(temperature);
            infusions.get(infusionIndex).setTemperaturecelsius(TemperatureConversation.fahrenheitToCelsius(temperature));
        }
    }

    public void deleteInfusion() {
        if (infusions.size() > 1) {
            infusions.remove(infusionIndex);
            if (infusionIndex == infusions.size()) {
                infusionIndex--;
            }
        }
    }

    public String getInfusionTime() {
        return infusions.get(infusionIndex).getTime();
    }

    public String getInfusionCooldowntime() {
        return infusions.get(infusionIndex).getCooldowntime();
    }

    public int getInfusionTemperature() {
        if (actualSettings.getTemperatureunit().equals("Celsius")) {
            return infusions.get(infusionIndex).getTemperaturecelsius();
        } else {
            return infusions.get(infusionIndex).getTemperaturefahrenheit();
        }
    }

    public void previousInfusion() {
        if (infusionIndex - 1 >= 0) {
            infusionIndex--;
        }
    }

    public void nextInfusion() {
        if (infusionIndex + 1 < infusions.size()) {
            infusionIndex++;
        }
    }

    public int getInfusionIndex() {
        return infusionIndex;
    }

    public int getInfusionSize() {
        return infusions.size();
    }

    // Settings
    public String getTemperatureunit(){
        return actualSettings.getTemperatureunit();
    }

    // Overall
    public void editTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);

        teaDAO.update(tea);

        setInfusionInformation(tea.getId());
    }

    public void createNewTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);
        tea.setLastInfusion(0);

        long teaId = teaDAO.insert(tea);

        setInfusionInformation(teaId);

        // create new counter
        Counter counter = new Counter();
        counter.setTeaId(teaId);
        counter.setDay(0);
        counter.setWeek(0);
        counter.setMonth(0);
        counter.setOverall(0);
        counter.setDaydate(Calendar.getInstance().getTime());
        counter.setWeekdate(Calendar.getInstance().getTime());
        counter.setMonthdate(Calendar.getInstance().getTime());

        counterDAO.insert(counter);

        // create standard note
        Note note = new Note();
        note.setTeaId(teaId);
        note.setPosition(1);
        note.setDescription("");
        noteDAO.insert(note);
    }

    private void setTeaInformation(String name, String variety, int amount, String amountkind, int color) {
        tea.setName(name);
        tea.setVariety(LanguageConversation.convertVarietyToCode(variety,context));
        tea.setAmount(amount);
        tea.setAmountkind(amountkind);
        tea.setColor(color);
        tea.setDate(Calendar.getInstance().getTime());
    }

    private void setInfusionInformation(long teaId) {
        infusionDAO.deleteInfusionByTeaId(teaId);
        for (int i = 0; i < infusions.size(); i++) {
            infusions.get(i).setTeaId(teaId);
            infusions.get(i).setInfusionindex(i);
            infusionDAO.insert(infusions.get(i));
        }
    }
}
