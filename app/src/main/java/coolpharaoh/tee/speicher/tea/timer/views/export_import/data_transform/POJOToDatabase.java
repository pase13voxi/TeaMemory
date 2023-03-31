package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import static android.os.Build.VERSION_CODES.Q;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.CounterPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.InfusionPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.NotePOJO;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO;

class POJOToDatabase {
    private final DataTransformViewModel dataTransformViewModel;

    POJOToDatabase(final DataTransformViewModel dataTransformViewModel) {
        this.dataTransformViewModel = dataTransformViewModel;
    }

    void fillDatabaseWithTeaList(final List<TeaPOJO> teaList, final boolean keepStoredTeas) {
        if (!keepStoredTeas) {
            deleteStoredTeas();
        }
        for (final TeaPOJO teaPOJO : teaList) {
            // insert Tea first
            final long teaId = insertTea(teaPOJO);
            insertInfusions(teaId, teaPOJO.getInfusions());
            insertCounters(teaId, teaPOJO.getCounters());
            insertNotes(teaId, teaPOJO.getNotes());
        }
    }

    private void deleteStoredTeas() {
        if (CurrentSdk.getSdkVersion() >= Q) {
            dataTransformViewModel.deleteAllTeaImages();
        }

        dataTransformViewModel.deleteAllTeas();
    }

    private long insertTea(final TeaPOJO teaPOJO) {
        final Tea tea = new Tea(teaPOJO.getName(), teaPOJO.getVariety(),
                teaPOJO.getAmount(), teaPOJO.getAmountKind(), teaPOJO.getColor(),
                teaPOJO.getNextInfusion(), teaPOJO.getDate());
        tea.setRating(teaPOJO.getRating());
        tea.setInStock(teaPOJO.isInStock());

        return dataTransformViewModel.insertTea(tea);
    }

    private void insertInfusions(final long teaId, final List<InfusionPOJO> infusionList) {
        for (final InfusionPOJO infusionPOJO : infusionList) {
            dataTransformViewModel.insertInfusion(new Infusion(teaId, infusionPOJO.getInfusionIndex(),
                    infusionPOJO.getTime(), infusionPOJO.getCoolDownTime(),
                    infusionPOJO.getTemperatureCelsius(), infusionPOJO.getTemperatureFahrenheit()));
        }
    }

    private void insertCounters(final long teaId, final List<CounterPOJO> counterList) {
        for (final CounterPOJO counterPOJO : counterList) {
            dataTransformViewModel.insertCounter(new Counter(teaId,
                    counterPOJO.getWeek(), counterPOJO.getMonth(), counterPOJO.getYear(), counterPOJO.getOverall(),
                    counterPOJO.getWeekDate(), counterPOJO.getMonthDate(), counterPOJO.getYearDate()));
        }
    }

    private void insertNotes(final long teaId, final List<NotePOJO> noteList) {
        for (final NotePOJO notePOJO : noteList) {
            dataTransformViewModel.insertNote(new Note(teaId, notePOJO.getPosition(),
                    notePOJO.getHeader(), notePOJO.getDescription()));
        }
    }
}
