package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R

enum class Variety(val code: String, val color: Int, val choice: Int) {
    BLACK_TEA("01_black", R.color.blacktea, 0),
    GREEN_TEA("02_green", R.color.greentea, 1),
    YELLOW_TEA("03_yellow", R.color.yellowtea, 2),
    WHITE_TEA("04_white", R.color.whitetea, 3),
    OOLONG_TEA("05_oolong", R.color.oolongtea, 4),
    PU_ERH_TEA("06_pu", R.color.puerhtea, 5),
    HERBAL_TEA("07_herbal", R.color.herbaltea, 6),
    FRUIT_TEA("08_fruit", R.color.fruittea, 7),
    ROOIBUS_TEA("09_rooibus", R.color.rooibustea, 8),
    OTHER("10_other", R.color.other, 9);

    companion object {
        @JvmStatic
        fun fromStoredText(storedText: String?): Variety {
            for (variety in values()) {
                if (variety.code.equals(storedText, ignoreCase = true)) {
                    return variety
                }
            }
            return OTHER
        }

        @JvmStatic
        fun fromChoice(choice: Int): Variety {
            for (variety in values()) {
                if (variety.choice == choice) {
                    return variety
                }
            }
            return OTHER
        }

        @JvmStatic
        fun convertTextToStoredVariety(text: String?, application: Application): String? {
            val texts = application.resources.getStringArray(R.array.new_tea_variety_teas)
            val varieties = values()
            for (i in texts.indices) {
                if (texts[i] == text) {
                    return varieties[i].code
                }
            }
            return text
        }

        @JvmStatic
        fun convertStoredVarietyToText(storedText: String?, application: Application): String? {
            val texts = application.resources.getStringArray(R.array.new_tea_variety_teas)
            val variety = fromStoredText(storedText)
            return if (OTHER == variety && OTHER.code != storedText) {
                storedText
            } else {
                texts[variety.choice]
            }
        }
    }
}