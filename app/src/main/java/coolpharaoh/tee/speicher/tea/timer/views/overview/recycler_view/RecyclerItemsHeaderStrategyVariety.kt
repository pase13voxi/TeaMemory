package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText

class RecyclerItemsHeaderStrategyVariety(private val application: Application) :
    RecyclerItemsHeaderStrategy {

    override fun generateFrom(teaList: List<Tea>): List<RecyclerItemOverview> {

        val recyclerItems = ArrayList<RecyclerItemOverview>()
        var lastVariety: String? = ""
        for ((id, name, variety1, _, _, color, _, inStock) in teaList) {
            val variety = convertStoredVarietyToText(variety1, application)
            if (variety != lastVariety) {
                recyclerItems.add(
                    RecyclerItemOverview("- $variety -", null, null, null, null, false)
                )
                lastVariety = variety
            }
            recyclerItems.add(RecyclerItemOverview(null, id, name, variety, color, inStock))
        }
        return recyclerItems
    }
}