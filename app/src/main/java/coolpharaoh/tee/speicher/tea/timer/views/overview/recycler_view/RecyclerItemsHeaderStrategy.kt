package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea

interface RecyclerItemsHeaderStrategy {
    fun generateFrom(teaList: List<Tea>): List<RecyclerItemOverview>
}