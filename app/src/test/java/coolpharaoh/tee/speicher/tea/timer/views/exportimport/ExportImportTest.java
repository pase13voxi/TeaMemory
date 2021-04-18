package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

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
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.ExportJson;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.ImportJson;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.JsonIOAdapter;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    public void exportTeasAndExpectPermissionRequest(){
        when(permissions.checkWritePermission(any())).thenReturn(false);
        when(permissions.checkWritePermissionDeniedBefore(any())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            verify(permissions).getWritePermission(any());
        });
    }

    @Test
    public void exportTeasAndExpectDialogBeforePermissionRequest(){
        when(permissions.checkWritePermission(any())).thenReturn(false);
        when(permissions.checkWritePermissionDeniedBefore(any())).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_write_permission_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_write_permission_dialog_description));

            verify(permissions, never()).getWritePermission(any());
        });
    }

    @Test
    public void exportTeasAndExpectDialogFileLocation(){
        when(permissions.checkWritePermission(any())).thenReturn(true);
        when(exportJson.write()).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_description));
        });
    }

    @Test
    public void exportTeasFailedAndExpectDialogExportFailed(){
        when(permissions.checkWritePermission(any())).thenReturn(true);
        when(exportJson.write()).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_description));
        });
    }

    @Test
    public void importTeasAndExpectPermissionRequest(){
        when(permissions.checkReadPermission(any())).thenReturn(false);
        when(permissions.checkReadPermissionDeniedBefore(any())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            verify(permissions).getReadPermission(any());
        });
    }

    @Test
    public void importTeasAndExpectDialogBeforePermissionRequest(){
        when(permissions.checkReadPermission(any())).thenReturn(false);
        when(permissions.checkReadPermissionDeniedBefore(any())).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_read_permission_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_read_permission_dialog_description));

            verify(permissions, never()).getReadPermission(any());
        });
    }

    @Test
    public void importTeasDeleteExistingAndExpectDialogImportComplete(){
        when(permissions.checkReadPermission(any())).thenReturn(true);
        when(importJson.read(any(), eq(false))).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialog = getLatestAlertDialog();
            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header));

            alertDialog.findViewById(R.id.button_export_import_import_delete).performClick();

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(actual.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActivityResult(exportImport);

            ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_delete_dialog_description));
        });
    }

    @Test
    public void importTeasKeepExistingAndExpectDialogImportComplete(){
        when(permissions.checkReadPermission(any())).thenReturn(true);
        when(importJson.read(any(), eq(true))).thenReturn(true);


        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialogImport = getLatestAlertDialog();
            ShadowAlertDialog shadowAlertDialogChooseImport = Shadows.shadowOf(alertDialogImport);
            assertThat(shadowAlertDialogChooseImport.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header));

            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActivityResult(exportImport);

            ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_keep_dialog_description));
        });
    }

    @Test
    public void importTeasAndExpectDialogImportFailed(){
        when(permissions.checkReadPermission(any())).thenReturn(true);
        when(importJson.read(any(), anyBoolean())).thenReturn(false);


        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            AlertDialog alertDialogImport = getLatestAlertDialog();
            ShadowAlertDialog shadowAlertDialogChooseImport = Shadows.shadowOf(alertDialogImport);
            assertThat(shadowAlertDialogChooseImport.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header));

            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActivityResult(exportImport);

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

    private void mockReturnActivityResult(ExportImport exportImport) {
        Intent intentResult = new Intent();
        intentResult.setData(Uri.EMPTY);
        ShadowActivity shadowActivity = Shadows.shadowOf(exportImport);
        shadowActivity.receiveResult(new Intent(Intent.ACTION_CHOOSER), Activity.RESULT_OK, intentResult);
    }
}
