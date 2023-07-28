package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application

object RecyclerItemsHeaderStrategyFactory {
    @JvmStatic
    fun getStrategy(sorting: Int, application: Application): RecyclerItemsHeaderStrategy {
        return when (sorting) {
            0 -> RecyclerItemsHeaderStrategyLastUsed(application)
            1 -> RecyclerItemsHeaderStrategyAlphabetical(application)
            2 -> RecyclerItemsHeaderStrategyVariety(application)
            3 -> RecyclerItemsHeaderStrategyRating(application)
            else -> RecyclerItemsHeaderStrategyDefault(application)
        }
    }
}