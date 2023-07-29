package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import android.app.Application

object SuggestionsFactory {
    @JvmStatic
    fun getSuggestions(variety: Int, application: Application): Suggestions {
        return when (variety) {
            0 -> BlackTeaSuggestions(application)
            1 -> GreenTeaSuggestions(application)
            2 -> YellowTeaSuggestions(application)
            3 -> WhiteTeaSuggestions(application)
            4 -> OolongTeaSuggestions(application)
            5 -> PuerhTeaSuggestions(application)
            6 -> HerbalTeaSuggestions(application)
            7 -> FruitTeaSuggestions(application)
            8 -> RooibusTeaSuggestions(application)
            else -> NoSuggestions()
        }
    }
}