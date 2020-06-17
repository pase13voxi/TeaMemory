package coolpharaoh.tee.speicher.tea.timer.views.exportimport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.ExportJson;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.ImportJson;
import coolpharaoh.tee.speicher.tea.timer.views.utils.Permissions;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.Permissions.REQUEST_CODE_READ;
import static coolpharaoh.tee.speicher.tea.timer.views.utils.Permissions.REQUEST_CODE_WRITE;

public class ExportImport extends AppCompatActivity {
    private boolean keepStoredTeas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_import);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        Button buttonExport = findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(v -> checkPermissionsBeforeExport());

        Button buttonImport = findViewById(R.id.buttonImport);
        buttonImport.setOnClickListener(v -> checkPermissionsBeforeImport());

        showWarning();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.exportimport_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void checkPermissionsBeforeExport() {
        if (!Permissions.checkWritePermission(this)) {
            if (Permissions.checkWritePermissionDeniedBefore(this)) {
                dialogAfterWritePermissionDenied();
            } else {
                Permissions.getWritePermission(this);
            }
        } else {
            exportJson();
        }
    }

    private void dialogAfterWritePermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_write_permission_dialog_header);
        builder.setMessage(R.string.exportimport_write_permission_dialog_description);
        builder.setPositiveButton(R.string.exportimport_location_dialog_ok, (dialog, which) -> Permissions.getWritePermission(this));
        builder.show();
    }

    private void checkPermissionsBeforeImport() {
        if (!Permissions.checkReadPermission(this)) {
            if (Permissions.checkReadPermissionDeniedBefore(this)) {
                dialogAfterReadPermissionDenied();
            } else {
                Permissions.getReadPermission(this);
            }
        } else {
            dialogImport();
        }
    }

    private void dialogAfterReadPermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_read_permission_dialog_header);
        builder.setMessage(R.string.exportimport_read_permission_dialog_description);
        builder.setPositiveButton(R.string.exportimport_location_dialog_ok, (dialog, which) -> Permissions.getReadPermission(this));
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ:
                codeRequestRead(grantResults);
                break;
            case REQUEST_CODE_WRITE:
                codeRequestWrite(grantResults);
                break;
            default:
        }
    }

    private void codeRequestWrite(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportJson();
        } else {
            Toast.makeText(getApplicationContext(), R.string.exportimport_write_permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void exportJson() {
        ExportJson exportJson = new ExportJson(getApplicationContext());
        if (exportJson.write()) {
            dialogExportLocation();
        } else {
            dialogExportFailed();
        }
    }

    private void dialogExportLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_location_dialog_header);
        builder.setMessage(R.string.exportimport_location_dialog_description).setPositiveButton(R.string.exportimport_location_dialog_ok, null).show();
    }

    private void dialogExportFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_export_failed_dialog_header);
        builder.setMessage(R.string.exportimport_export_failed_dialog_description).setPositiveButton(R.string.exportimport_export_failed_dialog_ok, null).show();
    }

    private void codeRequestRead(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dialogImport();
        } else {
            Toast.makeText(getApplicationContext(), R.string.exportimport_read_permission_denied, Toast.LENGTH_LONG).show();
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
            buttonImportDelete.setOnClickListener(view -> importFile(alertDialog, false));
            final Button buttonImportKeep = layoutDialogImport.findViewById(R.id.buttonImportKeep);
            buttonImportKeep.setOnClickListener(view -> importFile(alertDialog, true));

            alertDialog.show();
        }
    }

    private void importFile(AlertDialog alertDialog, boolean keepTeas) {
        dialogImportFile();
        keepStoredTeas = keepTeas;
        alertDialog.cancel();
    }

    private void dialogImportFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,
                getApplicationContext().getResources().getString(R.string.exportimport_import_choose_file)), ImportJson.READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ImportJson.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            ImportJson importJson = new ImportJson(resultData.getData(), getApplicationContext());
            if (importJson.read(keepStoredTeas)) {
                dialogImportComplete();
            } else {
                dialogImportFailed();
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void dialogImportComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_import_complete_dialog_header);
        if (keepStoredTeas) {
            builder.setMessage(R.string.exportimport_import_complete_keep_dialog_description).setPositiveButton(R.string.exportimport_import_complete_dialog_ok, null).show();
        } else {
            builder.setMessage(R.string.exportimport_import_complete_delete_dialog_description).setPositiveButton(R.string.exportimport_import_complete_dialog_ok, null).show();
        }
    }

    private void dialogImportFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exportimport_import_failed_dialog_header);
        builder.setMessage(R.string.exportimport_import_failed_dialog_description).setPositiveButton(R.string.exportimport_import_failed_dialog_ok, null).show();
    }

    private void showWarning() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            TextView warning = findViewById(R.id.textViewWarning);
            warning.setVisibility(View.VISIBLE);
        }
    }
}
