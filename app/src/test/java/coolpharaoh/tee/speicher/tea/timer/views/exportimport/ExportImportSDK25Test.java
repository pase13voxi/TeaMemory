package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

@Config(sdk = Build.VERSION_CODES.N_MR1)
@RunWith(RobolectricTestRunner.class)
public class ExportImportSDK25Test {
    @Test
    public void launchActivityAndExpectWarning(){
        ActivityScenario<ExportImport> exportImportActivityScenario = ActivityScenario.launch(ExportImport.class);
        exportImportActivityScenario.onActivity(exportImport -> {
            TextView textViewWarning = exportImport.findViewById(R.id.textViewWarning);
            assertThat(textViewWarning.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }
}
