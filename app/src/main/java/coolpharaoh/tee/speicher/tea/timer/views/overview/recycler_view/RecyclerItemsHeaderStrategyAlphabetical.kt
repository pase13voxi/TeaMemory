package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import java.util.Locale

class RecyclerItemsHeaderStrategyAlphabetical(private val application: Application) :
    RecyclerItemsHeaderStrategy {

    override fun generateFrom(teaList: List<Tea>): List<RecyclerItemOverview> {

        val recyclerItems = ArrayList<RecyclerItemOverview>()
        var lastFirstLetter = ""
        for ((id, name, variety1, _, _, color, _, inStock) in teaList) {
            val firstLetter = name!!.substring(0, 1).uppercase(Locale.getDefault())
            if (lastFirstLetter != firstLetter) {
                recyclerItems.add(
                    RecyclerItemOverview("- $firstLetter -", null, null, null, null, false)
                )
                lastFirstLetter = firstLetter
            }
            val variety = convertStoredVarietyToText(variety1, application)
            recyclerItems.add(RecyclerItemOverview(null, id, name, variety, color, inStock))
        }
        return recyclerItems
    }
}