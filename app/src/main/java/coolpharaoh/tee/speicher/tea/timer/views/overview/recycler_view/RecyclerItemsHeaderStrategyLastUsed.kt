package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import java.util.Calendar
import java.util.Date

class RecyclerItemsHeaderStrategyLastUsed(private val application: Application) :
    RecyclerItemsHeaderStrategy {

    override fun generateFrom(teaList: List<Tea>): List<RecyclerItemOverview> {

        val recyclerItems = ArrayList<RecyclerItemOverview>()
        var lastDate = ""

        for ((id, name, variety1, _, _, color, _, inStock, _, date1) in teaList) {
            val date = getLastUsedHeader(date1)
            if (date != lastDate) {
                recyclerItems.add(RecyclerItemOverview("- $date -", null, null, null, null, false))
                lastDate = date
            }
            val variety = convertStoredVarietyToText(variety1, application)
            recyclerItems.add(RecyclerItemOverview(null, id, name, variety, color, inStock))
        }
        return recyclerItems
    }

    private fun getLastUsedHeader(lastUsed: Date?): String {
        val today = getDate()
        return if (isCurrentWeek(lastUsed, today)) {
            application.getString(R.string.overview_sort_last_used_this_week)
        } else if (isCurrentMonth(lastUsed, today)) {
            application.getString(R.string.overview_sort_last_used_this_month)
        } else if (isCurrentYear(lastUsed, today)) {
            val monthNames = application.resources.getStringArray(R.array.overview_sort_last_used_month)
            val cal = Calendar.getInstance()
            cal.time = lastUsed
            val lastUsedMonth = cal[Calendar.MONTH]
            monthNames[lastUsedMonth]
        } else if (betweenLastTwelveMonth(lastUsed, today)) {
            val monthNamesShort = application.resources.getStringArray(R.array.overview_sort_last_used_month_short)
            val cal = Calendar.getInstance()
            cal.time = lastUsed
            val lastUsedMonth = cal[Calendar.MONTH]
            val monthNameShort = monthNamesShort[lastUsedMonth]
            val lastUsedYear = cal[Calendar.YEAR]
            val year = lastUsedYear.toString()
            "$monthNameShort $year"
        } else {
            val cal = Calendar.getInstance()
            cal.time = lastUsed
            val lastUsedYear = cal[Calendar.YEAR]
            lastUsedYear.toString()
        }
    }

    companion object {
        const val COUNT_MONTH_IN_YEAR = 12
        private fun isCurrentWeek(lastUsed: Date?, currentDate: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = currentDate
            val currentWeek = cal[Calendar.WEEK_OF_YEAR]
            val currentYear = cal[Calendar.YEAR]
            cal.time = lastUsed
            val lastUsedWeek = cal[Calendar.WEEK_OF_YEAR]
            val lastUsedYear = cal[Calendar.YEAR]
            return currentWeek == lastUsedWeek && currentYear == lastUsedYear
        }

        private fun isCurrentMonth(lastUsed: Date?, currentDate: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = currentDate
            val currentMonth = cal[Calendar.MONTH]
            val currentYear = cal[Calendar.YEAR]
            cal.time = lastUsed
            val lastUsedMonth = cal[Calendar.MONTH]
            val lastUsedYear = cal[Calendar.YEAR]
            return currentMonth == lastUsedMonth && currentYear == lastUsedYear
        }

        private fun isCurrentYear(lastUsed: Date?, currentDate: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = currentDate
            val currentYear = cal[Calendar.YEAR]
            cal.time = lastUsed
            val lastUsedYear = cal[Calendar.YEAR]
            return currentYear == lastUsedYear
        }

        private fun betweenLastTwelveMonth(lastUsed: Date?, currentDate: Date): Boolean {
            val cal = Calendar.getInstance()
            cal.time = currentDate
            val currentMonth = cal[Calendar.MONTH]
            val currentYear = cal[Calendar.YEAR]
            cal.time = lastUsed
            val lastUsedMonth = cal[Calendar.MONTH]
            val lastUsedYear = cal[Calendar.YEAR]
            val differenceYear = currentYear - lastUsedYear
            val differenceMonth = currentMonth + differenceYear * COUNT_MONTH_IN_YEAR - lastUsedMonth
            return COUNT_MONTH_IN_YEAR > differenceMonth
        }
    }
}