package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.GREEN_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OOLONG_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OTHER;

import android.app.Application;
import android.content.res.Resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import coolpharaoh.tee.speicher.tea.timer.R;

@ExtendWith(MockitoExtension.class)
class VarietyTest {
    @Mock
    private Application application;
    @Mock
    private Resources resources;

    private String[] varieties;

    @Test
    void getStoredTextFromGreenTea() {
        assertThat(GREEN_TEA.getCode()).isEqualTo("02_green");
    }

    @Test
    void getColorFromGreenTea() {
        assertThat(GREEN_TEA.getColor()).isEqualTo(R.color.greentea);
    }

    @Test
    void getChoiceFromGreenTea() {
        assertThat(GREEN_TEA.getChoice()).isEqualTo(1);
    }

    @Test
    void varietyFromStoredTextGreenTea() {
        final Variety variety = Variety.fromStoredText(GREEN_TEA.getCode());

        assertThat(variety).isEqualTo(GREEN_TEA);
    }

    @Test
    void varietyFromStoredTextNotDefined() {
        final String notDefinedText = "not defined";

        final Variety variety = Variety.fromStoredText(notDefinedText);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    void varietyFromStoredTextNull() {
        final Variety variety = Variety.fromStoredText(null);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    void varietyFromChoiceGreenTea() {
        final Variety variety = Variety.fromChoice(GREEN_TEA.getChoice());

        assertThat(variety).isEqualTo(GREEN_TEA);
    }

    @Test
    void varietyFromChoiceMinusValue() {
        final Variety variety = Variety.fromChoice(-1);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    void convertBlackTeaVarietyToCode() {
        mockVarietyStrings();
        final String code = Variety.convertTextToStoredVariety(varieties[0], application);

        assertThat(code).isEqualTo(BLACK_TEA.getCode());

    }

    @Test
    void convertOolongTeaVarietyToCode() {
        mockVarietyStrings();
        final String code = Variety.convertTextToStoredVariety(varieties[4], application);

        assertThat(code).isEqualTo(OOLONG_TEA.getCode());

    }

    @Test
    void convertVarietyToCodeReturnInputBecauseVarietyNotExist() {
        mockVarietyStrings();
        final String otherVariety = "Other Variety";
        final String code = Variety.convertTextToStoredVariety(otherVariety, application);

        assertThat(code).isEqualTo(otherVariety);
    }

    @Test
    void convertBlackTeaCodeToVariety() {
        mockVarietyStrings();
        final String variety = Variety.convertStoredVarietyToText(BLACK_TEA.getCode(), application);

        assertThat(variety).isEqualTo(varieties[0]);
    }

    @Test
    void convertOolongTeaCodeToVariety() {
        mockVarietyStrings();
        final String variety = Variety.convertStoredVarietyToText(OOLONG_TEA.getCode(), application);

        assertThat(variety).isEqualTo(varieties[4]);
    }

    @Test
    void convertCodeToVarietyReturnInputBecauseCodeNotExist() {
        mockVarietyStrings();
        final String otherCode = "Other Code";
        final String variety = Variety.convertStoredVarietyToText(otherCode, application);

        assertThat(variety).isEqualTo(otherCode);
    }

    private void mockVarietyStrings() {
        when(application.getResources()).thenReturn(resources);
        varieties = new String[]{"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varieties);
    }
}
