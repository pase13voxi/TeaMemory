package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO;

class POJOToDatabase {
    private final DataTransformViewModel dataTransformViewModel;

    POJOToDatabase(DataTransformViewModel dataTransformViewModel) {
        this.dataTransformViewModel = dataTransformViewModel;
    }

    void fillDatabaseWithTeaList(List<TeaPOJO> teaList, boolean keepStoredTeas) {
        if (!keepStoredTeas) {
            deleteStoredTeas();
        }
        for (TeaPOJO teaPOJO : teaList) {
            // insert Tea first
            long teaId = insertTea(teaPOJO);
            insertInfusions(teaId, teaPOJO.getInfusions());
            insertCounters(teaId, teaPOJO.getCounters());
            insertNotes(teaId, teaPOJO.getNotes());
        }
    }

    private void deleteStoredTeas() {
        dataTransformViewModel.deleteAllTeas();
    }

    private long insertTea(TeaPOJO teaPOJO) {
        final Tea tea = new Tea(teaPOJO.getName(), teaPOJO.getVariety(),
                teaPOJO.getAmount(), teaPOJO.getAmountKind(), teaPOJO.getColor(),
                teaPOJO.getNextInfusion(), teaPOJO.getDate());
        tea.setRating(teaPOJO.getRating());
        tea.setFavorite(teaPOJO.isFavorite());

        return dataTransformViewModel.insertTea(tea);
    }

    private void insertInfusions(long teaId, List<InfusionPOJO> infusionList) {
        for (InfusionPOJO infusionPOJO : infusionList) {
            dataTransformViewModel.insertInfusion(new Infusion(teaId, infusionPOJO.getInfusionindex(),
                    infusionPOJO.getTime(), infusionPOJO.getCooldowntime(),
                    infusionPOJO.getTemperaturecelsius(), infusionPOJO.getTemperaturefahrenheit()));
        }
    }

    private void insertCounters(long teaId, List<CounterPOJO> counterList) {
        for (CounterPOJO counterPOJO : counterList) {
            dataTransformViewModel.insertCounter(new Counter(teaId, counterPOJO.getDay(),
                    counterPOJO.getWeek(), counterPOJO.getMonth(), counterPOJO.getOverall(),
                    counterPOJO.getDaydate(), counterPOJO.getWeekdate(), counterPOJO.getMonthdate()));
        }
    }

    private void insertNotes(long teaId, List<NotePOJO> noteList) {
        for (NotePOJO notePOJO : noteList) {
            dataTransformViewModel.insertNote(new Note(teaId, notePOJO.getPosition(),
                    notePOJO.getHeader(), notePOJO.getDescription()));
        }
    }
}
