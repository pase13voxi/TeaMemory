package coolpharaoh.tee.speicher.tea.timer.core.settings

import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode.Companion.fromChoice
import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode.Companion.fromText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DarkModeTest {
    @Test
    fun textSystem() {
        val darkMode = DarkMode.ENABLED
        assertThat(darkMode.text).isEqualTo("enabled")
    }

    @Test
    fun choiceSystem() {
        val darkMode = DarkMode.ENABLED
        assertThat(darkMode.choice).isOne
    }

    @Test
    fun darkModeFromTextSystem() {
        val darkMode = fromText("enabled")
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED)
    }

    @Test
    fun darkModeFromTextNotDefined() {
        val darkMode = fromText("not defined")
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM)
    }

    @Test
    fun darkModeFromTextNull() {
        val darkMode = fromText(null)
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM)
    }

    @Test
    fun darkModeFromChoiceZero() {
        val darkMode = fromChoice(1)
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED)
    }

    @Test
    fun darkModeFromChoiceMinusOne() {
        val darkMode = fromChoice(-1)
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM)
    }
}