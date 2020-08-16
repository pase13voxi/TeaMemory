package coolpharaoh.tee.speicher.tea.timer.views.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class ColorConversationTest {
    public static final int BLACK_TEA = 1;
    public static final int GREEN_TEA = 2;
    public static final int YELLOW_TEA = 3;
    public static final int WHITE_TEA = 4;
    public static final int OOLONG_TEA = 5;
    public static final int PUERH_TEA = 6;
    public static final int HERBAL_TEA = 7;
    public static final int FRUIT_TEA = 8;
    public static final int ROOIBUS_TEA = 9;
    public static final int OTHER = -1;

    @Test
    public void getVarietyColorBlackTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(0, context);

        assertThat(color).isEqualTo(Color.parseColor("#141450"));
    }


    @Test
    public void getVarietyColorGreenTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(1, context);

        assertThat(color).isEqualTo(Color.parseColor("#9ACD32"));
    }

    @Test
    public void getVarietyColorYellowTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(2, context);

        assertThat(color).isEqualTo(Color.parseColor("#FFC24B"));
    }

    @Test
    public void getVarietyColorWhiteTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(3, context);

        assertThat(color).isEqualTo(Color.parseColor("#FFF996"));
    }

    @Test
    public void getVarietyColorOolongTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(4, context);

        assertThat(color).isEqualTo(Color.parseColor("#FFA500"));
    }

    @Test
    public void getVarietyColorPuerhTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(5, context);

        assertThat(color).isEqualTo(Color.parseColor("#8B2500"));
    }

    @Test
    public void getVarietyColorHerbalTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(6, context);

        assertThat(color).isEqualTo(Color.parseColor("#439936"));
    }

    @Test
    public void getVarietyColorFruitTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(7, context);

        assertThat(color).isEqualTo(Color.parseColor("#FF2A16"));
    }

    @Test
    public void getVarietyColorRooibusTea() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(8, context);

        assertThat(color).isEqualTo(Color.parseColor("#FA5A00"));
    }

    @Test
    public void getVarietyColorOther() {
        Context context = ApplicationProvider.getApplicationContext();

        int color = ColorConversation.getVarietyColor(-1, context);

        assertThat(color).isEqualTo(Color.parseColor("#7F7FBA"));
    }

    @Test
    public void discoverForgroundColorDark() {
        int colorForeground = ColorConversation.discoverForegroundColor(Color.parseColor("#FFF996"));
        assertThat(colorForeground).isEqualTo(Color.parseColor("#FF000000"));
    }

    @Test
    public void discoverForgroundColorLight() {
        int colorForeground = ColorConversation.discoverForegroundColor(Color.parseColor("#141450"));
        assertThat(colorForeground).isEqualTo(Color.parseColor("#FFFFFFFF"));
    }
}
