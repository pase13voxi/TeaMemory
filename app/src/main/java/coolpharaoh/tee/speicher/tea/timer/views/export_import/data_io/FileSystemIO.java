package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

class FileSystemIO implements DataIO {
    private static final String LOG_TAG = FileSystemIO.class.getSimpleName();

    final Application application;
    final Printer printer;
    final Uri uri;

    FileSystemIO(final Application application, final Printer printer, final Uri uri) {
        this.application = application;
        this.printer = printer;
        this.uri = uri;
    }

    @Override
    public boolean write(final String json) {
        final DocumentFile pickedFolder = DocumentFile.fromTreeUri(application, uri);
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
        } catch (final IOException e) {
            printer.print(application.getString(R.string.export_import_save_failed));
            return false;
        }
        printer.print(application.getString(R.string.export_import_saved));

        return true;
    }

    @Override
    public String read() {
        final StringBuilder stringBuilder = new StringBuilder();
        try (final InputStream inputStream =
                     application.getContentResolver().openInputStream(uri);
             final BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (final IOException e) {
            Log.e(LOG_TAG, "Cannot read from file uri", e);
        }
        return stringBuilder.toString();
    }
}
