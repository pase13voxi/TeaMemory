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
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.Exporter;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_io.Importer;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.TeaPOJO;

public class DatabaseJsonTransformer {
    public static final int WRITE_REQUEST_CODE = 8778;
    public static final int READ_REQUEST_CODE = 8777;

    private final Application application;
    private final Printer printer;

    public DatabaseJsonTransformer(final Application application, final Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    public boolean databaseToJson(final Exporter exporter) {
        final String json = generateJson();
        return exporter.write(json);
    }

    private String generateJson() {
        final DataTransformViewModel dataTransformViewModel = new DataTransformViewModel(application);

        final DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(dataTransformViewModel.getTeaList(),
                dataTransformViewModel.getInfusionList(), dataTransformViewModel.getCounterList(),
                dataTransformViewModel.getNoteList());

        final List<TeaPOJO> teaList = databaseToPojo.createTeaList();
        return createJsonFromTeaList(teaList);
    }

    private String createJsonFromTeaList(final List<TeaPOJO> teaList) {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        return gson.toJson(teaList);
    }

    public boolean jsonToDatabase(final Importer importer, final boolean keepStoredTeas) {
        final String json = importer.read();
        final List<TeaPOJO> teaList = createTeaListFromJson(json);
        if (teaList.isEmpty()) {
            return false;
        }
        final POJOToDatabase pojoToDatabase = new POJOToDatabase(new DataTransformViewModel(application));
        pojoToDatabase.fillDatabaseWithTeaList(teaList, keepStoredTeas);
        printer.print(application.getString(R.string.export_import_teas_imported));
        return true;
    }

    private List<TeaPOJO> createTeaListFromJson(final String json) {
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
