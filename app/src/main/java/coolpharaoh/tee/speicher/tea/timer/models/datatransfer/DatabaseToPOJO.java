package coolpharaoh.tee.speicher.tea.timer.models.datatransfer;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

class DatabaseToPOJO {
    private final List<Tea> teas;
    private final List<Infusion> infusions;
    private final List<Counter> counters;
    private final List<Note> notes;

    DatabaseToPOJO(List<Tea> teas, List<Infusion> infusions, List<Counter> counters, List<Note> notes) {
        this.teas = teas;
        this.infusions = infusions;
        this.counters = counters;
        this.notes = notes;
    }

    List<TeaPOJO> createTeaList() {
        List<TeaPOJO> mTeaList = new ArrayList<>();

        for (Tea tea : teas) {
            TeaPOJO teaPOJO = createTeaPOJO(tea);
            teaPOJO.setInfusions(createInfusionList(tea.getId()));
            teaPOJO.setCounters(createCounterList(tea.getId()));
            teaPOJO.setNotes(createNoteList(tea.getId()));
            mTeaList.add(teaPOJO);
        }

        return mTeaList;
    }

    private TeaPOJO createTeaPOJO(Tea tea) {
        TeaPOJO teaPOJO = new TeaPOJO();
        teaPOJO.setName(tea.getName());
        teaPOJO.setVariety(tea.getVariety());
        teaPOJO.setAmount(tea.getAmount());
        teaPOJO.setAmountkind(tea.getAmountkind());
        teaPOJO.setColor(tea.getColor());
        teaPOJO.setLastInfusion(tea.getLastInfusion());
        teaPOJO.setDate(tea.getDate());
        return teaPOJO;
    }

    private List<InfusionPOJO> createInfusionList(long teaId) {
        List<InfusionPOJO> infusionPOJOList = new ArrayList<>();

        for (Infusion infusion : infusions) {
            if (infusion.getTeaId() == teaId) {
                InfusionPOJO infusionPOJO = createInfusionPOJO(infusion);
                infusionPOJOList.add(infusionPOJO);
            }
        }
        return infusionPOJOList;
    }

    private InfusionPOJO createInfusionPOJO(Infusion infusion) {
        InfusionPOJO infusionPOJO = new InfusionPOJO();
        infusionPOJO.setInfusionindex(infusion.getInfusionindex());
        infusionPOJO.setTime(infusion.getTime());
        infusionPOJO.setCooldowntime(infusion.getCooldowntime());
        infusionPOJO.setTemperaturecelsius(infusion.getTemperaturecelsius());
        infusionPOJO.setTemperaturefahrenheit(infusion.getTemperaturefahrenheit());
        return infusionPOJO;
    }

    private List<CounterPOJO> createCounterList(long teaId) {
        List<CounterPOJO> counterPOJOList = new ArrayList<>();

        for (Counter counter : counters) {
            if (counter.getTeaId() == teaId) {
                CounterPOJO counterPOJO = createCounterPOJO(counter);
                counterPOJOList.add(counterPOJO);
            }
        }
        return counterPOJOList;
    }

    private CounterPOJO createCounterPOJO(Counter counter) {
        CounterPOJO counterPOJO = new CounterPOJO();
        counterPOJO.setDay(counter.getDay());
        counterPOJO.setWeek(counter.getWeek());
        counterPOJO.setMonth(counter.getMonth());
        counterPOJO.setOverall(counter.getOverall());
        counterPOJO.setDaydate(counter.getDaydate());
        counterPOJO.setWeekdate(counter.getWeekdate());
        counterPOJO.setMonthdate(counter.getMonthdate());
        return counterPOJO;
    }

    private List<NotePOJO> createNoteList(long teaId) {
        List<NotePOJO> notePOJOList = new ArrayList<>();

        for (Note note : notes) {
            if (note.getTeaId() == teaId) {
                NotePOJO notePOJO = createNotePOJO(note);
                notePOJOList.add(notePOJO);
            }
        }
        return notePOJOList;
    }

    private NotePOJO createNotePOJO(Note note) {
        NotePOJO notePOJO = new NotePOJO();
        notePOJO.setPosition(note.getPosition());
        notePOJO.setHeader(note.getHeader());
        notePOJO.setDescription(note.getDescription());
        return notePOJO;
    }
}
