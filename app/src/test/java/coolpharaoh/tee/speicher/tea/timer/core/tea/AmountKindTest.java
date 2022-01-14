package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AmountKindTest {

    @Test
    void getTextGram() {
        final AmountKind amountKind = AmountKind.GRAM;
        assertThat(amountKind.getText()).isEqualTo("Gr");
    }

    @Test
    void getChoiceGram() {
        final AmountKind amountKind = AmountKind.TEA_SPOON;
        assertThat(amountKind.getChoice()).isZero();
    }

    @Test
    void amountKindFromTextGram() {
        final AmountKind amountKind = AmountKind.fromText("Gr");
        assertThat(amountKind).isEqualTo(AmountKind.GRAM);
    }

    @Test
    void amountKindFromTextNotDefined() {
        final AmountKind amountKind = AmountKind.fromText("not defined");
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }

    @Test
    void amountKindFromTextNull() {
        final AmountKind amountKind = AmountKind.fromText(null);
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }

    @Test
    void amountKindFromChoiceZero() {
        final AmountKind amountKind = AmountKind.fromChoice(1);
        assertThat(amountKind).isEqualTo(AmountKind.GRAM);
    }

    @Test
    void amountKindFromChoiceMinusOne() {
        final AmountKind amountKind = AmountKind.fromChoice(-1);
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }
}
