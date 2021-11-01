package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO;

class DatabaseToPOJO {
    private final List<Tea> teas;
    private final List<Infusion> infusions;
    private final List<Counter> counters;
    private final List<Note> notes;

    DatabaseToPOJO(final List<Tea> teas, final List<Infusion> infusions, final List<Counter> counters,
                   final List<Note> notes) {
        this.teas = teas;
        this.infusions = infusions;
        this.counters = counters;
        this.notes = notes;
    }

    List<TeaPOJO> createTeaList() {
        final List<TeaPOJO> teaList = new ArrayList<>();

        for (final Tea tea : teas) {
            final TeaPOJO teaPOJO = createTeaPOJO(tea);
            teaPOJO.setInfusions(createInfusionList(tea.getId()));
            teaPOJO.setCounters(createCounterList(tea.getId()));
            teaPOJO.setNotes(createNoteList(tea.getId()));
            teaList.add(teaPOJO);
        }

        return teaList;
    }

    private TeaPOJO createTeaPOJO(final Tea tea) {
        final TeaPOJO teaPOJO = new TeaPOJO();
        teaPOJO.setName(tea.getName());
        teaPOJO.setVariety(tea.getVariety());
        teaPOJO.setAmount(tea.getAmount());
        teaPOJO.setAmountKind(tea.getAmountKind());
        teaPOJO.setColor(tea.getColor());
        teaPOJO.setRating(tea.getRating());
        teaPOJO.setInStock(tea.isInStock());
        teaPOJO.setNextInfusion(tea.getNextInfusion());
        teaPOJO.setDate(tea.getDate());
        return teaPOJO;
    }

    private List<InfusionPOJO> createInfusionList(final long teaId) {
        final List<InfusionPOJO> infusionPOJOList = new ArrayList<>();

        for (final Infusion infusion : infusions) {
            if (infusion.getTeaId() == teaId) {
                final InfusionPOJO infusionPOJO = createInfusionPOJO(infusion);
                infusionPOJOList.add(infusionPOJO);
            }
        }
        return infusionPOJOList;
    }

    private InfusionPOJO createInfusionPOJO(final Infusion infusion) {
        final InfusionPOJO infusionPOJO = new InfusionPOJO();
        infusionPOJO.setInfusionindex(infusion.getInfusionIndex());
        infusionPOJO.setTime(infusion.getTime());
        infusionPOJO.setCooldowntime(infusion.getCoolDownTime());
        infusionPOJO.setTemperaturecelsius(infusion.getTemperatureCelsius());
        infusionPOJO.setTemperaturefahrenheit(infusion.getTemperatureFahrenheit());
        return infusionPOJO;
    }

    private List<CounterPOJO> createCounterList(final long teaId) {
        final List<CounterPOJO> counterPOJOList = new ArrayList<>();

        for (final Counter counter : counters) {
            if (counter.getTeaId() == teaId) {
                final CounterPOJO counterPOJO = createCounterPOJO(counter);
                counterPOJOList.add(counterPOJO);
            }
        }
        return counterPOJOList;
    }

    private CounterPOJO createCounterPOJO(final Counter counter) {
        final CounterPOJO counterPOJO = new CounterPOJO();
        counterPOJO.setDay(counter.getDay());
        counterPOJO.setWeek(counter.getWeek());
        counterPOJO.setMonth(counter.getMonth());
        counterPOJO.setOverall(counter.getOverall());
        counterPOJO.setDaydate(counter.getDayDate());
        counterPOJO.setWeekdate(counter.getWeekDate());
        counterPOJO.setMonthdate(counter.getMonthDate());
        return counterPOJO;
    }

    private List<NotePOJO> createNoteList(final long teaId) {
        final List<NotePOJO> notePOJOList = new ArrayList<>();

        for (final Note note : notes) {
            if (note.getTeaId() == teaId) {
                final NotePOJO notePOJO = createNotePOJO(note);
                notePOJOList.add(notePOJO);
            }
        }
        return notePOJOList;
    }

    private NotePOJO createNotePOJO(final Note note) {
        final NotePOJO notePOJO = new NotePOJO();
        notePOJO.setPosition(note.getPosition());
        notePOJO.setHeader(note.getHeader());
        notePOJO.setDescription(note.getDescription());
        return notePOJO;
    }
}
