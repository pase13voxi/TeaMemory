package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.CounterPOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.InfusionPOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.NotePOJO
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO

internal class DatabaseToPOJO(private val teas: List<Tea>, private val infusions: List<Infusion>,
                              private val counters: List<Counter>, private val notes: List<Note>) {

    fun createTeaList(): List<TeaPOJO> {
        val teaList: MutableList<TeaPOJO> = ArrayList()

        for (tea in teas) {
            val teaPOJO = createTeaPOJO(tea)
            teaPOJO.infusions = createInfusionList(tea.id!!)
            teaPOJO.counters = createCounterList(tea.id!!)
            teaPOJO.notes = createNoteList(tea.id!!)
            teaList.add(teaPOJO)
        }

        return teaList
    }

    private fun createTeaPOJO(tea: Tea): TeaPOJO {
        val teaPOJO = TeaPOJO()
        teaPOJO.name = tea.name
        teaPOJO.variety = tea.variety
        teaPOJO.amount = tea.amount
        teaPOJO.amountKind = tea.amountKind
        teaPOJO.color = tea.color
        teaPOJO.rating = tea.rating
        teaPOJO.inStock = tea.inStock
        teaPOJO.nextInfusion = tea.nextInfusion
        teaPOJO.date = tea.date
        return teaPOJO
    }

    private fun createInfusionList(teaId: Long): List<InfusionPOJO> {
        val infusionPOJOList: MutableList<InfusionPOJO> = ArrayList()

        for (infusion in infusions) {
            if (infusion.teaId == teaId) {
                val infusionPOJO = createInfusionPOJO(infusion)
                infusionPOJOList.add(infusionPOJO)
            }
        }
        return infusionPOJOList
    }

    private fun createInfusionPOJO(infusion: Infusion): InfusionPOJO {
        val infusionPOJO = InfusionPOJO()
        infusionPOJO.infusionIndex = infusion.infusionIndex
        infusionPOJO.time = infusion.time
        infusionPOJO.coolDownTime = infusion.coolDownTime
        infusionPOJO.temperatureCelsius = infusion.temperatureCelsius
        infusionPOJO.temperatureFahrenheit = infusion.temperatureFahrenheit
        return infusionPOJO
    }

    private fun createCounterList(teaId: Long): List<CounterPOJO> {
        val counterPOJOList: MutableList<CounterPOJO> = ArrayList()

        for (counter in counters) {
            if (counter.teaId == teaId) {
                val counterPOJO = createCounterPOJO(counter)
                counterPOJOList.add(counterPOJO)
            }
        }
        return counterPOJOList
    }

    private fun createCounterPOJO(counter: Counter): CounterPOJO {
        val counterPOJO = CounterPOJO()
        counterPOJO.week = counter.week
        counterPOJO.month = counter.month
        counterPOJO.year = counter.year
        counterPOJO.overall = counter.overall
        counterPOJO.weekDate = counter.weekDate
        counterPOJO.monthDate = counter.monthDate
        counterPOJO.yearDate = counter.yearDate
        return counterPOJO
    }

    private fun createNoteList(teaId: Long): List<NotePOJO> {
        val notePOJOList: MutableList<NotePOJO> = ArrayList()

        for (note in notes) {
            if (note.teaId == teaId) {
                val notePOJO = createNotePOJO(note)
                notePOJOList.add(notePOJO)
            }
        }
        return notePOJOList
    }

    private fun createNotePOJO(note: Note): NotePOJO {
        val notePOJO = NotePOJO()
        notePOJO.position = note.position
        notePOJO.header = note.header
        notePOJO.description = note.description
        return notePOJO
    }
}