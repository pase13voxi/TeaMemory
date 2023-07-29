package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

class NoSuggestions : Suggestions {

    override val amountTsSuggestions: IntArray
        get() = intArrayOf()

    override val amountGrSuggestions: IntArray
        get() = intArrayOf()

    override val amountTbSuggestions: IntArray
        get() = intArrayOf()

    override val temperatureCelsiusSuggestions: IntArray
        get() = intArrayOf()

    override val temperatureFahrenheitSuggestions: IntArray
        get() = intArrayOf()

    override val timeSuggestions: Array<String>
        get() = arrayOf()
}