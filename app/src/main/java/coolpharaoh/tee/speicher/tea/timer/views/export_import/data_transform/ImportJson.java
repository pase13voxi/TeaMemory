package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.Importer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO;

public class ImportJson {
    public static final int READ_REQUEST_CODE = 8777;

    private final Application application;
    private final Printer printer;

    private String json;

    public ImportJson(final Application application, final Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    public boolean read(final Importer importer, final boolean keepStoredTeas) {
        json = importer.read();
        final List<TeaPOJO> teaList = createTeaListFromJson();
        if (teaList.isEmpty()) {
            return false;
        }
        final POJOToDatabase pojoToDatabase = new POJOToDatabase(new DataTransferViewModel(application));
        pojoToDatabase.fillDatabaseWithTeaList(teaList, keepStoredTeas);
        printer.print(application.getString(R.string.export_import_teas_imported));
        return true;
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
