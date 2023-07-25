package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import java.util.Arrays
import java.util.Collections

class RecyclerItemOverview (var category: String?,
                            var teaId: Long?,
                            var teaName: String?,
                            var variety: String?,
                            var color: Int?,
                            var favorite: Boolean
                            ) {
    val imageText: String
        get() {
            val split = ArrayList<String?>()
            Collections.addAll(split, *teaName!!.split(" ".toRegex()).toTypedArray())
            split.removeAll(setOf("", null))
            var imageText = ""
            if (split.size == 1) {
                imageText = split[0]!![0].toString()
            } else if (split.size > 1) {
                val firstCharFirstWord = split[0]!![0].toString()
                val firstCharLastWord = split[split.size - 1]!![0].toString()
                imageText = firstCharFirstWord + firstCharLastWord
            }
            imageText = imageText.uppercase()
            return imageText
        }
}