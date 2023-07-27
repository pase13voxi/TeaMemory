package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Objects

internal class FileSystemIOAdapter(val application: Application, val printer: Printer, val uri: Uri) :
    DataIOAdapter {

    override fun write(json: String): Boolean {
        val pickedFolder = DocumentFile.fromTreeUri(application, uri)
        if (pickedFolder == null) {
            printer.print(application.getString(R.string.export_import_save_failed))
            return false
        }
        val file = pickedFolder.createFile("application/json", "tealist.json")
        if (file == null) {
            printer.print(application.getString(R.string.export_import_save_failed))
            return false
        }

        try {
            application.contentResolver.openOutputStream(file.uri)
                .use { out -> out!!.write(json.toByteArray()) }
        } catch (e: IOException) {
            printer.print(application.getString(R.string.export_import_save_failed))
            return false
        }
        printer.print(application.getString(R.string.export_import_saved))

        return true
    }

    override fun read(): String {
        val stringBuilder = StringBuilder()
        try {
            application.contentResolver.openInputStream(uri).use { inputStream ->
                BufferedReader(InputStreamReader(Objects.requireNonNull(inputStream))).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Cannot read from file uri", e)
        }
        return stringBuilder.toString()
    }

    companion object {
        private val LOG_TAG = FileSystemIOAdapter::class.java.simpleName
    }
}