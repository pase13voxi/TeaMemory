package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.ExportJson;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.ImportJson;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.JsonIOAdapter;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ExportImportTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Permissions permissions;
    @Mock
    ExportJson exportJson;
    @Mock
    ImportJson importJson;
    @Mock
    SystemUtility systemUtility;


    @Before
    public void setUp() {
        PermissionRequester.setMockedPermissions(permissions);
        JsonIOAdapter.setMockedExportImport(exportJson, importJson);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.O_MR1);
        CurrentSdk.setFixedSystem(systemUtility);
    }

    @Test
    public void exportTeasAndExpectIntentActionOpenDocumentTree() {
        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(actual.getAction()).isEqualTo(Intent.ACTION_OPEN_DOCUMENT_TREE);
        });
    }

    @Test
    public void exportTeasAndExpectDialogFileLocation(){
        when(exportJson.write(any())).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE);

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_description));
        });
    }

    @Test
    public void exportTeasFailedAndExpectDialogExportFailed(){
        when(exportJson.write(any())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE);

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_description));
        });
    }

    @Test
    public void importTeasExpectImportDialog() {
        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialog = getLatestAlertDialog();
            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header));
        });
    }

    @Test
    public void importTeasDeleteExistingAndExpectDialogImportComplete(){
        when(importJson.read(any(), eq(false))).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialog = getLatestAlertDialog();
            alertDialog.findViewById(R.id.button_export_import_import_delete).performClick();

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(actual.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_delete_dialog_description));
        });
    }

    @Test
    public void importTeasKeepExistingAndExpectDialogImportComplete(){
        when(importJson.read(any(), eq(true))).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialogImport = getLatestAlertDialog();
            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_keep_dialog_description));
        });
    }

    @Test
    public void importTeasAndExpectDialogImportFailed(){
        when(importJson.read(any(), anyBoolean())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialogImport = getLatestAlertDialog();
            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_description));
        });
    }

    @Test
    public void launchActivityWithSdk25AndExpectWarning() {
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.N_MR1);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            TextView textViewWarning = exportImport.findViewById(R.id.text_view_export_import_warning);
            assertThat(textViewWarning.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    private void mockReturnActionActivityResult(ExportImport exportImport, String intent) {
        Intent intentResult = new Intent();
        intentResult.setData(Uri.EMPTY);
        ShadowActivity shadowActivity = Shadows.shadowOf(exportImport);
        shadowActivity.receiveResult(new Intent(intent), Activity.RESULT_OK, intentResult);
    }
}
