package coolpharaoh.tee.speicher.tea.timer.core.Language;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;

import static org.assertj.core.api.Assertions.assertThat;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class LanguageConversationTest {
    private static final Context CONTEXT = ApplicationProvider.getApplicationContext();
    private String[] codes;
    private String[] varieties;

    @Before
    public void setUp() {
        codes = CONTEXT.getResources().getStringArray(R.array.variety_codes);
        varieties = CONTEXT.getResources().getStringArray(R.array.variety_teas);
    }

    @Test
    public void convertBlackTeaVarietyToCode() {
        String code = LanguageConversation.convertVarietyToCode(varieties[0], CONTEXT);

        assertThat(code).isEqualTo(codes[0]);

    }

    @Test
    public void convertOolongTeaVarietyToCode() {
        String code = LanguageConversation.convertVarietyToCode(varieties[4], CONTEXT);

        assertThat(code).isEqualTo(codes[4]);

    }

    @Test
    public void convertVarietyToCodeReturnInputBecauseVarietyNotExist() {
        String otherVariety = "Other Variety";
        String code = LanguageConversation.convertVarietyToCode(otherVariety, CONTEXT);

        assertThat(code).isEqualTo(otherVariety);

    }

    @Test
    public void convertBlackTeaCodeToVariety() {
        String variety = LanguageConversation.convertCodeToVariety(codes[0], CONTEXT);

        assertThat(variety).isEqualTo(varieties[0]);
    }

    @Test
    public void convertOolongTeaCodeToVariety() {
        String variety = LanguageConversation.convertCodeToVariety(codes[4], CONTEXT);

        assertThat(variety).isEqualTo(varieties[4]);
    }

    @Test
    public void convertCodeToVarietyReturnInputBecauseCodeNotExist() {
        String otherCode = "Other Code";
        String variety = LanguageConversation.convertCodeToVariety(otherCode, CONTEXT);

        assertThat(variety).isEqualTo(otherCode);
    }
}
