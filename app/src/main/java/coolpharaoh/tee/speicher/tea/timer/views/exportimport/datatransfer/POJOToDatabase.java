package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.TeaPOJO;

class POJOToDatabase {
    private final DataTransferViewModel dataTransferViewModel;

    POJOToDatabase(DataTransferViewModel dataTransferViewModel) {
        this.dataTransferViewModel = dataTransferViewModel;
    }

    void fillDatabaseWithTeaList(List<TeaPOJO> teaList, boolean keepStoredTeas) {
        if (!keepStoredTeas) {
            deleteStoredTeas();
        }
        for (TeaPOJO teaPOJO : teaList) {
            // insert Tea first
            long teaId = dataTransferViewModel.insertTea(new Tea(teaPOJO.getName(), teaPOJO.getVariety(),
                    teaPOJO.getAmount(), teaPOJO.getAmountKind(), teaPOJO.getColor(),
                    teaPOJO.getNextInfusion(), teaPOJO.getDate()));
            insertInfusions(teaId, teaPOJO.getInfusions());
            insertCounters(teaId, teaPOJO.getCounters());
            insertNotes(teaId, teaPOJO.getNotes());
        }
    }

    private void deleteStoredTeas(){
        dataTransferViewModel.deleteAllTeas();
    }

    private void insertInfusions(long teaId, List<InfusionPOJO> infusionList) {
        for (InfusionPOJO infusionPOJO : infusionList) {
            dataTransferViewModel.insertInfusion(new Infusion(teaId, infusionPOJO.getInfusionindex(),
                    infusionPOJO.getTime(), infusionPOJO.getCooldowntime(),
                    infusionPOJO.getTemperaturecelsius(), infusionPOJO.getTemperaturefahrenheit()));
        }
    }

    private void insertCounters(long teaId, List<CounterPOJO> counterList) {
        for (CounterPOJO counterPOJO : counterList) {
            dataTransferViewModel.insertCounter(new Counter(teaId, counterPOJO.getDay(),
                    counterPOJO.getWeek(), counterPOJO.getMonth(), counterPOJO.getOverall(),
                    counterPOJO.getDaydate(), counterPOJO.getWeekdate(), counterPOJO.getMonthdate()));
        }
    }

    private void insertNotes(long teaId, List<NotePOJO> noteList) {
        for (NotePOJO notePOJO : noteList) {
            dataTransferViewModel.insertNote(new Note(teaId, notePOJO.getPosition(),
                    notePOJO.getHeader(), notePOJO.getDescription()));
        }
    }
}
