/*package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import coolpharaoh.tee.speicher.tea.timer.views.main.Main;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ShowTeaTest {

    private ShowTea activity;

    @Rule
    public IntentsTestRule<ShowTea> showTeaActivityTestRule
            = new IntentsTestRule<>(ShowTea.class, false, false);

    @Test
    public void launchActivityWithNoTeaId_ExpectFailingDialog() throws Throwable {
        launchActivityWithTeaId(null);

        AlertDialog dialog = activity.getLastDialog();

        assertThat(dialog.isShowing()).isTrue();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        performClick(button);

        intended(hasComponent(Main.class.getName()));
    }

    @Test
    public void launchActivityWithNotExistingTeaId_ExpectFailingDialog() throws Throwable {
        launchActivityWithTeaId(1);

        AlertDialog dialog = activity.getLastDialog();

        assertThat(dialog.isShowing()).isTrue();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        performClick(button);

        intended(hasComponent(Main.class.getName()));
    }

    private void launchActivityWithTeaId(Integer teaId) {
        if (teaId == null) {
            activity = showTeaActivityTestRule.launchActivity(null);
        } else {
            Intent intent = new Intent();
            intent.putExtra("teaId", teaId.longValue());
            activity = showTeaActivityTestRule.launchActivity(intent);
        }
    }

    private void performClick(Button button) throws Throwable {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                button.performClick();
            }
        });
    }
}
*/
