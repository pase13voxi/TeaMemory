package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.main.Main;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ShowTeaTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void launchActivityWithNoTeaIdAndExpectFailingDialog() {
        ActivityScenario<ShowTea> newTeaActivityScenario = ActivityScenario.launch(ShowTea.class);
        newTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            ShadowAlertDialog shadowDialogFail = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowDialogFail).isNotNull();
            assertThat(shadowDialogFail.getTitle()).isEqualTo(showTea.getString(R.string.showtea_dialog_tea_missing_header));
            assertThat(shadowDialogFail.getMessage()).isEqualTo(showTea.getString(R.string.showtea_dialog_tea_missing_description));

            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityWithNotExistingTeaIdExpectFailingDialog() {
        mockDB();
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra("teaId", 1l);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            ShadowAlertDialog shadowDialogFail = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowDialogFail).isNotNull();
            assertThat(shadowDialogFail.getTitle()).isEqualTo(showTea.getString(R.string.showtea_dialog_tea_missing_header));
            assertThat(shadowDialogFail.getMessage()).isEqualTo(showTea.getString(R.string.showtea_dialog_tea_missing_description));

            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

}
