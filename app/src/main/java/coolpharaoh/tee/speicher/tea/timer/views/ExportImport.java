package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datatransfer.ExportJson;
import coolpharaoh.tee.speicher.tea.timer.datatransfer.ImportJson;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.ExportImportViewModel;

public class ExportImport extends AppCompatActivity {
    private ExportImportViewModel mExportImportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_import);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.exportimport_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mExportImportViewModel = new ExportImportViewModel(getApplicationContext());

        Button buttonExport = findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(v -> {
            ExportJson exportJson = new ExportJson(mExportImportViewModel.getTeaList(),
                    mExportImportViewModel.getInfusionList(), mExportImportViewModel.getCounterList(), mExportImportViewModel.getNoteList());
            exportJson.write(getApplicationContext());
        });

        Button buttonImport = findViewById(R.id.buttonImport);
        buttonImport.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/octet-stream");
            startActivityForResult(Intent.createChooser(intent,
                    "Choose File to Upload.."), ImportJson.READ_REQUEST_CODE);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ImportJson.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                ImportJson importJson = new ImportJson(resultData.getData());
                importJson.read(getApplicationContext(), mExportImportViewModel);
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }
}
