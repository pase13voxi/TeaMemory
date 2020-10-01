package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
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
    Tea tea;
    List<Infusion> infusions;
    Counter counter;
    Note note;

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

    @Test
    public void launchActivityWithNoTeaIdAndExpectFailingDialog() {
        ActivityScenario<ShowTea> newTeaActivityScenario = ActivityScenario.launch(ShowTea.class);
        newTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            ShadowAlertDialog shadowDialogFail = Shadows.shadowOf(dialogFail);
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
        intent.putExtra("teaId", 5l);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            ShadowAlertDialog shadowDialogFail = Shadows.shadowOf(dialogFail);
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
    public void launchActivityAndExpectDescriptionDialog() {
        mockDB();
        mockTea(1, false, false);
        mockActualSettings("Celsius", true);
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra("teaId", tea.getId());

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            ShadowAlertDialog shadowDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowDialog.getTitle()).isEqualTo(showTea.getString(R.string.showtea_dialog_description_header));
        });
    }

    @Test
    public void launchActivityWithStandardValuesAndExpectFilledActivity() {
        mockDB();
        mockTea(1, false, false);
        mockActualSettings("Celsius", false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra("teaId", tea.getId());

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            TextView textViewName = showTea.findViewById(R.id.textViewName);
            TextView textViewVariety = showTea.findViewById(R.id.textViewVariety);
            Button buttonNote = showTea.findViewById(R.id.buttonNote);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewName.getText()).isEqualTo(tea.getName());
            assertThat(textViewVariety.getText()).isEqualTo(tea.getVariety());
            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(0).getTemperatureCelsius()+" °C");
            assertThat(textViewAmount.getText()).isEqualTo(tea.getAmount() + " g/L");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    private void mockTea(int infusionCount, boolean withCounter, boolean withNote){
        tea = new Tea("name", "variety", 1, "Gr", 1, 1, CurrentDate.getDate());
        tea.setId(1L);
        when(teaDao.getTeaById(tea.getId())).thenReturn(tea);

        infusions = new ArrayList<>();
        for(long id = 1L; id < (infusionCount + 1); id++) {
            Infusion infusion = new Infusion(1L, (int)(id - 1), "1:00", null, 100, 212);
            infusion.setId(id);
            infusions.add(infusion);
        }
        when(infusionDao.getInfusionsByTeaId(1L)).thenReturn(infusions);

        if(withCounter) {
            counter = new Counter(1L, 1, 1, 1, 1, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
            counter.setId(1L);
            when(counterDao.getCounterByTeaId(1L)).thenReturn(counter);
        }

        if(withNote) {
            note = new Note(1L, 1, "header", "description");
            note.setId(1L);
            when(noteDao.getNoteByTeaId(1L)).thenReturn(note);
        }
    }

    private void mockActualSettings(String temperatureUnit, boolean dialog) {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit(temperatureUnit);
        actualSettings.setShowTeaAlert(dialog);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

}
