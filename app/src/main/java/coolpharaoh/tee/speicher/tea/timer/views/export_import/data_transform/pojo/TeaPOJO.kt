package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo

import java.util.Date

class TeaPOJO {
    var name: String? = null
    var variety: String? = null
    var amount: Double = 0.0
    var amountKind: String? = null
    var color: Int = 0
    var rating: Int = 0
    var inStock: Boolean = false
    var nextInfusion: Int = 0
    var date: Date? = null
    var infusions: List<InfusionPOJO>? = null
    var counters: List<CounterPOJO>? = null
    var notes: List<NotePOJO>? = null
}