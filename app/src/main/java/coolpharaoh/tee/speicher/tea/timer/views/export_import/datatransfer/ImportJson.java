package coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.TeaPOJO;

public class ImportJson {
    public static final int READ_REQUEST_CODE = 8777;
    private static final String LOG_TAG = ImportJson.class.getSimpleName();

    private final Application application;
    private final Printer printer;

    private String json;

    public ImportJson(final Application application, final Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    public boolean read(final Uri fileUri, final boolean keepStoredTeas) {
        json = readJsonFile(fileUri);
        final List<TeaPOJO> teaList = createTeaListFromJson();
        if (teaList.isEmpty()) {
            return false;
        }
        final POJOToDatabase pojoToDatabase = new POJOToDatabase(new DataTransferViewModel(application));
        pojoToDatabase.fillDatabaseWithTeaList(teaList, keepStoredTeas);
        printer.print(application.getString(R.string.export_import_teas_imported));
        return true;
    }

    private String readJsonFile(final Uri fileUri) {
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

    private List<TeaPOJO> createTeaListFromJson() {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        final Type listType = new TypeToken<ArrayList<TeaPOJO>>() {
        }.getType();

        try {
            return gson.fromJson(json, listType);
        } catch (final JsonSyntaxException e) {
            printer.print(application.getString(R.string.export_import_import_parse_teas_failed));
            return Collections.emptyList();
        }
    }
}
