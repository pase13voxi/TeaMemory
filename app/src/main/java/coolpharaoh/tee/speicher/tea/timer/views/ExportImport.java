package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private Boolean mKeepStoredTeas = true;

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
            dialogExportLocation(v.getContext());
        });

        Button buttonImport = findViewById(R.id.buttonImport);
        buttonImport.setOnClickListener(v -> dialogImport());
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
            if (resultData != null) {
                ImportJson importJson = new ImportJson(resultData.getData());
                importJson.read(getApplicationContext(), mExportImportViewModel, mKeepStoredTeas);
                dialogImportComplete(this);
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void dialogExportLocation(Context context){
        //Infomationen anzeigen
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.exportimport_location_dialog_header);
        builder.setMessage(R.string.exportimport_location_dialog_description).setPositiveButton(R.string.exportimport_location_dialog_ok, null).show();
    }

    private void dialogImportComplete(Context context){
        //Infomationen anzeigen
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.exportimport_import_complete_dialog_header);
        if(mKeepStoredTeas) {
            builder.setMessage(R.string.exportimport_import_complete_keep_dialog_description).setPositiveButton(R.string.exportimport_import_complete_dialog_ok, null).show();
        }
        else {
            builder.setMessage(R.string.exportimport_import_complete_delete_dialog_description).setPositiveButton(R.string.exportimport_import_complete_dialog_ok, null).show();
        }
    }

    private void dialogImport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewGroup parent = findViewById(R.id.exportimport_parent);

            LayoutInflater inflater = getLayoutInflater();
            View layoutDialogImport = inflater.inflate(R.layout.dialogimport, parent, false);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setView(layoutDialogImport);
            alertDialog.setTitle(R.string.exportimport_import_dialog_header);

            final Button buttonImportDelete = layoutDialogImport.findViewById(R.id.buttonImportDelete);
            buttonImportDelete.setOnClickListener(view -> {
                dialogImportFile();
                mKeepStoredTeas = false;
                alertDialog.cancel();
            });
            final Button buttonImportKeep = layoutDialogImport.findViewById(R.id.buttonImportKeep);
            buttonImportKeep.setOnClickListener(view -> {
                dialogImportFile();
                mKeepStoredTeas = true;
                alertDialog.cancel();
            });

            alertDialog.show();

        }
    }

    private void dialogImportFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        startActivityForResult(Intent.createChooser(intent,
                getApplicationContext().getResources().getString(R.string.exportimport_import_choose_file)), ImportJson.READ_REQUEST_CODE);
    }
}
