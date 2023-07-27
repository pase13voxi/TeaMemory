package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform

import android.os.Build.VERSION_CODES
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.CounterPOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.InfusionPOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.NotePOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO

internal class POJOToDatabase(private val dataTransformViewModel: DataTransformViewModel) {

    fun fillDatabaseWithTeaList(teaList: List<TeaPOJO>, keepStoredTeas: Boolean) {
        if (!keepStoredTeas) {
            deleteStoredTeas()
        }
        for (teaPOJO in teaList) {
            // insert Tea first
            val teaId = insertTea(teaPOJO)
            insertInfusions(teaId, teaPOJO.infusions)
            insertCounters(teaId, teaPOJO.counters)
            insertNotes(teaId, teaPOJO.notes)
        }
    }

    private fun deleteStoredTeas() {
        if (sdkVersion >= VERSION_CODES.Q) {
            dataTransformViewModel.deleteAllTeaImages()
        }

        dataTransformViewModel.deleteAllTeas()
    }

    private fun insertTea(teaPOJO: TeaPOJO): Long {
        val tea = Tea(teaPOJO.name, teaPOJO.variety, teaPOJO.amount, teaPOJO.amountKind,
            teaPOJO.color, teaPOJO.nextInfusion, teaPOJO.date)
        tea.rating = teaPOJO.rating
        tea.inStock = teaPOJO.inStock

        return dataTransformViewModel.insertTea(tea)
    }

    private fun insertInfusions(teaId: Long, infusionList: List<InfusionPOJO>?) {
        for (infusionPOJO in infusionList!!) {
            dataTransformViewModel.insertInfusion(
                Infusion(teaId, infusionPOJO.infusionIndex, infusionPOJO.time,
                    infusionPOJO.coolDownTime, infusionPOJO.temperatureCelsius,
                    infusionPOJO.temperatureFahrenheit)
            )
        }
    }

    private fun insertCounters(teaId: Long, counterList: List<CounterPOJO>?) {
        for (counterPOJO in counterList!!) {
            dataTransformViewModel.insertCounter(
                Counter(teaId, counterPOJO.week, counterPOJO.month, counterPOJO.year,
                    counterPOJO.overall, counterPOJO.weekDate, counterPOJO.monthDate,
                    counterPOJO.yearDate)
            )
        }
    }

    private fun insertNotes(teaId: Long, noteList: List<NotePOJO>?) {
        for (notePOJO in noteList!!) {
            dataTransformViewModel.insertNote(
                Note(teaId, notePOJO.position, notePOJO.header, notePOJO.description)
            )
        }
    }
}