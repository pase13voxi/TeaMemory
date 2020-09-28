package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

import android.Manifest;
import android.os.Build;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowApplication;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ExportImportTest {

    @Test
    public void exportTeasAndExpectPermissionRequest(){
        ActivityScenario<ExportImport> mainActivityScenario = ActivityScenario.launch(ExportImport.class);
        mainActivityScenario.onActivity(exportImport -> {
            ShadowApplication app = Shadows.shadowOf(exportImport.getApplication());
            app.grantPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            Button buttonExport = exportImport.findViewById(R.id.buttonExport);
            buttonExport.performClick();
            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog.getTitle()).isEqualTo("Location of the file");
            assertThat(shadowAlertDialog.getMessage()).isEqualTo("The file tealist.json could be found in the internal storage of your smartphone in the folder TeaMemory.");
        });
    }

    @Test
    public void importTeasAndExpectPermissionRequest(){
        ActivityScenario<ExportImport> mainActivityScenario = ActivityScenario.launch(ExportImport.class);
        mainActivityScenario.onActivity(exportImport -> {
            Button buttonImport = exportImport.findViewById(R.id.buttonImport);
            buttonImport.performClick();
        });
    }
}
