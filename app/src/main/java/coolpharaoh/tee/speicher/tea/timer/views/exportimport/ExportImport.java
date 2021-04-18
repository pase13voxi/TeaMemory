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
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.ImportJson;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.JsonIOAdapter;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions.REQUEST_CODE_READ;
import static coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions.REQUEST_CODE_WRITE;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class ExportImport extends AppCompatActivity implements Printer {
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
        TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.export_import_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void checkPermissionsBeforeExport() {
        if (!PermissionRequester.checkWritePermission(this)) {
            if (PermissionRequester.checkWritePermissionDeniedBefore(this)) {
                dialogAfterWritePermissionDenied();
            } else {
                PermissionRequester.getWritePermission(this);
            }
        } else {
            exportJson();
        }
    }

    private void dialogAfterWritePermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_write_permission_dialog_header);
        builder.setMessage(R.string.export_import_write_permission_dialog_description);
        builder.setPositiveButton(R.string.export_import_location_dialog_ok, (dialog, which) -> PermissionRequester.getWritePermission(this));
        builder.show();
    }

    private void checkPermissionsBeforeImport() {
        if (!PermissionRequester.checkReadPermission(this)) {
            if (PermissionRequester.checkReadPermissionDeniedBefore(this)) {
                dialogAfterReadPermissionDenied();
            } else {
                PermissionRequester.getReadPermission(this);
            }
        } else {
            dialogImport();
        }
    }

    private void dialogAfterReadPermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_read_permission_dialog_header);
        builder.setMessage(R.string.export_import_read_permission_dialog_description);
        builder.setPositiveButton(R.string.export_import_location_dialog_ok, (dialog, which) -> PermissionRequester.getReadPermission(this));
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
            Toast.makeText(getApplicationContext(), R.string.export_import_write_permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void exportJson() {
        JsonIOAdapter.init(getApplication(), this);
        if (JsonIOAdapter.write()) {
            dialogExportLocation();
        } else {
            dialogExportFailed();
        }
    }

    private void dialogExportLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_location_dialog_header);
        builder.setMessage(R.string.export_import_location_dialog_description).setPositiveButton(R.string.export_import_location_dialog_ok, null).show();
    }

    private void dialogExportFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_export_failed_dialog_header);
        builder.setMessage(R.string.export_import_export_failed_dialog_description).setPositiveButton(R.string.export_import_export_failed_dialog_ok, null).show();
    }

    private void codeRequestRead(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dialogImport();
        } else {
            Toast.makeText(getApplicationContext(), R.string.export_import_read_permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void dialogImport() {
        if (CurrentSdk.getSdkVersion() >= Build.VERSION_CODES.O) {
            ViewGroup parent = findViewById(R.id.exportimport_parent);

            LayoutInflater inflater = getLayoutInflater();
            View layoutDialogImport = inflater.inflate(R.layout.dialog_import, parent, false);

            AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog_theme).create();
            alertDialog.setView(layoutDialogImport);
            alertDialog.setTitle(R.string.export_import_import_dialog_header);

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
                getApplicationContext().getResources().getString(R.string.export_import_import_choose_file)), ImportJson.READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ImportJson.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            JsonIOAdapter.init(getApplication(), this);
            if (JsonIOAdapter.read(resultData.getData(), keepStoredTeas)) {
                dialogImportComplete();
            } else {
                dialogImportFailed();
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void dialogImportComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_import_complete_dialog_header);
        if (keepStoredTeas) {
            builder.setMessage(R.string.export_import_import_complete_keep_dialog_description).setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show();
        } else {
            builder.setMessage(R.string.export_import_import_complete_delete_dialog_description).setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show();
        }
    }

    private void dialogImportFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.export_import_import_failed_dialog_header);
        builder.setMessage(R.string.export_import_import_failed_dialog_description).setPositiveButton(R.string.export_import_import_failed_dialog_ok, null).show();
    }

    private void showWarning() {
        if (CurrentSdk.getSdkVersion() < Build.VERSION_CODES.O) {
            TextView textViewWarning = findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
