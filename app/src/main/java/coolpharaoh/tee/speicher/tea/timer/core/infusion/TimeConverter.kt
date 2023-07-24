package coolpharaoh.tee.speicher.tea.timer.core.infusion

import java.text.DecimalFormat


class TimeConverter {
    var time: String? = null
    var minutes = 0
    var seconds = 0

    constructor(time: String?) {
        this.time = time
        extractMinutesAndSeconds()
    }

    constructor(minutes: Int, seconds: Int) {
        this.minutes = minutes
        this.seconds = seconds
        extractText()
    }

    private fun extractText() {
        val formatter = DecimalFormat("00")
        val formattedMinutes = formatter.format(minutes.toLong())
        val formattedSeconds = formatter.format(seconds.toLong())
        time = "$formattedMinutes:$formattedSeconds"
    }

    private fun extractMinutesAndSeconds() {
        if (time == null) {
            minutes = 0
            seconds = 0
        } else {
            val split = time!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            minutes = split[0].toInt()
            seconds = if (split.size > 1) split[1].toInt() else 0
        }
    }
}