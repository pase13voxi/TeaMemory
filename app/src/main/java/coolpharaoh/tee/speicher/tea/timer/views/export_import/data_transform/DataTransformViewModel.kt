package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform

import android.app.Application
import androidx.lifecycle.ViewModel
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.getImageController
import java.util.function.Consumer

class DataTransformViewModel(private val teaRepository: TeaRepository,
                                      private val infusionRepository: InfusionRepository,
                                      private val counterRepository: CounterRepository,
                                      private val noteRepository: NoteRepository,
                                      private val imageController: ImageController) : ViewModel() {

    constructor(application: Application) : this(TeaRepository(application),
        InfusionRepository(application), CounterRepository(application), NoteRepository(application),
        getImageController(application))

    val teas: List<Tea>
        get() = teaRepository.teas

    fun insertTea(tea: Tea): Long {
        return teaRepository.insertTea(tea)
    }


    fun deleteAllTeas() {
        teaRepository.deleteAllTeas()
    }

    fun deleteAllTeaImages() {
        val teas: List<Tea> = teaRepository.teas
        teas.forEach(Consumer { (id): Tea ->
            imageController.removeImageByTeaId(
                id!!
            )
        })
    }

    val infusions: List<Infusion>
        get() = infusionRepository.infusions

    fun insertInfusion(infusion: Infusion) {
        infusionRepository.insertInfusion(infusion)
    }

    val counters: List<Counter>
        get() = counterRepository.counters

    fun insertCounter(counter: Counter) {
        counterRepository.insertCounter(counter)
    }

    val notes: List<Note>
        get() = noteRepository.notes

    fun insertNote(note: Note) {
        noteRepository.insertNote(note)
    }
}