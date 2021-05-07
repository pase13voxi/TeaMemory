package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileReader implements Importer {
    private static final String LOG_TAG = FileReader.class.getSimpleName();

    final Application application;
    final Uri fileUri;

    public FileReader(final Application application, final Uri fileUri) {
        this.application = application;
        this.fileUri = fileUri;
    }

    @Override
    public String read() {
        final StringBuilder stringBuilder = new StringBuilder();
        try (final InputStream inputStream =
                     application.getContentResolver().openInputStream(fileUri);
             final BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot read from file uri", e);
        }
        return stringBuilder.toString();
    }
}
