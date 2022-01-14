package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DarkModeTest {

    @Test
    void getTextSystem() {
        final DarkMode darkMode = DarkMode.ENABLED;
        assertThat(darkMode.getText()).isEqualTo("enabled");
    }

    @Test
    void getChoiceSystem() {
        final DarkMode darkMode = DarkMode.ENABLED;
        assertThat(darkMode.getChoice()).isOne();
    }

    @Test
    void darkModeFromTextSystem() {
        final DarkMode darkMode = DarkMode.fromText("enabled");
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    void darkModeFromTextNotDefined() {
        final DarkMode darkMode = DarkMode.fromText("not defined");
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }

    @Test
    void darkModeFromTextNull() {
        final DarkMode darkMode = DarkMode.fromText(null);
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }

    @Test
    void darkModeFromChoiceZero() {
        final DarkMode darkMode = DarkMode.fromChoice(1);
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    void darkModeFromChoiceMinusOne() {
        final DarkMode darkMode = DarkMode.fromChoice(-1);
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }
}
