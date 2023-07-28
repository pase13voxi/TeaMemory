package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText

class RecyclerItemsHeaderStrategyRating(private val application: Application) :
    RecyclerItemsHeaderStrategy {

    override fun generateFrom(teaList: List<Tea>): List<RecyclerItemOverview> {

        val recyclerItems = ArrayList<RecyclerItemOverview>()
        var lastRating = -1
        for ((id, name, variety1, _, _, color, rating, inStock) in teaList) {
            if (rating != lastRating) {
                val ratingHeader = application.getString(R.string.overview_sort_header_star, rating)
                recyclerItems.add(
                    RecyclerItemOverview("- $ratingHeader -", null, null, null, null, false)
                )
                lastRating = rating
            }
            val variety = convertStoredVarietyToText(variety1, application)
            recyclerItems.add(RecyclerItemOverview(null, id, name, variety, color, inStock))
        }
        return recyclerItems
    }
}