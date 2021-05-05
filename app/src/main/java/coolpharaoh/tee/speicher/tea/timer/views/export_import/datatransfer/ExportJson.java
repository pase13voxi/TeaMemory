package coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo.TeaPOJO;

public class ExportJson {
    public static final int WRITE_REQUEST_CODE = 8778;

    private final Application application;

    public ExportJson(final Application application) {
        this.application = application;
    }

    public boolean write(final Exporter exporter) {
        final String json = generateJson();
        return exporter.write(json);
    }

    private String generateJson() {
        final DataTransferViewModel dataTransferViewModel = new DataTransferViewModel(application);

        final DatabaseToPOJO databaseToPojo = new DatabaseToPOJO(dataTransferViewModel.getTeaList(),
                dataTransferViewModel.getInfusionList(), dataTransferViewModel.getCounterList(),
                dataTransferViewModel.getNoteList());

        final List<TeaPOJO> teaList = databaseToPojo.createTeaList();
        return createJsonFromTeaList(teaList);
    }

    private String createJsonFromTeaList(final List<TeaPOJO> teaList) {
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        return gson.toJson(teaList);
    }
}
