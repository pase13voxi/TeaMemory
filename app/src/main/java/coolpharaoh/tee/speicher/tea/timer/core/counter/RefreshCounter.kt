package coolpharaoh.tee.speicher.tea.timer.core.counter

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import java.util.Calendar
import java.util.Date

object RefreshCounter {
    @JvmStatic
    fun refreshCounters(counters: List<Counter>): List<Counter> {
        for (counter in counters) {
            refreshCounter(counter)
        }
        return counters
    }

    @JvmStatic
    fun refreshCounter(counter: Counter) {
        val currentDate = getDate()
        if (counter.hasEmptyFields()) {
            resetCounter(counter, currentDate)
        } else {
            refreshWeek(counter, currentDate)
            refreshMonth(counter, currentDate)
            refreshYear(counter, currentDate)
        }
    }

    private fun resetCounter(counter: Counter, currentDate: Date?) {
        counter.week = 0
        counter.month = 0
        counter.year = 0
        counter.overall = 0
        counter.weekDate = currentDate
        counter.monthDate = currentDate
        counter.yearDate = currentDate
    }

    private fun refreshWeek(counter: Counter, currentDate: Date?) {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val currentWeek = cal[Calendar.WEEK_OF_YEAR]
        val currentYear = cal[Calendar.YEAR]
        cal.time = counter.weekDate
        val countWeek = cal[Calendar.WEEK_OF_YEAR]
        val countYear = cal[Calendar.YEAR]
        if (currentWeek != countWeek || currentYear != countYear) {
            counter.week = 0
            counter.weekDate = currentDate
        }
    }

    private fun refreshMonth(counter: Counter, currentDate: Date?) {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val currentMonth = cal[Calendar.MONTH]
        val currentYear = cal[Calendar.YEAR]
        cal.time = counter.monthDate
        val countMonth = cal[Calendar.MONTH]
        val countYear = cal[Calendar.YEAR]
        if (currentMonth != countMonth || currentYear != countYear) {
            counter.month = 0
            counter.monthDate = currentDate
        }
    }

    private fun refreshYear(counter: Counter, currentDate: Date?) {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val currentYear = cal[Calendar.YEAR]
        cal.time = counter.yearDate
        val countYear = cal[Calendar.YEAR]
        if (currentYear != countYear) {
            counter.year = 0
            counter.yearDate = currentDate
        }
    }
}