package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

import android.os.Build;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

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
import org.robolectric.shadows.ShadowAlertDialog;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ExportImportTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Permissions permissions;

    @Before
    public void setUp(){
        PermissionRequester.setMockedPermissions(permissions);
    }

    @Test
    public void exportTeasAndExpectPermissionRequest(){
        when(permissions.checkWritePermission(any())).thenReturn(false);
        when(permissions.checkWritePermissionDeniedBefore(any())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.buttonExport);
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
            Button buttonExport = exportImport.findViewById(R.id.buttonExport);
            buttonExport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.exportimport_write_permission_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.exportimport_write_permission_dialog_description));

            verify(permissions, never()).getWritePermission(any());
        });
    }

    @Test
    public void exportTeasAndExpectExportedJson(){
        when(permissions.checkWritePermission(any())).thenReturn(true);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonExport = exportImport.findViewById(R.id.buttonExport);
            buttonExport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.exportimport_location_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.exportimport_location_dialog_description));
        });
    }

    @Test
    public void importTeasAndExpectPermissionRequest(){
        when(permissions.checkReadPermission(any())).thenReturn(false);
        when(permissions.checkReadPermissionDeniedBefore(any())).thenReturn(false);

        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.buttonImport);
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
            Button buttonImport = exportImport.findViewById(R.id.buttonImport);
            buttonImport.performClick();

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo(exportImport.getString(R.string.exportimport_read_permission_dialog_header));
            assertThat(shadowAlertDialog.getMessage()).isEqualTo(exportImport.getString(R.string.exportimport_read_permission_dialog_description));

            verify(permissions, never()).getReadPermission(any());
        });
    }
}
