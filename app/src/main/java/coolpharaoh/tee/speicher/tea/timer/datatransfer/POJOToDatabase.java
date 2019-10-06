package coolpharaoh.tee.speicher.tea.timer.datatransfer;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo.TeaPOJO;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.ExportImportViewModel;

class POJOToDatabase {
    private ExportImportViewModel exportImportViewModel;

    POJOToDatabase(ExportImportViewModel exportImportViewModel) {
        this.exportImportViewModel = exportImportViewModel;
    }

    void fillDatabaseWithTeaList(List<TeaPOJO> teaList, boolean keepStoredTeas) {
        if(!keepStoredTeas){
            deleteStoredTeas();
        }
        int o = 0;
        for (TeaPOJO teaPOJO : teaList) {
            // insert Tea first
            long teaId = exportImportViewModel.insertTea(new Tea(teaPOJO.getName(), teaPOJO.getVariety(),
                    teaPOJO.getAmount(), teaPOJO.getAmountkind(), teaPOJO.getColor(),
                    teaPOJO.getLastInfusion(), teaPOJO.getDate()));
            insertInfusions(teaId, teaPOJO.getInfusions());
            insertCounters(teaId, teaPOJO.getCounters());
            insertNotes(teaId, teaPOJO.getNotes());
        }
    }

    private void deleteStoredTeas(){
        exportImportViewModel.deleteAllTeas();
    }

    private void insertInfusions(long teaId, List<InfusionPOJO> infusionList) {
        for (InfusionPOJO infusionPOJO : infusionList) {
            exportImportViewModel.insertInfusion(new Infusion(teaId, infusionPOJO.getInfusionindex(),
                    infusionPOJO.getTime(), infusionPOJO.getCooldowntime(),
                    infusionPOJO.getTemperaturecelsius(), infusionPOJO.getTemperaturefahrenheit()));
        }
    }

    private void insertCounters(long teaId, List<CounterPOJO> counterList) {
        for (CounterPOJO counterPOJO : counterList) {
            exportImportViewModel.insertCounter(new Counter(teaId, counterPOJO.getDay(),
                    counterPOJO.getWeek(), counterPOJO.getMonth(), counterPOJO.getOverall(),
                    counterPOJO.getDaydate(), counterPOJO.getWeekdate(), counterPOJO.getMonthdate()));
        }
    }

    private void insertNotes(long teaId, List<NotePOJO> noteList) {
        for (NotePOJO notePOJO : noteList) {
            exportImportViewModel.insertNote(new Note(teaId, notePOJO.getPosition(),
                    notePOJO.getHeader(), notePOJO.getDescription()));
        }
    }
}
