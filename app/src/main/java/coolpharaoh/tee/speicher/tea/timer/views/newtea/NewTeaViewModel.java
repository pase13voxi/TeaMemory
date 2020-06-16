package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.views.utils.TemperatureConversation;

public class NewTeaViewModel {
    private final Context context;

    private final TeaDAO teaDAO;
    private final InfusionDAO infusionDAO;
    private final NoteDAO noteDAO;
    private final CounterDAO counterDAO;
    private final ActualSettingsDAO actualSettingsDAO;

    private final Tea tea;
    private final List<Infusion> infusions;

    private int infusionIndex = 0;

    public NewTeaViewModel(long teaId, TeaMemoryDatabase database, Context context) {
        this.context = context;

        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();

        tea = teaDAO.getTeaById(teaId);
        infusions = infusionDAO.getInfusionsByTeaId(teaId);
    }

    public NewTeaViewModel(TeaMemoryDatabase database, Context context) {
        this.context = context;

        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();

        tea = new Tea();
        infusions = new ArrayList<>();
        addInfusion();
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
        return tea.getAmountKind();
    }

    public int getColor() {
        return tea.getColor();
    }

    // Infusion
    public void addInfusion() {
        Infusion infusion = new Infusion();
        infusion.setTemperatureCelsius(-500);
        infusion.setTemperatureFahrenheit(-500);
        infusions.add(infusion);
        if (getInfusionSize() > 1) {
            infusionIndex++;
        }
    }

    public void takeInfusionInformation(String time, String cooldowntime, int temperature) {
        infusions.get(infusionIndex).setTime(time);
        infusions.get(infusionIndex).setCoolDownTime(cooldowntime);
        if (actualSettingsDAO.getSettings().getTemperatureUnit().equals("Celsius")) {
            infusions.get(infusionIndex).setTemperatureCelsius(temperature);
            infusions.get(infusionIndex).setTemperatureFahrenheit(TemperatureConversation.celsiusToFahrenheit(temperature));
        } else {
            infusions.get(infusionIndex).setTemperatureFahrenheit(temperature);
            infusions.get(infusionIndex).setTemperatureCelsius(TemperatureConversation.fahrenheitToCelsius(temperature));
        }
    }

    public void deleteInfusion() {
        if (getInfusionSize() > 1) {
            infusions.remove(infusionIndex);
            if (infusionIndex == getInfusionSize()) {
                infusionIndex--;
            }
        }
    }

    public String getInfusionTime() {
        return infusions.get(infusionIndex).getTime();
    }

    public String getInfusionCooldowntime() {
        return infusions.get(infusionIndex).getCoolDownTime();
    }

    public int getInfusionTemperature() {
        if (getTemperatureunit().equals("Celsius")) {
            return infusions.get(infusionIndex).getTemperatureCelsius();
        } else {
            return infusions.get(infusionIndex).getTemperatureFahrenheit();
        }
    }

    public void previousInfusion() {
        if (infusionIndex - 1 >= 0) {
            infusionIndex--;
        }
    }

    public void nextInfusion() {
        if (infusionIndex + 1 < getInfusionSize()) {
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
    public String getTemperatureunit() {
        return actualSettingsDAO.getSettings().getTemperatureUnit();
    }

    // Overall
    public void editTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);

        teaDAO.update(tea);

        setInfusionInformation(tea.getId());
    }

    public void createNewTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);
        tea.setNextInfusion(0);

        long teaId = teaDAO.insert(tea);

        setInfusionInformation(teaId);

        // create new counter
        Counter counter = new Counter();
        counter.setTeaId(teaId);
        counter.setDay(0);
        counter.setWeek(0);
        counter.setMonth(0);
        counter.setOverall(0);
        counter.setDayDate(Calendar.getInstance().getTime());
        counter.setWeekDate(Calendar.getInstance().getTime());
        counter.setMonthDate(Calendar.getInstance().getTime());

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
        tea.setVariety(LanguageConversation.convertVarietyToCode(variety, context));
        tea.setAmount(amount);
        tea.setAmountKind(amountkind);
        tea.setColor(color);
        tea.setDate(Calendar.getInstance().getTime());
    }

    private void setInfusionInformation(long teaId) {
        infusionDAO.deleteInfusionByTeaId(teaId);
        for (int i = 0; i < getInfusionSize(); i++) {
            infusions.get(i).setTeaId(teaId);
            infusions.get(i).setInfusionIndex(i);
            infusionDAO.insert(infusions.get(i));
        }
    }
}