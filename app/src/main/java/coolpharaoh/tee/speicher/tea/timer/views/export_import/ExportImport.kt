package coolpharaoh.tee.speicher.tea.timer.views.export_import

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.init
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.read
import coolpharaoh.tee.speicher.tea.timer.views.export_import.JsonIOAdapter.write
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.DataIOAdapterFactory.getDataIO
import java.util.Objects

// This class has 9 Parent because of AppCompatActivity
class ExportImport : AppCompatActivity(), Printer {

    private var keepStoredTeas = true

    private val exportActivityResultLauncher = registerForActivityResult(StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data != null && result.data!!.data != null) {
            exportFile(result.data!!.data)
        }
    }

    private val importActivityResultLauncher = registerForActivityResult(StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data != null && result.data!!.data != null) {
            importFile(result.data!!.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_import)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        showWarning()

        val buttonExport = findViewById<Button>(R.id.button_export_import_export)
        buttonExport.setOnClickListener { chooseExportFolder() }

        val buttonImport = findViewById<Button>(R.id.button_export_import_import)
        buttonImport.setOnClickListener { dialogImportDecision() }
    }

    private fun showWarning() {
        if (sdkVersion < Build.VERSION_CODES.Q) {
            val textViewWarning = findViewById<TextView>(R.id.text_view_export_import_warning)
            textViewWarning.visibility = View.GONE

            val textViewWarningText = findViewById<TextView>(R.id.text_view_export_import_warning_text)
            textViewWarningText.visibility = View.GONE
        }
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.export_import_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun chooseExportFolder() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        exportActivityResultLauncher.launch(intent)
    }

    private fun dialogImportDecision() {
        val parent = findViewById<ViewGroup>(R.id.exportimport_parent)

        val inflater = layoutInflater
        val layoutDialogImport = inflater.inflate(R.layout.dialog_import, parent, false)

        val alertDialog = AlertDialog.Builder(this, R.style.dialog_theme).create()
        alertDialog.setView(layoutDialogImport)
        alertDialog.setTitle(R.string.export_import_import_dialog_header)

        val buttonImportDelete = layoutDialogImport.findViewById<Button>(R.id.button_export_import_import_delete)
        buttonImportDelete.setOnClickListener { chooseImportFile(alertDialog, false) }
        val buttonImportKeep = layoutDialogImport.findViewById<Button>(R.id.button_export_import_import_keep)
        buttonImportKeep.setOnClickListener { chooseImportFile(alertDialog, true) }

        alertDialog.show()
    }

    private fun chooseImportFile(alertDialog: AlertDialog, keepTeas: Boolean) {
        dialogChooseImportFile()
        keepStoredTeas = keepTeas
        alertDialog.cancel()
    }

    private fun dialogChooseImportFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        importActivityResultLauncher.launch(Intent.createChooser(intent,
            applicationContext.resources.getString(R.string.export_import_import_choose_file)))
    }

    private fun exportFile(folderPath: Uri?) {
        init(application, this)
        if (write(getDataIO(application, this, folderPath!!))) {
            dialogExportLocation()
        } else {
            dialogExportFailed()
        }
    }

    private fun importFile(filePath: Uri?) {
        init(application, this)
        if (read(getDataIO(application, this, filePath!!), keepStoredTeas)) {
            dialogImportComplete()
        } else {
            dialogImportFailed()
        }
    }

    private fun dialogExportLocation() {
        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.export_import_location_dialog_header)
            .setMessage(getString(R.string.export_import_location_dialog_description))
            .setPositiveButton(R.string.export_import_location_dialog_ok, null)
            .show()
    }

    private fun dialogExportFailed() {
        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.export_import_export_failed_dialog_header)
            .setMessage(R.string.export_import_export_failed_dialog_description)
            .setPositiveButton(R.string.export_import_export_failed_dialog_ok, null)
            .show()
    }

    private fun dialogImportComplete() {
        val builder = AlertDialog.Builder(this, R.style.dialog_theme).setTitle(R.string.export_import_import_complete_dialog_header)
        if (keepStoredTeas) {
            builder.setMessage(R.string.export_import_import_complete_keep_dialog_description)
                .setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show()
        } else {
            builder.setMessage(R.string.export_import_import_complete_delete_dialog_description)
                .setPositiveButton(R.string.export_import_import_complete_dialog_ok, null).show()
        }
    }

    private fun dialogImportFailed() {
        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.export_import_import_failed_dialog_header)
            .setMessage(R.string.export_import_import_failed_dialog_description)
            .setPositiveButton(R.string.export_import_import_failed_dialog_ok, null)
            .show()
    }

    override fun print(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}