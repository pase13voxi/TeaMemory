package coolpharaoh.tee.speicher.tea.timer.views.export_import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapter;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapterFactory;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.DatabaseJsonTransformer;

@RunWith(RobolectricTestRunner.class)
public class ExportImportTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    DatabaseJsonTransformer databaseJsonTransformer;
    @Mock
    DataIOAdapter dataIOAdapter;

    @Before
    public void setUp() {
        DataIOAdapterFactory.setMockedDataIO(dataIOAdapter);
        JsonIOAdapter.setMockedTransformer(databaseJsonTransformer);
    }

    @After
    public void tearDown() {
        JsonIOAdapter.setMockedTransformer(null);
    }

    @Test
    public void exportTeasAndExpectIntentActionOpenDocumentTree() {
        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(actual.getAction()).isEqualTo(Intent.ACTION_OPEN_DOCUMENT_TREE);
        });
    }

    @Test
    public void exportTeasAndExpectDialogFileLocation(){
        when(dataIOAdapter.write(any())).thenReturn(true);

        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_location_dialog_description));
        });
    }

    @Test
    public void exportTeasFailedAndExpectDialogExportFailed(){
        when(dataIOAdapter.write(any())).thenReturn(false);

        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonExport = exportImport.findViewById(R.id.button_export_import_export);
            buttonExport.performClick();

            mockReturnActionActivityResult(exportImport, Intent.ACTION_OPEN_DOCUMENT_TREE);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_export_failed_dialog_description));
        });
    }

    @Test
    public void importTeasExpectImportDialog() {
        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            final AlertDialog alertDialog = getLatestAlertDialog();
            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_dialog_header));
        });
    }

    @Test
    public void importTeasDeleteExistingAndExpectDialogImportComplete(){
        when(databaseJsonTransformer.jsonToDatabase(any(), eq(false))).thenReturn(true);

        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            final AlertDialog alertDialog = getLatestAlertDialog();
            alertDialog.findViewById(R.id.button_export_import_import_delete).performClick();

            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(actual.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            final ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_delete_dialog_description));
        });
    }

    @Test
    public void importTeasKeepExistingAndExpectDialogImportComplete(){
        when(databaseJsonTransformer.jsonToDatabase(any(), eq(true))).thenReturn(true);

        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            final AlertDialog alertDialogImport = getLatestAlertDialog();
            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            final Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            final ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_complete_keep_dialog_description));
        });
    }

    @Test
    public void importTeasAndExpectDialogImportFailed(){
        when(databaseJsonTransformer.jsonToDatabase(any(), anyBoolean())).thenReturn(false);

        final ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            final Button buttonImport = exportImport.findViewById(R.id.button_export_import_import);
            buttonImport.performClick();

            final AlertDialog alertDialogImport = getLatestAlertDialog();
            alertDialogImport.findViewById(R.id.button_export_import_import_keep).performClick();

            final Intent intentChooser = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();
            assertThat(intentChooser.getAction()).isEqualTo(Intent.ACTION_CHOOSER);

            mockReturnActionActivityResult(exportImport, Intent.ACTION_CHOOSER);

            final ShadowAlertDialog shadowAlertDialogImportComplete = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialogImportComplete.getTitle()).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_header));
            assertThat(shadowAlertDialogImportComplete.getMessage()).isEqualTo(exportImport.getString(R.string.export_import_import_failed_dialog_description));
        });
    }

    private void mockReturnActionActivityResult(final ExportImport exportImport, final String intent) {
        final Intent intentResult = new Intent();
        intentResult.setData(Uri.EMPTY);
        final ShadowActivity shadowActivity = Shadows.shadowOf(exportImport);
        shadowActivity.receiveResult(new Intent(intent), Activity.RESULT_OK, intentResult);
    }
}
