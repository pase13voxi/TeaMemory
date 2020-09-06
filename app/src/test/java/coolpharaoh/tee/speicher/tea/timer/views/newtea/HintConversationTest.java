package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HintConversationTest {

    @Mock
    Context context;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        when(context.getResources()).thenReturn(resources);
        when(resources.getString(anyInt())).thenReturn("Hint");
    }

    @Test
    public void getHintTemperatureCelsius() {
        for (int i = 0; i < 10; i++) {
            assertThat(HintConversation.getHintTemperature(i, "Celsius", context)).isEqualTo("Hint");
        }
    }

    @Test
    public void getHintTemperatureFahrenheit() {
        for (int i = 0; i < 10; i++) {
            assertThat(HintConversation.getHintTemperature(i, "Fahrenheit", context)).isEqualTo("Hint");
        }
    }

    @Test
    public void getHintAmountGram() {
        for (int i = 0; i < 10; i++) {
            assertThat(HintConversation.getHintAmount(i, "Gr", context)).isEqualTo("Hint");
        }
    }

    @Test
    public void getHintAmountTeaspoon() {
        for (int i = 0; i < 10; i++) {
            assertThat(HintConversation.getHintAmount(i, "Ts", context)).isEqualTo("Hint");
        }
    }

    @Test
    public void getHintTime() {
        for (int i = 0; i < 10; i++) {
            assertThat(HintConversation.getHintTime(i, context)).isEqualTo("Hint");
        }
    }

}
