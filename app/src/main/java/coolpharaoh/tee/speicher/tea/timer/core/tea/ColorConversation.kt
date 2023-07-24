package coolpharaoh.tee.speicher.tea.timer.core.tea

object ColorConversation {
    @JvmStatic
    fun discoverForegroundColor(color: Int): Int {
        val red = color shr 16 and 0x000000FF
        val green = color shr 8 and 0x000000FF
        val blue = color and 0x000000FF
        val lum = 0.299 * red + 0.587 * green + 0.114 * blue
        return if (lum > 186) -0x1000000 else -0x1
    }
}