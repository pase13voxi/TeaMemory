package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.app.Application;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.io.OutputStream;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

public class FileWriter implements Exporter {
    final Application application;
    final Printer printer;
    final Uri folderPath;

    public FileWriter(final Application application, final Printer printer, final Uri folderPath) {
        this.application = application;
        this.printer = printer;
        this.folderPath = folderPath;
    }

    @Override
    public boolean write(final String json) {
        final DocumentFile pickedFolder = DocumentFile.fromTreeUri(application, folderPath);
        if (pickedFolder == null) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        final DocumentFile file = pickedFolder.createFile("application/json", "tealist.json");
        if (file == null) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }

        try (final OutputStream out = application.getContentResolver().openOutputStream(file.getUri())) {
            out.write(json.getBytes());
        } catch (IOException e) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        printer.print(application.getString(R.string.export_import_saved));

        return true;
    }
}
